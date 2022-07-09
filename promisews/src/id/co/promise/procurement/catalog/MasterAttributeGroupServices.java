package id.co.promise.procurement.catalog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.Attribute;
import id.co.promise.procurement.catalog.entity.AttributeGroup;
import id.co.promise.procurement.catalog.entity.AttributeOption;
import id.co.promise.procurement.catalog.entity.AttributePanelGroup;
import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogVendor;
import id.co.promise.procurement.catalog.entity.Category;
import id.co.promise.procurement.catalog.entity.InputType;
import id.co.promise.procurement.catalog.session.AttributeGroupSession;
import id.co.promise.procurement.catalog.session.AttributeSession;
import id.co.promise.procurement.catalog.session.CatalogCategorySession;
import id.co.promise.procurement.catalog.session.CategorySession;
import id.co.promise.procurement.catalog.session.InputTypeSession;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.master.AddressBookServices;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.template.TemplateXls;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@Path("/procurement/catalog/AttributeGroupServices")
@Produces(MediaType.APPLICATION_JSON)
public class MasterAttributeGroupServices {
	final static Logger log = Logger.getLogger(MasterAttributeGroupServices.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();
	
	@EJB private AttributeGroupSession attributeGroupSession;
	@EJB private AttributeSession attributeSession;
	@EJB private CatalogCategorySession catalogCategorySession;
	@EJB private InputTypeSession inputTypeSession;
	
	@EJB private VendorSession vendorSession;
	@EJB private CategorySession categorySession;
	@EJB private TokenSession tokenSession;
	
	@Path("/findAll")
	@GET
	public List<AttributeGroup> findAll () {
		return attributeGroupSession.getAttributeGroupList();
	}
	
	@Path("/findByCategory")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public List<AttributeGroup> findByCategory (CatalogVendor catalogVendor) {
		List<Category> categoryList = null;
		if (catalogVendor.getCategory() != null) {
			categoryList = new ArrayList<Category>();
			categoryList = populateCategoryFromTreeList(catalogVendor.getCategory(), categoryList);
		}
		
		catalogVendor.setCategoryList(categoryList);
		catalogVendor.setCurrentPage((catalogVendor.getCurrentPage()-1) * catalogVendor.getPageSize());
		
		// Di ganti dari User Id enjadi vendor ID (Jangan SALAH)
		if(catalogVendor.getUserId() != null) {
			Vendor vendor = vendorSession.find(catalogVendor.getUserId());
			catalogVendor.setVendor(vendor);
		}
		
		List<Catalog> catalogList = catalogCategorySession.getActiveCatalogList(catalogVendor);
		
		List<AttributeGroup> hasilList = new ArrayList<AttributeGroup>();
		List<String> attIdList = new ArrayList<String>();
		if(attIdList.size() == 0 && catalogList.size() > 0) {
			attIdList.add(catalogList.get(0).getAttributeGroup().getId().toString());
			hasilList.add(catalogList.get(0).getAttributeGroup());
		}
		for(Catalog catalog : catalogList) {
			String dataAttGrpId = catalog.getAttributeGroup().getId().toString();
			if(!attIdList.contains(dataAttGrpId)) {
				attIdList.add(dataAttGrpId);
				hasilList.add(catalog.getAttributeGroup());
			}
		}
		return hasilList;
	}
	
	@Path("/save")
	@POST
	public AttributeGroup save (AttributeGroup attributeGroup, @HeaderParam("Authorization") String token) {
		Token tempToken = tokenSession.findByToken(token);		
		if (attributeGroup.getId() != null) {
			AttributeGroup tempAttributeGroup = attributeGroupSession.find(attributeGroup.getId());
			tempAttributeGroup.setDescription(attributeGroup.getDescription());
			tempAttributeGroup.setFlagEnabled(attributeGroup.getFlagEnabled());
			tempAttributeGroup.setIsDelete(attributeGroup.getIsDelete());
			tempAttributeGroup.setName(attributeGroup.getName());
			
			int sizeNewList = attributeGroup.getAttributePanelGroupList().size();
			int sizeOldList = tempAttributeGroup.getAttributePanelGroupList().size();
			if (sizeNewList < sizeOldList) {
				tempAttributeGroup.setAttributePanelGroupList(tempAttributeGroup.getAttributePanelGroupList().subList(0, sizeNewList));
				sizeOldList = tempAttributeGroup.getAttributePanelGroupList().size();
			}
			
			for (int a=0; a < sizeNewList; a++) {
				if (a < sizeOldList) {
					AttributePanelGroup oldAttributePanelGroup = tempAttributeGroup.getAttributePanelGroupList().get(a);
					AttributePanelGroup newAttributePanelGroup = attributeGroup.getAttributePanelGroupList().get(a);
					oldAttributePanelGroup.setAttribute(newAttributePanelGroup.getAttribute());
					oldAttributePanelGroup.setIsDelete(newAttributePanelGroup.getIsDelete());
					oldAttributePanelGroup.setName(newAttributePanelGroup.getName());
					oldAttributePanelGroup.setUrutan(newAttributePanelGroup.getUrutan());
				} else {
					AttributePanelGroup newAttributePanelGroup = attributeGroup.getAttributePanelGroupList().get(a);
					tempAttributeGroup.getAttributePanelGroupList().add(newAttributePanelGroup);
				}
			}
			return attributeGroupSession.updateAttributeGroup(tempAttributeGroup, tempToken);
		} else {
			for(AttributePanelGroup attPanelGrp : attributeGroup.getAttributePanelGroupList()) {
				attPanelGrp.setAttribute(attributeSession.find(attPanelGrp.getAttribute().getId()));
			}
			return attributeGroupSession.insertAttributeGroup(attributeGroup, tempToken);
		}
	}
	
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public AttributeGroup delete (AttributeGroup attributeGroup, @HeaderParam("Authorization") String token) {
		Token tempToken = tokenSession.findByToken(token);
		return attributeGroupSession.deleteAttributeGroup(attributeGroup.getId(), tempToken);
	}
	
	@POST
	@Path("/readTemplateXlsAttGr")
	@Consumes("multipart/form-data")
	public Response readExcell(MultipartFormDataInput input, @HeaderParam("Authorization") String token) {

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts 				= uploadForm.get("file");

		for (InputPart inputPart : inputParts) {
			MultivaluedMap<String, String> header = inputPart.getHeaders();
			String fileName 	= TemplateXls.getFileNameTemplateXls(header);
			Workbook workbook 	= null;
			try {
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
	        	if(fileName.endsWith("xlsx") || fileName.endsWith("xls")){
	        		workbook = new XSSFWorkbook(inputStream);
	        		analyseExcelForAttGr(workbook, token);	
	        	}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Response.ok().build();
	}
	
	private void analyseExcelForAttGr(Workbook workbook, String token) {
    	try{
    		Sheet datatypeSheet = workbook.getSheetAt(0);
	        Row row ;
	        for(int i=1;i<=datatypeSheet.getLastRowNum();i++){
	        	row = datatypeSheet.getRow(i);
	        	int lastCellNumber = row.getLastCellNum();
	        	int batasOption = Constant.TEMPLATE_ATT_GR_ATT_INPUT_TYPE + 1;
	        	
	        	String namaGroup 		= row.getCell(Constant.TEMPLATE_ATT_GR_NAMA_GROUP).toString();
	        	String namaPanel 		= row.getCell(Constant.TEMPLATE_ATT_GR_NAMA_PANEL).toString();
	        	String namaAtt	 		= row.getCell(Constant.TEMPLATE_ATT_GR_NAMA_ATT).toString();
	        	String inputType 		= row.getCell(Constant.TEMPLATE_ATT_GR_ATT_INPUT_TYPE).toString();
	        	Boolean sudahAdaAtt		= false;
	        	
	        	// Cek Attribute Group
	        	Attribute tempAttribute = new Attribute();
	        	AttributeGroup tempAttributeGroup = new AttributeGroup();
	        	AttributePanelGroup tempAttributePanelGroup = new AttributePanelGroup();
	        	
	        	tempAttributeGroup.setName(namaGroup);
	        	tempAttributePanelGroup.setName(namaPanel);
	        	tempAttribute.setName(namaAtt);
	        	
	        	List<AttributeGroup> attGroupList = attributeGroupSession.getAttributeGroupList(tempAttributeGroup);
	        	List<AttributePanelGroup> attributePanelGroupList = new ArrayList<AttributePanelGroup>();
	        	List<Attribute> tempAttributeList = new ArrayList<Attribute>();
	        	
	        	if(attGroupList.size() > 0) {
	        		tempAttributeGroup = attGroupList.get(0);
	        		
	        		// Cek Attribute Panel Group
	        		if(tempAttributeGroup.getAttributePanelGroupList() != null && tempAttributeGroup.getAttributePanelGroupList().size() > 0) {
	        			
	        			for(AttributePanelGroup attPnGr : tempAttributeGroup.getAttributePanelGroupList()) {
	        				if(namaAtt.equals(attPnGr.getAttribute().getName())) {
	        					sudahAdaAtt = true;
	        				}
	        			}
	        		}
	        	} else {
	        		tempAttributeGroup.setFlagEnabled(true);
	        		tempAttributeGroup.setDescription(namaGroup);
	        	}
	        	
        		if(!sudahAdaAtt) {
        			tempAttributeList = attributeSession.getAttributeList(tempAttribute);
        			if(tempAttributeList.size() > 0) {
        				tempAttributePanelGroup.setAttribute(tempAttributeList.get(0));
        			} else {
        				tempAttribute.setConfigurable(true);
        				tempAttribute.setFlagEnabled(true);
        				tempAttribute.setRequired(true);
        				tempAttribute.setSearchable(true);
        				tempAttribute.setSortable(true);
        				tempAttribute.setUnique(true);
        				tempAttribute.setIsDelete(0);
        				tempAttribute.setTranslateInd(namaAtt);
        				
        				// Cari Input Type berdasarkan Nama
        				InputType inputTp = new InputType();
        				inputTp.setName(inputType);
        				List<InputType> inputTypeList = inputTypeSession.getInputTypeList(inputTp);
        				if (inputTypeList != null && inputTypeList.size() > 0) {
        					tempAttribute.setInputType(inputTypeList.get(0));
        				}
        				
        				List<AttributeOption> attOptionList = new ArrayList<AttributeOption>();
        				if(lastCellNumber > batasOption) {
        					int panjangOption = lastCellNumber - batasOption;
        					for(int j=0; j<panjangOption; j++) {
        						AttributeOption attOption = new AttributeOption();
        						int cellAttOption = Constant.TEMPLATE_ATT_GR_OPTION + j;
        						attOption.setName(row.getCell(cellAttOption).toString());
        						attOptionList.add(attOption);
        					}
        					tempAttribute.setAttributeOptionList(attOptionList);
        				}
        				tempAttributePanelGroup.setAttribute(tempAttribute);
        			}
        			
        			tempAttributePanelGroup.setIsDelete(0);
        			if(tempAttributeGroup.getAttributePanelGroupList() != null) {
        				tempAttributePanelGroup.setUrutan(tempAttributeGroup.getAttributePanelGroupList().size() + 1);
        			} else {
        				tempAttributePanelGroup.setUrutan(1);
        			}
					
        			if(tempAttributeGroup.getId() != null) {
        				tempAttributeGroup.getAttributePanelGroupList().add(tempAttributePanelGroup);
        			} else {
        				attributePanelGroupList.add(tempAttributePanelGroup);
        				tempAttributeGroup.setAttributePanelGroupList(attributePanelGroupList);
        			}
        			
        			attributeGroupSession.insertAttributeGroup(tempAttributeGroup, tokenSession.findByToken(token));
        		}
    			
	        }
    	}catch(Exception e){
			e.printStackTrace();
		}
    }
	
	protected List<Category> populateCategoryFromTreeList(Category category, List<Category> resultCategory) {
		if (category != null && category.getId() != null) {
			resultCategory.add(categorySession.find(category.getId()));
			if (category.getCategoryChildList() != null && category.getCategoryChildList().size() > 0) {
				for (Category categoryChild : category.getCategoryChildList()) {
					resultCategory = populateCategoryFromTreeList(categoryChild, resultCategory);
				}
			}
		}
		return resultCategory;
	}
	
	@Path("/getAttributeGroupByname/{data}")
	@GET
	public Response getAttributeGroupByname (
			@HeaderParam("Authorization") String token,
			@PathParam("data") String nama) {
		
		try {
			List<AttributeGroup> attList = attributeGroupSession.getAttributeGroupList();
			List<AttributeGroup> attListNew = new ArrayList<AttributeGroup>();
			for(AttributeGroup att : attList) {
				if(att.getName().equalsIgnoreCase(nama)) {
					attListNew.add(att);
				}
			}
			return Response.status(201).entity(attListNew).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}
}
