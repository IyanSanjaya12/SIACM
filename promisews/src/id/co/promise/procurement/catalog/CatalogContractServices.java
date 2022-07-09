package id.co.promise.procurement.catalog;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import id.co.promise.procurement.approval.ApprovalProcessSession;
import id.co.promise.procurement.catalog.entity.CatalogKontrak;
import id.co.promise.procurement.catalog.session.AttributeGroupSession;
import id.co.promise.procurement.catalog.session.AttributeOptionSession;
import id.co.promise.procurement.catalog.session.AttributeSession;
import id.co.promise.procurement.catalog.session.CatalogAttributeSession;
import id.co.promise.procurement.catalog.session.CatalogBulkPriceSession;
import id.co.promise.procurement.catalog.session.CatalogCategorySession;
import id.co.promise.procurement.catalog.session.CatalogCommentSession;
import id.co.promise.procurement.catalog.session.CatalogContractDetailSession;
import id.co.promise.procurement.catalog.session.CatalogFeeSession;
import id.co.promise.procurement.catalog.session.CatalogHistorySession;
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
import id.co.promise.procurement.master.AutoNumberSession;
import id.co.promise.procurement.master.ItemOrganisasiSession;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.ItemTypeSession;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.ProductTypeSession;
import id.co.promise.procurement.master.SatuanSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.vendor.VendorProfileSession;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@Path(value = "/procurement/catalog/catalog-contract-service")
@Produces(MediaType.APPLICATION_JSON)
public class CatalogContractServices {

	@EJB
	private TokenSession tokenSession;

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
	

	
	@Path("/get-all-list-catalog-contract")
	@GET
	public Response getAllListCatalogKontrak(@HeaderParam("Authorization") String token) {
		try {
			List<CatalogKontrak> catalogkontrakList=catalogKontrakSession.getAllCatalogContractByVendor(token);
			return Response.ok(catalogkontrakList).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Path("/get-by-number/{number}")
	@GET
	public Response getbyNumber(@HeaderParam("Authorization") String token, @PathParam("number") String number) {
		try {
			List<CatalogKontrak> catalogkontrakList=catalogKontrakSession.getAllCatalogContractByVendorAndNumber(token, number);
			return Response.ok(catalogkontrakList).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Path("/get-by-id")
	@POST
	public CatalogKontrak getByContractNo(Integer catalogContractId,
			@HeaderParam("Authorization") String tokenStr) throws SQLException, Exception {
		try {
			CatalogKontrak catalogkontrak=catalogKontrakSession.find(catalogContractId);
			return catalogkontrak;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
}
