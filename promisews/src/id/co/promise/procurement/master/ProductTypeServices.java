package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.ProductType;
import id.co.promise.procurement.security.TokenSession;

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
@Path(value = "/procurement/master/ProductTypeServices")
@Produces(MediaType.APPLICATION_JSON)
public class ProductTypeServices {

	@EJB
	TokenSession tokenSession;
	
	@EJB
	private ProductTypeSession productTypeSession;
	
	@Path("/getProductType/{id}")
	@GET
	public ProductType getProductType(@PathParam("id") int id) {
		return productTypeSession.getProductType(id);
	}
	
	@Path("/getItemTypeList")
	@GET
	public List<ProductType> getProductTypeList() {
		return productTypeSession.getProductTypeList();
	}
	
	@Path("/insertProductType")
	@POST
	public ProductType insertProductType(@FormParam("nama") String nama, 
			@HeaderParam("Authorization") String token) {
		ProductType it = new ProductType();
		it.setNama(nama);
		it.setCreated(new Date());
		productTypeSession.insertProductType(it, tokenSession.findByToken(token));
		return it;
	}

	@Path("/updateProductType")
	@POST
	public ProductType updateProductType(@FormParam("id") int id,
			@FormParam("nama") String nama,
			@HeaderParam("Authorization") String token) {
		ProductType it = productTypeSession.find(id);
		it.setNama(nama);
		it.setUpdated(new Date());
		productTypeSession.updateProductType(it, tokenSession.findByToken(token));
		return it;
	}

	@Path("/deleteProductType/{id}")
	@GET
	public ProductType deleteProductType(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return productTypeSession.deleteProductType(id,
				tokenSession.findByToken(token));
	}

	@Path("/deleteRowProductType/{id}")
	@GET
	public ProductType deleteRowProductType(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return productTypeSession.deleteRowProductType(id,
				tokenSession.findByToken(token));
	}
}
