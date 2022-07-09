package id.co.promise.procurement.master;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import id.co.promise.procurement.entity.Jabatan;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/procurement/master/jabatan")
@Produces(MediaType.APPLICATION_JSON)
public class JabatanService {
	@EJB
	JabatanSession jabatanSession;

	@EJB
	TokenSession tokenSession;
	
	@EJB
	RoleUserSession roleUserSession;
	
	@EJB
	OrganisasiSession organisasiSession;

	@Path("/get-list")
	@GET
	public List<Jabatan> getJabatanList() {
		return jabatanSession.getJabatanList();
	}
	
	@Path("/get-list-by-organisasi/{childId}")
	@GET
	public List<Object[]> getListByOrganisasi(@PathParam("childId") Integer childId) {
		List<Organisasi> organisasiList = organisasiSession.getAllParentListByChild(childId);
		organisasiList.addAll(organisasiSession.getOrganisasiListByParentId(1));
		List<Object[]> objList = jabatanSession.getJabatanAndOrganisasiByOrganisasiIdList(organisasiList);
		
		return objList;
	}
	
	@Path("/get-list-by-organisasi-and-additional/{childId}")
	@GET
	public List<Object[]> getListByOrganisasiAndAdditional(@PathParam("childId") Integer childId) {
		List<Organisasi> organisasiList = organisasiSession.getAllParentListByChild(childId);
		//untuk hardcode dihilangin, jabatan yg mau dijadikan approval dipindah ke tabel T3_APPROVAL_ORG_JABATAN
//		organisasiList.addAll(organisasiSession.getOrganisasiListByParentId(1)); 
		Organisasi organisasi = organisasiSession.getRootParentByOrganisasi(childId);
		List<Object[]> objList = jabatanSession.getJabatanAndOrganisasiByOrganisasiIdList(organisasiList);
		objList.addAll(jabatanSession.getAdditionalApprovalList(organisasi));
		
		return objList;
	}
	
	@Path("/get-not-registered-list/{organisasiId}")
	@GET
	public List<Jabatan> getNotRegisteredList(@PathParam("organisasiId") Integer organisasiId) {
		return jabatanSession.getNotRegisteredList(organisasiId);
	}

	@Path("/getJabatanListByOrganisasi/{organisasiId}")
	@GET
	public List<Jabatan> getJabatanListByOrganisasi(@PathParam("organisasiId") Integer organisasiId) {
		List<Jabatan> jabatanList = jabatanSession.getJabatanListByOrganisasi(organisasiId);
		 return jabatanList;
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(Jabatan jabatan,
			@HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";

		Boolean isSaveNama = jabatanSession.checkNamaJabatan(jabatan.getNama(), toDo, jabatan.getId());

		if (!isSaveNama) {
			String errorNama = "Nama Jabatan Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (isSaveNama) {
			jabatanSession.insertJabatan(jabatan, tokenSession.findByToken(token));
		}

		map.put("Jabatan", jabatan);

		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/update")
	@POST
	public Map update(Jabatan jabatan,
			@HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";

		Boolean isSaveNama = jabatanSession.checkNamaJabatan(jabatan.getNama(), toDo, jabatan.getId());

		if (!isSaveNama) {
			String errorNama = "Nama Jabatan Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (isSaveNama) {
			jabatanSession.updateJabatan(jabatan, tokenSession.findByToken(token));
		}

		map.put("Jabatan", jabatan);

		return map;
	}

	@Path("/delete")
	@POST
	public Jabatan delete(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return jabatanSession.deleteJabatan(id, tokenSession.findByToken(token));
	}

}
