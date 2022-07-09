package id.co.promise.procurement.master;

import id.co.promise.procurement.catalog.entity.CatalogFee;
import id.co.promise.procurement.catalog.entity.CatalogVendor;
import id.co.promise.procurement.catalog.session.CatalogFeeSession;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Role;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.security.TokenSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import javax.ws.rs.core.Response;

@Stateless
@Path(value = "/procurement/master/organisasi")
@Produces(MediaType.APPLICATION_JSON)
public class OrganisasiService {
	@EJB
	private OrganisasiSession organisasiSession;
	
	@EJB
	private CatalogFeeSession catalogFeeSession;
	
	@EJB
	TokenSession tokenSession;

	@Path("/get-all")
	@GET
	public String getOrganisasiAllJSON() {
		return organisasiSession.getOrganisasiAllJSON();
	}

	@Path("/get-all-by-id/{parentId}/{id}")
	@GET
	public String findOrganisasiAllJSON(@PathParam("parentId") int parentId,
			@PathParam("id") int id) {
		return organisasiSession.findOrganisasiAllJSON(parentId, id);
	}

	@Path("/get-by-id/{organisasiId}")
	@GET
	public Organisasi getById(@PathParam("organisasiId") int organisasiId) {
		return organisasiSession.find(organisasiId);
	}
	
	@Path("/get-by-name/{name}")
	@GET
	public List<Organisasi> getByName(@PathParam("name") String name) {
		return organisasiSession.findByName(name);
	}
	
	@Path("/get-organisasi-afco-by-token")
	@GET
	public Organisasi getAfcoOrganisasiByToken(@HeaderParam("Authorization") String token) {
		User user = tokenSession.findByToken(token).getUser();
		return organisasiSession.getAfcoOrganisasiByUserId(user.getId());
	}
	
	@Path("/get-organisasi-afco-by-id/{organisasiId}")
	@GET
	public Organisasi getAfcoOrganisasiById(@PathParam("organisasiId") Integer organisasiId) {
		return organisasiSession.getAfcoOrganisasiByOrganisasiId(organisasiId);
	}
	
	@Path("/get-organisasi-afco-by-parentId/{organisasiId}")
	@GET
	public Organisasi getAfcoOrganisasiByParentId(@PathParam("organisasiId") Integer organisasiId) {
		return organisasiSession.getAfcoOrganisasiByParentId(organisasiId);
	}
	

	@Path("/get-list")
	@GET
	public List<Organisasi> getOrganisasiList() {
		return organisasiSession.getOrganisasiAll();
	}

	@Path("/get-list-by-parent-id/{parentId}")
	@GET
	public List<Organisasi> getOrganisasiListByParentId(
			@PathParam("parentId") int parentId) {
		return organisasiSession.getOrganisasiListByParentId(parentId);
	}

	@Path("/get-list-parent")
	@GET
	public List<Organisasi> getOrganisasiParent() {
		return organisasiSession.getOrganisasiParentList();
	}
	
	@Path("/create")
	@POST
	public Organisasi createOrganisasi(@FormParam("nama") String nama,
			@FormParam("parentId") int parentId,
			@HeaderParam("Authorization") String token) {
		Organisasi org = new Organisasi();
		org.setNama(nama);
		org.setParentId(parentId);
		return organisasiSession.createOrganisasi(org, tokenSession.findByToken(token));
	}
	
	@Path("/insert")
	@POST
	public Organisasi insertOrganisasi(Organisasi organisasi,
			@HeaderParam("Authorization") String token){
		return organisasiSession.createOrganisasi(organisasi, tokenSession.findByToken(token));
	}

	@Path("/edit")
	@POST
	public Organisasi editOrganisasi(Organisasi organisasi,
			@HeaderParam("Authorization") String token) {
		organisasi.setIsDelete(0);
		return organisasiSession.editOrganisasi(organisasi, tokenSession.findByToken(token));
	}

	@Path("/delete")
	@POST
	public Organisasi deleteOrganisasi(
			@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return organisasiSession.delete(id, tokenSession.findByToken(token));
	}
	
	@Path("/get-afco-list-parent")
	@GET
	public List<Organisasi> getAfcoListParent(){
		return organisasiSession.getOrganisasiListAfco();
	}
	
	@Path("/get-organisasi-level2-list")
	@GET
	public List<Organisasi> getOrganisasiLevel2List(){
		return organisasiSession.getOrganisasiLevel2List();
	}
	
	
	
	@Path("/get-all-list-child-by-parent-id/{parentId}")
	@GET
	public List<Organisasi> getAllChildListByOrganisasi(@PathParam("parentId") Integer organisasiId){
		return organisasiSession.getAllChildListByOrganisasi(organisasiId);
	}
	
	@Path("/get-all-list-child-by-parent-id-and-self/{parentId}")
	@GET
	public List<Organisasi> getAllChildListByOrganisasiAndSelf(@PathParam("parentId") Integer organisasiId){
		List<Organisasi> organisasiList = organisasiSession.getAllChildListByOrganisasi(organisasiId);
	    Organisasi parentOrganisasi = getById(organisasiId);
	    organisasiList.add(parentOrganisasi);
	    
	    return organisasiList;
	 }
	
	@Path("/get-list-not-select")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getOrganisasiListNotSelect(List<Integer> selectList, @HeaderParam("Authorization") String token) {

		List<Organisasi> organisasiList = organisasiSession.getListNotSelect(selectList);
		return Response.ok(organisasiList).build();
	}
	
	@Path("/get-list-not-select-by-catalogId")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getOrganisasiListNotSelectByCatalogId( @HeaderParam("Authorization") String token, Integer catalogId) {
		List <CatalogFee> catalogFeeList = catalogFeeSession.getListCatalogFeeByCatalogId(catalogId);
		List<Integer> selectList= new ArrayList();
		for (CatalogFee catalogFee : catalogFeeList ) {
			selectList.add(catalogFee.getOrganisasi().getId());
		}
		
		List<Organisasi> organisasiList = organisasiSession.getListNotSelect(selectList);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("organisasiList", organisasiList);
		map.put("selectList", selectList);

		
		return Response.ok(map).build();
	}
	
	@Path("/getOrganisasiListParentIdIsNull")
	@GET
	public List<Organisasi> getOrganisasiListParentIdIsNull(){
		return organisasiSession.getOrganisasiListParentIdIsNull();
	}
}
