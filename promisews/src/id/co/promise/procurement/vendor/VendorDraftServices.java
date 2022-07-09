package id.co.promise.procurement.vendor;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.approval.ApprovalLevelSession;
import id.co.promise.procurement.approval.ApprovalProcessSession;
import id.co.promise.procurement.approval.ApprovalProcessTypeSession;
import id.co.promise.procurement.approval.ApprovalSession;
import id.co.promise.procurement.approval.ApprovalTahapanSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.VendorDraft;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.master.BidangUsahaSession;
import id.co.promise.procurement.master.BuktiKepemilikanSession;
import id.co.promise.procurement.master.HariLiburSession;
import id.co.promise.procurement.master.JabatanSession;
import id.co.promise.procurement.master.KondisiPeralatanVendorSession;
import id.co.promise.procurement.master.KualifikasiVendorSession;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.ParameterSession;
import id.co.promise.procurement.master.WilayahSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;

@Stateless
@Path(value = "/procurement/vendor/vendorDraftServices")
@Produces(MediaType.APPLICATION_JSON)
public class VendorDraftServices {
	final static CustomResponseMessage message = CustomResponse
			.getCustomResponseMessage();

	@EJB
	private VendorDraftSession vendorDraftSession;
	@EJB
	private DokumenRegistrasiVendorSession dokumenRegistrasiVendorSession;
	
	@EJB
	private TokenSession tokenSession;
	
	@EJB
	private VendorApprovalSession vendorApprovalSession;
	
	@EJB
	private SertifikatVendorSession sertifikatVendorSession;
	
	@EJB
	private SertifikatVendorRiwayatSession sertifikatVendorRiwayatSession;
	
	@EJB
	private ParameterSession parameterSession;
	
	@EJB
	private UserSession userSession;	
	
	@EJB
	private RoleUserSession roleUserSession;
	
	@EJB
	private OrganisasiSession organisasiSession;

	@EJB
	private ApprovalTahapanSession approvalTahapanSession;
	
	@EJB
	private ApprovalProcessTypeSession approvalProcessTypeSession;
	
	@EJB
	private ApprovalSession approvalSession;
	
	@EJB
	private ApprovalLevelSession approvalLevelSession;
	
	@EJB
	private ApprovalProcessSession approvalProcessSession;
	
	@EJB
	private HariLiburSession hariLiburSession;
	
	@EJB
	private EmailNotificationSession emailNotificationSession;
	
	@EJB
	private VendorProfileSession vendorProfileSession;
	
	@EJB
	private VendorSKDSession vendorSkdSession;
	
	@EJB
	private BidangUsahaSession bidangUsahaSession;
	
	@EJB
	private MataUangSession mataUangSession;
	
	@EJB
	private JabatanSession jabatanSession;
	
	@EJB
	private KualifikasiVendorSession kualifikasiVendorSession;
	
	@EJB
	private WilayahSession wilayahSession;
	
	@EJB
	private BankVendorSession bankVendorSession;
	
	@EJB
	private SegmentasiVendorSession segmentasiVendorSession;
	
	@EJB
	private PeralatanVendorSession peralatanVendorSession;
	
	@EJB
	private KeuanganVendorSession keuanganVendorSession;
	
	@EJB
	private PengalamanVendorSession pengalamanVendorSession;
	
	@EJB
	private VendorPICSession vendorPICSession;
	
	@EJB
	KondisiPeralatanVendorSession kondisiSession;
	
