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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.entity.OrgApprovalPath;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value="/procurement/master/approval-path")
@Produces(MediaType.APPLICATION_JSON)
public class OrgApprovalPathServices {
	
	@EJB OrgApprovalPathSession orgApprovalPathSession;
	@EJB TokenSession tokenSession;
	@EJB OrganisasiSession organisasiSession;
	
	@Path("/get-list")
	@GET
	public List<OrgApprovalPath> getlist(){
		return orgApprovalPathSession.getList();
	}
	
	@Path("/get-list-organisai-parent")
	@GET
	public List<Organisasi> getOrganisasiParentIdNull(){
		return organisasiSession.getOrganisasiListAfco();
	}	
	
	@SuppressWarnings({"rawtypes"})
	@Path("/insert")
	@POST
	public Map insert(OrgApprovalPath orgApprovalPath, @HeaderParam("Authorization") String token) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		String pathName = orgApprovalPath.getApprovalPathName().toUpperCase();
		orgApprovalPath.setApprovalPathName(pathName);
		String toDo = "insert";
		
		Boolean validateSaveIdPath = orgApprovalPathSession.checkPathId(orgApprovalPath.getApprovalPathId(), toDo, orgApprovalPath.getId());
		Boolean validateSavePathName = orgApprovalPathSession.checkPathName(orgApprovalPath.getApprovalPathName(), toDo, orgApprovalPath.getId());
		
		if (!validateSaveIdPath) {
			Boolean isValid = false;
			String errorPathId = "Id Tidak Boleh Sama";
			map.put("errorPathId", errorPathId);
			map.put("isValidId", isValid);
		}
		
		if (!validateSavePathName) {
			Boolean isValid = false;
			String errorPathName = "Name Tidak Boleh Sama";
			map.put("errorPathName", errorPathName);
			map.put("isValidName", isValid);
		}
		
		if (validateSaveIdPath && validateSavePathName) {
			orgApprovalPathSession.insert(orgApprovalPath, tokenSession.findByToken(token));
		}
		
		map.put("orgApprovalPath", orgApprovalPath);
		return map;
	}
	
	@SuppressWarnings({"rawtypes"})
	@Path("/update")
	@POST
	public Map update(OrgApprovalPath orgApprovalPath,
			@HeaderParam("Authorization") String token) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		String pathName = orgApprovalPath.getApprovalPathName().toUpperCase();
		orgApprovalPath.setApprovalPathName(pathName);
		String toDo = "update";
		
		Boolean validateSaveIdPath = orgApprovalPathSession.checkPathId(orgApprovalPath.getApprovalPathId(), toDo, orgApprovalPath.getId());
		Boolean validateSavePathName = orgApprovalPathSession.checkPathName(orgApprovalPath.getApprovalPathName(), toDo, orgApprovalPath.getId());
		if (!validateSaveIdPath) {
			Boolean isValid = false;
			String errorPathId = "Id Tidak Boleh Sama";
			map.put("errorPathId", errorPathId);
			map.put("isValidId", isValid);
		}
		
		if (!validateSavePathName) {
			Boolean isValid = false;
			String errorPathName = "Nama Tidak Boleh Sama";
			map.put("errorPathName", errorPathName);
			map.put("isValidName", isValid);
		}
		if (validateSaveIdPath && validateSavePathName) {
			orgApprovalPathSession.update(orgApprovalPath, tokenSession.findByToken(token));
		}
		
		map.put("orgApprovalPath", orgApprovalPath);	  
		return map;
	}

	@Path("/delete")
	@POST
	public OrgApprovalPath deleteOrgApprovalPath(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return orgApprovalPathSession.delete(id, tokenSession.findByToken(token));
	}
}
