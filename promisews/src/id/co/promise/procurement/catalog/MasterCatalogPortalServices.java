package id.co.promise.procurement.catalog;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import id.co.promise.procurement.catalog.entity.Attribute;
import id.co.promise.procurement.catalog.entity.AttributeGroup;
import id.co.promise.procurement.catalog.entity.AttributeOption;
import id.co.promise.procurement.catalog.entity.AttributePanelGroup;
import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogAttribute;
import id.co.promise.procurement.catalog.entity.CatalogCategory;
import id.co.promise.procurement.catalog.entity.CatalogComment;
import id.co.promise.procurement.catalog.entity.CatalogImage;
import id.co.promise.procurement.catalog.entity.CatalogKontrak;
import id.co.promise.procurement.catalog.entity.CatalogLocation;
import id.co.promise.procurement.catalog.entity.CatalogPriceRange;
import id.co.promise.procurement.catalog.entity.CatalogVendor;
import id.co.promise.procurement.catalog.entity.Category;
import id.co.promise.procurement.catalog.entity.InputType;
import id.co.promise.procurement.catalog.session.AttributeGroupSession;
import id.co.promise.procurement.catalog.session.AttributeOptionSession;
import id.co.promise.procurement.catalog.session.AttributeSession;
import id.co.promise.procurement.catalog.session.CatalogAttributeSession;
import id.co.promise.procurement.catalog.session.CatalogCategorySession;
import id.co.promise.procurement.catalog.session.CatalogCommentSession;
import id.co.promise.procurement.catalog.session.CatalogImageSession;
import id.co.promise.procurement.catalog.session.CatalogKontrakSession;
import id.co.promise.procurement.catalog.session.CatalogLocationSession;
import id.co.promise.procurement.catalog.session.CatalogPriceRangeSession;
import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.catalog.session.CategorySession;
import id.co.promise.procurement.catalog.session.InputTypeSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.BidangUsaha;
import id.co.promise.procurement.entity.EmailNotification;
import id.co.promise.procurement.entity.ItemType;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorProfile;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.ItemTypeSession;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.ProductTypeSession;
import id.co.promise.procurement.master.SatuanSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.template.TemplateXls;
import id.co.promise.procurement.utils.JsonObject;
import id.co.promise.procurement.utils.StaticUtility;
import id.co.promise.procurement.vendor.VendorProfileSession;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@Path(value = "/catalog/CatalogServices")
@Produces(MediaType.APPLICATION_JSON)
public class MasterCatalogPortalServices {

	@EJB private TokenSession tokenSession;
	
	@EJB private CatalogSession catalogSession;
	@EJB private CatalogPriceRangeSession catalogPriceRangeSession;
	@EJB private CatalogCategorySession catalogCategorySession;
	@EJB private CatalogLocationSession catalogLocationSession;
	@EJB private CatalogAttributeSession catalogAttributeSession;
	@EJB private CatalogImageSession catalogImageSession;
	@EJB private CatalogKontrakSession catalogKontrakSession;
	@EJB private CatalogCommentSession catalogCommentSession;

	@EJB private AttributeSession attributeSession;
	@EJB private AttributeGroupSession attributeGroupSession;
	@EJB private AttributeOptionSession attributeOptionSession;
	@EJB private ItemTypeSession itemTypeSession;
	@EJB private MataUangSession mataUangSession;
	@EJB private VendorSession vendorSession;
	@EJB private SatuanSession satuanSession;
	@EJB private CategorySession categorySession;
	@EJB private ItemSession itemSession;
	@EJB private InputTypeSession inputTypeSession;
	@EJB private VendorProfileSession vendorProfileSession;
	@EJB private ProductTypeSession productTypeSession;
	@EJB private RoleUserSession roleUserSession;
	@EJB private UserSession userSession;
	@EJB private EmailNotificationSession emailNotificationSession;
	@EJB private OrganisasiSession organisasiSession;
	
	@Path("/findAll")
	@POST
	public JsonObject<Catalog> findAll (
			@Context HttpServletRequest httpServletRequest, 
			@FormParam("contract") Boolean contract,
			@HeaderParam("Authorization") String strToken
			) {
		//kontrak
		String isContractStr = httpServletRequest.getParameter("param[contract]");
		if(isContractStr!=null){
			if(isContractStr.equals("true"))
				contract = true;			
			if(isContractStr.equals("false"))
				contract = false;
		}
		
		String start = httpServletRequest.getParameter("start");
		String draw	 = httpServletRequest.getParameter("draw");
		String length = httpServletRequest.getParameter("length");
		String search = httpServletRequest.getParameter("search[value]");
		String order = httpServletRequest.getParameter("order[0][dir]");
		String vIndexSort = httpServletRequest.getParameter("order[0][column]");
		String orderField = null;
		if (vIndexSort != null && vIndexSort.length() > 0 && vIndexSort.equals("0") == false) {
			orderField = httpServletRequest.getParameter("columns["+vIndexSort+"][data]");
		}		
		
		Token token = tokenSession.findByToken(strToken);
		Integer vVendorId = null;
		if (token != null && token.getUser() != null) {
			Vendor vendor = vendorSession.getVendorByUserId(token.getUser().getId());
			if (vendor != null) {
				vVendorId = vendor.getId();
			}
		}
		String keyword = (search != null && search.length() > 0) ? search : null;
		JsonObject<Catalog> jo = new JsonObject<Catalog>();
		jo.setData(catalogSession.getCatalogListForManageCatalog(keyword, vVendorId, contract, Integer.valueOf(start), Integer.valueOf(length), orderField, order));
		Integer jmlData = catalogSession.countCatalogListForManageCatalog(keyword, vVendorId, contract).intValue();
		jo.setRecordsTotal(jmlData);
		jo.setRecordsFiltered(jmlData);
		jo.setDraw(draw);
		
		return jo;
	}
	
	@Path("/findAllByProperty")
	@POST
	public List<Catalog> findAllByProperty (Catalog catalog) {
		return catalogSession.getCatalogList(catalog);
	}

	@Path("/findCatalogByKontrak")
	@POST
	public List<Catalog> findCatalogByKontrak (Catalog catalog) {
		return catalogSession.getCatalogListWithKontrak(catalog);
	}
	
	@Path("/getCatalogListByCategory")
	@POST
	public List<Catalog> getCatalogListByCategory (Catalog catalog) {
		return catalogSession.getCatalogListByCategory(catalog);
	}
	
	@Path("/getCatalogListByPagination")
	@POST 
	public Response getAlokasiAnggaranByPagination(
			@FormParam("search[value]") String keyword,
			@FormParam("start") Integer start,
			@FormParam("length") Integer length,
			@FormParam("draw") Integer draw,
			@FormParam("order[0][column]") Integer columnOrder,
			@FormParam("order[0][dir]") String tipeOrder,
			@HeaderParam("Authorization") String token
			){
		Token objToken = tokenSession.findByToken(token);
		//Integer userId = objToken.getUser().getId();
		
		String tempKeyword = "%" + keyword + "%";
		long countData = catalogSession.getCatalogListCount(tempKeyword);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("draw", draw);
		result.put("recordsTotal", countData);
		result.put("recordsFiltered", countData);		
		result.put("data", catalogSession.getCatalogListWithPagination(start, length, tempKeyword, columnOrder, tipeOrder));
		
		return Response.ok(result).build();
	}
	
	
	//--------------------------------------------------------------------
	
	@Path("/findCatalogByKontrakManageKontrak")
	@POST
	public  JsonObject<Catalog> findCatalogByKontrakManageKontrak (
				@Context HttpServletRequest httpServletRequest, 
				@FormParam("contract") Boolean contract,
				@HeaderParam("Authorization") String strToken
				) {
			
		String start = httpServletRequest.getParameter("start");
		String draw	 = httpServletRequest.getParameter("draw");
		String length = httpServletRequest.getParameter("length");
		String search = httpServletRequest.getParameter("search[value]");
		String order = httpServletRequest.getParameter("order[0][dir]");
		String vIndexSort = httpServletRequest.getParameter("order[0][column]");
		String orderField = null;
		
		if (vIndexSort != null && vIndexSort.length() > 0 && vIndexSort.equals("0") == false) {
			orderField = httpServletRequest.getParameter("columns["+vIndexSort+"][data]");
		}		
		
		Token token = tokenSession.findByToken(strToken);
		Integer vVendorId = null;
		if (token != null && token.getUser() != null) {
			Vendor vendor = vendorSession.getVendorByUserId(token.getUser().getId());
			if (vendor != null) {
				vVendorId = vendor.getId();
			}
		}
		
		
		String keyword = (search != null && search.length() > 0) ? search : null;
		JsonObject<Catalog> jo = new JsonObject<Catalog>();
		jo.setData(catalogSession.getCatalogListByKontrak(keyword, vVendorId, contract, Integer.valueOf(start), Integer.valueOf(length), orderField, order));
		Integer jmlData = catalogSession.countCatalogListByKontrak(keyword, vVendorId, contract).intValue();
		jo.setRecordsTotal(jmlData);
		jo.setRecordsFiltered(jmlData);
		jo.setDraw(draw);
		
		return jo;
		
	}
	
