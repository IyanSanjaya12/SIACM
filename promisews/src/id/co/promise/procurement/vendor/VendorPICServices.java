package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.VendorPIC;
import id.co.promise.procurement.master.JabatanSession;
import id.co.promise.procurement.security.TokenSession;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path(value="/procurement/vendor/VendorPICServices")
@Produces(MediaType.APPLICATION_JSON)
public class VendorPICServices {
	@EJB private VendorPICSession vendorPICSession;
	@EJB private VendorSession vendorSession;
	@EJB private JabatanSession jabatanSession;
	@EJB private TokenSession tokenSession;
	
	@Path("/getVendorPICByVendorId/{vendorId}")
	@GET
	public List<VendorPIC> getVendorPICByVendorId(@PathParam("vendorId") int vendorId){
		return vendorPICSession.getVendorPICByVendorId(vendorId);
	}
	
	@Path("/createVendorPIC")
	@POST
	public VendorPIC createVendorPIC (
			@FormParam("vendor") int vendorId, 
			@FormParam("nama") String nama, 
			@FormParam("jabatan") int jabatanId, 
			@FormParam("email") String email,
			@HeaderParam("Authorization") String token
			){
		VendorPIC vendorPIC = new VendorPIC();
		
		if (vendorId > 0) {
			vendorPIC.setVendor(vendorSession.find(vendorId));
		}
		
		if (jabatanId > 0) {
			vendorPIC.setJabatan(jabatanSession.find(jabatanId));
		}
		
		if (nama != null && nama.length() > 0) {
			vendorPIC.setNama(nama);
		}
		
		if (email != null && email.length() > 0) {
			vendorPIC.setEmail(email);
		}
		
		vendorPIC.setCreated(new Date());
		vendorPIC.setIsDelete(0);
		
		vendorPICSession.insertVendorPIC(vendorPIC, tokenSession.findByToken(token));
		
		return vendorPIC;
	}
	
	@Path("/editVendorPIC")
	@POST
	public VendorPIC editVendorPIC (
			@FormParam("id") int vendorPICId, 
			@FormParam("vendor") int vendorId, 
			@FormParam("nama") String nama, 
			@FormParam("jabatan") int jabatanId, 
			@FormParam("email") String email,
			@HeaderParam("Authorization") String token
			){
		VendorPIC vendorPIC = vendorPICSession.find(vendorPICId);
		
		if (vendorId > 0) {
			vendorPIC.setVendor(vendorSession.find(vendorId));
		}
		
		if (jabatanId > 0) {
			vendorPIC.setJabatan(jabatanSession.find(jabatanId));
		}
		
		if (nama != null && nama.length() > 0) {
			vendorPIC.setNama(nama);
		}
		
		if (email != null && email.length() > 0) {
			vendorPIC.setEmail(email);
		}
		
		vendorPICSession.updateVendorPIC(vendorPIC, tokenSession.findByToken(token));
		
		return vendorPIC;
	}

	@Path("/deleteRowVendorPIC/{id}")
	@GET
	public void deleteRowVendorPIC(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		vendorPICSession.deleteRowVendorPIC(id, tokenSession.findByToken(token));
	}

}