	@EJB
	BuktiKepemilikanSession buktiKepemilikanSession;
	
	
	//update modul data perusahaan vendor
	@Path("/updateVendor")
	@POST
	public VendorDraft updateVendor(VendorDraft vendorDraft, String token) {
		//Vendor vendor = vendorSession.find(vendor.getId());

		if (vendorDraft.getNama() != null && vendorDraft.getNama().length() > 0) {
			vendorDraft.setNama(vendorDraft.getNama());
		}

		if (vendorDraft.getAlamat() != null && vendorDraft.getAlamat().length() > 0) {
			vendorDraft.setAlamat(vendorDraft.getAlamat());
		}

		if (vendorDraft.getNomorTelpon() != null && vendorDraft.getNomorTelpon().length() > 0) {
			vendorDraft.setNomorTelpon(vendorDraft.getNomorTelpon());
		}

		if (vendorDraft.getEmail() != null && vendorDraft.getEmail().length() > 0) {
			vendorDraft.setEmail(vendorDraft.getEmail());
		}

		if (vendorDraft.getNpwp() != null && vendorDraft.getNpwp().length() > 0) {
			vendorDraft.setNpwp(vendorDraft.getNpwp());
		}

		if (vendorDraft.getPenanggungJawab() != null && vendorDraft.getPenanggungJawab().length() > 0) {
			vendorDraft.setPenanggungJawab(vendorDraft.getPenanggungJawab());
		}

		if (vendorDraft.getUser() > 0) {
			vendorDraft.setUser(vendorDraft.getUser());
		}

		if (vendorDraft.getLogoImage() != null) {
			vendorDraft.setLogoImage(vendorDraft.getLogoImage());
		}

		if (vendorDraft.getLogoImageSize() != null) {
			vendorDraft.setLogoImageSize(vendorDraft.getLogoImageSize());
		}

		if (vendorDraft.getBackgroundImage() != null) {
			vendorDraft.setBackgroundImage(vendorDraft.getBackgroundImage());
		}
		
		if (vendorDraft.getBackgroundImageSize() != null) {
			vendorDraft.setBackgroundImageSize(vendorDraft.getBackgroundImageSize());
		}
		
		if (vendorDraft.getDeskripsi() != null) {
			vendorDraft.setDeskripsi(vendorDraft.getDeskripsi());
		}
		
		if (vendorDraft.getKota() != null && vendorDraft.getKota().length() > 0) {
			vendorDraft.setKota(vendorDraft.getKota());
		}
		
		if (vendorDraft.getProvinsi() != null && vendorDraft.getProvinsi().length() > 0) {
			vendorDraft.setProvinsi(vendorDraft.getProvinsi());
		}
		
		vendorDraftSession.updateVendorDraft(vendorDraft, tokenSession.findByToken(token));

		return vendorDraft;
	}

	
		@Path("/insertVendor")
		@POST
		public VendorDraft insertVendor(VendorDraft vendorDraft, String token) {
			//Vendor vendor = vendorSession.find(vendor.getId());

			if (vendorDraft.getNama() != null && vendorDraft.getNama().length() > 0) {
				vendorDraft.setNama(vendorDraft.getNama());
			}

			if (vendorDraft.getAlamat() != null && vendorDraft.getAlamat().length() > 0) {
				vendorDraft.setAlamat(vendorDraft.getAlamat());
			}

			if (vendorDraft.getNomorTelpon() != null && vendorDraft.getNomorTelpon().length() > 0) {
				vendorDraft.setNomorTelpon(vendorDraft.getNomorTelpon());
			}

			if (vendorDraft.getEmail() != null && vendorDraft.getEmail().length() > 0) {
				vendorDraft.setEmail(vendorDraft.getEmail());
			}

			if (vendorDraft.getNpwp() != null && vendorDraft.getNpwp().length() > 0) {
				vendorDraft.setNpwp(vendorDraft.getNpwp());
			}

			if (vendorDraft.getPenanggungJawab() != null && vendorDraft.getPenanggungJawab().length() > 0) {
				vendorDraft.setPenanggungJawab(vendorDraft.getPenanggungJawab());
			}

			if (vendorDraft.getUser() > 0) {
				vendorDraft.setUser(vendorDraft.getUser());
			}

			if (vendorDraft.getLogoImage() != null) {
				vendorDraft.setLogoImage(vendorDraft.getLogoImage());
			}

			if (vendorDraft.getLogoImageSize() != null) {
				vendorDraft.setLogoImageSize(vendorDraft.getLogoImageSize());
			}

			if (vendorDraft.getBackgroundImage() != null) {
				vendorDraft.setBackgroundImage(vendorDraft.getBackgroundImage());
			}
			
			if (vendorDraft.getBackgroundImageSize() != null) {
				vendorDraft.setBackgroundImageSize(vendorDraft.getBackgroundImageSize());
			}
			
			if (vendorDraft.getDeskripsi() != null) {
				vendorDraft.setDeskripsi(vendorDraft.getDeskripsi());
			}
			
			if (vendorDraft.getKota() != null && vendorDraft.getKota().length() > 0) {
				vendorDraft.setKota(vendorDraft.getKota());
			}
			
			if (vendorDraft.getProvinsi() != null && vendorDraft.getProvinsi().length() > 0) {
				vendorDraft.setProvinsi(vendorDraft.getProvinsi());
			}
			
			vendorDraftSession.insertVendorDraft(vendorDraft, tokenSession.findByToken(token));

			return vendorDraft;
		}


	@Path("/delete/{vendorId}")
	@GET
	public VendorDraft delete(@PathParam("vendorId")int vendorId, 
			@HeaderParam("Authorization") String token
			){
		return vendorDraftSession.deleteVendor(vendorId, tokenSession.findByToken(token));
	}	
	
	
			
}