	@Path("/findAllVendor")
	@POST
	public List<Vendor> findAllVendor (BidangUsaha bidangUsaha ) {
		List<Vendor> vendorList = vendorSession.getVendorListByCatalog(null, 0, 6);
		return vendorList;
	}
	
	@Path("/findCatalogCommentByCatalog")
	@POST
	public List<CatalogComment> findCatalogCommentByCatalog (
			@FormParam("catalogId")Integer catalogId) {
		
		List<CatalogComment> hasilList = new ArrayList<CatalogComment>();
		
		CatalogComment catalogComment = new CatalogComment();
		catalogComment.setCatalog(catalogSession.find(catalogId));
		List<CatalogComment> catalogCommentList = catalogCommentSession.getCatalogCommentList(catalogComment);
		
		if(catalogCommentList.size() > 0) {
			for(CatalogComment catComment : catalogCommentList) {
				// Menentukan Role
				List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(catComment.getUser().getId());
				if(roleUserList != null && roleUserList.size() > 0) {
					catComment.setRoleUser(roleUserList.get(0).getRole().getNama());
				}
				
				// Menentukan Afco
				
				Organisasi afcoOrganisasi =organisasiSession.getAfcoOrganisasiByUserId(catComment.getUser().getId());
				if (afcoOrganisasi != null) {
					catComment.setOrganisasi(afcoOrganisasi.getNama());
				}
				
				hasilList.add(catComment);
			}
		}
		return hasilList;
	}
	
	@Path("/findECatalogVendorById")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findECatalogVendorByVendorId(CatalogVendor catalogVendor){
		List<Catalog> catalogList = catalogCategorySession.getCatalogByVendorId(catalogVendor);
		if (catalogList != null && catalogList.size() > 0) {
			for (Catalog catalog : catalogList) {
				CatalogCategory catalogCategory = new CatalogCategory();
				catalogCategory.setCatalog(catalog);
				List<CatalogCategory> catalogCategoryList = catalogCategorySession.getCatalogCategoryList(catalogCategory);
				if (catalogCategoryList != null && catalogCategoryList.size() > 0) {
					for (CatalogCategory tempCatalogCategory : catalogCategoryList) {
						catalogCategory = new CatalogCategory();
						
						Category tempCategory = new Category(); 
						tempCategory.setAdminLabel(tempCatalogCategory.getCategory().getAdminLabel());
						tempCategory.setDescription(tempCatalogCategory.getCategory().getDescription());
						tempCategory.setFlagEnabled(tempCatalogCategory.getCategory().getFlagEnabled());
						tempCategory.setId(tempCatalogCategory.getCategory().getId());
						tempCategory.setIsDelete(tempCatalogCategory.getCategory().getIsDelete());
						tempCategory.setTranslateEng(tempCatalogCategory.getCategory().getTranslateEng());
						tempCategory.setTranslateInd(tempCatalogCategory.getCategory().getTranslateInd());
						
						catalogCategory.setCategory(tempCategory);
						
						if (catalog.getCatalogCategoryList() == null) {
							catalog.setCatalogCategoryList(new ArrayList<CatalogCategory>());
						}
						catalog.getCatalogCategoryList().add(catalogCategory);
					}
				}
				
				/** Catalog Images **/
				CatalogImage catalogImage = new CatalogImage();
				catalogImage.setCatalog(catalog);
				List<CatalogImage> catalogImageList = catalogImageSession.getCatalogImageList(catalogImage);
				if (catalogImageList != null && catalogImageList.size() > 0) {
					for (CatalogImage tempCatalogImage : catalogImageList) {
						catalogImage = new CatalogImage();
						catalogImage.setImagesRealName(tempCatalogImage.getImagesRealName());
						catalogImage.setImagesFileName(tempCatalogImage.getImagesFileName());
						
						
						if (catalog.getCatalogImageList() == null) {
							catalog.setCatalogImageList(new ArrayList<CatalogImage>());
						}
						
						catalog.getCatalogImageList().add(catalogImage);
					}
				}
				
				/** Catalog Attributes **/
				CatalogAttribute catalogAttribute = new CatalogAttribute();
				catalogAttribute.setCatalog(catalog);
				List<CatalogAttribute> catalogAttributeList = catalogAttributeSession.getCatalogAttributeList(catalogAttribute);
				if(catalogAttributeList !=null){
					if(catalogAttributeList.size()>0){
						for(CatalogAttribute attribute : catalogAttributeList){
							
							CatalogAttribute catalogAttr = new CatalogAttribute();
							catalogAttr.setId(attribute.getId());
							if(attribute.getNilai() != null) {
								Integer attOptionId = Integer.valueOf(attribute.getNilai());
								AttributeOption attOption = attributeOptionSession.find(attOptionId);
								catalogAttr.setNilai(attOption.getName());
							}
				
							catalogAttr.setAttributePanelGroup(attribute.getAttributePanelGroup());
							
							if(catalog.getCatalogAttributeList() ==null){
								catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
							}
							catalog.getCatalogAttributeList().add(catalogAttr);
						}
					}else{
						if(catalog.getCatalogAttributeList() ==null){
							catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
						}
						
						catalog.getCatalogAttributeList().add(new CatalogAttribute());
					}
				}
				
				/** Catalog Location **/
				CatalogLocation catalogLocation = new CatalogLocation();
				catalogLocation.setCatalog(catalog);
				List<CatalogLocation> catalogLocationList = catalogLocationSession.getCatalogLocationList(catalogLocation);
				if(catalogLocationList !=null){
					if(catalogLocationList.size() > 0){
						for(CatalogLocation location : catalogLocationList){
							CatalogLocation catalogLoc = new CatalogLocation();
							catalogLoc.setId(location.getId());
							catalogLoc.setMinimalOrder(location.getMinimalOrder());
							catalogLoc.setOfficeAddress(location.getOfficeAddress());
							catalogLoc.setOutStock(location.getOutStock());
							catalogLoc.setQuantityNotify(location.getQuantityNotify());
							catalogLoc.setStockProduct(location.getStockProduct());
							catalogLoc.setSupplyAbility(location.getSupplyAbility());
							
							if(catalog.getCatalogLocationList() == null){
								catalog.setCatalogLocationList(new ArrayList<CatalogLocation>());
							}
							catalog.getCatalogLocationList().add(catalogLoc);
						}
					}
				}else{
					if(catalog.getCatalogLocationList() == null){
						catalog.setCatalogLocationList(new ArrayList<CatalogLocation>());
					}
					catalog.getCatalogLocationList().add(new CatalogLocation());
				}
				
				if (catalog.getVendor() != null) {
					List<VendorProfile> vendorProfileList = vendorProfileSession.getVendorProfileByVendorId(catalog.getVendor().getId());
					if (vendorProfileList != null && vendorProfileList.size() > 0) {
						catalog.setVendorProfile(vendorProfileList.get(0));
					}
				}
			}
		}	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("catalogList", catalogList);
		map.put("totalList", catalogCategorySession.countCatalogList(catalogVendor));
		
		return Response.ok(map).build();
	}

