package id.co.promise.procurement.vendor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.entity.VendorApproval;
import id.co.promise.procurement.security.TokenSession;


@Stateless
@Path(value = "/procurement/vendor/VendorApprovalServices")
@Produces(MediaType.APPLICATION_JSON) 
public class VendorApprovalServices {

	@EJB
	private VendorApprovalSession vendorApprovalSession;

	@EJB
	private VendorSession vendorSession;
	@EJB private TokenSession tokenSession;
	
	private DateFormat formatIndonesia = new SimpleDateFormat("dd-MM-yyyy");

	@Path("/insert")
	@POST
	public VendorApproval insert(@FormParam("nomor") String nomor,
			@FormParam("vendor") Integer vendor,
			@FormParam("tanggal") String tanggal,
			@FormParam("keterangan") String keterangan,
			@HeaderParam("Authorization") String token) throws ParseException {

		VendorApproval vendorApproval = new VendorApproval();
		vendorApproval.setUserId(0);
		vendorApproval.setNomor(nomor);
		vendorApproval.setVendor(vendorSession.find(vendor));
		vendorApproval.setTanggal(formatIndonesia.parse(tanggal));
		vendorApproval.setKeterangan(keterangan);
		
		vendorApprovalSession.insertVendorApproval(vendorApproval, tokenSession.findByToken(token));
		return vendorApproval;
	}

	@Path("/update")
	@POST
	public VendorApproval update(@FormParam("id") int id,
			@FormParam("nomor") String nomor,
			@FormParam("vendor") Integer vendor,
			@FormParam("tanggal") String tanggal,
			@FormParam("keterangan") String keterangan,
			@HeaderParam("Authorization") String token) throws ParseException {
		
		VendorApproval vendorApproval = vendorApprovalSession.find(id);
		vendorApproval.setUserId(0);
		vendorApproval.setNomor(nomor);
		vendorApproval.setVendor(vendorSession.find(vendor));
		vendorApproval.setTanggal(formatIndonesia.parse(tanggal));
		vendorApproval.setKeterangan(keterangan);
		
		vendorApprovalSession.updateVendorApproval(vendorApproval, tokenSession.findByToken(token));
		
		return vendorApproval;
	}

}
