package id.co.promise.procurement.catalog;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.client.ClientProtocolException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.json.JSONException;

import id.co.promise.procurement.approval.ApprovalProcessSession;
import id.co.promise.procurement.approval.ApprovalProcessTypeSession;
import id.co.promise.procurement.approval.ApprovalTahapanSession;
import id.co.promise.procurement.catalog.entity.Attribute;
import id.co.promise.procurement.catalog.entity.AttributeGroup;
import id.co.promise.procurement.catalog.entity.AttributeOption;
import id.co.promise.procurement.catalog.entity.AttributePanelGroup;
import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogAttribute;
import id.co.promise.procurement.catalog.entity.CatalogAttributeHistory;
import id.co.promise.procurement.catalog.entity.CatalogBulkPrice;
import id.co.promise.procurement.catalog.entity.CatalogBulkPriceHistory;
import id.co.promise.procurement.catalog.entity.CatalogCategory;
import id.co.promise.procurement.catalog.entity.CatalogCategoryHistory;
import id.co.promise.procurement.catalog.entity.CatalogComment;
import id.co.promise.procurement.catalog.entity.CatalogContractDetail;
import id.co.promise.procurement.catalog.entity.CatalogFee;
import id.co.promise.procurement.catalog.entity.CatalogFeeHistory;
import id.co.promise.procurement.catalog.entity.CatalogHistory;
import id.co.promise.procurement.catalog.entity.CatalogImage;
import id.co.promise.procurement.catalog.entity.CatalogImageHistory;
import id.co.promise.procurement.catalog.entity.CatalogKontrak;
import id.co.promise.procurement.catalog.entity.CatalogLocation;
import id.co.promise.procurement.catalog.entity.CatalogPriceRange;
import id.co.promise.procurement.catalog.entity.CatalogStock;
import id.co.promise.procurement.catalog.entity.CatalogVendor;
import id.co.promise.procurement.catalog.entity.Category;
import id.co.promise.procurement.catalog.entity.InputType;
import id.co.promise.procurement.catalog.session.AttributeGroupSession;
import id.co.promise.procurement.catalog.session.AttributeOptionSession;
import id.co.promise.procurement.catalog.session.AttributeSession;
import id.co.promise.procurement.catalog.session.CatalogAttributeHistorySession;
import id.co.promise.procurement.catalog.session.CatalogAttributeSession;
import id.co.promise.procurement.catalog.session.CatalogBulkPriceHistorySession;
import id.co.promise.procurement.catalog.session.CatalogBulkPriceSession;
import id.co.promise.procurement.catalog.session.CatalogCategoryHistorySession;
import id.co.promise.procurement.catalog.session.CatalogCategorySession;
import id.co.promise.procurement.catalog.session.CatalogCommentSession;
import id.co.promise.procurement.catalog.session.CatalogContractDetailSession;
import id.co.promise.procurement.catalog.session.CatalogFeeHistorySession;
import id.co.promise.procurement.catalog.session.CatalogFeeSession;
import id.co.promise.procurement.catalog.session.CatalogHistorySession;
import id.co.promise.procurement.catalog.session.CatalogImageHistorySession;
import id.co.promise.procurement.catalog.session.CatalogImageSession;
import id.co.promise.procurement.catalog.session.CatalogKontrakSession;
import id.co.promise.procurement.catalog.session.CatalogLocationSession;
import id.co.promise.procurement.catalog.session.CatalogPriceRangeSession;
import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.catalog.session.CatalogStockSession;
import id.co.promise.procurement.catalog.session.CatalogTempHargaSession;
import id.co.promise.procurement.catalog.session.CategorySession;
import id.co.promise.procurement.catalog.session.InputTypeSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.ApprovalTahapan;
import id.co.promise.procurement.entity.BidangUsaha;
import id.co.promise.procurement.entity.EmailNotification;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.ItemType;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorProfile;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.master.AddressBookServices;
import id.co.promise.procurement.master.AutoNumberSession;
import id.co.promise.procurement.master.ItemOrganisasiSession;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.ItemTypeSession;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.ProductTypeSession;
import id.co.promise.procurement.master.SatuanSession;
import id.co.promise.procurement.master.TahapanSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.template.TemplateXls;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.JsonObject;
import id.co.promise.procurement.utils.StaticUtility;
import id.co.promise.procurement.vendor.VendorProfileSession;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@Path(value = "/procurement/catalog/CatalogServices")
@TransactionManagement(TransactionManagementType.BEAN)
@Produces(MediaType.APPLICATION_JSON)
public class MasterCatalogServices {
	