	@Path("/findECatalogVendor")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findECatalogVendor(CatalogVendor catalogVendor) {
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
		
		//Filter dengan List User yang satu Organisasi
		List<User> userList = new ArrayList<User>();
		if (catalogVendor.getUserId() != null) {
			
			//Cari Organisasi User di Tabel RoleUser
			List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(catalogVendor.getUserId());
			if(roleUserList != null && roleUserList.size() > 0) {
				Integer orgId = roleUserList.get(0).getOrganisasi().getId();
				List<RoleUser> organisasiRoleUserList = roleUserSession.getRoleUserByOrganisasiAndAppCode(orgId, "PM");
				
				if(organisasiRoleUserList != null && organisasiRoleUserList.size() > 0) {
					for(RoleUser roleUser : organisasiRoleUserList) {
						userList.add(roleUser.getUser());
					}
					catalogVendor.setUserList(userList);
				}
			}
		}

		List<Catalog> catalogList = catalogCategorySession.getCatalogListByVendorId(catalogVendor);
		if (catalogList != null && catalogList.size() > 0) {
			for (Catalog catalog : catalogList) {
				CatalogCategory catalogCategory = new CatalogCategory();
				catalogCategory.setCatalog(catalog);
				List<CatalogCategory> catalogCategoryList = catalogCategorySession.getCatalogCategoryList(catalogCategory);
				if (catalogCategoryList != null && catalogCategoryList.size() > 0) {
					for (CatalogCategory tempCatalogCategory : catalogCategoryList) {
						catalogCategory = new CatalogCategory();
						
						Category tempCategory = new Category(); 
						tempCategory.setAdminLabel(tempCatalogCategory.getCategory().getAdminLabel());
						tempCategory.setDescription(tempCatalogCategory.getCategory().getDescription());
						tempCategory.setFlagEnabled(tempCatalogCategory.getCategory().getFlagEnabled());
						tempCategory.setId(tempCatalogCategory.getCategory().getId());
						tempCategory.setIsDelete(tempCatalogCategory.getCategory().getIsDelete());
						tempCategory.setTranslateEng(tempCatalogCategory.getCategory().getTranslateEng());
						tempCategory.setTranslateInd(tempCatalogCategory.getCategory().getTranslateInd());
						
						catalogCategory.setCategory(tempCategory);
						
						if (catalog.getCatalogCategoryList() == null) {
							catalog.setCatalogCategoryList(new ArrayList<CatalogCategory>());
						}
						catalog.getCatalogCategoryList().add(catalogCategory);
					}
				}
				
				/** Catalog Images **/
				CatalogImage catalogImage = new CatalogImage();
				catalogImage.setCatalog(catalog);
				List<CatalogImage> catalogImageList = catalogImageSession.getCatalogImageList(catalogImage);
				if (catalogImageList != null && catalogImageList.size() > 0) {
					for (CatalogImage tempCatalogImage : catalogImageList) {
						catalogImage = new CatalogImage();
						catalogImage.setImagesRealName(tempCatalogImage.getImagesRealName());
						catalogImage.setImagesFileName(tempCatalogImage.getImagesFileName());
						
						if (catalog.getCatalogImageList() == null) {
							catalog.setCatalogImageList(new ArrayList<CatalogImage>());
						}
						
						catalog.getCatalogImageList().add(catalogImage);
					}
				}
				
				/** Catalog Attributes **/
				CatalogAttribute catalogAttribute = new CatalogAttribute();
				catalogAttribute.setCatalog(catalog);
				List<CatalogAttribute> catalogAttributeList = catalogAttributeSession.getCatalogAttributeList(catalogAttribute);
				if(catalogAttributeList !=null){
					if(catalogAttributeList.size()>0){
						for(CatalogAttribute attribute : catalogAttributeList){
							
							CatalogAttribute catalogAttr = new CatalogAttribute();
							catalogAttr.setId(attribute.getId());
							if(attribute.getNilai() != null) {
								Integer attOptionId = Integer.valueOf(attribute.getNilai());
								AttributeOption attOption = attributeOptionSession.find(attOptionId);
								catalogAttr.setNilai(attOption.getName());
							}
				
							catalogAttr.setAttributePanelGroup(attribute.getAttributePanelGroup());
							
							if(catalog.getCatalogAttributeList() ==null){
								catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
							}
							catalog.getCatalogAttributeList().add(catalogAttr);
						}
					}else{
						if(catalog.getCatalogAttributeList() ==null){
							catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
						}
						
						catalog.getCatalogAttributeList().add(new CatalogAttribute());
					}
				}
				
				/** Catalog Location **/
				CatalogLocation catalogLocation = new CatalogLocation();
				catalogLocation.setCatalog(catalog);
				List<CatalogLocation> catalogLocationList = catalogLocationSession.getCatalogLocationList(catalogLocation);
				if(catalogLocationList !=null){
					if(catalogLocationList.size() > 0){
						for(CatalogLocation location : catalogLocationList){
							CatalogLocation catalogLoc = new CatalogLocation();
							catalogLoc.setId(location.getId());
							catalogLoc.setMinimalOrder(location.getMinimalOrder());
							catalogLoc.setOfficeAddress(location.getOfficeAddress());
							catalogLoc.setOutStock(location.getOutStock());
							catalogLoc.setQuantityNotify(location.getQuantityNotify());
							catalogLoc.setStockProduct(location.getStockProduct());
							catalogLoc.setSupplyAbility(location.getSupplyAbility());
							
							if(catalog.getCatalogLocationList() == null){
								catalog.setCatalogLocationList(new ArrayList<CatalogLocation>());
							}
							catalog.getCatalogLocationList().add(catalogLoc);
						}
					}
				}else{
					if(catalog.getCatalogLocationList() == null){
						catalog.setCatalogLocationList(new ArrayList<CatalogLocation>());
					}
					catalog.getCatalogLocationList().add(new CatalogLocation());
				}
				
				
				
				if (catalog.getVendor() != null) {
					List<VendorProfile> vendorProfileList = vendorProfileSession.getVendorProfileByVendorId(catalog.getVendor().getId());
					if (vendorProfileList != null && vendorProfileList.size() > 0) {
						catalog.setVendorProfile(vendorProfileList.get(0));
					}
				}
			}
		}	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("catalogList", catalogList);
		map.put("totalList", catalogCategorySession.countCatalogList(catalogVendor));
		
		return Response.ok(map).build();
	}
	
