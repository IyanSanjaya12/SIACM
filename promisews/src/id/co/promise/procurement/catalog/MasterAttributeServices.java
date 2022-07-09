package id.co.promise.procurement.catalog;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.Attribute;
import id.co.promise.procurement.catalog.entity.AttributeOption;
import id.co.promise.procurement.catalog.entity.AttributePanelGroup;
import id.co.promise.procurement.catalog.entity.InputType;
import id.co.promise.procurement.catalog.session.AttributeSession;
import id.co.promise.procurement.catalog.session.InputTypeSession;
import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.master.AddressBookServices;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.template.TemplateXls;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.StaticUtility;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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

@Stateless
@Path(value = "/procurement/catalog/AttributeServices")
@Produces(MediaType.APPLICATION_JSON)
public class MasterAttributeServices {
	final static Logger log = Logger.getLogger(MasterAttributeServices.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();
	@EJB
	private AttributeSession attributeSession;
	@EJB
	private InputTypeSession inputTypeSession;
	@EJB
	private TokenSession tokenSession;

	@Path("/findAll")
	@GET
	public List<Attribute> findAll() {
		return attributeSession.getAttributeList();
	}

	@Path("/findAllInputType")
	@GET
	public List<InputType> findAllInputType() {
		return inputTypeSession.getInputTypeList();
	}

	@Path("/findAllExcludeGroup")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAllExcludeGroup(
			List<AttributePanelGroup> attributePanelGroupList) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Attribute> tempAttribute = new ArrayList<Attribute>();
		List<Attribute> attributeList = attributeSession.getAttributeList();

		if (attributeList != null && attributeList.size() > 0
				&& attributePanelGroupList != null
				&& attributePanelGroupList.size() > 0) {
			for (Attribute attribute : attributeList) {
				Boolean exist = false;
				for (AttributePanelGroup attributePanelGroup : attributePanelGroupList) {
					if (attribute.getId() == attributePanelGroup.getAttribute()
							.getId()) {
						exist = true;
					}
				}
				if (exist == false) {
					tempAttribute.add(attribute);
				}
			}
		}

		map.put("attributePanelGroupList", StaticUtility
				.populateAttributeGroupList(attributePanelGroupList));
		map.put("attributeList", tempAttribute);

		return Response.ok(map).build();
	}

	@Path("/getAttributeInput")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Attribute> getAttributeInput(Integer catalogId) {
		List<Attribute> list = attributeSession
				.getAttributeListInput(catalogId);
		return list;
	}

	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Attribute save(Attribute attribute,
			@HeaderParam("Authorization") String token) {
		Token tempToken = tokenSession.findByToken(token);

		if (attribute.getId() != null) {
			return attributeSession.updateAttribute(attribute, tempToken);
		} else {
			return attributeSession.insertAttribute(attribute, tempToken);
		}
	}

	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Attribute delete(Attribute attribute,
			@HeaderParam("Authorization") String token) {
		Token tempToken = tokenSession.findByToken(token);
		return attributeSession.deleteAttribute(attribute.getId(), tempToken);
	}

	@POST
	@Path("/readTemplateXlsAtt")
	@Consumes("multipart/form-data")
	public Response readExcell(MultipartFormDataInput input,
			@HeaderParam("Authorization") String token) {

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");

		for (InputPart inputPart : inputParts) {
			MultivaluedMap<String, String> header = inputPart.getHeaders();
			String fileName = TemplateXls.getFileNameTemplateXls(header);
			Workbook workbook = null;
			try {
				InputStream inputStream = inputPart.getBody(InputStream.class,
						null);
				if (fileName.endsWith("xlsx") || fileName.endsWith("xls")) {
					workbook = new XSSFWorkbook(inputStream);
					analyseExcelForAttribute(workbook, token);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Response.ok().build();
	}

	private void analyseExcelForAttribute(Workbook workbook, String token) {
		try {
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Row row;
			for (int i = 1; i <= datatypeSheet.getLastRowNum(); i++) {
				row = datatypeSheet.getRow(i);
				int lastCellNumber = row.getLastCellNum();
				int batasOption = Constant.TEMPLATE_ATT_INPUT_TYPE + 1;

				String namaAtt = row.getCell(Constant.TEMPLATE_ATT_NAMA)
						.toString();
				String inputType = row
						.getCell(Constant.TEMPLATE_ATT_INPUT_TYPE).toString();

				Attribute tempAttribute = new Attribute();
				tempAttribute.setName(namaAtt);
				List<Attribute> tempAttributeList = attributeSession
						.getAttributeList(tempAttribute);
				if (tempAttributeList.size() > 0) {
					tempAttribute = tempAttributeList.get(0);
				} else {
					tempAttribute.setConfigurable(true);
					tempAttribute.setFlagEnabled(true);
					tempAttribute.setRequired(true);
					tempAttribute.setSearchable(true);
					tempAttribute.setSortable(true);
					tempAttribute.setUnique(true);
					tempAttribute.setTranslateInd(namaAtt);

					// Cari Input Type berdasarkan Nama
					InputType inputTp = new InputType();
					inputTp.setName(inputType);
					List<InputType> inputTypeList = inputTypeSession
							.getInputTypeList(inputTp);
					if (inputTypeList != null && inputTypeList.size() > 0) {
						tempAttribute.setInputType(inputTypeList.get(0));
					}

					List<AttributeOption> attOptionList = new ArrayList<AttributeOption>();
					if (lastCellNumber > batasOption) {
						int panjangOption = lastCellNumber - batasOption;
						for (int j = 0; j < panjangOption; j++) {
							AttributeOption attOption = new AttributeOption();
							int cellAttOption = Constant.TEMPLATE_ATT_OPTION
									+ j;
							attOption.setName(row.getCell(cellAttOption)
									.toString());
							attOptionList.add(attOption);
						}
						tempAttribute.setAttributeOptionList(attOptionList);
					}
				}
				tempAttribute = attributeSession.insertAttribute(tempAttribute,
						tokenSession.findByToken(token));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Path("/getAttributeByname/{data}")
	@GET
	public Response getAttributeByname(
			@HeaderParam("Authorization") String token,
			@PathParam("data") String nama) {
		
		try {
			List<Attribute> attList = attributeSession.getAttributeList();
			List<Attribute> attListNew = new ArrayList<Attribute>();
			for(Attribute att : attList) {
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
	
	@Path("/getListPaging")
	@POST
	public Response getListPaging(
			@FormParam("refresh") String refresh,
			@FormParam("search[value]") String keyword,
			@FormParam("start") Integer start,
			@FormParam("length") Integer length,
			@FormParam("draw") Integer draw,
			@FormParam("order[0][column]") Integer columnOrder,
			@FormParam("order[0][dir]") String tipeOrder,			
			@HeaderParam("Authorization") String token
			) {
		try {
			List<Attribute> attributeList = attributeSession.getListPaging(keyword, start, length, columnOrder, tipeOrder);
			Long size = attributeSession.getListPagingSize(keyword);
			Map<String, Object> result = new HashMap<>();
			result.put("draw", draw);
			result.put("data", attributeList);
			result.put("recordsTotal", size);
			result.put("recordsFiltered", size);
			
			return Response.ok(result).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		
	}
	
}