	final static Logger log = Logger.getLogger(MasterCatalogServices.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();
	@EJB
	private TokenSession tokenSession;
	
	@EJB
	private TahapanSession tahapanSession;
	
	@EJB
	private ApprovalTahapanSession approvalTahapanSession;

	@EJB
	private CatalogSession catalogSession;
	@EJB
	private CatalogPriceRangeSession catalogPriceRangeSession;
	@EJB
	private CatalogCategorySession catalogCategorySession;
	@EJB
	private CatalogLocationSession catalogLocationSession;
	@EJB
	private CatalogAttributeSession catalogAttributeSession;
	@EJB
	private CatalogImageSession catalogImageSession;
	
	@EJB
	private CatalogKontrakSession catalogKontrakSession;

	@EJB
	private CatalogBulkPriceSession catalogBulkPriceSession;
	
	@EJB
	private CatalogStockSession catalogStockSession;
	
	@EJB
	private CatalogCommentSession catalogCommentSession;

	@EJB
	private AttributeSession attributeSession;
	
	@EJB
	private AttributeGroupSession attributeGroupSession;
	
	@EJB
	private AttributeOptionSession attributeOptionSession;
	
	@EJB
	private ItemTypeSession itemTypeSession;
	
	@EJB
	private MataUangSession mataUangSession;
	
	@EJB
	private VendorSession vendorSession;
	
	@EJB
	private SatuanSession satuanSession;
	
	@EJB
	private CategorySession categorySession;
	
	@EJB
	private ItemSession itemSession;
	
	@EJB
	private InputTypeSession inputTypeSession;
	
	@EJB
	private VendorProfileSession vendorProfileSession;
	
	@EJB
	private ProductTypeSession productTypeSession;
	
	@EJB
	private RoleUserSession roleUserSession;
	
	@EJB
	private UserSession userSession;
	
	@EJB
	private EmailNotificationSession emailNotificationSession;
	
	@EJB
	private OrganisasiSession organisasiSession;
	
	@EJB
	private ItemOrganisasiSession itemOrganisasiSession;
	
	@EJB
	private CatalogFeeSession catalogFeeSession;
	
	@EJB
	ApprovalProcessSession approvalProcessSession;
	
	@EJB
	CatalogTempHargaSession catalogTempHargaSession;
	
	@EJB
	CatalogHistorySession catalogHistorySession;
	
	@EJB
	private AutoNumberSession autoNumberSession;
	
	@EJB
	private CatalogContractDetailSession catalogContractDetailSession;
	
	@EJB
	private ApprovalProcessTypeSession approvalProcessTypeSession;
	
	@EJB
	PurchaseRequestSession purchaseRequestSession;
	@Resource private UserTransaction userTransaction;
	
	@EJB
	private CatalogCategoryHistorySession catalogCategoryHistorySession;
	
	@EJB
	private CatalogAttributeHistorySession catalogAttributeHistorySession;
	
	@EJB
	private CatalogImageHistorySession catalogImageHistorySession;
	
	@EJB
	private CatalogFeeHistorySession catalogFeeHistorySession;
	
	@EJB
	CatalogBulkPriceHistorySession catalogBulkPriceHistorySession;
	
	@EJB
	PurchaseRequestItemSession purchaseRequestItemSession;
	
	@Path("/findAll")
	@POST
	public JsonObject<Catalog> findAll(@Context HttpServletRequest httpServletRequest,
			@FormParam("contract") Boolean contract, @HeaderParam("Authorization") String strToken) {
		// kontrak
		String isContractStr = httpServletRequest.getParameter("param[contract]");
		if (isContractStr != null) {
			if (isContractStr.equals("true"))
				contract = true;
			if (isContractStr.equals("false"))
				contract = false;
		}
		
		String start = httpServletRequest.getParameter("start");
		String draw = httpServletRequest.getParameter("draw");
		String length = httpServletRequest.getParameter("length");
		String search = httpServletRequest.getParameter("search[value]");
		String order = httpServletRequest.getParameter("order[0][dir]");
		String vIndexSort = httpServletRequest.getParameter("order[0][column]");
		String orderField = null;
		if (vIndexSort != null && vIndexSort.length() > 0 && vIndexSort.equals("0") == false) {
			orderField = httpServletRequest.getParameter("columns[" + vIndexSort + "][data]");
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
		//KAI 28/01/2021 [21865]
		List<Catalog> catalogList = catalogSession.getCatalogListForManageCatalog(keyword, vVendorId, contract, Integer.valueOf(start),
				Integer.valueOf(length), orderField, order);
		for(Catalog catalog : catalogList) {
			Date today = new Date();
			Date tglAwalKontrak = catalog.getCatalogKontrak().getTglMulailKontrak();
			Date tglAkhirKontrak = catalog.getCatalogKontrak().getTglAkhirKontrak();
			CatalogKontrak catalogKontrak = catalogKontrakSession.find(catalog.getCatalogKontrak().getId());
			
			if(tglAwalKontrak.after(today) || tglAkhirKontrak.before(today)) {
				catalogKontrak.setStatusCatalog(Constant.CATALOG_KONTRAK_STATUS_CATALOG_EXPIRED);
				if(catalog.getApprovalProcess().getStatus().equals(Constant.STATUS_CATALOG_WAITING_FOR_APPROVAL)) {
					catalog.getApprovalProcess().setStatus(Constant.STATUS_CATALOG_EXPIRED);					
				}
			}else {
				if(catalog.getStatus()) {
					catalogKontrak.setStatusCatalog(Constant.CATALOG_KONTRAK_STATUS_CATALOG_ACTIVE);
				}else {
					catalogKontrak.setStatusCatalog(Constant.CATALOG_KONTRAK_STATUS_CATALOG_NONACTIVE);					
				}
			}
			catalog.setCatalogKontrak(catalogKontrak);
		}
		jo.setData(catalogList);
		Integer jmlData = catalogSession.countCatalogListForManageCatalog(keyword, vVendorId, contract).intValue();
		jo.setRecordsTotal(jmlData);
		jo.setRecordsFiltered(jmlData);
		jo.setDraw(draw);

		return jo;
	}

	@Path("/findAllByProperty")
	@POST
	public List<Catalog> findAllByProperty(Catalog catalog) {
		return catalogSession.getCatalogList(catalog);
	}

	@Path("/findCatalogByKontrak")
	@POST
	public List<Catalog> findCatalogByKontrak(Catalog catalog) {
		return catalogSession.getCatalogListWithKontrak(catalog);
	}

	@Path("/getCatalogListByCategory")
	@POST
	public List<Catalog> getCatalogListByCategory(Catalog catalog) {
		return catalogSession.getCatalogListByCategory(catalog);
	}

	@Path("/getCatalogListByPagination")
	@POST
	public Response getAlokasiAnggaranByPagination(@FormParam("search[value]") String keyword,
			@FormParam("start") Integer start, @FormParam("length") Integer length, @FormParam("draw") Integer draw,
			@FormParam("order[0][column]") Integer columnOrder, @FormParam("order[0][dir]") String tipeOrder,
			@HeaderParam("Authorization") String token) {
		Token objToken = tokenSession.findByToken(token);
		// Integer userId = objToken.getUser().getId();

		String tempKeyword = "%" + keyword + "%";
		long countData = catalogSession.getCatalogListCount(tempKeyword);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("draw", draw);
		result.put("recordsTotal", countData);
		result.put("recordsFiltered", countData);
		result.put("data",
				catalogSession.getCatalogListWithPagination(start, length, tempKeyword, columnOrder, tipeOrder));

		return Response.ok(result).build();
	}

	// --------------------------------------------------------------------

	@Path("/findCatalogByKontrakManageKontrak")
	@POST
	public JsonObject<Catalog> findCatalogByKontrakManageKontrak(@Context HttpServletRequest httpServletRequest,
			@FormParam("contract") Boolean contract, @HeaderParam("Authorization") String strToken) {

		String start = httpServletRequest.getParameter("start");
		String draw = httpServletRequest.getParameter("draw");
		String length = httpServletRequest.getParameter("length");
		String search = httpServletRequest.getParameter("search[value]");
		String order = httpServletRequest.getParameter("order[0][dir]");
		String vIndexSort = httpServletRequest.getParameter("order[0][column]");
		String orderField = null;

		if (vIndexSort != null && vIndexSort.length() > 0 && vIndexSort.equals("0") == false) {
			orderField = httpServletRequest.getParameter("columns[" + vIndexSort + "][data]");
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
		jo.setData(catalogSession.getCatalogListByKontrak(keyword, vVendorId, contract, Integer.valueOf(start),
				Integer.valueOf(length), orderField, order));
		Integer jmlData = catalogSession.countCatalogListByKontrak(keyword, vVendorId, contract).intValue();
		jo.setRecordsTotal(jmlData);
		jo.setRecordsFiltered(jmlData);
		jo.setDraw(draw);

		return jo;

	}

	@Path("/findAllByLogin")
	@POST
	public List<Catalog> findAllByLogin(User user) {
		Vendor vendor = vendorSession.getVendorByUserId(user.getId());
		if (vendor != null) {
			Catalog catalog = new Catalog();
			catalog.setVendor(vendor);
			catalog.setStatus(true);
			List<Catalog> catList = catalogSession.getCatalogList(catalog);

			return catList;
		} else {
			Catalog catalog = new Catalog();
			catalog.setStatus(true);
			return catalogSession.getCatalogList(catalog);
		}
	}

	@Path("/findAllVendor")
	@POST
	public List<Vendor> findAllVendor(BidangUsaha bidangUsaha) {
		List<Vendor> vendorList = vendorSession.getVendorListByCatalog(null, 0, 6);
		return vendorList;
	}

	@Path("/findCatalogCommentByCatalog")
	@POST
	public List<CatalogComment> findCatalogCommentByCatalog(@FormParam("catalogId") Integer catalogId) {

		List<CatalogComment> hasilList = new ArrayList<CatalogComment>();

		CatalogComment catalogComment = new CatalogComment();
		catalogComment.setCatalog(catalogSession.find(catalogId));
		List<CatalogComment> catalogCommentList = catalogCommentSession.getCatalogCommentList(catalogComment);

		if (catalogCommentList.size() > 0) {
			for (CatalogComment catComment : catalogCommentList) {
				// Menentukan Role
				List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(catComment.getUser().getId());
				if (roleUserList != null && roleUserList.size() > 0) {
					catComment.setRoleUser(roleUserList.get(0).getRole().getNama());
				}

				// Menentukan Organisasi
				Organisasi organisasiAfco = organisasiSession.getAfcoOrganisasiByUserId(catComment.getUser().getId());
				if (organisasiAfco != null) {
					catComment.setOrganisasi(organisasiAfco.getNama());
				}

				hasilList.add(catComment);
			}
		}
		return hasilList;
	}

	@Path("/findECatalogVendorById")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findECatalogVendorByVendorId(CatalogVendor catalogVendor) {
		List<Catalog> catalogList = catalogCategorySession.getCatalogByVendorId(catalogVendor);
		if (catalogList != null && catalogList.size() > 0) {
			for (Catalog catalog : catalogList) {
				CatalogCategory catalogCategory = new CatalogCategory();
				catalogCategory.setCatalog(catalog);
				List<CatalogCategory> catalogCategoryList = catalogCategorySession
						.getCatalogCategoryList(catalogCategory);
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
				List<CatalogAttribute> catalogAttributeList = catalogAttributeSession
						.getCatalogAttributeList(catalogAttribute);
				if (catalogAttributeList != null) {
					if (catalogAttributeList.size() > 0) {
						for (CatalogAttribute attribute : catalogAttributeList) {

							CatalogAttribute catalogAttr = new CatalogAttribute();
							catalogAttr.setId(attribute.getId());
							if (attribute.getNilai() != null) {
								try {
									Integer attOptionId = Integer.valueOf(attribute.getNilai());
									AttributeOption attOption = attributeOptionSession.find(attOptionId);
									if (attOption != null) {
										catalogAttr.setNilai(attOption.getName());
									} else {
										catalogAttr.setNilai(attribute.getNilai());
									}
								} catch (Exception E) {
									catalogAttr.setNilai(attribute.getNilai());
								}
							}

							catalogAttr.setAttributePanelGroup(attribute.getAttributePanelGroup());

							if (catalog.getCatalogAttributeList() == null) {
								catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
							}
							catalog.getCatalogAttributeList().add(catalogAttr);
						}
					} else {
						if (catalog.getCatalogAttributeList() == null) {
							catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
						}

						catalog.getCatalogAttributeList().add(new CatalogAttribute());
					}
				}

				/** Catalog Location **/
				CatalogLocation catalogLocation = new CatalogLocation();
				catalogLocation.setCatalog(catalog);
				List<CatalogLocation> catalogLocationList = catalogLocationSession
						.getCatalogLocationList(catalogLocation);
				if (catalogLocationList != null) {
					if (catalogLocationList.size() > 0) {
						for (CatalogLocation location : catalogLocationList) {
							CatalogLocation catalogLoc = new CatalogLocation();
							catalogLoc.setId(location.getId());
							catalogLoc.setMinimalOrder(location.getMinimalOrder());
							catalogLoc.setOfficeAddress(location.getOfficeAddress());
							catalogLoc.setOutStock(location.getOutStock());
							catalogLoc.setQuantityNotify(location.getQuantityNotify());
							catalogLoc.setStockProduct(location.getStockProduct());
							catalogLoc.setSupplyAbility(location.getSupplyAbility());

							if (catalog.getCatalogLocationList() == null) {
								catalog.setCatalogLocationList(new ArrayList<CatalogLocation>());
							}
							catalog.getCatalogLocationList().add(catalogLoc);
						}
					}
				} else {
					if (catalog.getCatalogLocationList() == null) {
						catalog.setCatalogLocationList(new ArrayList<CatalogLocation>());
					}
					catalog.getCatalogLocationList().add(new CatalogLocation());
				}
				if (catalog.getVendor() != null) {
					List<VendorProfile> vendorProfileList = vendorProfileSession
							.getVendorProfileByVendorId(catalog.getVendor().getId());
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
		catalogVendor.setCurrentPage((catalogVendor.getCurrentPage() - 1) * catalogVendor.getPageSize());

		// Di ganti dari User Id enjadi vendor ID (Jangan SALAH)
		if (catalogVendor.getUserId() != null) {
			Vendor vendor = vendorSession.find(catalogVendor.getUserId());
			catalogVendor.setVendor(vendor);
		}

		// Filter dengan List User yang satu Organisasi
		List<User> userList = new ArrayList<User>();
		if (catalogVendor.getUserId() != null) {

			// Cari Organisasi User di Tabel RoleUser
			List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(catalogVendor.getUserId());
			if (roleUserList != null && roleUserList.size() > 0) {
				Integer orgId = roleUserList.get(0).getOrganisasi().getId();
				List<RoleUser> organisasiRoleUserList = roleUserSession.getRoleUserByOrganisasiAndAppCode(orgId, "PM");

				if (organisasiRoleUserList != null && organisasiRoleUserList.size() > 0) {
					for (RoleUser roleUser : organisasiRoleUserList) {
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
				List<CatalogCategory> catalogCategoryList = catalogCategorySession
						.getCatalogCategoryList(catalogCategory);
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
				List<CatalogAttribute> catalogAttributeList = catalogAttributeSession
						.getCatalogAttributeList(catalogAttribute);
				if (catalogAttributeList != null) {
					if (catalogAttributeList.size() > 0) {
						for (CatalogAttribute attribute : catalogAttributeList) {

							CatalogAttribute catalogAttr = new CatalogAttribute();
							catalogAttr.setId(attribute.getId());
							if (attribute.getNilai() != null) {
								Integer attOptionId = Integer.valueOf(attribute.getNilai());
								AttributeOption attOption = attributeOptionSession.find(attOptionId);
								catalogAttr.setNilai(attOption.getName());
							}

							catalogAttr.setAttributePanelGroup(attribute.getAttributePanelGroup());

							if (catalog.getCatalogAttributeList() == null) {
								catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
							}
							catalog.getCatalogAttributeList().add(catalogAttr);
						}
					} else {
						if (catalog.getCatalogAttributeList() == null) {
							catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
						}

						catalog.getCatalogAttributeList().add(new CatalogAttribute());
					}
				}

				/** Catalog Location **/
				CatalogLocation catalogLocation = new CatalogLocation();
				catalogLocation.setCatalog(catalog);
				List<CatalogLocation> catalogLocationList = catalogLocationSession
						.getCatalogLocationList(catalogLocation);
				if (catalogLocationList != null) {
					if (catalogLocationList.size() > 0) {
						for (CatalogLocation location : catalogLocationList) {
							CatalogLocation catalogLoc = new CatalogLocation();
							catalogLoc.setId(location.getId());
							catalogLoc.setMinimalOrder(location.getMinimalOrder());
							catalogLoc.setOfficeAddress(location.getOfficeAddress());
							catalogLoc.setOutStock(location.getOutStock());
							catalogLoc.setQuantityNotify(location.getQuantityNotify());
							catalogLoc.setStockProduct(location.getStockProduct());
							catalogLoc.setSupplyAbility(location.getSupplyAbility());

							if (catalog.getCatalogLocationList() == null) {
								catalog.setCatalogLocationList(new ArrayList<CatalogLocation>());
							}
							catalog.getCatalogLocationList().add(catalogLoc);
						}
					}
				} else {
					if (catalog.getCatalogLocationList() == null) {
						catalog.setCatalogLocationList(new ArrayList<CatalogLocation>());
					}
					catalog.getCatalogLocationList().add(new CatalogLocation());
				}
				if (catalog.getVendor() != null) {
					List<VendorProfile> vendorProfileList = vendorProfileSession
							.getVendorProfileByVendorId(catalog.getVendor().getId());
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
	public Response findECatalog(CatalogVendor catalogVendor, @HeaderParam("Authorization") String token) {
		User user = tokenSession.findByToken(token).getUser();
		RoleUser roleUsers = roleUserSession.getRoleUserByUserId(user.getId()).get(0);
		List<Category> categoryList = null;
		if (catalogVendor.getCategory() != null) {
			categoryList = new ArrayList<Category>();
			categoryList = populateCategoryFromTreeList(catalogVendor.getCategory(), categoryList);
		}

		catalogVendor.setCategoryList(categoryList);
		catalogVendor.setCurrentPage((catalogVendor.getCurrentPage() - 1) * catalogVendor.getPageSize());

		// Di ganti dari User Id menjadi vendor ID (Jangan SALAH)
		if (catalogVendor.getUserId() != null) {
			Vendor vendor = vendorSession.find(catalogVendor.getUserId());
			catalogVendor.setVendor(vendor);
		}
		
		// Filter dengan List User yang satu Organisasi
		List<User> userList = new ArrayList<User>();
		if (catalogVendor.getUserId() != null) {

			// Cari Organisasi User di Tabel RoleUser
			List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(catalogVendor.getUserId());
			if (roleUserList != null && roleUserList.size() > 0) {
				Integer orgId= roleUserList.get(0).getOrganisasi().getId();
				List<RoleUser> organisasiRoleUserList = roleUserSession.getRoleUserByOrganisasiAndAppCode(orgId, "PM");

				if (organisasiRoleUserList != null && organisasiRoleUserList.size() > 0) {
					for (RoleUser roleUser : organisasiRoleUserList) {
						userList.add(roleUser.getUser());
					}
					catalogVendor.setUserList(userList);
				}
			}
		}
		//Integer rootOrgId = organisasiSession.getRootParentByOrganisasi(roleUsers.getOrganisasi()).getId();
		List<Catalog> catalogList = catalogCategorySession.getActiveCatalogList(catalogVendor);
		if (catalogList != null && catalogList.size() > 0) {
			for (Catalog catalog : catalogList) {
				//List <ItemOrganisasi> itemOrganisasiList = itemOrganisasiSession.getItemOrganisasiListByItemId(catalog.getItem().getId(), rootOrgId);
				//if (itemOrganisasiList.size() > 0) {
				//	catalog.setIsAvailable(true);
				//} else {
				//	catalog.setIsAvailable(false);
				//}
				catalog.setIsAvailable(true);
				CatalogCategory catalogCategory = new CatalogCategory();
				catalogCategory.setCatalog(catalog);
				List<CatalogCategory> catalogCategoryList = catalogCategorySession
						.getCatalogCategoryList(catalogCategory);
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
				List<CatalogAttribute> catalogAttributeList = catalogAttributeSession
						.getCatalogAttributeList(catalogAttribute);
				if (catalogAttributeList != null) {
					if (catalogAttributeList.size() > 0) {
						for (CatalogAttribute attribute : catalogAttributeList) {
							CatalogAttribute catalogAttr = new CatalogAttribute();
							catalogAttr.setId(attribute.getId());
							if (attribute.getNilai() != null) {
								try {
									Integer attOptionId = Integer.valueOf(attribute.getNilai());
									AttributeOption attOption = attributeOptionSession.find(attOptionId);
									if (attOption != null) {
										catalogAttr.setNilai(attOption.getName());
									} else {
										catalogAttr.setNilai(attribute.getNilai());
									}
								} catch (Exception E) {
									catalogAttr.setNilai(attribute.getNilai());
								}

							}

							catalogAttr.setAttributePanelGroup(attribute.getAttributePanelGroup());

							if (catalog.getCatalogAttributeList() == null) {
								catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
							}
							catalog.getCatalogAttributeList().add(catalogAttr);
						}
					} else {
						if (catalog.getCatalogAttributeList() == null) {
							catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
						}

						catalog.getCatalogAttributeList().add(new CatalogAttribute());
					}
				}

				/** Catalog Location **/
				CatalogLocation catalogLocation = new CatalogLocation();
				catalogLocation.setCatalog(catalog);
				List<CatalogLocation> catalogLocationList = catalogLocationSession
						.getCatalogLocationList(catalogLocation);
				if (catalogLocationList != null) {
					if (catalogLocationList.size() > 0) {
						for (CatalogLocation location : catalogLocationList) {
							CatalogLocation catalogLoc = new CatalogLocation();
							catalogLoc.setId(location.getId());
							catalogLoc.setMinimalOrder(location.getMinimalOrder());
							catalogLoc.setOfficeAddress(location.getOfficeAddress());
							catalogLoc.setOutStock(location.getOutStock());
							catalogLoc.setQuantityNotify(location.getQuantityNotify());
							catalogLoc.setStockProduct(location.getStockProduct());
							catalogLoc.setSupplyAbility(location.getSupplyAbility());

							if (catalog.getCatalogLocationList() == null) {
								catalog.setCatalogLocationList(new ArrayList<CatalogLocation>());
							}
							catalog.getCatalogLocationList().add(catalogLoc);
						}
					}
				} else {
					if (catalog.getCatalogLocationList() == null) {
						catalog.setCatalogLocationList(new ArrayList<CatalogLocation>());
					}
					catalog.getCatalogLocationList().add(new CatalogLocation());
				}

				CatalogBulkPrice tempCatalogBulkPrice = new CatalogBulkPrice();
				tempCatalogBulkPrice.setCatalog(catalog);
				List<CatalogBulkPrice> catalogBulkPriceListbyCatalog = catalogBulkPriceSession
						.getCatalogBulkPriceListByCatalog(catalog);
				if (catalogBulkPriceListbyCatalog != null && catalogBulkPriceListbyCatalog.size() > 0) {
					catalog.setCatalogBulkPriceList(new ArrayList<CatalogBulkPrice>());
					for (CatalogBulkPrice catalogBulkPrice : catalogBulkPriceListbyCatalog) {
						tempCatalogBulkPrice = new CatalogBulkPrice();
						tempCatalogBulkPrice.setId(catalogBulkPrice.getId());
						tempCatalogBulkPrice.setMaxQuantity(catalogBulkPrice.getMaxQuantity());
						tempCatalogBulkPrice.setMinQuantity(catalogBulkPrice.getMinQuantity());
						tempCatalogBulkPrice.setDiskon(catalogBulkPrice.getDiskon());

						catalog.getCatalogBulkPriceList().add(tempCatalogBulkPrice);
					}
				}

				CatalogFee tempCatalogFee = new CatalogFee();
				tempCatalogFee.setCatalog(catalog);
				List<CatalogFee> catalogFeeListbyCatalog = catalogFeeSession.getListCatalogFeeByCatalog(catalog);
				if (catalogFeeListbyCatalog != null && catalogFeeListbyCatalog.size() > 0) {
					catalog.setCatalogFeeList(new ArrayList<CatalogFee>());
					for (CatalogFee catalogFee : catalogFeeListbyCatalog) {
						tempCatalogFee = new CatalogFee();
						tempCatalogFee.setId(catalogFee.getId());
						tempCatalogFee.setAsuransi(catalogFee.getAsuransi());
						//tempCatalogFee.setAsuransi(Constant.ASURANSI);
						tempCatalogFee.setQuantityBatch(catalogFee.getQuantityBatch());
						tempCatalogFee.setOngkosKirim(catalogFee.getOngkosKirim());
						tempCatalogFee.setSlaDeliveryTime(catalogFee.getSlaDeliveryTime());
						catalog.getCatalogFeeList().add(tempCatalogFee);
					}
				}

				CatalogStock tempCatalogStock = new CatalogStock();
				tempCatalogStock.setCatalog(catalog);
				List<CatalogStock> catalogStockListbyCatalog = catalogStockSession
						.getCatalogStockListByCatalog(catalog);
				if (catalogStockListbyCatalog != null && catalogStockListbyCatalog.size() > 0) {
					catalog.setCatalogStockList(new ArrayList<CatalogStock>());
					for (CatalogStock catalogStock : catalogStockListbyCatalog) {
						tempCatalogStock = new CatalogStock();
						tempCatalogStock.setId(catalogStock.getId());
						tempCatalogStock.setQuantity(catalogStock.getQuantity());
						tempCatalogStock.setTotalStock(catalogStock.getTotalStock());

						catalog.getCatalogStockList().add(tempCatalogStock);
					}
				}

				if (catalog.getVendor() != null) {
					List<VendorProfile> vendorProfileList = vendorProfileSession
							.getVendorProfileByVendorId(catalog.getVendor().getId());
					if (vendorProfileList != null && vendorProfileList.size() > 0) {
						catalog.setVendorProfile(vendorProfileList.get(0));
					}
				}
				//set transaksi
				Integer transaksi = purchaseRequestSession.getTransaction(catalog.getId(), catalog.getItem().getId());
				catalog.setTransaksi(transaksi);
				Integer qtySoldToday = purchaseRequestItemSession.getTotalProductSoldToday(catalog.getId(), catalog.getItem().getId());
				if(catalog.getMaksimumQuantityOrderDay() != null) {
					catalog.setMaksimumQuantityOrderDay(catalog.getMaksimumQuantityOrderDay() - qtySoldToday);
				}
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("catalogList", catalogList);
		map.put("totalList", catalogList.size()/*catalogCategorySession.getActiveCatalogListCount(catalogVendor)*/);

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
	

	@Path("/get-list-catalg-fee-by-catalog-id")
	@POST
	public List<CatalogFee> geCatalogFeeByCatalogId(Integer catalogId){
		List<CatalogFee>tempCatalogFee=catalogFeeSession.getListCatalogFeeByCatalogId(catalogId);
		return tempCatalogFee;
	}

	@Path("/findCatalogProperties")
	@POST
	public Response findCatalogProperties(Catalog catalog) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("itemTypeList", itemTypeSession.getItemTypeList());
		map.put("mataUangList", mataUangSession.getMataUanglist());
		map.put("satuanList", satuanSession.getSatuanList());
		map.put("productTypeList", productTypeSession.getProductTypeList());
//		map.put("vendorList", vendorSession.getVendorList());
		
		List<AttributeGroup> attributeGroupList = attributeGroupSession.getAttributeGroupList();
		if (catalog != null && catalog.getId() != null) {
			CatalogAttribute tempCatalogAttribute = new CatalogAttribute();
			tempCatalogAttribute.setCatalog(catalog);
			List<CatalogAttribute> catalogAttributeList = catalogAttributeSession
					.getCatalogAttributeList(tempCatalogAttribute);
			if (catalogAttributeList != null && catalogAttributeList.size() > 0) {
				for (CatalogAttribute catalogAttribute : catalogAttributeList) {
					if (attributeGroupList != null && attributeGroupList.size() > 0) {
						for (AttributeGroup attributeGroup : attributeGroupList) {
							Boolean nextState = false;
							if (attributeGroup.getAttributePanelGroupList() != null
									&& attributeGroup.getAttributePanelGroupList().size() > 0) {
								for (AttributePanelGroup attributePanelGroup : attributeGroup
										.getAttributePanelGroupList()) {
									if (attributePanelGroup.getId().equals(catalogAttribute.getAttributePanelGroup()
											.getId()) ) {
										if (attributePanelGroup.getAttribute() != null
												&& attributePanelGroup.getAttribute().getInputType() != null
												&& attributePanelGroup.getAttribute().getInputType().getName()
														.equals("Checkbox") == false) {
											attributePanelGroup.setValue(catalogAttribute.getNilai());
										} else {
											String strValue = catalogAttribute.getNilai();
											if (strValue != null) {
												String[] splitValue = strValue.split(",");
												if (attributePanelGroup.getAttribute().getAttributeOptionList() != null
														&& attributePanelGroup.getAttribute().getAttributeOptionList()
																.size() > 0) {
													for (AttributeOption attributeOption : attributePanelGroup
															.getAttribute().getAttributeOptionList()) {
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
			List<CatalogCategory> catalogCategoryList = catalogCategorySession
					.getCatalogCategoryList(tempCatalogCategory);
			if (catalogCategoryList != null && catalogCategoryList.size() > 0) {
				for (CatalogCategory catalogCategory : catalogCategoryList) {
					if (categoryList != null && categoryList.size() > 0) {
						for (Category category : categoryList) {
							if (catalogCategory.getCategory().getId().equals(category.getId()) ) {
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

//		CatalogKontrak ck = catalogKontrakSession.getActiveCatalogKontrak(catalog);
//		if (ck != null) {
//			Date today = new Date();
//			Calendar date = Calendar.getInstance();
//			today = date.getTime();
//			long tanggalMulai=0;
//			long tanggalAkhir = 0;
//			if(ck.getTglMulailKontrak()!=null) {
//				tanggalMulai = ck.getTglMulailKontrak().getTime();
//			}
//			if(ck.getTglMulailKontrak()!=null) {
//				tanggalAkhir = ck.getTglAkhirKontrak().getTime();
//			}
//			long diff = tanggalAkhir - tanggalMulai;
//			long diff2 = today.getTime() - tanggalMulai;
//			long lama = diff / (24 * 60 * 60 * 1000);
//			long lama2 = diff2 / (24 * 60 * 60 * 1000);
//			Boolean isEditable = true;
//			if (lama > 540 && lama2 > 390) {
//				isEditable = false;
//			}
//			map.put("isEditable", isEditable);
//		}

		map.put("categoryList", categoryTreeList);

		if (catalog != null && catalog.getId() != null) {
			CatalogLocation catalogLocation = new CatalogLocation();
			catalogLocation.setCatalog(catalog);
			map.put("catalogLocationList", catalogLocationSession.getCatalogLocationList(catalogLocation));

			CatalogImage catalogImage = new CatalogImage();
			catalogImage.setCatalog(catalog);
			map.put("catalogImageList", catalogImageSession.getCatalogImageList(catalogImage));

			CatalogBulkPrice cataBulkPrice = new CatalogBulkPrice();
			cataBulkPrice.setCatalog(catalog);
			map.put("catalogBulkPriceList", catalogBulkPriceSession.getCatalogBulkPriceList(cataBulkPrice));

			CatalogFee cataFee = new CatalogFee();
			cataFee.setCatalog(catalog);
			map.put("catalogFeeList", catalogFeeSession.getCatalogFeeList(cataFee));

			CatalogStock cataStock = new CatalogStock();
			cataStock.setCatalog(catalog);
			map.put("catalogStockList", catalogStockSession.getCatalogStockListByCatalog(catalog));

			CatalogPriceRange catalogPriceRange = new CatalogPriceRange();
			catalogPriceRange.setCatalog(catalog);
			map.put("catalogPriceRangeList", catalogPriceRangeSession.getCatalogPriceRangeList(catalogPriceRange));
		}

		return Response.ok(map).build();
	}

	@Path("/checkKodeProductPanitia")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean checkKodeProductPanitia(Catalog catalog, @HeaderParam("Authorization") String token) {
		String toDo = "";
		if (catalog.getId() != null) {
			toDo = "update";
		} else {
			toDo = "insert";
		}

		return catalogSession.checkKodePanitiaCatalog(catalog.getKodeProdukPanitia(), toDo, catalog.getId());
	}
	
	/* perubahan KAI 30/11/2020 */
	@Path("/checkKodeProduct")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean checkKodeProduct(Catalog catalog, @HeaderParam("Authorization") String token) {
		Boolean valid = false;
		Catalog catalgBycode = new Catalog();
		if(catalog.getId()!=null) {
			Catalog catalogOld = catalogSession.getCatalog(catalog.getId());
			if(catalogOld.getVendor().getId().equals(catalog.getVendor().getId()) && catalog.getKodeProduk().equalsIgnoreCase(catalogOld.getKodeProduk())) {
				valid = false;
			}else {
				catalgBycode = catalogSession.getCatalogByKodeProduk(catalog.getKodeProduk());
				if(catalgBycode != null && catalgBycode == catalogOld) {
					valid=false;
				}else if(catalgBycode == null){
					valid=false;
				}else {
					valid = true;
				}
			}
		}else {
			catalgBycode = catalogSession.getCatalogByKodeProduk(catalog.getKodeProduk());
			if(catalgBycode != null) {
				valid=true;
			}else {
				valid = false;
			}
		}

		return valid;
	}

	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Catalog save(Catalog catalog, @HeaderParam("Authorization") String token) throws Exception {
		// validasi data satuan harus terisi
		
		Boolean approvalEdit=false;
		Boolean catalogHistoryEdit=false;
		Token tempToken = tokenSession.findByToken(token);
		CatalogHistory catalogHistory= new CatalogHistory();
		try {
			userTransaction.begin();
			if(catalog.getId()!=null) {
				Catalog catalogOld =catalogSession.getCatalog(catalog.getId());
						
				/* set catalog history */
				catalogHistory.setCatalog(catalogOld);
				catalogHistory.setNamaIND(catalog.getNamaIND());
				catalogHistory.setItem(catalog.getItem());
				catalogHistory.setSatuan(catalog.getSatuan());
				catalogHistory.setMataUang(catalog.getMataUang());
				catalogHistory.setHarga(catalog.getHarga());
				catalogHistory.setBerat(catalog.getBerat());
				catalogHistory.setCurrentStock(catalog.getCurrentStock());
				catalogHistory.setDeskripsiIND(catalog.getDeskripsiIND());
				catalogHistory.setHarga(catalog.getHarga());
				catalogHistory.setSatuanBerat(catalog.getSatuanBerat());
				catalogHistory.setSatuanLebar(catalog.getSatuanLebar());
				catalogHistory.setSatuanPanjang(catalog.getSatuanPanjang());
				catalogHistory.setSatuanTinggi(catalog.getSatuanTinggi());
				catalogHistory.setCatalogKontrak(catalog.getCatalogKontrak());
				catalogHistory.setKodeItem(catalog.getKodeItem());
				catalogHistory.setItemType(catalog.getItemType());
				catalogHistory.setMinimalQuantity(catalog.getMinimalQuantity());
				catalogHistory.setProductType(catalog.getProductType());
				catalogHistory.setKodeProduk(catalog.getKodeProduk());
				catalogHistory.setAttributeGroup(catalog.getAttributeGroup());
				catalogHistory.setPanjang(catalog.getPanjang());
				catalogHistory.setLebar(catalog.getLebar());
				catalogHistory.setTinggi(catalog.getTinggi());
				catalogHistory.setVendor(catalogOld.getVendor());
				catalogHistory.setIsVendor(catalog.getIsVendor());
				catalogHistory.setIsApproval(catalog.getIsApproval());
				catalogHistory.setCatalogCategoryList(catalog.getCatalogCategoryList());
				catalogHistory.setCatalogFeeList(catalog.getCatalogFeeList());
				catalogHistory.setCatalogBulkPriceList(catalog.getCatalogBulkPriceList());
				catalogHistory.setCatalogStockList(catalog.getCatalogStockList());
				catalogHistory.setCatalogImageList(catalog.getCatalogImageList());
				catalogHistory.setCatalogAttributeList(catalog.getCatalogAttributeList());
				catalogHistory.setMinimumQuantityOrder(catalog.getMinimumQuantityOrder());
				catalogHistory.setMaksimumQuantityOrderDay(catalog.getMaksimumQuantityOrderDay());
				catalogHistory.setMaksimumQuantityPerOrder(catalog.getMaksimumQuantityPerOrder());
				catalogHistory.setCategoryList(catalog.getCategoryList());
				/* set catalog */
	//			catalog.setNamaIND(catalogOld.getNamaIND());
	//			catalog.setItem(catalogOld.getItem());
	//			catalog.setSatuan(catalogOld.getSatuan());
	//			catalog.setMataUang(catalogOld.getMataUang());
	//			catalog.setCatalogCategoryList(catalogOld.getCatalogCategoryList());
				catalog.setIsApproval(Constant.ONE_VALUE);
				approvalEdit=true;
				catalogHistoryEdit=true;
				
				
				if(catalog.getHarga() != null && catalogOld.getHarga() < catalog.getHarga()) {
					/* Set harga sebelum di approv */
					catalogHistory.setHarga(catalog.getHarga());
					catalog.setStatus(false);
					catalog.setHarga(catalogOld.getHarga());
							
				}
				catalogHistorySession.insertCatalogHistory(catalogHistory, tempToken);
				
				catalog = catalogOld;
				catalog.setCatalogCategoryList(catalogHistory.getCatalogCategoryList());
				catalog.setCatalogFeeList(catalogHistory.getCatalogFeeList());
				catalog.setCatalogBulkPriceList(catalogHistory.getCatalogBulkPriceList());
				catalog.setCatalogStockList(catalogHistory.getCatalogStockList());
				catalog.setCatalogImageList(catalogHistory.getCatalogImageList());
				catalog.setCategoryList(catalogHistory.getCategoryList());
				catalog.setAttributeGroup(catalogHistory.getAttributeGroup());
				catalog.setTodo("edit");
			}else {
				catalogHistoryEdit=false;
				catalog.setIsApproval(Constant.ONE_VALUE);
				catalog.setStatus(false);
				catalog.setTodo("add");
			}
			
			
			ApprovalTahapan approvalTahapan = new ApprovalTahapan();
			String tahapan = null;
			if(approvalEdit) {
				tahapan = Constant.TAHAPAN_APPROVAL_EDIT_CATALOG;
			}else if(!catalogHistoryEdit){
				tahapan = Constant.TAHAPAN_APPROVAL_INSERT_CATALOG;
			}
			approvalTahapan = approvalTahapanSession.getApprovalTahapanByTahapan(tahapanSession.getByName(tahapan));
			
			if (approvalTahapan != null) {
				if (catalog.getSatuan() != null) {
					/*perubahan KAI 01/18/2021*/
					Catalog tempCatalog = simpanCatalogWithItem(catalog, tempToken, null);
					
					simpanCatalogBulkPrice(tempCatalog, catalog, tempToken);
					simpanCatalogCategory(tempCatalog, catalog, tempToken);
					simpanCatalogPriceRange(tempCatalog, catalog, tempToken);
					simpanCatalogLocation(tempCatalog, catalog, tempToken);
					simpanCatalogImage(tempCatalog, catalog, tempToken);
					simpanCatalogStock(tempCatalog, catalog, tempToken);
					simpanCatalogFee(tempCatalog, catalog, tempToken);
					simpanCatalogAttribute(tempCatalog, catalog, tempToken);
				} else {
					catalog.setSatuan(null);
				}
					
				if(approvalEdit) {
					approvalProcessSession.doCreateApproval(catalog.getId(), Constant.TAHAPAN_APPROVAL_EDIT_CATALOG, tempToken, null);
				}else if(!catalogHistoryEdit){
					approvalProcessSession.doCreateApproval(catalog.getId(), Constant.TAHAPAN_APPROVAL_INSERT_CATALOG, tempToken, null);
				}
				
				catalog.setIsApprovalTahapan(Constant.ONE_VALUE);
			} 
			userTransaction.commit();
		}catch (Exception e) {
			e.printStackTrace();
			userTransaction.rollback();
		}
		
		return catalog;
	}

	protected Catalog simpanCatalogWithItem(Catalog catalog, Token token, String status) {
		Boolean isInsert = true;
		catalog.setNamaIND(catalog.getNamaIND().trim());
		if (token.getUser() != null) {
			Vendor vendor = vendorSession.getVendorByUserId(token.getUser().getId());
			/*perubahan KAI 01/18/2021*/
			if (vendor != null || status.equalsIgnoreCase("approval")) {
				catalog.setIsVendor(true);
				if(vendor != null) {
					catalog.setVendor(vendor);					
				}
				isInsert = false;
			} else {
				if (catalog.getStatus() == false) {
					catalog.setStatus(false);
				} else {
					catalog.setStatus(true);
				}
				catalog.setIsVendor(false);
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
		

		//update catalog contract detail item nya jika sudah di realease
		CatalogContractDetail catalogContractDetail = catalogContractDetailSession.find(catalog.getCatalogContractDetail().getId());
		catalogContractDetail.setFlagItemUsed(Constant.ONE_VALUE);
		catalogContractDetailSession.update(catalogContractDetail, token);	
		

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
		CatalogCategoryHistory catalogCategoryHistory = new CatalogCategoryHistory();
		catalogCategory.setCatalog(catalogBackend);
		List<CatalogCategory> catalogCategoryList = catalogCategorySession.getCatalogCategoryList(catalogCategory);
		int lenCatalogCategoryList = 0;
		if (catalogCategoryList.size() > 0) {
			lenCatalogCategoryList = catalogCategoryList.size();
		}
		/* perubahan KAI 14/12/2020 [20852]*/
		if (lenCatalogCategoryList >= lenCategoryList) {
			// update sebanyak category dan hapus sisanya
			for (int a = 0; a < lenCategoryList; a++) {
				catalogCategory = catalogCategoryList.get(a);
				catalogCategory.setCategory(categoryList.get(a));
				catalogCategory.setCatalog(catalogBackend);
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					catalogCategoryHistory.setCatalogCategory(catalogCategoryList.get(a));
					catalogCategoryHistory.setCategory(categoryList.get(a));
					catalogCategoryHistory.setCatalog(catalogBackend);
					Integer version = new Integer(1);
					CatalogCategoryHistory cch = catalogCategoryHistorySession.getCatalogCategoryHistoryByVersion(catalogCategory.getId());
					if(cch != null) {
						version += cch.getPerubahanVersi();
						catalogCategoryHistory.setPerubahanVersi(version);
						catalogCategoryHistory.setId(cch.getId());
						catalogCategoryHistory.setIsDelete(cch.getIsDelete());
						catalogCategoryHistorySession.updateCatalogCategoryHistory(catalogCategoryHistory, tempToken);
					}else {
						CatalogCategoryHistory catalogCategoryHistoryNew = new CatalogCategoryHistory();
						catalogCategoryHistoryNew = catalogCategoryHistory;
						catalogCategoryHistoryNew.setPerubahanVersi(version);
						catalogCategoryHistoryNew.setId(null);
						catalogCategoryHistorySession.insertCatalogCategoryHistory(catalogCategoryHistoryNew, tempToken);						
					}
					
				}else {
					catalogCategorySession.updateCatalogCategory(catalogCategory, tempToken);					
				}
			}
			for (int a = lenCategoryList; a < lenCatalogCategoryList; a++) {
				catalogCategory = catalogCategoryList.get(a);
				catalogCategorySession.deleteCatalogCategory(catalogCategory.getId(), tempToken);
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
		List<CatalogPriceRange> catalogPriceRangeList = catalogPriceRangeSession
				.getCatalogPriceRangeList(catalogPriceRange);

		int lenOldPriceRange = 0;
		if (catalogPriceRangeList.size() > 0) {
			lenOldPriceRange = catalogPriceRangeList.size();
		}
		int lenNewPriceRange = 0;
		if (catalogFrontend.getCatalogPriceRangeList() != null
				&& catalogFrontend.getCatalogPriceRangeList().size() > 0) {
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
		CatalogAttributeHistory catalogAttributeHistory = new CatalogAttributeHistory();
		catalogAttribute.setCatalog(catalogBackend);
		List<CatalogAttribute> catalogAttributeList = catalogAttributeSession.getCatalogAttributeList(catalogAttribute);

		int lenOldAttribute = 0;
		if (catalogAttributeList != null && catalogAttributeList.size() > 0) {
			lenOldAttribute = catalogAttributeList.size();
		}
		int lenNewAttribute = 0;
		if (catalogFrontend.getAttributeGroup() != null
				&& catalogFrontend.getAttributeGroup().getAttributePanelGroupList() != null
				&& catalogFrontend.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
			lenNewAttribute = catalogFrontend.getAttributeGroup().getAttributePanelGroupList().size();
		}

		if (catalogFrontend.getAttributeGroup() != null
				&& catalogFrontend.getAttributeGroup().getAttributePanelGroupList() != null
				&& catalogFrontend.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
			for (AttributePanelGroup attributePanelGroup : catalogFrontend.getAttributeGroup()
					.getAttributePanelGroupList()) {
				if (attributePanelGroup.getAttribute() != null
						&& attributePanelGroup.getAttribute().getInputType() != null
						&& attributePanelGroup.getAttribute().getInputType().getName().equals("Checkbox")
						&& attributePanelGroup.getAttribute().getAttributeOptionList() != null
						&& attributePanelGroup.getAttribute().getAttributeOptionList().size() > 0) {
					String strValue = "";
					for (AttributeOption attributeOption : attributePanelGroup.getAttribute()
							.getAttributeOptionList()) {
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
		/* perubahan KAI 14/12/2020 [20852]*/
		if (lenOldAttribute >= lenNewAttribute) {
			for (int a = 0; a < lenNewAttribute; a++) {
				catalogAttribute = catalogAttributeList.get(a);
				catalogAttribute.setCatalog(catalogBackend);
				AttributePanelGroup tempAttributePanelGroup = catalogFrontend.getAttributeGroup()
						.getAttributePanelGroupList().get(a);
				catalogAttribute.setAttributePanelGroup(tempAttributePanelGroup);
				catalogAttribute.setNilai(tempAttributePanelGroup.getValue());
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					catalogAttributeHistory.setAttributePanelGroup(catalogAttribute.getAttributePanelGroup());
					catalogAttributeHistory.setCatalog(catalogAttribute.getCatalog());
					catalogAttributeHistory.setCatalogAttribute(catalogAttribute);
					catalogAttributeHistory.setNilai(catalogAttribute.getNilai());
					catalogAttributeHistory.setCatalog(catalogBackend);
					Integer version = new Integer(1);
					CatalogAttributeHistory cah = catalogAttributeHistorySession.getCatalogAttributeHistoryByVersion(catalogAttribute.getId());
					if(cah != null) {
						version += cah.getPerubahanVersi();
						catalogAttributeHistory.setPerubahanVersi(version);
						catalogAttributeHistory.setId(cah.getId());
						catalogAttributeHistory.setIsDelete(cah.getIsDelete());
						catalogAttributeHistorySession.updateCatalogAttributeHistory(catalogAttributeHistory, tempToken);
					}else {
						CatalogAttributeHistory catalogAttributeHistoryNew = new CatalogAttributeHistory();
						catalogAttributeHistoryNew = catalogAttributeHistory;
						catalogAttributeHistoryNew.setPerubahanVersi(version);
						catalogAttributeHistoryNew.setId(null);
						catalogAttributeHistorySession.insertCatalogAttributeHistory(catalogAttributeHistory, tempToken);
					}
				}else {
					catalogAttributeSession.updateCatalogAttribute(catalogAttribute, tempToken);					
				}
			}
			for (int a = lenNewAttribute; a < lenOldAttribute; a++) {
				catalogAttribute = catalogAttributeList.get(a);
				catalogAttributeSession.deleteCatalogAttribute(catalogAttribute.getId(), tempToken);
			}
		} else {
			for (int a = 0; a < lenOldAttribute; a++) {
				catalogAttribute = catalogAttributeList.get(a);
				catalogAttribute.setCatalog(catalogBackend);
				AttributePanelGroup tempAttributePanelGroup = catalogFrontend.getAttributeGroup()
						.getAttributePanelGroupList().get(a);
				catalogAttribute.setAttributePanelGroup(tempAttributePanelGroup);
				catalogAttribute.setNilai(tempAttributePanelGroup.getValue());
				Integer version = new Integer(1);
				CatalogAttributeHistory cah = catalogAttributeHistorySession.getCatalogAttributeHistoryByVersion(catalogAttribute.getId());
				if(cah != null) {
					catalogAttributeHistory.setAttributePanelGroup(catalogAttribute.getAttributePanelGroup());
					catalogAttributeHistory.setCatalog(catalogAttribute.getCatalog());
					catalogAttributeHistory.setCatalogAttribute(catalogAttribute);
					catalogAttributeHistory.setNilai(catalogAttribute.getNilai());
					version += cah.getPerubahanVersi();
					catalogAttributeHistory.setPerubahanVersi(version);
					catalogAttributeHistory.setId(cah.getId());
					catalogAttributeHistory.setCatalog(catalogBackend);
					catalogAttributeHistory.setIsDelete(cah.getIsDelete());
					catalogAttributeHistorySession.updateCatalogAttributeHistory(catalogAttributeHistory, tempToken);
				}else {
					catalogAttributeSession.updateCatalogAttribute(catalogAttribute, tempToken);					
				}

			}
			for (int a = lenOldAttribute; a < lenNewAttribute; a++) {
				catalogAttribute = new CatalogAttribute();
				catalogAttribute.setCatalog(catalogBackend);
				AttributePanelGroup tempAttributePanelGroup = catalogFrontend.getAttributeGroup()
						.getAttributePanelGroupList().get(a);
				catalogAttribute.setAttributePanelGroup(tempAttributePanelGroup);
				catalogAttribute.setIsDelete(0);
				catalogAttribute.setNilai(tempAttributePanelGroup.getValue());
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					
					CatalogAttributeHistory catalogAttributeHistoryNew = new CatalogAttributeHistory();
					catalogAttributeHistoryNew.setAttributePanelGroup(catalogAttribute.getAttributePanelGroup());
					catalogAttributeHistoryNew.setCatalog(catalogAttribute.getCatalog());
					catalogAttributeHistoryNew.setCatalogAttribute(catalogAttribute);
					catalogAttributeHistoryNew.setNilai(catalogAttribute.getNilai());
					catalogAttributeHistoryNew.setPerubahanVersi(Constant.ONE_VALUE);
					catalogAttributeHistoryNew.setCatalog(catalogBackend);
					catalogAttributeHistoryNew.setId(null);
					catalogAttributeHistorySession.insertCatalogAttributeHistory(catalogAttributeHistoryNew, tempToken);
				}else {
					catalogAttributeSession.insertCatalogAttribute(catalogAttribute, tempToken);					
				}
				
			}
		}
	}


	protected void simpanCatalogBulkPrice(Catalog catalogBackend, Catalog catalogFrontend, Token tempToken) {
		CatalogBulkPrice catalogBulkPrice = new CatalogBulkPrice();
		catalogBulkPrice.setCatalog(catalogBackend);
		List<CatalogBulkPrice> catalogBulkPriceList = catalogBulkPriceSession.getCatalogBulkPriceList(catalogBulkPrice);
		CatalogBulkPriceHistory catalogBulkPriceHistory = new CatalogBulkPriceHistory();
		int lenOldBulkPrice = 0;
		if (catalogBulkPriceList != null && catalogBulkPriceList.size() > 0) {
			lenOldBulkPrice = catalogBulkPriceList.size();
		}
		int lenNewBulkPrice = 0;
		if (catalogFrontend.getCatalogBulkPriceList() != null && catalogFrontend.getCatalogBulkPriceList().size() > 0) {
			lenNewBulkPrice = catalogFrontend.getCatalogBulkPriceList().size();
		}
		if (lenOldBulkPrice >= lenNewBulkPrice) {
			for (int a = 0; a < lenNewBulkPrice; a++) {
				catalogBulkPrice = catalogBulkPriceList.get(a);

				CatalogBulkPrice tempCatalogBulkPrice = catalogFrontend.getCatalogBulkPriceList().get(a);
				
				/* perubahan KAI 16/12/2020 [20852]*/
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					catalogBulkPriceHistory.setMaxQuantity(tempCatalogBulkPrice.getMaxQuantity());
					catalogBulkPriceHistory.setMinQuantity(tempCatalogBulkPrice.getMinQuantity());
					catalogBulkPriceHistory.setDiskon(tempCatalogBulkPrice.getDiskon());
					catalogBulkPriceHistory.setCatalogBulkPrice(tempCatalogBulkPrice);
					catalogBulkPriceHistory.setCatalog(catalogBackend);
					catalogBulkPriceHistory.setAction(Constant.UPDATE_ACTION);
					Integer version = new Integer(1);
					CatalogBulkPriceHistory cbph = catalogBulkPriceHistorySession.getCatalogBulkPriceHistoryByVersion(tempCatalogBulkPrice.getId());
					if(cbph != null) {
						version +=cbph.getPerubahanVersi();
						catalogBulkPriceHistory.setPerubahanVersi(version);
						catalogBulkPriceHistory.setIsDelete(cbph.getIsDelete());
						catalogBulkPriceHistory.setId(cbph.getId());
						catalogBulkPriceHistorySession.updateCatalogBulkPriceHistory(catalogBulkPriceHistory, tempToken);
					}else {
						CatalogBulkPriceHistory newCatalogBulkPriceHistory = new CatalogBulkPriceHistory();
						newCatalogBulkPriceHistory = catalogBulkPriceHistory;
						newCatalogBulkPriceHistory.setPerubahanVersi(version);
						newCatalogBulkPriceHistory.setId(null);
						
						catalogBulkPriceHistorySession.insertCatalogBulkPriceHistory(catalogBulkPriceHistory, tempToken);
					}
					
				}else {
					catalogBulkPrice.setCatalog(catalogBackend);
					catalogBulkPrice.setMaxQuantity(tempCatalogBulkPrice.getMaxQuantity());
					catalogBulkPrice.setMinQuantity(tempCatalogBulkPrice.getMinQuantity());
					catalogBulkPrice.setDiskon(tempCatalogBulkPrice.getDiskon());
					if(catalogBulkPriceHistory.getAction()!= null && catalogBulkPriceHistory.getAction().equalsIgnoreCase(Constant.DELETE_ACTION)) {
						catalogBulkPrice.setIsDelete(Constant.ONE_VALUE);
					}
					catalogBulkPriceSession.updateCatalogBulkPrice(catalogBulkPrice, tempToken);					
				}
			}
			for (int a = lenNewBulkPrice; a < lenOldBulkPrice; a++) {
				catalogBulkPrice = catalogBulkPriceList.get(a);
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					CatalogBulkPriceHistory catBulkPriceTamp = catalogBulkPriceHistorySession.getCatalogBulkPriceHistoryByVersion(catalogBulkPrice.getId());
					if(catBulkPriceTamp != null) {
						catBulkPriceTamp.setAction(Constant.DELETE_ACTION);
						catalogBulkPriceHistorySession.updateCatalogBulkPriceHistory(catBulkPriceTamp, tempToken);						
					}else {
						CatalogBulkPriceHistory newCatalogBulkPriceHistory = new CatalogBulkPriceHistory();
						newCatalogBulkPriceHistory.setPerubahanVersi(Constant.ONE_VALUE);
						newCatalogBulkPriceHistory.setCatalogBulkPrice(catalogBulkPriceSession.find(catalogBulkPrice.getId()));
						newCatalogBulkPriceHistory.setCatalog(catalogBackend);
						newCatalogBulkPriceHistory.setAction(Constant.DELETE_ACTION);
						catalogBulkPriceHistorySession.insertCatalogBulkPriceHistory(newCatalogBulkPriceHistory, tempToken);
					}
				}else {
					catalogBulkPriceSession.deleteCatalogBulkPrice(catalogBulkPrice.getId(), tempToken);					
				}
			}
		} else {
			for (int a = 0; a < lenOldBulkPrice; a++) {
				catalogBulkPrice = catalogBulkPriceList.get(a);

				CatalogBulkPrice tempCatalogBulkPrice = catalogFrontend.getCatalogBulkPriceList().get(a);
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					catalogBulkPriceHistory.setMaxQuantity(tempCatalogBulkPrice.getMaxQuantity());
					catalogBulkPriceHistory.setMinQuantity(tempCatalogBulkPrice.getMinQuantity());
					catalogBulkPriceHistory.setDiskon(tempCatalogBulkPrice.getDiskon());
					catalogBulkPriceHistory.setCatalogBulkPrice(catalogBulkPriceSession.find(catalogBulkPrice.getId()));
					catalogBulkPriceHistory.setCatalog(catalogBackend);
					catalogBulkPriceHistory.setAction(Constant.UPDATE_ACTION);
					Integer version = new Integer(1);
					CatalogBulkPriceHistory cbph = catalogBulkPriceHistorySession.getCatalogBulkPriceHistoryByVersion(tempCatalogBulkPrice.getId());
					if(cbph != null) {
						version +=cbph.getPerubahanVersi();
						catalogBulkPriceHistory.setPerubahanVersi(version);
						catalogBulkPriceHistory.setIsDelete(cbph.getIsDelete());
						catalogBulkPriceHistory.setId(cbph.getId());
						catalogBulkPriceHistorySession.updateCatalogBulkPriceHistory(catalogBulkPriceHistory, tempToken);
					}else {
						CatalogBulkPriceHistory newCatalogBulkPriceHistory = new CatalogBulkPriceHistory();
						newCatalogBulkPriceHistory = catalogBulkPriceHistory;
						newCatalogBulkPriceHistory.setPerubahanVersi(version);
						newCatalogBulkPriceHistory.setId(null);
						
						catalogBulkPriceHistorySession.insertCatalogBulkPriceHistory(catalogBulkPriceHistory, tempToken);
					}
					
				}else {
					catalogBulkPrice.setCatalog(catalogBackend);
					catalogBulkPrice.setMaxQuantity(tempCatalogBulkPrice.getMaxQuantity());
					catalogBulkPrice.setMinQuantity(tempCatalogBulkPrice.getMinQuantity());
					catalogBulkPrice.setDiskon(tempCatalogBulkPrice.getDiskon());
					if(catalogBulkPriceHistory.getAction()!= null && catalogBulkPriceHistory.getAction().equalsIgnoreCase(Constant.DELETE_ACTION)) {
						catalogBulkPrice.setIsDelete(Constant.ONE_VALUE);
					}
					catalogBulkPriceSession.updateCatalogBulkPrice(catalogBulkPrice, tempToken);					
				}
			}
			for (int a = lenOldBulkPrice; a < lenNewBulkPrice; a++) {
				catalogBulkPrice = new CatalogBulkPrice();
				catalogBulkPrice.setCatalog(catalogBackend);

				CatalogBulkPrice tempCatalogBulkPrice = catalogFrontend.getCatalogBulkPriceList().get(a);
				catalogBulkPrice.setMaxQuantity(tempCatalogBulkPrice.getMaxQuantity());
				catalogBulkPrice.setMinQuantity(tempCatalogBulkPrice.getMinQuantity());
				catalogBulkPrice.setDiskon(tempCatalogBulkPrice.getDiskon());
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					CatalogBulkPriceHistory newCatalogBulkPriceHistory = new CatalogBulkPriceHistory();
					newCatalogBulkPriceHistory.setMaxQuantity(tempCatalogBulkPrice.getMaxQuantity());
					newCatalogBulkPriceHistory.setMinQuantity(tempCatalogBulkPrice.getMinQuantity());
					newCatalogBulkPriceHistory.setDiskon(tempCatalogBulkPrice.getDiskon());
					newCatalogBulkPriceHistory.setCatalog(catalogBackend);
					newCatalogBulkPriceHistory.setPerubahanVersi(Constant.ONE_VALUE);
					newCatalogBulkPriceHistory.setId(null);
					newCatalogBulkPriceHistory.setAction(Constant.INSERT_ACTION);
					catalogBulkPriceHistorySession.insertCatalogBulkPriceHistory(newCatalogBulkPriceHistory, tempToken);
				}else {
					catalogBulkPriceSession.insertCatalogBulkPrice(catalogBulkPrice, tempToken);					
				}
			}
		}
	}

	protected void simpanCatalogFee(Catalog catalogBackend, Catalog catalogFrontend, Token tempToken) {
		CatalogFee catalogFee = new CatalogFee();
		catalogFee.setCatalog(catalogBackend);
		List<CatalogFee> catalogFeeList = catalogFeeSession.getCatalogFeeList(catalogFee);
		CatalogFeeHistory catalogFeeHistory = new CatalogFeeHistory();
		int lenOldFee = 0;
		if (catalogFeeList != null && catalogFeeList.size() > 0) {
			lenOldFee = catalogFeeList.size();
		}
		int lenNewFee = 0;
		if (catalogFrontend.getCatalogFeeList() != null && catalogFrontend.getCatalogFeeList().size() > 0) {
			lenNewFee = catalogFrontend.getCatalogFeeList().size();
		}
		List<Integer> idDihapusList = new ArrayList<>();
		for(CatalogFee feeOld : catalogFeeList) {
			Integer idDihapus = null;
			for(CatalogFee feeNew : catalogFrontend.getCatalogFeeList()) {
				if(!feeOld.getId().equals(feeNew.getId())) {
					idDihapus = feeOld.getId();
				}else {
					idDihapus = null;
					break;
				}
			}
			if(idDihapus != null) {
				idDihapusList.add(idDihapus);
			}
		}
		
		if(lenOldFee >= lenNewFee) {
			for (int a = 0; a < lenNewFee; a++) {
				catalogFee = catalogFeeList.get(a);
				catalogFeeHistory = new CatalogFeeHistory();
				CatalogFee tempCatalogFee = catalogFrontend.getCatalogFeeList().get(a);
				//catalogFee.setAsuransi(tempCatalogFee.getAsuransi());
				
				/* perubahan KAI 16/12/2020 [20852]*/
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					catalogFeeHistory.setAsuransi(Constant.ASURANSI);
					catalogFeeHistory.setOngkosKirim(tempCatalogFee.getOngkosKirim());
					catalogFeeHistory.setQuantityBatch(tempCatalogFee.getQuantityBatch());
					catalogFeeHistory.setOrganisasi(tempCatalogFee.getOrganisasi());
					catalogFeeHistory.setSlaDeliveryTime(tempCatalogFee.getSlaDeliveryTime());
					catalogFeeHistory.setCatalog(catalogBackend);
					CatalogFeeHistory cfh = null;
					
					if(tempCatalogFee.getId() != null) {
						catalogFeeHistory.setCatalogFee(catalogFeeSession.find(tempCatalogFee.getId()));						
						cfh = catalogFeeHistorySession.getCatalogFeeHistoryByVersion(tempCatalogFee.getId());
					}
					catalogFeeHistory.setAction(Constant.UPDATE_ACTION);
					Integer version = new Integer(1);
					if(cfh != null) {
						version += cfh.getPerubahanVersi();
						catalogFeeHistory.setPerubahanVersi(version);
						catalogFeeHistory.setId(cfh.getId());
						catalogFeeHistory.setIsDelete(cfh.getIsDelete());
						catalogFeeHistorySession.updateCatalogFeeHistory(catalogFeeHistory, tempToken);
					}else {
						CatalogFeeHistory catalogFeeHistoryNew = new CatalogFeeHistory();
						if(catalogFeeHistory.getCatalogFee() == null) {
							catalogFeeHistory.setAction(Constant.INSERT_ACTION);
						}
						catalogFeeHistoryNew = catalogFeeHistory;
						catalogFeeHistoryNew.setId(null);
						catalogFeeHistoryNew.setPerubahanVersi(version);
						catalogFeeHistorySession.insertCatalogFeeHistory(catalogFeeHistoryNew, tempToken);
					}
				}else {
					catalogFee.setCatalog(catalogBackend);
					catalogFee.setAsuransi(Constant.ASURANSI);
					catalogFee.setOngkosKirim(tempCatalogFee.getOngkosKirim());
					catalogFee.setQuantityBatch(tempCatalogFee.getQuantityBatch());
					catalogFee.setOrganisasi(tempCatalogFee.getOrganisasi());
					catalogFee.setSlaDeliveryTime(tempCatalogFee.getSlaDeliveryTime());
					catalogFeeHistory = catalogFeeHistorySession.getCatalogFeeHistoryByVersion(tempCatalogFee.getId());
					if(catalogFeeHistory.getAction()!= null && catalogFeeHistory.getAction().equalsIgnoreCase(Constant.DELETE_ACTION)) {
						catalogFee.setIsDelete(Constant.ONE_VALUE);
					}
					catalogFeeSession.updateCatalogFee(catalogFee, tempToken);					
				}
			}
			for (Integer catalogFeeId : idDihapusList) {
				CatalogFee tempCatalogFee = catalogFeeSession.find(catalogFeeId);
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					CatalogFeeHistory cfh = catalogFeeHistorySession.getCatalogFeeHistoryByVersion(catalogFeeId);
					if(cfh != null) {
						cfh.setAction(Constant.DELETE_ACTION);
						catalogFeeHistorySession.updateCatalogFeeHistory(catalogFeeHistory, tempToken);
					}else {
						CatalogFeeHistory catalogFeeHistoryNew = new CatalogFeeHistory();
						CatalogFee catalogFeeDeleted = catalogFeeSession.find(catalogFeeId);
						catalogFeeHistoryNew.setCatalogFee(catalogFeeDeleted);
						catalogFeeHistoryNew.setAction(Constant.DELETE_ACTION);
						catalogFeeHistoryNew.setCatalog(catalogBackend);
						catalogFeeHistoryNew.setAsuransi(Constant.ASURANSI);
						catalogFeeHistoryNew.setOngkosKirim(catalogFeeDeleted.getOngkosKirim());
						catalogFeeHistoryNew.setQuantityBatch(catalogFeeDeleted.getQuantityBatch());
						catalogFeeHistoryNew.setSlaDeliveryTime(catalogFeeDeleted.getSlaDeliveryTime());
						catalogFeeHistoryNew.setCatalog(catalogFeeDeleted.getCatalog());
						catalogFeeHistoryNew.setOrganisasi(catalogFeeDeleted.getOrganisasi());
						catalogFeeHistoryNew.setPerubahanVersi(Constant.ZERO_VALUE);
						catalogFeeHistorySession.insertCatalogFeeHistory(catalogFeeHistoryNew, tempToken);
					}
				}else {
					catalogFeeSession.deleteCatalogFee(tempCatalogFee.getId(), tempToken);					
				}
			}
			
		}else {
			for (int a = 0; a < lenOldFee; a++) {
				catalogFee = catalogFeeList.get(a);
				catalogFeeHistory = new CatalogFeeHistory();
				CatalogFee tempCatalogFee = catalogFrontend.getCatalogFeeList().get(a);
				//catalogFee.setAsuransi(tempCatalogFee.getAsuransi());
				
				/* perubahan KAI 16/12/2020 [20852]*/
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					catalogFeeHistory.setAsuransi(Constant.ASURANSI);
					catalogFeeHistory.setOngkosKirim(tempCatalogFee.getOngkosKirim());
					catalogFeeHistory.setQuantityBatch(tempCatalogFee.getQuantityBatch());
					catalogFeeHistory.setOrganisasi(tempCatalogFee.getOrganisasi());
					catalogFeeHistory.setSlaDeliveryTime(tempCatalogFee.getSlaDeliveryTime());
					catalogFeeHistory.setCatalog(catalogBackend);
					catalogFeeHistory.setCatalogFee(catalogFeeSession.find(tempCatalogFee.getId()));
					catalogFeeHistory.setAction(Constant.UPDATE_ACTION);
					Integer version = new Integer(1);
					CatalogFeeHistory cfh = catalogFeeHistorySession.getCatalogFeeHistoryByVersion(tempCatalogFee.getId());
					if(cfh != null) {
						version += cfh.getPerubahanVersi();
						catalogFeeHistory.setPerubahanVersi(version);
						catalogFeeHistory.setId(cfh.getId());
						catalogFeeHistory.setIsDelete(cfh.getIsDelete());
						catalogFeeHistorySession.updateCatalogFeeHistory(catalogFeeHistory, tempToken);
					}else {
						CatalogFeeHistory catalogFeeHistoryNew = new CatalogFeeHistory();
						catalogFeeHistoryNew = catalogFeeHistory;
						catalogFeeHistoryNew.setId(null);
						catalogFeeHistoryNew.setPerubahanVersi(version);
						catalogFeeHistorySession.insertCatalogFeeHistory(catalogFeeHistoryNew, tempToken);
					}
				}else {
					catalogFee.setCatalog(catalogBackend);
					catalogFee.setAsuransi(Constant.ASURANSI);
					catalogFee.setOngkosKirim(tempCatalogFee.getOngkosKirim());
					catalogFee.setQuantityBatch(tempCatalogFee.getQuantityBatch());
					catalogFee.setOrganisasi(tempCatalogFee.getOrganisasi());
					catalogFee.setSlaDeliveryTime(tempCatalogFee.getSlaDeliveryTime());
					catalogFeeHistory = catalogFeeHistorySession.getCatalogFeeHistoryByVersion(catalogFee.getId());
					if(catalogFeeHistory.getAction()!= null && catalogFeeHistory.getAction().equalsIgnoreCase(Constant.DELETE_ACTION)) {
						catalogFee.setIsDelete(Constant.ONE_VALUE);
					}
					catalogFeeSession.updateCatalogFee(catalogFee, tempToken);					
				}
			}
			
			for (int a = lenOldFee; a < lenNewFee; a++) {
				catalogFeeHistory = new CatalogFeeHistory();
				catalogFee = new CatalogFee();
				catalogFee.setCatalog(catalogBackend);
				CatalogFee tempCatalogFee = catalogFrontend.getCatalogFeeList().get(a);
				//catalogFee.setAsuransi(tempCatalogFee.getAsuransi());
				catalogFee.setAsuransi(Constant.ASURANSI);
				catalogFee.setOngkosKirim(tempCatalogFee.getOngkosKirim());
				catalogFee.setQuantityBatch(tempCatalogFee.getQuantityBatch());
				catalogFee.setOrganisasi(tempCatalogFee.getOrganisasi());
				catalogFee.setIsDelete(0);
				catalogFee.setSlaDeliveryTime(tempCatalogFee.getSlaDeliveryTime());
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					CatalogFeeHistory catalogFeeHistoryNew = new CatalogFeeHistory();
					catalogFeeHistoryNew.setAsuransi(Constant.ASURANSI);
					catalogFeeHistoryNew.setOngkosKirim(tempCatalogFee.getOngkosKirim());
					catalogFeeHistoryNew.setQuantityBatch(tempCatalogFee.getQuantityBatch());
					catalogFeeHistoryNew.setOrganisasi(tempCatalogFee.getOrganisasi());
					catalogFeeHistoryNew.setSlaDeliveryTime(tempCatalogFee.getSlaDeliveryTime());
					catalogFeeHistoryNew.setCatalog(catalogBackend);
					catalogFeeHistoryNew.setPerubahanVersi(Constant.ONE_VALUE);
					catalogFeeHistoryNew.setAction(Constant.INSERT_ACTION);
					catalogFeeHistorySession.insertCatalogFeeHistory(catalogFeeHistoryNew, tempToken);
				}else {
					catalogFeeSession.insertCatalogFee(catalogFee, tempToken);					
				}
			}
			
			for (Integer catalogFeeId : idDihapusList) {
				CatalogFee tempCatalogFee = catalogFeeSession.find(catalogFeeId);
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					CatalogFeeHistory cfh = catalogFeeHistorySession.getCatalogFeeHistoryByVersion(catalogFeeId);
					if(cfh != null) {
						cfh.setAction(Constant.DELETE_ACTION);
						catalogFeeHistorySession.updateCatalogFeeHistory(catalogFeeHistory, tempToken);
					}else {
						CatalogFeeHistory catalogFeeHistoryNew = new CatalogFeeHistory();
						CatalogFee catalogFeeDeleted = catalogFeeSession.find(catalogFeeId);
						catalogFeeHistoryNew.setCatalogFee(catalogFeeDeleted);
						catalogFeeHistoryNew.setAction(Constant.DELETE_ACTION);
						catalogFeeHistoryNew.setCatalog(catalogBackend);
						catalogFeeHistoryNew.setAsuransi(Constant.ASURANSI);
						catalogFeeHistoryNew.setOngkosKirim(catalogFeeDeleted.getOngkosKirim());
						catalogFeeHistoryNew.setQuantityBatch(catalogFeeDeleted.getQuantityBatch());
						catalogFeeHistoryNew.setSlaDeliveryTime(catalogFeeDeleted.getSlaDeliveryTime());
						catalogFeeHistoryNew.setCatalog(catalogFeeDeleted.getCatalog());
						catalogFeeHistoryNew.setOrganisasi(catalogFeeDeleted.getOrganisasi());
						catalogFeeHistoryNew.setPerubahanVersi(Constant.ZERO_VALUE);
						catalogFeeHistorySession.insertCatalogFeeHistory(catalogFeeHistoryNew, tempToken);
					}
				}else {
					catalogFeeSession.deleteCatalogFee(tempCatalogFee.getId(), tempToken);					
				}
			}
		}
		
		

	}

	protected void simpanCatalogStock(Catalog catalogBackend, Catalog catalogFrontend, Token tempToken) {
		CatalogStock catalogStock = new CatalogStock();
		catalogStock.setCatalog(catalogBackend);
		List<CatalogStock> catalogStockList = catalogStockSession.getCatalogStockListByCatalog(catalogFrontend);
		catalogStockSession.updateCatalogStockByCatalog(catalogStock, tempToken);
		if (catalogFrontend.getQuantity() != null) {
			catalogStock = new CatalogStock();
			catalogStock.setCatalog(catalogBackend);
			catalogStock.setQuantity(catalogFrontend.getQuantity());
			catalogStock.setTotalStock((catalogFrontend.getCurrentStock() - catalogFrontend.getQuantity())
					+ catalogFrontend.getQuantity());
			catalogStock.setIsDelete(0);

			catalogStockSession.insertCatalogStock(catalogStock, tempToken);
		}

	}

	protected void simpanCatalogImage(Catalog catalogBackend, Catalog catalogFrontend, Token tempToken) {
		CatalogImage catalogImage = new CatalogImage();
		CatalogImageHistory catalogImageHistory = new CatalogImageHistory();
		catalogImage.setCatalog(catalogBackend);
		List<CatalogImage> catalogImageList = catalogImageSession.getCatalogImageList(catalogImage);

		int lenOldImage = 0;
		if (catalogImageList != null && catalogImageList.size() > 0) {
			lenOldImage = catalogImageList.size();
		}
		int lenNewImage = 0;
		if (catalogFrontend.getCatalogImageList() != null && catalogFrontend.getCatalogImageList().size() > 0) {
			lenNewImage = catalogFrontend.getCatalogImageList().size();
		}
		/* perubahan KAI 14/12/2020 [20852]*/
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
				if(catalogImageHistory.getAction()!= null && catalogImageHistory.getAction().equalsIgnoreCase(Constant.DELETE_ACTION)) {
					catalogImage.setIsDelete(Constant.ONE_VALUE);
				}
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					catalogImageHistory.setCatalogImage(catalogImageSession.find(catalogImage.getId()));
					catalogImageHistory.setImagesFileName(catalogImage.getImagesFileName());
					catalogImageHistory.setImagesFileSize(catalogImage.getImagesFileSize());
					catalogImageHistory.setImagesRealName(catalogImage.getImagesRealName());
					catalogImageHistory.setStatus(catalogImage.getStatus());
					catalogImageHistory.setUrutanPesanan(catalogImage.getUrutanPesanan());
					catalogImageHistory.setCatalog(catalogBackend);
					Integer version = new Integer(1);
					CatalogImageHistory cih = catalogImageHistorySession.getCatalogImageHistoryByVersion(catalogImage.getId());
					if(cih != null) {
						version += cih.getPerubahanVersi();
						catalogImageHistory.setPerubahanVersi(version);
						catalogImageHistory.setId(cih.getId());
						catalogImageHistory.setIsDelete(cih.getIsDelete());
						catalogImageHistorySession.updateCatalogImageHistory(catalogImageHistory, tempToken);
					}else {
						catalogImageHistory.setPerubahanVersi(version);
						catalogImageHistory.setId(null);
						catalogImageHistorySession.insertCatalogImageHistory(catalogImageHistory, tempToken);						
					}
					
				}else {
					catalogImageSession.updateCatalogImage(catalogImage, tempToken);					
				}

			}
			for (int a = lenNewImage; a < lenOldImage; a++) {
				catalogImage = catalogImageList.get(a);
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					CatalogImageHistory cih = catalogImageHistorySession.getCatalogImageHistoryByVersion(catalogImage.getId());
					if(cih != null) {
						cih.setAction(Constant.DELETE_ACTION);
						catalogImageHistorySession.updateCatalogImageHistory(cih, tempToken);
					}else {
						CatalogImageHistory catalogImageHistoryNew = new CatalogImageHistory();
						catalogImageHistoryNew.setPerubahanVersi(Constant.ONE_VALUE);
						catalogImageHistoryNew.setAction(Constant.DELETE_ACTION);
						catalogImageHistoryNew.setCatalog(catalogBackend);
						catalogImageHistoryNew.setCatalogImage(catalogImageSession.find(catalogImage.getId()));
						catalogImageHistorySession.insertCatalogImageHistory(catalogImageHistoryNew, tempToken);
					}
				}else {
					catalogImageSession.deleteCatalogImage(catalogImage.getId(), tempToken);					
				}
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
				if(catalogImageHistory.getAction()!= null && catalogImageHistory.getAction().equalsIgnoreCase(Constant.DELETE_ACTION)) {
					catalogImage.setIsDelete(Constant.ONE_VALUE);
				}
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					catalogImageHistory.setCatalogImage(catalogImageSession.find(catalogImage.getId()));
					catalogImageHistory.setImagesFileName(catalogImage.getImagesFileName());
					catalogImageHistory.setImagesFileSize(catalogImage.getImagesFileSize());
					catalogImageHistory.setImagesRealName(catalogImage.getImagesRealName());
					catalogImageHistory.setStatus(catalogImage.getStatus());
					catalogImageHistory.setUrutanPesanan(catalogImage.getUrutanPesanan());
					catalogImageHistory.setCatalog(catalogBackend);
					Integer version = new Integer(1);
					CatalogImageHistory cih = catalogImageHistorySession.getCatalogImageHistoryByVersion(catalogImage.getId());
					if(cih != null) {
						version += cih.getPerubahanVersi();
						catalogImageHistory.setPerubahanVersi(version);
						catalogImageHistory.setId(cih.getId());
						catalogImageHistory.setIsDelete(cih.getIsDelete());
						catalogImageHistorySession.updateCatalogImageHistory(catalogImageHistory, tempToken);
					}else {
						CatalogImageHistory catalogImageHistoryNew = new CatalogImageHistory();
						catalogImageHistoryNew = catalogImageHistory;
						catalogImageHistoryNew.setPerubahanVersi(version);
						catalogImageHistoryNew.setId(null);
						catalogImageHistorySession.insertCatalogImageHistory(catalogImageHistoryNew, tempToken);
						
					}
				}else {
					catalogImageSession.updateCatalogImage(catalogImage, tempToken);					
				}
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
				if(catalogFrontend.getTodo().equalsIgnoreCase("edit")) {
					CatalogImageHistory catalogImageHistoryNew = new CatalogImageHistory();
					catalogImageHistoryNew.setImagesFileName(catalogImage.getImagesFileName());
					catalogImageHistoryNew.setImagesFileSize(catalogImage.getImagesFileSize());
					catalogImageHistoryNew.setImagesRealName(catalogImage.getImagesRealName());
					catalogImageHistoryNew.setStatus(catalogImage.getStatus());
					catalogImageHistoryNew.setUrutanPesanan(catalogImage.getUrutanPesanan());
					catalogImageHistoryNew.setPerubahanVersi(Constant.ONE_VALUE);
					catalogImageHistoryNew.setCatalog(catalogBackend);
					catalogImageHistoryNew.setId(null);
					catalogImageHistorySession.insertCatalogImageHistory(catalogImageHistoryNew, tempToken);
				}else {
					catalogImageSession.insertCatalogImage(catalogImage, tempToken);					
				}
			}
		}
	}

	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Catalog delete(Catalog catalog, @HeaderParam("Authorization") String token) {
		Token tempToken = tokenSession.findByToken(token);
		for(CatalogAttribute catalogAttribute : catalogAttributeSession.getCatalogAttributeListByCatalogId(catalog)) {
			catalogAttributeSession.deleteCatalogAttribute(catalogAttribute.getId(), tempToken);			
		}
		CatalogCategory catalogCategory = catalogCategorySession.getCatalogCategoryByCatalogId(catalog);
		catalogCategorySession.deleteCatalogCategory(catalogCategory.getId(), tempToken);
		for(CatalogBulkPrice catalogBulkPrice : catalogBulkPriceSession.getCatalogBulkPriceListByCatalog(catalog)) {
			catalogBulkPriceSession.deleteCatalogBulkPrice(catalogBulkPrice.getId(), tempToken);
		}
		for(CatalogImage catalogImage : catalogImageSession.getCatalogImageListByCatalogId(catalog)) {
			catalogImageSession.deleteCatalogImage(catalogImage.getId(), tempToken);
		}
		for(CatalogFee catalogFee : catalogFeeSession.getListCatalogFeeByCatalogId(catalog.getId())) {
			catalogFeeSession.deleteCatalogFee(catalogFee.getId(), tempToken);			
		}
		for(CatalogAttributeHistory catalogAttributeHistory : catalogAttributeHistorySession.getCatalogAttributeHistoryByCatalog(catalog)) {
			catalogAttributeHistorySession.deleteCatalogAttributeHistory(catalogAttributeHistory.getId(), tempToken);			
		}
		for(CatalogCategoryHistory catalogCategoryHistory : catalogCategoryHistorySession.getCatalogCategoryHistoryByCatalogId(catalog)) {
			catalogCategoryHistorySession.deleteCatalogCategoryHistory(catalogCategoryHistory.getId(), tempToken);
		}
		for(CatalogBulkPriceHistory catalogBulkPriceHistory : catalogBulkPriceHistorySession.getCatalogBulkPriceHistoryByCatalogId(catalog)) {
			catalogBulkPriceHistorySession.deleteCatalogBulkPriceHistory(catalogBulkPriceHistory.getId(), tempToken);
		}
		for(CatalogImageHistory catalogImageHistory : catalogImageHistorySession.getCatalogImageHistoryByCatalog(catalog)) {
			catalogImageHistorySession.deleteCatalogImageHistory(catalogImageHistory.getId(), tempToken);
		}
		for(CatalogFeeHistory catalogFeeHistory : catalogFeeHistorySession.getCatalogFeeHistoryByCatalogId(catalog)) {
			catalogFeeHistorySession.deleteCatalogFeeHistory(catalogFeeHistory.getId(), tempToken);			
		}
		catalogSession.deleteCatalog(catalog.getId(), tempToken);
		CatalogContractDetail catalogcContractDetail = catalogContractDetailSession.find(catalog.getCatalogContractDetail().getId());
		catalogcContractDetail.setFlagItemUsed(Constant.ZERO_VALUE);
		catalogContractDetailSession.update(catalogcContractDetail, tokenSession.findByToken(token));
		return catalog;
	}

	@POST
	@Path("/readExcell")
	@Consumes("multipart/form-data")
	public Response readExcell(MultipartFormDataInput input, @HeaderParam("Authorization") String token) {

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");
		String returnMsg = null;
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

				returnMsg = analyseExcel(workbook, token);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String json = "{\"msg\":\"" + returnMsg + "\"}";
		return Response.ok(json, MediaType.APPLICATION_JSON).build();

	}

	protected String analyseExcel(Workbook workbook, String token) {
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
		// start read sheet
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
					if (nextRow.getCell(4) != null) {
						if (header) {
							if (lastCellNumber > 17) {
								attributeList = new Attribute[lastCellNumber + 1];
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
									List<Attribute> tempAttributeList = attributeSession
											.getAttributeList(tempAttribute);
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
										for (int checkRowIndex = rowNum
												+ 1; checkRowIndex <= lastRowNum; checkRowIndex++) {
											Row checkRow = sheet.getRow(checkRowIndex);
											Cell checkCell = checkRow.getCell(currentCellColumn);

											if (checkCell != null) {
												if (checkCell.getCellType() != Cell.CELL_TYPE_BLANK
														&& checkCell.getCellType() == Cell.CELL_TYPE_NUMERIC
														&& HSSFDateUtil.isCellDateFormatted(checkCell)) {
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
								List<AttributeGroup> attributeGroupList = attributeGroupSession
										.getAttributeGroupList(attributeGroup);
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
								for (int a = 18; a < lastCellNumber; a++) {
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
									attributeGroup = attributeGroupSession.updateAttributeGroup(attributeGroup,
											tempToken);
								} else {
									attributeGroup = attributeGroupSession.insertAttributeGroup(attributeGroup,
											tempToken);
								}
							}
						} else {
							Catalog tempCatalog = new Catalog();
							if (attributeGroup != null) {
								tempCatalog.setAttributeGroup(attributeGroup);
							}
							Cell cell = nextRow.getCell(4);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_STRING) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								return "ERROR!, Kode Produk Tidak Boleh Kosong!";
							} else {
								// memastikan tipe data string
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									String kodeProduk = cell.getStringCellValue();
									Catalog catalog = catalogSession.getCatalogByKodeProduk(kodeProduk);
									if (catalog != null) {
										return "ERROR!, Kode Produk Tidak Boleh Sama!";
									} else {
										Catalog checkCatalog = new Catalog();
										checkCatalog.setKodeProduk(kodeProduk);
										List<Catalog> catalogList = catalogSession.getCatalogList(checkCatalog);
										if (catalogList != null && catalogList.size() > 0) {
											tempCatalog = catalogList.get(0);
										} else {
											tempCatalog.setKodeProduk(kodeProduk.toUpperCase());
										}
									}

								} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									String kodeProduk = Double.toString(cell.getNumericCellValue());
									Catalog checkCatalog = new Catalog();
									checkCatalog.setKodeProduk(kodeProduk);
									List<Catalog> catalogList = catalogSession.getCatalogList(checkCatalog);
									if (catalogList != null && catalogList.size() > 0) {
										tempCatalog = catalogList.get(0);
									} else {
										tempCatalog.setKodeProduk(kodeProduk.toUpperCase());
									}
								} else {
									return "ERROR!, Kode Produk Harus Berupa Alpha Numerik!";
								}

							}

							cell = nextRow.getCell(0);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_STRING) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								return "ERROR!, Item Type (Material/Jasa) Tidak boleh Kosong!";

							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									List<ItemType> itemTypeList = itemTypeSession
											.getItemTypeListByNameEqual(cell.getStringCellValue());
									if (itemTypeList != null && itemTypeList.size() > 0) {
										tempCatalog.setItemType(itemTypeList.get(0));
									}
								} else {
									return "ERROR!, Item Type Harus Berupa Text!";
								}
							}

							cell = nextRow.getCell(1);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_STRING) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								Vendor vendor = vendorSession.getVendorByUserId(tempToken.getUser().getId()); // cek
																												// vendor
																												// atau
																												// bukan
								if (vendor != null) {
									tempCatalog.setVendor(vendor); // jika vendor
								} else {
									return "ERROR!, Nama Vendor Tidak Boleh Kosong!"; // jika panitia
								}

							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									List<Vendor> vendorList = vendorSession
											.getVendorListByNameEqual(cell.getStringCellValue());
									if (vendorList != null && vendorList.size() > 0) {
										tempCatalog.setVendor(vendorList.get(0));
									}
								} else {
									return "ERROR!, Nama Vendor Harus Berupa Text!";
								}
							}

							cell = nextRow.getCell(2);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_STRING) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								return "ERROR!, Nama Produk Tidak Boleh Kosong!";
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									tempCatalog.setNamaIND(cell.getStringCellValue());
								} else {
									return "ERROR!, Nama Produk Harus Berupa Text!";
								}

							}

							cell = nextRow.getCell(3);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_STRING) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								return "ERROR!, Deskripsi Katalog Tidak Boleh Kosong!";
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									tempCatalog.setDeskripsiIND(cell.getStringCellValue());
								} else {
									return "ERROR!, Deskripsi Produk Harus Berupa Text!";
								}

							}
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_STRING) {
							cell = nextRow.getCell(5);
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								return "ERROR!, Satuan Tidak Boleh Kosong!";
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									List<Satuan> satuanList = satuanSession
											.getSatuanListByNameEqual(cell.getStringCellValue());
									if (satuanList != null && satuanList.size() > 0) {
										tempCatalog.setSatuan(satuanList.get(0));
									} else {
										return "ERROR!, Satuan Tidak Terdaftar";
									}
								} else {
									return "ERROR!, Satuan Harus Berupa Text!";
								}
							}

							cell = nextRow.getCell(6);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								return "ERROR!, Harga Tidak Boleh Kosong!";
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									tempCatalog.setHarga(cell.getNumericCellValue());
								} else {
									return "ERROR!, Harga Harus Berupa Angka!";
								}
							}

							cell = nextRow.getCell(7);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_STRING) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								return "ERROR!, Mata Uang Tidak Boleh Kosong!";
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									List<MataUang> mataUangList = mataUangSession
											.getMataUangListByKodeEqual(cell.getStringCellValue());
									if (mataUangList != null && mataUangList.size() > 0) {
										tempCatalog.setMataUang(mataUangList.get(0));
									} else {
										return "ERROR!, Mata Uang Tidak Terdaftar!";
									}
								} else {
									return "ERROR!, Mata Uang Harus Berupa Text!";
								}
							}

							// boleh kosong
							cell = nextRow.getCell(8);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								// return "ERROR!, Berat Tidak Boleh Kosong!";
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									tempCatalog.setBerat((Double) (cell.getNumericCellValue()));
								} else {
									return "ERROR!, Berat Harus Berupa Angka!";
								}
							}

							cell = nextRow.getCell(9);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_STRING) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								if (tempCatalog.getBerat() != null) {
									return "ERROR!, Satuan Berat Tidak Boleh Kosong!";
								}
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									tempCatalog.setSatuanBerat(cell.getStringCellValue().toLowerCase());
								} else {
									return "ERROR!, Satuan Berat Harus Berupa Text!";
								}
							}

							// boleh kosong
							cell = nextRow.getCell(10);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								// return "ERROR!, Panjang Tidak Boleh Kosong!";
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									tempCatalog.setPanjang((Double) (cell.getNumericCellValue()));
								} else {
									return "ERROR!, Panjang Harus Berupa Angka!";
								}
							}

							cell = nextRow.getCell(11);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_STRING) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								if (tempCatalog.getPanjang() != null) {
									return "ERROR!, Satuan Panjang Tidak Boleh Kosong!";
								}
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									tempCatalog.setSatuanPanjang(cell.getStringCellValue().toLowerCase());
								} else {
									return "ERROR!, Satuan Panjang Harus Berupa Text!";
								}
							}

							// boleh kosong
							cell = nextRow.getCell(12);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								// return "ERROR!, Lebar Tidak Boleh Kosong!";
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									tempCatalog.setLebar((Double) (cell.getNumericCellValue()));
								} else {
									return "ERROR!, Lebar Harus Berupa Angka!";
								}
							}

							cell = nextRow.getCell(13);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_STRING) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								// return "ERROR!, Satuan Lebar Tidak Boleh Kosong!";
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									tempCatalog.setSatuanLebar(cell.getStringCellValue().toLowerCase());
								} else {
									return "ERROR!, Satuan Lebar Harus Berupa Text!";
								}
							}

							cell = nextRow.getCell(14);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								// return "ERROR!, Tinggi Tidak Boleh Kosong!";
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									tempCatalog.setTinggi((Double) (cell.getNumericCellValue()));
								} else {
									return "ERROR!, Tinggi Harus Berupa Angka!";
								}
							}

							cell = nextRow.getCell(15);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_STRING) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								if (tempCatalog.getTinggi() != null) {
									return "ERROR!, Satuan Tinggi Tidak Boleh Kosong!";
								}
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									tempCatalog.setSatuanTinggi(cell.getStringCellValue().toLowerCase());
								} else {
									return "ERROR!, Satuan Tinggi Harus Berupa Text!";
								}
							}
							tempCatalog.setStatus(true); // set status aktif
							/*perubahan KAI 01/18/2021*/
							tempCatalog = simpanCatalogWithItem(tempCatalog, tempToken, null); // simpan catalog

							// kategory diskip setnya
							cell = nextRow.getCell(16); // category
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_STRING) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								return "ERROR!, Kategori Tidak Boleh Kosong!";
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									String categoryImp = cell.getStringCellValue();
									Category categoryInd = categorySession.getCategoryByNameInd(categoryImp);

									if (categoryInd != null) {
										// skip

										List<Category> categoryList = new ArrayList<Category>();
										categoryList.add(categoryInd);
										for (int i = 0; i < categoryList.size(); i++) {
											if (categoryList.get(i).getParentCategory() != null) {
												Category parentCategories = new Category();
												parentCategories.setId(categoryList.get(i).getParentCategory().getId());
												parentCategories.setAdminLabel(
														categoryList.get(i).getParentCategory().getAdminLabel());
												parentCategories.setDescription(
														categoryList.get(i).getParentCategory().getDescription());
												parentCategories.setFlagEnabled(
														categoryList.get(i).getParentCategory().getFlagEnabled());
												parentCategories.setIsDelete(
														categoryList.get(i).getParentCategory().getIsDelete());
												parentCategories.setTranslateEng(
														categoryList.get(i).getParentCategory().getTranslateEng());
												parentCategories.setTranslateInd(
														categoryList.get(i).getParentCategory().getTranslateInd());
												parentCategories
														.setValue(categoryList.get(i).getParentCategory().getValue());
												parentCategories.setParentCategory(
														categoryList.get(i).getParentCategory().getParentCategory());
												categoryList.add(parentCategories);
											}
										}

										Collections.reverse(categoryList);

										for (Category category2 : categoryList) {
											CatalogCategory catalogCategory = new CatalogCategory();
											catalogCategory.setCatalog(tempCatalog);
											catalogCategory.setCategory(category2);
											catalogCategorySession.insertCatalogCategory(catalogCategory, null);
										}

									} else {
										return "ERROR!, Kategori Tidak Terdaftar!";
									}
								} else {
									return "ERROR!, Kategori Harus Berupa Text!";
								}

							}

							cell = nextRow.getCell(17);
							// if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK &&
							// cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								return "ERROR!, Jumlah Stok Tidak Boleh Kosong!";
							} else {
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									CatalogLocation catalogLocation = new CatalogLocation();
									catalogLocation.setCatalog(tempCatalog);
									List<CatalogLocation> catalogLocationList = catalogLocationSession
											.getCatalogLocationList(catalogLocation);
									if (catalogLocationList != null && catalogLocationList.size() > 0) {
										catalogLocation = catalogLocationList.get(0);
									} else {
										catalogLocation.setIsDelete(0);
									}
									catalogLocation.setStockProduct((Double) (cell.getNumericCellValue()));

									if (catalogLocation.getId() != null) {
										catalogLocationSession.updateCatalogLocation(catalogLocation, tempToken);
									} else {
										catalogLocationSession.insertCatalogLocation(catalogLocation, tempToken);
									}
								} else {
									return "ERROR!, Jumlah Stok Harus Berupa Angka!";
								}
							}

							// catalog attribute
							int cn = 0;
							for (int a = 18; a < lastCellNumber; a++) {
								cell = nextRow.getCell(a);
								CatalogAttribute catalogAttribute = new CatalogAttribute();
								catalogAttribute.setCatalog(tempCatalog);
								if (attributeGroup != null && attributeGroup.getAttributePanelGroupList() != null
										&& attributeGroup.getAttributePanelGroupList().size() > 0) {
									catalogAttribute.setAttributePanelGroup(
											attributeGroup.getAttributePanelGroupList().get(cn));
								}
								cn++;
								List<CatalogAttribute> catalogAttributeList = catalogAttributeSession
										.getCatalogAttributeList(catalogAttribute);
								if (catalogAttributeList != null && catalogAttributeList.size() > 0) {
									catalogAttribute = catalogAttributeList.get(0);
								}
								catalogAttribute.setIsDelete(0);
								if (cell.getCellType() != Cell.CELL_TYPE_BLANK
										&& cell.getCellType() == Cell.CELL_TYPE_NUMERIC
										&& HSSFDateUtil.isCellDateFormatted(cell)) {
									DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
									Date tanggal = cell.getDateCellValue();
									catalogAttribute.setNilai(df.format(tanggal));
								}
								if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK
										&& cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									catalogAttribute.setNilai(Double.toString(cell.getNumericCellValue()));
								}
								if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK
										&& cell.getCellType() == Cell.CELL_TYPE_STRING) {
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
		} // end read sheet

		try {
			// workbook.close(); gw matiin dulu mas bikin error java compiler.
		} catch (Exception e) {
			e.printStackTrace();
		}

		return " Import Katalog Sukses!";
	}

	@Path("/simpanRating")
	@POST
	public Catalog simpanRating(@FormParam("catalogId") Integer catalogId, @HeaderParam("Authorization") String token) {

		Token tempToken = tokenSession.findByToken(token);
		Catalog catalog = catalogSession.find(catalogId);

		Double jumlahRating = new Double(0);
		CatalogComment catalogComment = new CatalogComment();
		catalogComment.setCatalog(catalog);
		List<CatalogComment> catalogCommentList = catalogCommentSession.getCatalogCommentList(catalogComment);
		if (catalogCommentList.size() > 0) {
			for (CatalogComment catCom : catalogCommentList) {
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
		if (catalogAttributeList != null) {
			if (catalogAttributeList.size() > 0) {
				for (CatalogAttribute attribute : catalogAttributeList) {

					CatalogAttribute catalogAttr = new CatalogAttribute();
					catalogAttr.setId(attribute.getId());
					catalogAttr.setNilai(attribute.getNilai());

					/** repalce value nilai if have option list */
					if (attribute.getAttributePanelGroup().getAttribute().getAttributeOptionList().size() > 0) {
						String valueNew = "";
						for (AttributeOption option : attribute.getAttributePanelGroup().getAttribute()
								.getAttributeOptionList()) {
							if (attribute.getNilai() != null && attribute.getNilai().length() > 0) {
								String valueAttrs[] = attribute.getNilai().split(",");
								for (String valueAtt : valueAttrs) {
									if (option.getId().intValue() == new Integer(valueAtt).intValue()) {
										valueNew = valueNew + option.getName() + ",";
									}
								}
							}
						}
						if (valueNew != null && valueNew.length() > 0) {
							catalogAttr.setNilai(valueNew.substring(0, valueNew.lastIndexOf(",")));
						}
					}

					catalogAttr.setAttributePanelGroup(attribute.getAttributePanelGroup());

					if (catalog.getCatalogAttributeList() == null) {
						catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
					}
					catalog.getCatalogAttributeList().add(catalogAttr);
				}
			} else {
				if (catalog.getCatalogAttributeList() == null) {
					catalog.setCatalogAttributeList(new ArrayList<CatalogAttribute>());
				}

				catalog.getCatalogAttributeList().add(new CatalogAttribute());
			}
		}

		return catalogSession.updateCatalog(catalog, tempToken);
	}

	@Path("/simpanCatalogComment")
	@POST
	public CatalogComment simpanCatalogComment(@FormParam("rating") Integer rating,
			@FormParam("ratingComment") String ratingComment, @FormParam("catalogId") Integer catalogId,
			@FormParam("userId") Integer userId, @HeaderParam("Authorization") String token) {
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
	public Response sendingEmail(@FormParam("emailTujuan") String emailTujuan, @FormParam("emailCC") String emailCC,
			@FormParam("emailSubject") String emailSubject, @FormParam("emailIsi") String emailIsi,
			@FormParam("emailFrom") String emailFrom) {

		EmailNotification en = new EmailNotification();
		en.setCreated(new Date());
		en.setEmailTo(emailTujuan);
		en.setSubject(emailSubject);
		en.setMessage(emailIsi);
		en.setStatusEmailSending(0);
		en.setIsDelete(0);
		en.setEmailForm(emailFrom);
		emailNotificationSession.create(en);

		if (emailCC != null) {
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

	@Path("/getTotalPagesAxiqoe")
	@POST
	public Response getTotalPagesAxiqoe() throws ClientProtocolException, IOException, JSONException {
		catalogSession.getTotalPagesAxiqoeFull();
		String berhasil = "Ok";

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("message", berhasil);

		return Response.ok(result).build();
	}

	@Path("/insertCatalogFromAxiqoe")
	@POST
	@TransactionTimeout(value = 120, unit = TimeUnit.MINUTES)
	public Response insertCatalogFromAxiqoeToPromiseFull(@FormParam("startPage") Integer startPage,
			@FormParam("endPage") Integer endPage, @FormParam("startDate") String startDate,
			@FormParam("endDate") String endDate) throws ClientProtocolException, IOException, JSONException {
		catalogSession.insertCatalogFromAxiqoeToPromiseFull(startPage, endPage, startDate, endDate);
		String berhasil = "Ok";

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("message", berhasil);

		return Response.ok(result).build();
	}

	@Path("/insertCatalogFromAxiqoeToday")
	@POST
	@TransactionTimeout(value = 60, unit = TimeUnit.MINUTES)
	public Response insertCatalogFromAxiqoeToPromiseToday() throws ClientProtocolException, IOException, JSONException {
		int startPages = 1;
		int totalPages = catalogSession.getTotalPagesAxiqoeToday();
		catalogSession.insertCatalogFromAxiqoeToPromiseToday(startPages, totalPages);
		String berhasil = "Ok";

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("message", berhasil);

		return Response.ok(result).build();
	}

	@Path("/insertCatalogFromSeva")
	@POST
	@TransactionTimeout(value = 20, unit = TimeUnit.MINUTES)
	public Response insertCatalogFromSevaToPromise() throws ClientProtocolException, IOException, JSONException {
		catalogSession.insertCatalogFromSevaToPromise();
		String berhasil = "Ok";

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("message", berhasil);

		return Response.ok(result).build();
	}
	
	@Path("/get-all-list-catalog-contract")
	@POST
	public Response getAllListCatalogKontrak(@HeaderParam("Authorization") String token) {
		try {
			List<CatalogKontrak> catalogkontrakList=catalogKontrakSession.getAllCatalogContractByVendor(token);
			return Response.ok(catalogkontrakList).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Path("/getRole")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getRole(@HeaderParam("Authorization") String token) {
		Token tokenObj = tokenSession.findByToken(token);
		RoleUser roleUsers = roleUserSession.getByToken(tokenObj);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("role", roleUsers.getRole().getCode());

		return Response.ok(map).build();
	}
	
	@Path("/getOrganisasiLogin")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getOrganisasiLogin(@HeaderParam("Authorization") String token) {
		Token tokenObj = tokenSession.findByToken(token);
		User User = userSession.getByToken(tokenObj);
		RoleUser roleUser = roleUserSession.getByUserId(User.getId());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("organisasi", roleUser.getOrganisasi().getNama());

		return Response.ok(map).build();
	}
	
	@Path("/get-catalog-by-id")
	@POST
	public Response getCatalogById(Integer id,@HeaderParam("Authorization") String token) {
		try {
			Catalog catalog=catalogSession.getCatalog(id);
			return Response.ok(catalog).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Path("/get-catalog-history-by-catalog")
	@POST
	public CatalogHistory getCatalogHistoryByCatalog(Catalog catalogFront,@HeaderParam("Authorization") String token) {
		try {
			CatalogHistory catalogHistory=catalogHistorySession.getCatalogHistoryByCatalaog(catalogFront);
			Boolean isChange = false;
			List<Integer> IdcategoryInHistory = new ArrayList<Integer>();
			List<Integer> Idcategory = new ArrayList<Integer>();
			List<Category> categoryList = categorySession.getCategoryForTreeList(null);
			if (catalogFront != null && catalogFront.getId() != null) {
				CatalogCategory tempCatalogCategory = new CatalogCategory();
				tempCatalogCategory.setCatalog(catalogFront);
				List<CatalogCategoryHistory> catalogCategoryHistory = catalogCategoryHistorySession.getCatalogCategoryHistoryByCatalogId(catalogFront);
				for(CatalogCategoryHistory cch : catalogCategoryHistory) {
					for (Category category : categoryList) {
						if (cch.getCategory().getId().equals(category.getId())) {
							category.setValue(true);
							IdcategoryInHistory.add(category.getId());
						}
					}					
				}
				List<CatalogCategory> catalogCategoryList = catalogCategorySession.getCatalogCategoryList(tempCatalogCategory);
				if (catalogCategoryList != null && catalogCategoryList.size() > 0) {
					for (CatalogCategory catalogCategory : catalogCategoryList) {
						for (Category category : categoryList) {
							if (catalogCategory.getCategory().getId().equals(category.getId()) ) {
								Idcategory.add(category.getId());
							}
						}
						
					}
				}
			}
			
			if(Idcategory.size() == IdcategoryInHistory.size()) {
				for(int a = 0; a < IdcategoryInHistory.size(); a++) {
					if(IdcategoryInHistory.get(a).equals(Idcategory.get(a))) {
						isChange = false;
					}else {
						isChange = true;
						break;
					}
				}
			}
			catalogHistory.setIsChange(isChange);
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
			catalogHistory.setCategoryList(categoryTreeList);
			
			List<CatalogImageHistory> catalogImageHistory = catalogImageHistorySession.getCatalogImageHistoryByCatalog(catalogFront);
			catalogHistory.setCatalogImageHistoryList(catalogImageHistory);
			
			List<AttributeGroup> attributeGroupList = attributeGroupSession.getAttributeGroupList();
			if (catalogFront != null && catalogFront.getId() != null) {
				List<CatalogAttributeHistory> catalogAttributeHistoryList = catalogAttributeHistorySession.getCatalogAttributeHistoryByCatalog(catalogFront);
				if (catalogAttributeHistoryList != null && catalogAttributeHistoryList.size() > 0) {
					for (CatalogAttributeHistory catalogAttributeHistory : catalogAttributeHistoryList) {
						if (attributeGroupList != null && attributeGroupList.size() > 0) {
							for (AttributeGroup attributeGroup : attributeGroupList) {
								Boolean nextState = false;
								if (attributeGroup.getAttributePanelGroupList() != null
										&& attributeGroup.getAttributePanelGroupList().size() > 0) {
									for (AttributePanelGroup attributePanelGroup : attributeGroup
											.getAttributePanelGroupList()) {
										if (attributePanelGroup.getId().equals(catalogAttributeHistory.getAttributePanelGroup()
												.getId()) ) {
											if (attributePanelGroup.getAttribute() != null
													&& attributePanelGroup.getAttribute().getInputType() != null
													&& attributePanelGroup.getAttribute().getInputType().getName()
															.equals("Checkbox") == false) {
												attributePanelGroup.setValue(catalogAttributeHistory.getNilai());
											} else {
												String strValue = catalogAttributeHistory.getNilai();
												if (strValue != null) {
													String[] splitValue = strValue.split(",");
													if (attributePanelGroup.getAttribute().getAttributeOptionList() != null
															&& attributePanelGroup.getAttribute().getAttributeOptionList()
																	.size() > 0) {
														for (AttributeOption attributeOption : attributePanelGroup
																.getAttribute().getAttributeOptionList()) {
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
			
			if (catalogHistory.getAttributeGroup() != null) {
				for(AttributeGroup attGroup : attributeGroupList ) {
					if (attGroup.getId().equals(catalogHistory.getAttributeGroup().getId())) {
						catalogHistory.setAttributeGroup(attGroup);
					}
				}
			}
			
			return catalogHistory;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Path("/update-catalog-after-approve")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Catalog updateCatalogAfterApprove(CatalogHistory catalogHistory, @HeaderParam("Authorization") String token) {
		if(catalogHistory != null) {
			Token tempToken=tokenSession.findByToken(token);
			Catalog catalogOld=catalogSession.getCatalog(catalogHistory.getCatalog().getId());
			
			String revision=autoNumberSession.generateNumber("REVISION", tempToken);
			if(catalogHistory.getIsDelete() != 1) {				
				if(catalogHistory.getTodo().equalsIgnoreCase("add")) {
					catalogOld.setIsApproval(Constant.ZERO_VALUE);
					catalogHistory.setIsDelete(Constant.ONE_VALUE);
				}else {
					try {
						catalogOld.setNamaIND(catalogHistory.getNamaIND());
						catalogOld.setItem(catalogHistory.getItem());
						catalogOld.setSatuan(catalogHistory.getSatuan());
						catalogOld.setMataUang(catalogHistory.getMataUang());
						catalogOld.setBerat(catalogHistory.getBerat());
						catalogOld.setCurrentStock(catalogHistory.getCurrentStock());
						catalogOld.setDeskripsiIND(catalogHistory.getDeskripsiIND());
						catalogOld.setHarga(catalogHistory.getHarga());
						catalogOld.setSatuanBerat(catalogHistory.getSatuanBerat());
						catalogOld.setSatuanLebar(catalogHistory.getSatuanLebar());
						catalogOld.setSatuanPanjang(catalogHistory.getSatuanPanjang());
						catalogOld.setSatuanTinggi(catalogHistory.getSatuanTinggi());
						catalogOld.setCatalogKontrak(catalogHistory.getCatalogKontrak());
						catalogOld.setKodeItem(catalogHistory.getKodeItem());
						catalogOld.setItemType(catalogHistory.getItemType());
						catalogOld.setMinimalQuantity(catalogHistory.getMinimalQuantity());
						catalogOld.setProductType(catalogHistory.getProductType());
						catalogOld.setKodeProduk(catalogHistory.getKodeProduk());
						catalogOld.setAttributeGroup(catalogHistory.getAttributeGroup());
						catalogOld.setPanjang(catalogHistory.getPanjang());
						catalogOld.setLebar(catalogHistory.getLebar());
						catalogOld.setTinggi(catalogHistory.getTinggi());
						catalogOld.setVendor(catalogOld.getVendor());
						catalogOld.setMinimumQuantityOrder(catalogHistory.getMinimumQuantityOrder());
						catalogOld.setMaksimumQuantityOrderDay(catalogHistory.getMaksimumQuantityOrderDay());
						catalogOld.setMaksimumQuantityPerOrder(catalogHistory.getMaksimumQuantityPerOrder());
						//catalogOld.setCatalogCategoryList(catalogHistory.getCatalogCategoryList());
						catalogOld.setStatus(true);
						catalogOld.setIsApproval(Constant.ZERO_VALUE);
						catalogOld.setIsDelete(Constant.ZERO_VALUE);
						catalogHistory.setRevisionNumber(revision);
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else {
				catalogHistory.setRevisionNumber(revision);
				catalogOld.setStatus(true);
				catalogOld.setIsApproval(Constant.ZERO_VALUE);
				catalogOld.setIsDelete(Constant.ZERO_VALUE);
			}
			/* perubahan KAI 17/12/2020 [21100]*/
			catalogOld.setCatalogCategoryList(catalogHistory.getCatalogCategoryList());
			catalogOld.setCatalogFeeList(catalogHistory.getCatalogFeeList());
			catalogOld.setCatalogBulkPriceList(catalogHistory.getCatalogBulkPriceList());
			catalogOld.setCatalogStockList(catalogHistory.getCatalogStockList());
			List<CatalogImage> catalogImageList = new ArrayList<CatalogImage>();
			List<CatalogFee> catalogFeeList = new ArrayList<CatalogFee>();
			List<CatalogBulkPrice> catalogBulkPriceList = new ArrayList<CatalogBulkPrice>();
			for(CatalogImageHistory catImageHistory : catalogHistory.getCatalogImageHistoryList()) {
				CatalogImage catImage = new CatalogImage();
				catImage.setCatalog(catalogOld);
				if(catImageHistory.getCatalogImage() != null) {
					catImage.setId(catImageHistory.getCatalogImage().getId());					
				}
				catImage.setImagesFileName(catImageHistory.getImagesFileName());
				catImage.setImagesFileSize(catImageHistory.getImagesFileSize());
				catImage.setImagesRealName(catImageHistory.getImagesRealName());
				catImage.setIsDelete(catImageHistory.getIsDelete());
				
				catImage.setStatus(catImageHistory.getStatus());
				catImage.setUrutanPesanan(catImageHistory.getUrutanPesanan());
				catalogImageList.add(catImage);
			}
			catalogOld.setCatalogImageList(catalogImageList);
			
			for(CatalogBulkPriceHistory catBulkPriceHistory : catalogBulkPriceHistorySession.getCatalogBulkPriceHistoryByCatalogId(catalogOld)) {
				CatalogBulkPrice catBulkPrice = new CatalogBulkPrice();
				catBulkPrice.setCatalog(catBulkPriceHistory.getCatalog());
				catBulkPrice.setDiskon(catBulkPriceHistory.getDiskon());
				if(catBulkPriceHistory.getCatalogBulkPrice() != null) {
					catBulkPrice.setId(catBulkPriceHistory.getCatalogBulkPrice().getId());
				}
				catBulkPrice.setIsDelete(catBulkPriceHistory.getIsDelete());
				
				catBulkPrice.setMaxQuantity(catBulkPriceHistory.getMaxQuantity());
				catBulkPrice.setMinQuantity(catBulkPriceHistory.getMinQuantity());
				catalogBulkPriceList.add(catBulkPrice);
			}
			catalogOld.setCatalogBulkPriceList(catalogBulkPriceList);
			
			for(CatalogFeeHistory catFeeHistory : catalogFeeHistorySession.getCatalogFeeHistoryByCatalogId(catalogOld)) {
				CatalogFee catFee = new CatalogFee();
				catFee.setAsuransi(Constant.ASURANSI);
				catFee.setCatalog(catFeeHistory.getCatalog());
				catFee.setIsDelete(catFeeHistory.getIsDelete());
				
				if(catFeeHistory.getCatalogFee() != null) {
					catFee.setId(catFeeHistory.getCatalogFee().getId());
				}
				catFee.setOngkosKirim(catFeeHistory.getOngkosKirim());
				catFee.setOrganisasi(catFeeHistory.getOrganisasi());
				catFee.setQuantityBatch(catFeeHistory.getQuantityBatch());
				catFee.setSlaDeliveryTime(catFeeHistory.getSlaDeliveryTime());
				catalogFeeList.add(catFee);
			}
			catalogOld.setCatalogFeeList(catalogFeeList);
			
			catalogOld.setCategoryList(catalogHistory.getCategoryList());
			catalogOld.setAttributeGroup(catalogHistory.getAttributeGroup());
			catalogOld.setCatalogAttributeList(catalogHistory.getCatalogAttributeList());
			catalogOld.setTodo("update after approve");
			/*perubahan KAI 01/18/2021*/
			Catalog tempCatalog = simpanCatalogWithItem(catalogOld, tempToken, "approval");
			if(!catalogHistory.getIsDelete().equals(Constant.ONE_VALUE)) {
				simpanCatalogCategory(tempCatalog, catalogOld, tempToken);
				simpanCatalogImage(tempCatalog, catalogOld, tempToken);
				simpanCatalogAttribute(tempCatalog, catalogOld, tempToken);
				/* perubahan KAI 16/12/2020 [20852]*/
				simpanCatalogBulkPrice(tempCatalog, catalogOld, tempToken);
				simpanCatalogFee(tempCatalog, catalogOld, tempToken);				
			}
			
			for(CatalogImageHistory catImgHistory : catalogImageHistorySession.getCatalogImageHistoryByCatalog(catalogHistory.getCatalog())) {
				catalogImageHistorySession.deleteCatalogImageHistory(catImgHistory.getId(), tempToken);
			}
			
			for(CatalogAttributeHistory catAttrHistory : catalogAttributeHistorySession.getCatalogAttributeHistoryByCatalog(catalogHistory.getCatalog())) {
				catalogAttributeHistorySession.deleteCatalogAttributeHistory(catAttrHistory.getId(), tempToken);
			}
			
			for(CatalogCategoryHistory catCategoryHistory : catalogCategoryHistorySession.getCatalogCategoryHistoryByCatalogId(catalogHistory.getCatalog())) {
				catalogCategoryHistorySession.deleteCatalogCategoryHistory(catCategoryHistory.getId(), tempToken);
			}
			/* perubahan KAI 17/12/2020 [21100]*/
			for(CatalogFeeHistory catFeeHistory : catalogFeeHistorySession.getCatalogFeeHistoryByCatalogId(catalogHistory.getCatalog())) {
				catalogFeeHistorySession.deleteCatalogFeeHistory(catFeeHistory.getId(), tempToken);
			}
			
			for(CatalogBulkPriceHistory catBulkPriceHistory : catalogBulkPriceHistorySession.getCatalogBulkPriceHistoryByCatalogId(catalogHistory.getCatalog())) {
				catalogBulkPriceHistorySession.deleteCatalogBulkPriceHistory(catBulkPriceHistory.getId(), tempToken);
			}
			
			catalogHistory.setIsDelete(Constant.ONE_VALUE);
			catalogSession.updateCatalog(catalogOld, tempToken);
			catalogHistorySession.updateCatalogHistory(catalogHistory, tempToken);
			return catalogOld;
		}else {
			return null;
		}
	}
	
	@Path("/approve-new-catalog")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Catalog approveNewCatalog(Catalog catalog, @HeaderParam("Authorization") String token) {
		if(catalog != null) {
			try {				
				Token tempToken=tokenSession.findByToken(token);
				if(catalog.getIsDelete()!=1) {
					catalog.setIsApproval(Constant.ZERO_VALUE);
					catalog.setStatus(true);
				}else {
					catalog.setStatus(false);
					catalog.setIsApproval(Constant.ZERO_VALUE);
					catalog.setIsDelete(Constant.ZERO_VALUE);
				}
				catalogSession.updateCatalog(catalog, tempToken);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return catalog;
	}
	@Path("/get-catalog-contract-detail-by-item")
	@POST
	public Response getCatalogContractDetailByItem(Item item,@HeaderParam("Authorization") String token) {
		try {
			CatalogContractDetail catalogcontractDetail = catalogContractDetailSession.getCatalogContractDetailByItem(item);
			return Response.ok(catalogcontractDetail).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Path("/check-existing-catalog")
	@POST
	public Boolean checkExistingCatalog(Catalog catalog,@HeaderParam("Authorization") String token) {
		Boolean isExist = false;
		try {
			List<Catalog> catalogList = new ArrayList<Catalog>();
			Integer idFront;
			Integer idBack;
			catalogList = catalogSession.getCatalogExisting(catalog.getCatalogKontrak(), catalog.getItem());				
			if(catalog.getId() != null) {
				idFront = catalog.getId();
				for(Catalog catalogTmp : catalogList) {
					idBack = catalogTmp.getId();
					if(catalogList.size() > 1) {
						isExist = true;
						break;
					}else {
						if(idBack.equals(idFront)) {
							isExist = false;
						}else {
							isExist = true;			
						}
					}
				}
			}else {
				if(catalogList.size() > 0) {
					isExist = true;
				}else {
					isExist = false;
				}
			}
			return isExist;
		} catch (Exception e) {
			e.printStackTrace();
			return isExist;
		}
		
	}
	
	@Path("/findCatalogPropertiesApproval")
	@POST
	public Response findCatalogPropertiesApproval(Catalog catalog) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("itemTypeList", itemTypeSession.getItemTypeList());
		map.put("mataUangList", mataUangSession.getMataUanglist());
		map.put("satuanList", satuanSession.getSatuanList());
		map.put("productTypeList", productTypeSession.getProductTypeList());
//		map.put("vendorList", vendorSession.getVendorList());
		/* perubahan KAI 14/12/2020 [20852]*/
		List<AttributeGroup> attributeGroupList = attributeGroupSession.getAttributeGroupList();
		if (catalog != null && catalog.getId() != null) {
			CatalogAttribute tempCatalogAttribute = new CatalogAttribute();
			tempCatalogAttribute.setCatalog(catalog);
			List<CatalogAttribute> catalogAttributeList = catalogAttributeSession
					.getCatalogAttributeList(tempCatalogAttribute);
			if (catalogAttributeList != null && catalogAttributeList.size() > 0) {
				for (CatalogAttribute catalogAttribute : catalogAttributeList) {
					if (attributeGroupList != null && attributeGroupList.size() > 0) {
						for (AttributeGroup attributeGroup : attributeGroupList) {
							Boolean nextState = false;
							if (attributeGroup.getAttributePanelGroupList() != null
									&& attributeGroup.getAttributePanelGroupList().size() > 0) {
								for (AttributePanelGroup attributePanelGroup : attributeGroup
										.getAttributePanelGroupList()) {
									if (attributePanelGroup.getId().equals(catalogAttribute.getAttributePanelGroup()
											.getId()) ) {
										if (attributePanelGroup.getAttribute() != null
												&& attributePanelGroup.getAttribute().getInputType() != null
												&& attributePanelGroup.getAttribute().getInputType().getName()
														.equals("Checkbox") == false) {
											attributePanelGroup.setValue(catalogAttribute.getNilai());
										} else {
											String strValue = catalogAttribute.getNilai();
											if (strValue != null) {
												String[] splitValue = strValue.split(",");
												if (attributePanelGroup.getAttribute().getAttributeOptionList() != null
														&& attributePanelGroup.getAttribute().getAttributeOptionList()
																.size() > 0) {
													for (AttributeOption attributeOption : attributePanelGroup
															.getAttribute().getAttributeOptionList()) {
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
			List<CatalogCategory> catalogCategoryList = catalogCategorySession
					.getCatalogCategoryList(tempCatalogCategory);
			if (catalogCategoryList != null && catalogCategoryList.size() > 0) {
				for (CatalogCategory catalogCategory : catalogCategoryList) {
					if (categoryList != null && categoryList.size() > 0) {
						for (Category category : categoryList) {
							if (catalogCategory.getCategory().getId().equals(category.getId()) ) {
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

		if (catalog != null && catalog.getId() != null) {
			CatalogLocation catalogLocation = new CatalogLocation();
			catalogLocation.setCatalog(catalog);
			map.put("catalogLocationList", catalogLocationSession.getCatalogLocationList(catalogLocation));

			CatalogImage catalogImage = new CatalogImage();
			catalogImage.setCatalog(catalog);
			map.put("catalogImageList", catalogImageSession.getCatalogImageList(catalogImage));
			/* perubahan KAI 17/12/2020 [21100]*/
			CatalogBulkPrice cataBulkPrice = new CatalogBulkPrice();
			cataBulkPrice.setCatalog(catalog);
			List<CatalogBulkPrice> catalogBulkPriceList = catalogBulkPriceSession.getCatalogBulkPriceList(cataBulkPrice);
			List<CatalogBulkPriceHistory> catalogBulkPriceHistoryList = catalogBulkPriceHistorySession.getCatalogBulkPriceHistoryByCatalogId(catalog);
			List<CatalogBulkPrice> catalogBulkPriceListNew = new ArrayList<CatalogBulkPrice>();
			for(CatalogBulkPriceHistory catBulkPriceHistory : catalogBulkPriceHistoryList) {
				Boolean isSame = true;
				if (catalogBulkPriceList.size() > 0) {
					for(CatalogBulkPrice catBulkPrice : catalogBulkPriceList) {
						if(isSame) {
							if(catBulkPriceHistory.getCatalogBulkPrice() != null) {
								if(catBulkPriceHistory.getCatalogBulkPrice().getId().equals(catBulkPrice.getId())) {
									catBulkPrice.setCatalogBulkPriceHistory(catBulkPriceHistory);
									catalogBulkPriceListNew.add(catBulkPrice);
									isSame = true;
								}
							}else {
								CatalogBulkPrice newCatBulkPrice = new CatalogBulkPrice();
								newCatBulkPrice.setCatalogBulkPriceHistory(catBulkPriceHistory);
								catalogBulkPriceListNew.add(newCatBulkPrice);
								isSame = false;
							}						
						}
					}
				}else {
					CatalogBulkPrice newCatBulkPriceTamp = new CatalogBulkPrice();
					newCatBulkPriceTamp.setCatalogBulkPriceHistory(catBulkPriceHistory);
					catalogBulkPriceListNew.add(newCatBulkPriceTamp);
					isSame = false;
				}
				
			}
			if(catalogBulkPriceHistoryList.size() == 0) {
				catalogBulkPriceListNew = catalogBulkPriceList;
			}
			map.put("catalogBulkPriceList", catalogBulkPriceListNew);

			CatalogFee cataFee = new CatalogFee();
			cataFee.setCatalog(catalog);
			List<CatalogFee> catalogFeeList = catalogFeeSession.getCatalogFeeList(cataFee);
			List<CatalogFeeHistory> catalogFeeHistoryList = catalogFeeHistorySession.getCatalogFeeHistoryByCatalogId(catalog);
			List<CatalogFee> catalogFeeListNew = new ArrayList<CatalogFee>();;
			for(CatalogFeeHistory catFeeHistory : catalogFeeHistoryList) {
				Boolean isSame = true;
				for(CatalogFee catFee : catalogFeeList) {
					if(isSame) {
						if(catFeeHistory.getCatalogFee() != null) {
							if(catFeeHistory.getCatalogFee().getId().equals(catFee.getId())) {
								catFee.setCatalogFeeHistory(catFeeHistory);
								catalogFeeListNew.add(catFee);
								isSame = true;
							}
						}else {
							CatalogFee newCatFee = new CatalogFee();
							newCatFee.setCatalogFeeHistory(catFeeHistory);
							catalogFeeListNew.add(newCatFee);
							isSame = false;
						}						
					}
				}
				
			}
			if(catalogFeeHistoryList.size() == 0) {
				catalogFeeListNew = catalogFeeList;
			}
			map.put("catalogFeeList", catalogFeeListNew);

			CatalogStock cataStock = new CatalogStock();
			cataStock.setCatalog(catalog);
			map.put("catalogStockList", catalogStockSession.getCatalogStockListByCatalog(catalog));

			CatalogPriceRange catalogPriceRange = new CatalogPriceRange();
			catalogPriceRange.setCatalog(catalog);
			map.put("catalogPriceRangeList", catalogPriceRangeSession.getCatalogPriceRangeList(catalogPriceRange));
		}

		return Response.ok(map).build();
	}
	@Path("/getApprovalProcessTypeByCatalog/{catalogId}")
	@GET
	public Response getApprovalProcessTypeByCatalog(@PathParam("catalogId")Integer catalogId) {
		try {
			List<ApprovalProcessType> appProcessTypeList = approvalProcessTypeSession.getListByCatalogId(catalogId);
			List<ApprovalProcess> appProcessList = new ArrayList<ApprovalProcess>();
			for(ApprovalProcessType apt : appProcessTypeList) {
				ApprovalProcess approvalProcess = 
						approvalProcessSession.findByApprovalProcessType(apt.getId()).size() > 0?approvalProcessSession.findByApprovalProcessType(apt.getId()).get(0):null;
				
				appProcessList.add(approvalProcess);
			}
			return Response.status(201).entity(appProcessList).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}
	
	
}