	@Path("/findECatalog")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findECatalog(CatalogVendor catalogVendor) {
		List<Category> categoryList = null;
		if (catalogVendor.getCategory() != null) {
			categoryList = new ArrayList<Category>();
			categoryList = populateCategoryFromTreeList(catalogVendor.getCategory(), categoryList);
		}
		
		catalogVendor.setCategoryList(categoryList);
		catalogVendor.setCurrentPage((catalogVendor.getCurrentPage()-1) * catalogVendor.getPageSize());
		
		// Di ganti dari User Id menjadi vendor ID (Jangan SALAH)
		if(catalogVendor.getUserId() != null) {
			Vendor vendor = vendorSession.find(catalogVendor.getUserId());
			catalogVendor.setVendor(vendor);
		}
		
		//Filter dengan List User yang satu Organisasi
		List<User> userList = new ArrayList<User>();
		if (catalogVendor.getUserId() != null) {
			
			//Cari Organisasi User di Tabel RoleUser
			List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(catalogVendor.getUserId());
			if(roleUserList != null && roleUserList.size() > 0) {
				Integer orgId = roleUserList.get(0).getOrganisasi().getId();
				List<RoleUser> organisasiRoleUserList = roleUserSession.getRoleUserByOrganisasiAndAppCode(orgId, "PM");
				
				if(organisasiRoleUserList != null && organisasiRoleUserList.size() > 0) {
					for(RoleUser roleUser : organisasiRoleUserList) {
						userList.add(roleUser.getUser());
					}
					catalogVendor.setUserList(userList);
				}
			}
		}

		List<Catalog> catalogList = catalogCategorySession.getActiveCatalogList(catalogVendor);
		if (catalogList != null && catalogList.size() > 0) {
			for (Catalog catalog : catalogList) {
				CatalogCategory catalogCategory = new CatalogCategory();
				catalogCategory.setCatalog(catalog);
				List<CatalogCategory> catalogCategoryList = catalogCategorySession.getCatalogCategoryList(catalogCategory);
				if (catalogCategoryList != null && catalogCategoryList.size() > 0) {
					for (CatalogCategory tempCatalogCategory : catalogCategoryList) {
						catalogCategory = new CatalogCategory();
						
						Category tempCategory = new Category(); 
						tempCategory.setAdminLabel(tempCatalogCategory.getCategory().getAdminLabel());
						tempCategory.setDescription(tempCatalogCategory.getCategory().getDescription());
						tempCategory.setFlagEnabled(tempCatalogCategory.getCategory().getFlagEnabled());
						tempCategory.setId(tempCatalogCategory.getCategory().getId());
						tempCategory.setIsDelete(tempCatalogCategory.getCategory().getIsDelete());
						tempCategory.setTranslateEng(tempCatalogCategory.getCategory().getTranslateEng());
						tempCategory.setTranslateInd(tempCatalogCategory.getCategory().getTranslateInd());
						
						catalogCategory.setCategory(tempCategory);
						
						if (catalog.getCatalogCategoryList() == null) {
							catalog.setCatalogCategoryList(new ArrayList<CatalogCategory>());
						}
						catalog.getCatalogCategoryList().add(catalogCategory);
					}
				}
				
				/** Catalog Images **/
				CatalogImage catalogImage = new CatalogImage();
				catalogImage.setCatalog(catalog);
				List<CatalogImage> catalogImageList = catalogImageSession.getCatalogImageList(catalogImage);
				if (catalogImageList != null && catalogImageList.size() > 0) {
					for (CatalogImage tempCatalogImage : catalogImageList) {
						catalogImage = new CatalogImage();
						catalogImage.setImagesRealName(tempCatalogImage.getImagesRealName());
						catalogImage.setImagesFileName(tempCatalogImage.getImagesFileName());
						
						if (catalog.getCatalogImageList() == null) {
							catalog.setCatalogImageList(new ArrayList<CatalogImage>());
						}
						
						catalog.getCatalogImageList().add(catalogImage);
					}
				}
				
				/** Catalog Attributes **/
				CatalogAttribute catalogAttribute = new CatalogAttribute();
				catalogAttribute.setCatalog(catalog);
				List<CatalogAttribute> catalogAttributeList = catalogAttributeSession.getCatalogAttributeList(catalogAttribute);
				if(catalogAttributeList !=null){
					if(catalogAttributeList.size()>0){
						for(CatalogAttribute attribute : catalogAttributeList){
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------							
							CatalogAttribute catalogAttr = new CatalogAttribute();
							catalogAttr.setId(attribute.getId());
							if(attribute.getNilai() != null) {
								try{
									Integer attOptionId = Integer.valueOf(attribute.getNilai());
									AttributeOption attOption = attributeOptionSession.find(attOptionId);
									if (attOption != null){
									catalogAttr.setNilai(attOption.getName());
									}
									else {
									catalogAttr.setNilai(attribute.getNilai());	
									}
								}catch(Exception E){catalogAttr.setNilai(attribute.getNilai());}
								
							}
				
							catalogAttr.setAttributePanelGroup(attribute.getAttributePanelGroup());
							
							if(catalog.getCatalogAttributeList() ==null){
								catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
							}
							catalog.getCatalogAttributeList().add(catalogAttr);
						}
					}else{
						if(catalog.getCatalogAttributeList() ==null){
							catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
						}
						
						catalog.getCatalogAttributeList().add(new CatalogAttribute());
					}
				}
				
				/** Catalog Location **/
				CatalogLocation catalogLocation = new CatalogLocation();
				catalogLocation.setCatalog(catalog);
				List<CatalogLocation> catalogLocationList = catalogLocationSession.getCatalogLocationList(catalogLocation);
				if(catalogLocationList !=null){
					if(catalogLocationList.size() > 0){
						for(CatalogLocation location : catalogLocationList){
							CatalogLocation catalogLoc = new CatalogLocation();
							catalogLoc.setId(location.getId());
							catalogLoc.setMinimalOrder(location.getMinimalOrder());
							catalogLoc.setOfficeAddress(location.getOfficeAddress());
							catalogLoc.setOutStock(location.getOutStock());
							catalogLoc.setQuantityNotify(location.getQuantityNotify());
							catalogLoc.setStockProduct(location.getStockProduct());
							catalogLoc.setSupplyAbility(location.getSupplyAbility());
							
							if(catalog.getCatalogLocationList() == null){
								catalog.setCatalogLocationList(new ArrayList<CatalogLocation>());
							}
							catalog.getCatalogLocationList().add(catalogLoc);
						}
					}
				}else{
					if(catalog.getCatalogLocationList() == null){
						catalog.setCatalogLocationList(new ArrayList<CatalogLocation>());
					}
					catalog.getCatalogLocationList().add(new CatalogLocation());
				}
				
				
				if (catalog.getVendor() != null) {
					List<VendorProfile> vendorProfileList = vendorProfileSession.getVendorProfileByVendorId(catalog.getVendor().getId());
					if (vendorProfileList != null && vendorProfileList.size() > 0) {
						catalog.setVendorProfile(vendorProfileList.get(0));
					}
				}
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("catalogList", catalogList);
		map.put("totalList", catalogCategorySession.getActiveCatalogListCount(catalogVendor));
		
		return Response.ok(map).build();
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
	
	@Path("/findCatalogProperties")
	@POST
	public Response findCatalogProperties(Catalog catalog) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("itemTypeList", itemTypeSession.getItemTypeList());
		map.put("mataUangList", mataUangSession.getMataUanglist());
		map.put("vendorList", vendorSession.getVendorList());
		map.put("satuanList", satuanSession.getSatuanList());
		map.put("productTypeList", productTypeSession.getProductTypeList());
		
		List<AttributeGroup> attributeGroupList = attributeGroupSession.getAttributeGroupList();
		if (catalog != null && catalog.getId() != null) {
			CatalogAttribute tempCatalogAttribute = new CatalogAttribute();
			tempCatalogAttribute.setCatalog(catalog);
			List<CatalogAttribute> catalogAttributeList = catalogAttributeSession.getCatalogAttributeList(tempCatalogAttribute);
			if (catalogAttributeList != null && catalogAttributeList.size() > 0) {
				for (CatalogAttribute catalogAttribute : catalogAttributeList) {
					if (attributeGroupList != null && attributeGroupList.size() > 0) {
						for (AttributeGroup attributeGroup : attributeGroupList) {
							Boolean nextState = false;
							if (attributeGroup.getAttributePanelGroupList() != null && attributeGroup.getAttributePanelGroupList().size() > 0) {
								for (AttributePanelGroup attributePanelGroup : attributeGroup.getAttributePanelGroupList()) {
									if (attributePanelGroup.getId() == catalogAttribute.getAttributePanelGroup().getId()) {
										if (attributePanelGroup.getAttribute() != null  && attributePanelGroup.getAttribute().getInputType() != null
												&& attributePanelGroup.getAttribute().getInputType().getName().equals("Checkbox") == false) {
											attributePanelGroup.setValue(catalogAttribute.getNilai());
										} else {
											String strValue = catalogAttribute.getNilai();
											if (strValue != null) {
												String[] splitValue = strValue.split(",");
												if (attributePanelGroup.getAttribute().getAttributeOptionList() != null && attributePanelGroup.getAttribute().getAttributeOptionList().size() > 0) {
													for (AttributeOption attributeOption : attributePanelGroup.getAttribute().getAttributeOptionList()) {
														for (String checkValue : splitValue) {
															if (checkValue.equals(attributeOption.getId().toString())) {
																attributeOption.setValue(checkValue);
																break;
															}
														}
													}
												}
											}
										}
										nextState = true;
										break;
									}
								}
							}
							if (nextState) {
								break;
							}
						}
					}
				}
			}
		}
		map.put("attributeGroupList", attributeGroupList);
		
		List<Category> categoryList = categorySession.getCategoryForTreeList(null);
		if (catalog != null && catalog.getId() != null) {			
			CatalogCategory tempCatalogCategory = new CatalogCategory();
			tempCatalogCategory.setCatalog(catalog);
			List<CatalogCategory> catalogCategoryList = catalogCategorySession.getCatalogCategoryList(tempCatalogCategory);
			if (catalogCategoryList != null && catalogCategoryList.size() > 0) {
				for (CatalogCategory catalogCategory : catalogCategoryList) {
					if (categoryList != null && categoryList.size() > 0) {
						for (Category category : categoryList) {
							if (catalogCategory.getCategory().getId() == category.getId()) {
								category.setValue(true);
								break;
							}
						}
					}
				}
			}
		}
		List<Category> categoryTreeList = new ArrayList<Category>();
		
		if (categoryList != null && categoryList.size() > 0) {
			for (Category category : categoryList) {
				if (category.getParentCategory() == null) {
					Category tempCategory = new Category();
					tempCategory.setId(category.getId());
					tempCategory.setAdminLabel(category.getAdminLabel());
					tempCategory.setDescription(category.getDescription());
					tempCategory.setFlagEnabled(category.getFlagEnabled());
					tempCategory.setIsDelete(category.getIsDelete());
					tempCategory.setTranslateEng(category.getTranslateEng());
					tempCategory.setTranslateInd(category.getTranslateInd());
					tempCategory.setValue(category.getValue());
					
					categoryTreeList.add(tempCategory);
				} else {
					StaticUtility.populateTreeList(category, categoryTreeList, false);
				}
			}
		}
		map.put("categoryList", categoryTreeList);	
		return Response.ok(map).build();
	}
	
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Catalog save (Catalog catalog, @HeaderParam("Authorization") String token) {
		
		//validasi data satuan harus terisi
		if (catalog.getSatuan() !=null){
			Token tempToken = tokenSession.findByToken(token);
			
			Catalog tempCatalog = simpanCatalogWithItem(catalog, tempToken);
			
			simpanCatalogCategory(tempCatalog, catalog, tempToken);
			simpanCatalogPriceRange(tempCatalog, catalog, tempToken);
			simpanCatalogLocation(tempCatalog, catalog, tempToken);
			simpanCatalogImage(tempCatalog, catalog, tempToken);
			simpanCatalogAttribute(tempCatalog, catalog, tempToken);
			
			return catalog;
		}else{
			catalog.setSatuan(null);
			return catalog;
		}
		
	}
	
	protected Catalog simpanCatalogWithItem(Catalog catalog, Token token) {
//		Item item = null;
//		if (catalog.getItem() != null) {
//			item = itemSession.find(catalog.getItem().getId());
//			item.setDeskripsi(catalog.getDeskripsiIND());
//			item.setItemType(catalog.getItemType());
//			item.setKode(catalog.getKodeItem());
//			item.setNama(catalog.getNamaIND());
//			item.setSatuanId(catalog.getSatuan());
//			item.setIsDelete(0);
//		} else {
//			item = new Item();
//			item.setDeskripsi(catalog.getDeskripsiIND());
//			item.setItemType(catalog.getItemType());
//			item.setKode(catalog.getKodeItem());
//			item.setNama(catalog.getNamaIND());
//			item.setSatuanId(catalog.getSatuan());
//			item.setIsDelete(0);
//		}
//		catalog.setItem(item);
		Boolean isInsert = true;
		if (token.getUser() != null) {
			Vendor vendor = vendorSession.getVendorByUserId(token.getUser().getId());
			if (vendor != null) {
				catalog.setIsVendor(true);
				catalog.setStatus(true);
				catalog.setVendor(vendor);
				isInsert = false;
			} else {
				if(catalog.getStatus() == false) {
					catalog.setStatus(false);
				} else {
					catalog.setStatus(true);
				}
				catalog.setIsVendor(true);
				
				// check kode panitia if exist
				Catalog tempCatalog = new Catalog();
				tempCatalog.setKodeProdukPanitia(catalog.getKodeProdukPanitia());
				List<Catalog> checkCatalogList = catalogSession.getCatalogList(tempCatalog);
				if (checkCatalogList != null && checkCatalogList.size() > 0) {
					isInsert = false;
				}
			}
		} else {
			catalog.setIsVendor(false);
			isInsert = false;
		}
		
		if (catalog.getItem() != null && catalog.getItem().getId() != null) {
			catalog.setItem(itemSession.getItem(catalog.getItem().getId()));
			
			catalog.setKodeItem(catalog.getItem().getKode());
		}
		
		if (catalog.getId() != null && isInsert == false) {			
			return catalogSession.updateCatalog(catalog, token);
		} else {
			catalog.setId(null);
			return catalogSession.insertCatalog(catalog, token);
		}
	}
	
	protected List<Category> findCategoryFromCatalog(List<Category> categoryList, List<Category> resultCategory) {
		if (categoryList != null && categoryList.size() > 0) {
			for (Category category : categoryList) {
				if (category.getValue() != null && category.getValue()) {
					resultCategory.add(categorySession.find(category.getId()));
				}
				findCategoryFromCatalog(category.getCategoryChildList(), resultCategory);
			}
		}
		return resultCategory;
	}
	
	protected void simpanCatalogCategory(Catalog catalogBackend, Catalog catalogFrontend, Token tempToken) {
		List<Category> categoryList = new ArrayList<Category>();
		categoryList = findCategoryFromCatalog(catalogFrontend.getCategoryList(), categoryList);
		int lenCategoryList = 0;
		if (categoryList.size() > 0) {
			lenCategoryList = categoryList.size();
		}
		
		CatalogCategory catalogCategory = new CatalogCategory();
		catalogCategory.setCatalog(catalogBackend);
		List<CatalogCategory> catalogCategoryList = catalogCategorySession.getCatalogCategoryList(catalogCategory);
		int lenCatalogCategoryList = 0;
		if (catalogCategoryList.size() > 0) {
			lenCatalogCategoryList = catalogCategoryList.size();
		}
		
		if (lenCatalogCategoryList >= lenCategoryList) {
			// update sebanyak category dan hapus sisanya
			for (int a = 0; a < lenCategoryList; a++) {
				catalogCategory = catalogCategoryList.get(a);
				catalogCategory.setCategory(categoryList.get(a));
				catalogCategory.setCatalog(catalogBackend);
				catalogCategorySession.updateCatalogCategory(catalogCategory, tempToken);
			}
			for (int a = lenCategoryList; a < lenCatalogCategoryList; a++) {
				catalogCategory = catalogCategoryList.get(a);
				catalogCategorySession.deleteRowCatalogCategory(catalogCategory.getId(), tempToken);
			}
		} else {
			for (int a = 0; a < lenCatalogCategoryList; a++) {
				catalogCategory = catalogCategoryList.get(a);
				catalogCategory.setCategory(categoryList.get(a));
				catalogCategory.setCatalog(catalogBackend);
				catalogCategorySession.updateCatalogCategory(catalogCategory, tempToken);
			}
			for (int a = lenCatalogCategoryList; a < lenCategoryList; a++) {
				catalogCategory = new CatalogCategory();
				catalogCategory.setCategory(categoryList.get(a));
				catalogCategory.setCatalog(catalogBackend);
				catalogCategory.setIsDelete(0);
				catalogCategorySession.insertCatalogCategory(catalogCategory, tempToken);
			}
		}
	}
	
	protected void simpanCatalogPriceRange(Catalog catalogBackend, Catalog catalogFrontend, Token tempToken) {
		CatalogPriceRange catalogPriceRange = new CatalogPriceRange();
		catalogPriceRange.setCatalog(catalogBackend);
		List<CatalogPriceRange> catalogPriceRangeList = catalogPriceRangeSession.getCatalogPriceRangeList(catalogPriceRange);
		
		int lenOldPriceRange = 0;
		if (catalogPriceRangeList.size() > 0) {
			lenOldPriceRange = catalogPriceRangeList.size();
		}
		int lenNewPriceRange = 0;
		if (catalogFrontend.getCatalogPriceRangeList() != null && catalogFrontend.getCatalogPriceRangeList().size() > 0) {
			lenNewPriceRange = catalogFrontend.getCatalogPriceRangeList().size();
		}
		
		if (lenOldPriceRange >= lenNewPriceRange) {
			for (int a = 0; a < lenNewPriceRange; a++) {
				catalogPriceRange = catalogPriceRangeList.get(a);
				catalogPriceRange.setCatalog(catalogBackend);
				CatalogPriceRange tempCatalogPriceRange = catalogFrontend.getCatalogPriceRangeList().get(a);
				catalogPriceRange.setFromPrice(tempCatalogPriceRange.getFromPrice());
				catalogPriceRange.setHarga(tempCatalogPriceRange.getHarga());
				catalogPriceRange.setToPrice(tempCatalogPriceRange.getToPrice());
				
				catalogPriceRangeSession.updateCatalogPriceRange(catalogPriceRange, tempToken);
			}
			for (int a = lenNewPriceRange; a < lenOldPriceRange; a++) {
				catalogPriceRange = catalogPriceRangeList.get(a);
				catalogPriceRangeSession.deleteRowCatalogPriceRange(catalogPriceRange.getId(), tempToken);
			}
		} else {
			for (int a = 0; a < lenOldPriceRange; a++) {
				catalogPriceRange = catalogPriceRangeList.get(a);
				catalogPriceRange.setCatalog(catalogBackend);
				CatalogPriceRange tempCatalogPriceRange = catalogFrontend.getCatalogPriceRangeList().get(a);
				catalogPriceRange.setFromPrice(tempCatalogPriceRange.getFromPrice());
				catalogPriceRange.setHarga(tempCatalogPriceRange.getHarga());
				catalogPriceRange.setToPrice(tempCatalogPriceRange.getToPrice());
				
				catalogPriceRangeSession.updateCatalogPriceRange(catalogPriceRange, tempToken);
			}
			for (int a = lenOldPriceRange; a < lenNewPriceRange; a++) {
				catalogPriceRange = new CatalogPriceRange();
				catalogPriceRange.setCatalog(catalogBackend);
				CatalogPriceRange tempCatalogPriceRange = catalogFrontend.getCatalogPriceRangeList().get(a);
				catalogPriceRange.setFromPrice(tempCatalogPriceRange.getFromPrice());
				catalogPriceRange.setHarga(tempCatalogPriceRange.getHarga());
				catalogPriceRange.setToPrice(tempCatalogPriceRange.getToPrice());
				catalogPriceRange.setIsDelete(0);
				
				catalogPriceRangeSession.insertCatalogPriceRange(catalogPriceRange, tempToken);
			}
		}
	}
	
	protected void simpanCatalogLocation(Catalog catalogBackend, Catalog catalogFrontend, Token tempToken) {
		CatalogLocation catalogLocation = new CatalogLocation();
		catalogLocation.setCatalog(catalogBackend);
		List<CatalogLocation> catalogLocationList = catalogLocationSession.getCatalogLocationList(catalogLocation);
		
		int lenOldLocation = 0;
		if (catalogLocationList.size() > 0) {
			lenOldLocation = catalogLocationList.size();
		}
		int lenNewLocation = 0;
		if (catalogFrontend.getCatalogLocationList() != null && catalogFrontend.getCatalogLocationList().size() > 0) {
			lenNewLocation = catalogFrontend.getCatalogLocationList().size();
		}
		
		if (lenOldLocation >= lenNewLocation) {
			for (int a = 0; a < lenNewLocation; a++) {
				catalogLocation = catalogLocationList.get(a);
				catalogLocation.setCatalog(catalogBackend);
				CatalogLocation tempCatalogLocation = catalogFrontend.getCatalogLocationList().get(a);
				catalogLocation.setMinimalOrder(tempCatalogLocation.getMinimalOrder());
				catalogLocation.setOfficeAddress(tempCatalogLocation.getOfficeAddress());
				catalogLocation.setOutStock(tempCatalogLocation.getOutStock());
				catalogLocation.setQuantityNotify(tempCatalogLocation.getQuantityNotify());
				catalogLocation.setStockProduct(tempCatalogLocation.getStockProduct());
				catalogLocation.setSupplyAbility(tempCatalogLocation.getSupplyAbility());
				
				catalogLocationSession.updateCatalogLocation(catalogLocation, tempToken);
			}
			for (int a = lenNewLocation; a < lenOldLocation; a++) {
				catalogLocation = catalogLocationList.get(a);
				catalogLocationSession.deleteRowCatalogLocation(catalogLocation.getId(), tempToken);
			}
		} else {
			for (int a = 0; a < lenOldLocation; a++) {
				catalogLocation = catalogLocationList.get(a);
				catalogLocation.setCatalog(catalogBackend);
				CatalogLocation tempCatalogLocation = catalogFrontend.getCatalogLocationList().get(a);
				catalogLocation.setMinimalOrder(tempCatalogLocation.getMinimalOrder());
				catalogLocation.setOfficeAddress(tempCatalogLocation.getOfficeAddress());
				catalogLocation.setOutStock(tempCatalogLocation.getOutStock());
				catalogLocation.setQuantityNotify(tempCatalogLocation.getQuantityNotify());
				catalogLocation.setStockProduct(tempCatalogLocation.getStockProduct());
				catalogLocation.setSupplyAbility(tempCatalogLocation.getSupplyAbility());
				
				catalogLocationSession.updateCatalogLocation(catalogLocation, tempToken);
			}
			for (int a = lenOldLocation; a < lenNewLocation; a++) {
				catalogLocation = new CatalogLocation();
				catalogLocation.setCatalog(catalogBackend);
				CatalogLocation tempCatalogLocation = catalogFrontend.getCatalogLocationList().get(a);
				catalogLocation.setMinimalOrder(tempCatalogLocation.getMinimalOrder());
				catalogLocation.setOfficeAddress(tempCatalogLocation.getOfficeAddress());
				catalogLocation.setOutStock(tempCatalogLocation.getOutStock());
				catalogLocation.setQuantityNotify(tempCatalogLocation.getQuantityNotify());
				catalogLocation.setStockProduct(tempCatalogLocation.getStockProduct());
				catalogLocation.setSupplyAbility(tempCatalogLocation.getSupplyAbility());
				catalogLocation.setIsDelete(0);
				
				catalogLocationSession.insertCatalogLocation(catalogLocation, tempToken);
			}
		}
	}
	
	protected void simpanCatalogAttribute(Catalog catalogBackend, Catalog catalogFrontend, Token tempToken) {
		CatalogAttribute catalogAttribute = new CatalogAttribute();
		catalogAttribute.setCatalog(catalogBackend);
		List<CatalogAttribute> catalogAttributeList = catalogAttributeSession.getCatalogAttributeList(catalogAttribute);
		
		int lenOldAttribute = 0;
		if (catalogAttributeList != null && catalogAttributeList.size() > 0) {
			lenOldAttribute = catalogAttributeList.size();
		}
		int lenNewAttribute = 0;
		if (catalogFrontend.getAttributeGroup() != null && catalogFrontend.getAttributeGroup().getAttributePanelGroupList() != null && catalogFrontend.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
			lenNewAttribute = catalogFrontend.getAttributeGroup().getAttributePanelGroupList().size();
		}
		
		if (catalogFrontend.getAttributeGroup() != null && catalogFrontend.getAttributeGroup().getAttributePanelGroupList() != null
				&& catalogFrontend.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
			for (AttributePanelGroup attributePanelGroup : catalogFrontend.getAttributeGroup().getAttributePanelGroupList()) {
				if (attributePanelGroup.getAttribute() != null && attributePanelGroup.getAttribute().getInputType() != null
						&& attributePanelGroup.getAttribute().getInputType().getName().equals("Checkbox")
						&& attributePanelGroup.getAttribute().getAttributeOptionList() != null
						&& attributePanelGroup.getAttribute().getAttributeOptionList().size() > 0) {
					String strValue = "";
					for (AttributeOption attributeOption : attributePanelGroup.getAttribute().getAttributeOptionList()) {
						if (attributeOption.getValue() != null && attributeOption.getValue().equals("false") == false) {
							if (strValue != null && strValue.length() > 0) {
								strValue += "," + attributeOption.getValue();
							} else {
								strValue = attributeOption.getValue();
							}
						}
					}
					if (strValue != null && strValue.length() > 0) {
						attributePanelGroup.setValue(strValue);
					}
				}
			}
		}
		
		if (lenOldAttribute >= lenNewAttribute) {
			for (int a = 0; a < lenNewAttribute; a++) {
				catalogAttribute = catalogAttributeList.get(a);
				catalogAttribute.setCatalog(catalogBackend);
				AttributePanelGroup tempAttributePanelGroup = catalogFrontend.getAttributeGroup().getAttributePanelGroupList().get(a);
				catalogAttribute.setAttributePanelGroup(tempAttributePanelGroup);
				catalogAttribute.setNilai(tempAttributePanelGroup.getValue());
				
				catalogAttributeSession.updateCatalogAttribute(catalogAttribute, tempToken);
			}
			for (int a = lenNewAttribute; a < lenOldAttribute; a++) {
				catalogAttribute = catalogAttributeList.get(a);
				catalogAttributeSession.deleteRowCatalogAttribute(catalogAttribute.getId(), tempToken);
			}
		} else {
			for (int a = 0; a < lenOldAttribute; a++) {
				catalogAttribute = catalogAttributeList.get(a);
				catalogAttribute.setCatalog(catalogBackend);
				AttributePanelGroup tempAttributePanelGroup = catalogFrontend.getAttributeGroup().getAttributePanelGroupList().get(a);
				catalogAttribute.setAttributePanelGroup(tempAttributePanelGroup);
				catalogAttribute.setNilai(tempAttributePanelGroup.getValue());
				
				catalogAttributeSession.updateCatalogAttribute(catalogAttribute, tempToken);
			}
			for (int a = lenOldAttribute; a < lenNewAttribute; a++) {
				catalogAttribute = new CatalogAttribute();
				catalogAttribute.setCatalog(catalogBackend);
				AttributePanelGroup tempAttributePanelGroup = catalogFrontend.getAttributeGroup().getAttributePanelGroupList().get(a);
				catalogAttribute.setAttributePanelGroup(tempAttributePanelGroup);
				catalogAttribute.setIsDelete(0);
				catalogAttribute.setNilai(tempAttributePanelGroup.getValue());
				
				catalogAttributeSession.insertCatalogAttribute(catalogAttribute, tempToken);
			}
		}
	}
		
	protected void simpanCatalogImage(Catalog catalogBackend, Catalog catalogFrontend, Token tempToken) {
		CatalogImage catalogImage = new CatalogImage();
		catalogImage.setCatalog(catalogBackend);
		List<CatalogImage> catalogImageList = catalogImageSession.getCatalogImageList(catalogImage);
		
		int lenOldImage = 0;
		if (catalogImageList.size() > 0) {
			lenOldImage = catalogImageList.size();
		}
		int lenNewImage = 0;
		if (catalogFrontend.getCatalogImageList() != null && catalogFrontend.getCatalogImageList().size() > 0) {
			lenNewImage = catalogFrontend.getCatalogImageList().size();
		}
		
		if (lenOldImage >= lenNewImage) {
			for (int a = 0; a < lenNewImage; a++) {
				catalogImage = catalogImageList.get(a);
				catalogImage.setCatalog(catalogBackend);
				CatalogImage tempCatalogImage = catalogFrontend.getCatalogImageList().get(a);
				catalogImage.setImagesFileName(tempCatalogImage.getImagesFileName());
				catalogImage.setImagesFileSize(tempCatalogImage.getImagesFileSize());
				catalogImage.setImagesRealName(tempCatalogImage.getImagesRealName());
				catalogImage.setStatus(tempCatalogImage.getStatus());
				catalogImage.setUrutanPesanan(tempCatalogImage.getUrutanPesanan());
				
				catalogImageSession.updateCatalogImage(catalogImage, tempToken);
			}
			for (int a = lenNewImage; a < lenOldImage; a++) {
				catalogImage = catalogImageList.get(a);
				catalogImageSession.deleteRowCatalogImage(catalogImage.getId(), tempToken);
			}
		} else {
			for (int a = 0; a < lenOldImage; a++) {
				catalogImage = catalogImageList.get(a);
				catalogImage.setCatalog(catalogBackend);
				CatalogImage tempCatalogImage = catalogFrontend.getCatalogImageList().get(a);
				catalogImage.setImagesFileName(tempCatalogImage.getImagesFileName());
				catalogImage.setImagesFileSize(tempCatalogImage.getImagesFileSize());
				catalogImage.setImagesRealName(tempCatalogImage.getImagesRealName());
				catalogImage.setStatus(tempCatalogImage.getStatus());
				catalogImage.setUrutanPesanan(tempCatalogImage.getUrutanPesanan());
				
				catalogImageSession.updateCatalogImage(catalogImage, tempToken);
			}
			for (int a = lenOldImage; a < lenNewImage; a++) {
				catalogImage = new CatalogImage();
				catalogImage.setCatalog(catalogBackend);
				CatalogImage tempCatalogImage = catalogFrontend.getCatalogImageList().get(a);
				catalogImage.setImagesFileName(tempCatalogImage.getImagesFileName());
				catalogImage.setImagesFileSize(tempCatalogImage.getImagesFileSize());
				catalogImage.setImagesRealName(tempCatalogImage.getImagesRealName());
				catalogImage.setStatus(tempCatalogImage.getStatus());
				catalogImage.setUrutanPesanan(tempCatalogImage.getUrutanPesanan());
				catalogImage.setIsDelete(0);
				
				catalogImageSession.insertCatalogImage(catalogImage, tempToken);
			}
		}
	}
	
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Catalog delete (Catalog catalog, @HeaderParam("Authorization") String token) {
		catalogAttributeSession.deleteCatalogAttributeList(catalog.getId());
		catalogCategorySession.deleteCatalogCategoryList(catalog.getId());
		catalogImageSession.deleteCatalogImageList(catalog.getId());
//		catalogKontrakSession.deleteCatalogKontrakList(catalog.getId());
		catalogLocationSession.deleteCatalogLocationList(catalog.getId());
		catalogPriceRangeSession.deleteCatalogPriceRangeList(catalog.getId());
		Catalog catalogDelete = catalogSession.deleteCatalog(catalog.getId(), tokenSession.findByToken(token));
		return catalogDelete;
	}
	
	@POST
	@Path("/readExcell")
	@Consumes("multipart/form-data")
	public Response readExcell(MultipartFormDataInput input, @HeaderParam("Authorization") String token) {

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");

		for (InputPart inputPart : inputParts) {
			MultivaluedMap<String, String> header = inputPart.getHeaders();
			String fileName = TemplateXls.getFileNameTemplateXls(header);

			Workbook workbook = null;

			try {

				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
				if (fileName.endsWith("xlsx")) {
	        		workbook = new XSSFWorkbook(inputStream);
	            } else if (fileName.endsWith("xls")) {
	                workbook = new HSSFWorkbook(inputStream);
	            }
				
				analyseExcel(workbook, token);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return Response.ok().build();

	}
    
    protected void analyseExcel(Workbook workbook, String token) {
		InputType inputTypeDate = new InputType();
    	inputTypeDate.setName("Date");
		List<InputType> inputTypeList = inputTypeSession.getInputTypeList(inputTypeDate);
		if (inputTypeList != null && inputTypeList.size() > 0) {
			inputTypeDate = inputTypeList.get(0);
		}
		
		InputType inputTypeText = new InputType();
		inputTypeText.setName("Text");
		inputTypeList = inputTypeSession.getInputTypeList(inputTypeText);
		if (inputTypeList != null && inputTypeList.size() > 0) {
			inputTypeText = inputTypeList.get(0);
		}
		
		Token tempToken = tokenSession.findByToken(token);
		
    	int activeSheet = workbook.getNumberOfSheets();
    	for (int sheetNum = 0; sheetNum < activeSheet; sheetNum++) {
    		if (workbook.isSheetHidden(sheetNum) == false) {
    			Sheet sheet = workbook.getSheetAt(sheetNum);
    			    			
                Iterator<Row> iterator = sheet.iterator();
                Boolean header = true;
                AttributeGroup attributeGroup = null;
                // 18 first column static attribute
                // next ones is dynamic attribute need to check
                while (iterator.hasNext()) {
                    Row nextRow = iterator.next();
                	int lastCellNumber = nextRow.getLastCellNum();
                	Attribute[] attributeList = null;
                	if(nextRow.getCell(4) != null) {
                		if (header) {                    	
                        	if (lastCellNumber > 17) {
                        		attributeList = new Attribute[lastCellNumber+1];
                        	}
                        	Iterator<Cell> cellIterator = nextRow.iterator();
                        	while (cellIterator.hasNext()) {
                        		Cell headerCell = cellIterator.next();
                        		int currentCellColumn = headerCell.getColumnIndex();
                        		// check if got dynamic attribute
                        		if (currentCellColumn > 17) {
                        			// check Attribute exist
                        			Attribute tempAttribute = new Attribute();
                        			tempAttribute.setName(headerCell.getStringCellValue());
                        			List<Attribute> tempAttributeList = attributeSession.getAttributeList(tempAttribute);
                        			if (tempAttributeList != null && tempAttributeList.size() > 0) {
                        				tempAttribute = tempAttributeList.get(0);
                        			} else {
                        				tempAttribute.setConfigurable(true);
                        				tempAttribute.setFlagEnabled(true);
                        				tempAttribute.setRequired(true);
                        				tempAttribute.setSearchable(true);
                        				tempAttribute.setSortable(true);
                        				tempAttribute.setTranslateInd(headerCell.getStringCellValue());
                        				tempAttribute.setUnique(true);
                        				
                        				// check input type
                            			int rowNum = nextRow.getRowNum();
                            			int lastRowNum = sheet.getLastRowNum();
                            			for (int checkRowIndex = rowNum + 1; checkRowIndex <= lastRowNum; checkRowIndex++) {
                            				Row checkRow = sheet.getRow(checkRowIndex);
                            				Cell checkCell = checkRow.getCell(currentCellColumn);
                            				
                            				if (checkCell != null) {
                            					if (checkCell.getCellType() != Cell.CELL_TYPE_BLANK && checkCell.getCellType() == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(checkCell)) {
                            						tempAttribute.setInputType(inputTypeDate);
                                				} else {
                                					tempAttribute.setInputType(inputTypeText);
                                				}
                            					break;
                            				}
                            			}
                            			
                            			tempAttribute = attributeSession.insertAttribute(tempAttribute, tempToken);
                        			}
                        			attributeList[currentCellColumn] = tempAttribute;
                        		}
                        	}
                        	if (lastCellNumber > 17) {
                        		String sheetName = sheet.getSheetName();
                    			attributeGroup = new AttributeGroup();
                    			attributeGroup.setName(sheetName);
                    			List<AttributeGroup> attributeGroupList = attributeGroupSession.getAttributeGroupList(attributeGroup);
                    			if (attributeGroupList != null && attributeGroupList.size() > 0) {
                    				attributeGroup = attributeGroupList.get(0);
                    			} else {
                    				attributeGroup.setFlagEnabled(true);
                    				attributeGroup.setIsDelete(0);
                    			}
                    			if (attributeGroup.getAttributePanelGroupList() == null) {
                    				attributeGroup.setAttributePanelGroupList(new ArrayList<AttributePanelGroup>());
                    			}
                    			int urutan = 1;
                    			for (int a=18; a<lastCellNumber;a++) {
                    				if (attributeList[a].getId() != null) {
                    					AttributePanelGroup attributePanelGroup = new AttributePanelGroup();
                        				attributePanelGroup.setAttribute(attributeList[a]);
                        				attributePanelGroup.setIsDelete(0);
                        				attributePanelGroup.setName(sheetName);
                        				attributePanelGroup.setUrutan(urutan++);
                        				
                        				attributeGroup.getAttributePanelGroupList().add(attributePanelGroup);
                    				}
                    			}
                    			
                    			if (attributeGroup.getId() != null) {
                    				attributeGroup = attributeGroupSession.updateAttributeGroup(attributeGroup, tempToken);
                    			} else {
                    				attributeGroup = attributeGroupSession.insertAttributeGroup(attributeGroup, tempToken);
                    			}
                        	}
                        } else {
                        	Catalog tempCatalog = new Catalog();
                        	if (attributeGroup != null) {
                        		tempCatalog.setAttributeGroup(attributeGroup);
                        	}
                        	Cell cell = nextRow.getCell(4);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        		String kodeProduk = cell.getStringCellValue();
                        		Catalog checkCatalog = new Catalog();
                        		checkCatalog.setKodeProduk(kodeProduk);
                        		List<Catalog> catalogList = catalogSession.getCatalogList(checkCatalog);
                        		if (catalogList != null && catalogList.size() > 0) {
                        			tempCatalog = catalogList.get(0);
                        		} else {
                        			tempCatalog.setKodeProduk(kodeProduk.toUpperCase());
                        		}
                        	}
                        	cell = nextRow.getCell(0);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        		List<ItemType> itemTypeList = itemTypeSession.getItemTypeListByNameEqual(cell.getStringCellValue());
                    			if (itemTypeList != null && itemTypeList.size() > 0) {
                    				tempCatalog.setItemType(itemTypeList.get(0));
                    			}
                        	}
                        	cell = nextRow.getCell(1);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        		List<Vendor> vendorList = vendorSession.getVendorListByNameEqual(cell.getStringCellValue());
                    			if (vendorList != null && vendorList.size() > 0) {
                    				tempCatalog.setVendor(vendorList.get(0));
                    			}
                        	}
                        	cell = nextRow.getCell(2);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        		tempCatalog.setNamaIND(cell.getStringCellValue());
                        	}
                        	cell = nextRow.getCell(3);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        		tempCatalog.setDeskripsiIND(cell.getStringCellValue());
                        	}
                        	cell = nextRow.getCell(5);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        		List<Satuan> satuanList = satuanSession.getSatuanListByNameEqual(cell.getStringCellValue());
                    			if (satuanList != null && satuanList.size() > 0) {
                    				tempCatalog.setSatuan(satuanList.get(0));
                    			}
                        	}
                        	cell = nextRow.getCell(6);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        		tempCatalog.setHarga(cell.getNumericCellValue());
                        	}
                        	cell = nextRow.getCell(7);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        		List<MataUang> mataUangList = mataUangSession.getMataUangListByKodeEqual(cell.getStringCellValue());
                    			if (mataUangList != null && mataUangList.size() > 0) {
                    				tempCatalog.setMataUang(mataUangList.get(0));
                    			}
                        	}
                        	cell = nextRow.getCell(8);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        		tempCatalog.setBerat((Double)(cell.getNumericCellValue()));
                        	}
                        	cell = nextRow.getCell(9);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        		tempCatalog.setSatuanBerat(cell.getStringCellValue().toLowerCase());
                        	}
                        	cell = nextRow.getCell(10);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        		tempCatalog.setPanjang((Double)(cell.getNumericCellValue()));
                        	}
                        	cell = nextRow.getCell(11);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        		tempCatalog.setSatuanPanjang(cell.getStringCellValue().toLowerCase());
                        	}
                        	cell = nextRow.getCell(12);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        		tempCatalog.setLebar((Double)(cell.getNumericCellValue()));
                        	}
                        	cell = nextRow.getCell(13);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        		tempCatalog.setSatuanLebar(cell.getStringCellValue().toLowerCase());
                        	}
                        	cell = nextRow.getCell(14);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        		tempCatalog.setTinggi((Double)(cell.getNumericCellValue()));
                        	}
                        	cell = nextRow.getCell(15);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        		tempCatalog.setSatuanTinggi(cell.getStringCellValue().toLowerCase());
                        	}
                        	
                        	tempCatalog = simpanCatalogWithItem(tempCatalog, tempToken);
                        	
                        	cell = nextRow.getCell(16);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        		// skip this
                        	}
                        	
                        	cell = nextRow.getCell(17);
                        	if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        		CatalogLocation catalogLocation = new CatalogLocation();
                        		catalogLocation.setCatalog(tempCatalog);
                        		List<CatalogLocation> catalogLocationList = catalogLocationSession.getCatalogLocationList(catalogLocation);
                        		if (catalogLocationList != null && catalogLocationList.size() > 0) {
                        			catalogLocation = catalogLocationList.get(0);
                        		} else {
                        			catalogLocation.setIsDelete(0);
                        		}
                        		catalogLocation.setStockProduct((Double)(cell.getNumericCellValue()));
                        		
                        		if (catalogLocation.getId() != null) {
                        			catalogLocationSession.updateCatalogLocation(catalogLocation, tempToken);
                        		} else {
                        			catalogLocationSession.insertCatalogLocation(catalogLocation, tempToken);
                        		}
                        	}
                        	// catalog attribute
                        	int cn = 0;
                        	for (int a=18; a<lastCellNumber;a++) {
                        		cell = nextRow.getCell(a);
                        		CatalogAttribute catalogAttribute = new CatalogAttribute();
                        		catalogAttribute.setCatalog(tempCatalog);
                        		if (attributeGroup != null && attributeGroup.getAttributePanelGroupList() != null && attributeGroup.getAttributePanelGroupList().size() > 0) {
                        			catalogAttribute.setAttributePanelGroup(attributeGroup.getAttributePanelGroupList().get(cn));
                        		}
                        		cn++;
                        		List<CatalogAttribute> catalogAttributeList = catalogAttributeSession.getCatalogAttributeList(catalogAttribute);
                        		if (catalogAttributeList != null && catalogAttributeList.size() > 0) {
                        			catalogAttribute = catalogAttributeList.get(0);
                        		}
                        		catalogAttribute.setIsDelete(0);
                        		if (cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)) {
                        			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        			Date tanggal = cell.getDateCellValue();
                        			catalogAttribute.setNilai(df.format(tanggal));
                        		}
                        		if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        			catalogAttribute.setNilai(Double.toString(cell.getNumericCellValue()));
                            	}
                        		if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        			catalogAttribute.setNilai(cell.getStringCellValue().toLowerCase());
                            	}
                        		if (catalogAttribute.getId() != null) {
                        			catalogAttributeSession.updateCatalogAttribute(catalogAttribute, tempToken);
                        		} else {
                        			catalogAttributeSession.insertCatalogAttribute(catalogAttribute, tempToken);
                        		}
                        	}
                        }
                        header = false;
                	}
                    
                }
    		}
    	}
    	
    	try {
			//workbook.close(); gw matiin dulu mas bikin error java compiler.
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @Path("/simpanRating")
	@POST
	public Catalog simpanRating (
			@FormParam("catalogId")Integer catalogId, 
			@HeaderParam("Authorization") String token) {
    	
		Token tempToken = tokenSession.findByToken(token);
		Catalog catalog = catalogSession.find(catalogId);
		
		Double jumlahRating = new Double(0);
		CatalogComment catalogComment = new CatalogComment();
		catalogComment.setCatalog(catalog);
		List<CatalogComment> catalogCommentList = catalogCommentSession.getCatalogCommentList(catalogComment);
		if(catalogCommentList.size() > 0) {
			for(CatalogComment catCom : catalogCommentList) {
				jumlahRating += catCom.getRating();
			}
			jumlahRating = Math.ceil(jumlahRating / catalogCommentList.size());
		}
		
		catalog.setRating(jumlahRating.intValue());
		
		/** Catalog Image **/
		CatalogImage catalogImage = new CatalogImage();
		catalogImage.setCatalog(catalog);
		List<CatalogImage> catalogImageList = catalogImageSession.getCatalogImageList(catalogImage);
		if (catalogImageList != null && catalogImageList.size() > 0) {
			for (CatalogImage tempCatalogImage : catalogImageList) {
				catalogImage = new CatalogImage();
				catalogImage.setImagesRealName(tempCatalogImage.getImagesRealName());
				
				if (catalog.getCatalogImageList() == null) {
					catalog.setCatalogImageList(new ArrayList<CatalogImage>());
				}
				
				catalog.getCatalogImageList().add(catalogImage);
			}
		}
		
		/** Catalog Attributes **/
		CatalogAttribute catalogAttribute = new CatalogAttribute();
		catalogAttribute.setCatalog(catalog);
		List<CatalogAttribute> catalogAttributeList = catalogAttributeSession.getCatalogAttributeList(catalogAttribute);
		if(catalogAttributeList !=null){
			if(catalogAttributeList.size()>0){
				for(CatalogAttribute attribute : catalogAttributeList){
					
					CatalogAttribute catalogAttr = new CatalogAttribute();
					catalogAttr.setId(attribute.getId());
					catalogAttr.setNilai(attribute.getNilai());
					
					/** repalce value nilai if have option list */
					if(attribute.getAttributePanelGroup().getAttribute().getAttributeOptionList().size() > 0){
						String valueNew = "";
						for(AttributeOption option : attribute.getAttributePanelGroup().getAttribute().getAttributeOptionList()){
							if (attribute.getNilai() != null && attribute.getNilai().length() > 0) {
								String valueAttrs[]= attribute.getNilai().split(",");
								for(String valueAtt : valueAttrs){
									if(option.getId().intValue() == new Integer(valueAtt).intValue()){
										valueNew = valueNew + option.getName()+",";
									}
								}
							}
						}
						if (valueNew != null && valueNew.length() > 0) {
							catalogAttr.setNilai(valueNew.substring(0,valueNew.lastIndexOf(",")));
						}
					}							
					
					catalogAttr.setAttributePanelGroup(attribute.getAttributePanelGroup());
					
					if(catalog.getCatalogAttributeList() ==null){
						catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
					}
					catalog.getCatalogAttributeList().add(catalogAttr);
				}
			}else{
				if(catalog.getCatalogAttributeList() ==null){
					catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
				}
				
				catalog.getCatalogAttributeList().add(new CatalogAttribute());
			}
		}
		
		return catalogSession.updateCatalog(catalog, tempToken);
	}
    
    @Path("/simpanCatalogComment")
	@POST
	public CatalogComment simpanCatalogComment (
			@FormParam("rating")Integer rating, 
			@FormParam("ratingComment")String ratingComment, 
			@FormParam("catalogId")Integer catalogId,
			@FormParam("userId")Integer userId,
			@HeaderParam("Authorization") String token) {
    	Token tempToken = tokenSession.findByToken(token);
    	
    	CatalogComment catalogComment = new CatalogComment();
    	catalogComment.setCatalog(catalogSession.find(catalogId));
    	catalogComment.setUser(userSession.find(userId));
    	catalogComment.setRating(rating);
    	catalogComment.setRatingComment(ratingComment);
    	
    	return catalogCommentSession.insertCatalogComment(catalogComment, tempToken);
    }
    
    @Path("/sendingEmail")
	@POST
	public Response sendingEmail (
			@FormParam("emailTujuan")String emailTujuan,
			@FormParam("emailCC")String emailCC,
			@FormParam("emailSubject")String emailSubject, 
			@FormParam("emailIsi")String emailIsi,
			@FormParam("emailFrom")String emailFrom) {
    	
    	EmailNotification en = new EmailNotification();
		en.setCreated(new Date());
		en.setEmailTo(emailTujuan);
		en.setSubject(emailSubject);
		en.setMessage(emailIsi);
		en.setStatusEmailSending(0);
		en.setIsDelete(0);
		en.setEmailForm(emailFrom);
		emailNotificationSession.create(en);
		
		if(emailCC != null) {
			EmailNotification en1 = new EmailNotification();
			en1.setCreated(new Date());
			en1.setEmailTo(emailCC);
			en1.setSubject(emailSubject);
			en1.setMessage(emailIsi);
			en1.setStatusEmailSending(0);
			en1.setIsDelete(0);
			en1.setEmailForm(emailFrom);
			emailNotificationSession.create(en1);
		}
    	
    	return Response.ok().build();
    }       
}
