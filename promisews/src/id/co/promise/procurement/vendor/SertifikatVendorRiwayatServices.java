package id.co.promise.procurement.vendor;

import java.util.List;

import id.co.promise.procurement.entity.SertifikatVendorRiwayat;
import id.co.promise.procurement.security.TokenSession;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path(value="/procurement/vendor/SertifikatVendorRiwayatServices")
@Produces(MediaType.APPLICATION_JSON)
public class SertifikatVendorRiwayatServices {
	
	@EJB private SertifikatVendorRiwayatSession SertifikatVendorRiwayatSession;
	
	@EJB private VendorSession vendorSession;
	
  	@EJB private TokenSession tokenSession;
	
	@Path("/insertSertifikatVendorRiwayat")
	@POST
	public SertifikatVendorRiwayat insertSertifikatVendorRiwayat(SertifikatVendorRiwayat SertifikatVendorRiwayat, @HeaderParam("Authorization") String token) {
		return SertifikatVendorRiwayatSession.insertSertifikatVendorRiwayat(SertifikatVendorRiwayat, tokenSession.findByToken(token));
	} 
	
	@Path("/updateSertifikatVendorRiwayat")
	@POST
	public SertifikatVendorRiwayat updateSertifikatVendorRiwayat(SertifikatVendorRiwayat SertifikatVendorRiwayat, @HeaderParam("Authorization") String token) {
		return SertifikatVendorRiwayatSession.updateSertifikatVendorRiwayat(SertifikatVendorRiwayat, tokenSession.findByToken(token));
	}  

	@Path("/deleteSertifikatVendorRiwayat/{id}")
	@GET
	public SertifikatVendorRiwayat deleteSertifikatVendorRiwayat(@PathParam("id") int id, @HeaderParam("Authorization") String token) {
		return SertifikatVendorRiwayatSession.deleteSertifikatVendorRiwayat(id, tokenSession.findByToken(token));
	}

	@Path("/getSertifikatVendorRiwayatByVendor/{id}")
	@GET	
	public List<SertifikatVendorRiwayat> getSertifikatVendorRiwayatByVendor(@PathParam("id") int id) {
		return SertifikatVendorRiwayatSession.getSertifikatVendorRiwayatByVendor(id);
	} 
	
}
