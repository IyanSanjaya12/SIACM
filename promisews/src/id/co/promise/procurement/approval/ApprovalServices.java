package id.co.promise.procurement.approval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

import com.google.gson.Gson;

import id.co.promise.procurement.DTO.ApprovalDtl;
import id.co.promise.procurement.entity.Approval;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Tahapan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.RoleSession;
import id.co.promise.procurement.master.TahapanSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;

@Stateless
@Path(value = "/procurement/approval")
@Produces(MediaType.APPLICATION_JSON)
public class ApprovalServices {
	final static Logger log = Logger.getLogger(ApprovalServices.class);
	final static CustomResponseMessage message = CustomResponse
			.getCustomResponseMessage();
	final Gson gson = new Gson();

	@EJB
	ApprovalSession approvalSession;

	@EJB
	ApprovalTypeSession approvalTypeSession;

	@EJB
	ApprovalLevelSession approvalLevelSession;

	@EJB
	ApprovalProcessSession approvalProcessSession;

	@EJB
	ApprovalProcessTypeSession approvalProcessTypeSession;

	@EJB
	RoleUserSession roleUserSession;

	@EJB
	OrganisasiSession organisasiSession;

	@EJB
	UserSession userSession;

	@EJB
	TokenSession tokenSession;
	
	@EJB
	RoleSession roleSession;
	
	@EJB
	TahapanSession tahapanSession;


	@Path("/get-list")
	@GET
	public Response getList(@HeaderParam("Authorization") String token) {
		try {
			return Response.status(Status.CREATED)
					.entity(approvalSession.getList()).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
	}
	
	@Path("/get-list-by-organisasi")
	@GET
	public Response getListByOrganisasi(@HeaderParam("Authorization") String token) {
		RoleUser roleUser = roleUserSession.getByToken(tokenSession.findByToken(token));
		try {
			return Response.status(Status.CREATED)
					.entity(approvalSession.getListByOrganisasi(roleUser.getOrganisasi())).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
	}
	
	@Path("/get-list-by-organisasi/{unitId}")
	@GET
	public Response getListByOrganisasi(@PathParam("unitId") Integer unitId) {
		Organisasi organisasi = organisasiSession.find(unitId);
		try {
			return Response.status(Status.CREATED)
					.entity(approvalSession.getListByOrganisasi(organisasi)).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
	}

	@Path("/get-approval-by-tahapan-and-token/{tahapanName}")
	@GET
	public List<Approval> getApprovalByAfco(@PathParam("tahapanName") String tahapanName,
			@HeaderParam("Authorization") String token) {
		/* Tahapan tahapan = tahapanSession.find(tahapanName); */
		Tahapan tahapan = tahapanSession.getByName(tahapanName); 
		User user =tokenSession.findByToken(token).getUser();
		Organisasi afco = organisasiSession.getAfcoOrganisasiByUserId(user.getId());
		
		
		return approvalSession.getListApprovalByTahapanAndOrganisasi(tahapan, afco);
	}

	@SuppressWarnings("rawtypes")
	@Path("/get-master-approval-list-detil")
	@GET
	public List getMasterApprovalListDetil(@HeaderParam("Authorization") String token) {		
		return approvalSession.getMasterApprovalDetil();
	}
	
//	@SuppressWarnings({ "rawtypes" })
//	@Path("/insert")
//	@POST
//	public Map insert(ApprovalDtl approvaldtl, @HeaderParam("Authorization") String token) {
//
//		Map<Object, Object> map = new HashMap<Object, Object>();
//		String toDo = "insert";
//		Token tokenObj = tokenSession.findByToken(token);
//		RoleUser roleUser =roleUserSession.getByToken(tokenObj);
//		Boolean isSaveNama = approvalSession.checkNamaApproval(approvaldtl.getApproval().getName(), toDo, approvaldtl.getApproval().getId());
//		Approval approval = new Approval();
//		if (!isSaveNama) {
//			String errorNama = "Data Sudah Ada";
//			Boolean isValid = false;
//			map.put("isValid", isValid);
//			map.put("errorNama", errorNama);
//		}
//
//		if (isSaveNama) {
//			approvaldtl.getApproval().setCreated(new Date());
//			approvaldtl.getApproval().setUserId(0);
//			approvaldtl.getApproval().setOrganisasi(roleUser.getOrganisasi());
//			approval =approvalSession.create(approvaldtl.getApproval(), tokenSession.findByToken(token));
//			for (ApprovalLevel approvalLevel : approvaldtl.getApprovalLevelList()) {
//			approvalLevel.setCreated(new Date());
//			approvalLevel.setApproval(approval);
//			approvalLevelSession.create(approvalLevel, tokenSession.findByToken(token));
//			}
//		}
//
//		map.put("approvalDto", approvaldtl);
//		return map;
//	}
	
	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(ApprovalDtl approvaldtl, @HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";
		Boolean isSaveNama = approvalSession.checkNamaApproval(approvaldtl.getApproval().getName(), toDo, approvaldtl.getApproval().getId());
		Approval approval = new Approval();
		if (!isSaveNama) {
			String errorNama = "Data Sudah Ada";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (isSaveNama) {
			approvaldtl.getApproval().setCreated(new Date());
			approvaldtl.getApproval().setUserId(0);
			approval =approvalSession.create(approvaldtl.getApproval(), tokenSession.findByToken(token));
			for (ApprovalLevel approvalLevel : approvaldtl.getApprovalLevelList()) {
			approvalLevel.setCreated(new Date());
			approvalLevel.setApproval(approval);
			approvalLevelSession.create(approvalLevel, tokenSession.findByToken(token));
			}
		}

		map.put("approvalDto", approvaldtl);
		return map;
	}
	
	
	@SuppressWarnings({ "rawtypes" })
	@Path("/update")
	@POST
	public Map update(ApprovalDtl approvaldtl, @HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";

		Boolean isSaveNama = approvalSession.checkNamaApproval(approvaldtl.getApproval().getName(), toDo, approvaldtl.getApproval().getId());
		Approval approval = new Approval();
		if (!isSaveNama) {
			String errorNama = "Data Sudah Ada";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (isSaveNama) {
			approvaldtl.getApproval().setCreated(new Date());
			approvaldtl.getApproval().setUserId(0);
			approval =approvalSession.update(approvaldtl.getApproval(), tokenSession.findByToken(token));
			//delete dulu yang lama
			List<ApprovalLevel> approvalLevelListOld = approvalLevelSession.findByApproval(approval);
			for(ApprovalLevel approvalLevel : approvalLevelListOld){
				approvalLevelSession.delete(approvalLevel.getId(), tokenSession.findByToken(token));
			}
			//insert yang baru
			for (ApprovalLevel approvalLevel : approvaldtl.getApprovalLevelList()) {
			approvalLevel.setCreated(new Date());
			approvalLevel.setId(null);
			approvalLevel.setApproval(approval);
			approvalLevelSession.create(approvalLevel, tokenSession.findByToken(token));
			}
		}

		map.put("approvalDto", approvaldtl);
		return map;
	}
	
	
	@Path("/role-user-list-organisasi")
	@POST
	public Response getRoleUserByAfcoOrganisasi(@FormParam("name") String name, @HeaderParam("Authorization") String token) {
		try {
			return Response.status(201)
					.entity(roleUserSession.getRoleUserByAfcoOrganisasi(name, tokenSession.findByToken(token))).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
	}
	

	@SuppressWarnings("unused")
	@Path("/create")
	@POST
	public Response create(String jsonString,
			@HeaderParam("Authorization") String token) {

		Map<Integer, Organisasi> mapOrganisasi = new HashMap<Integer, Organisasi>();
		for (Organisasi o : organisasiSession.findAll()) {
			mapOrganisasi.put(o.getId(), o);
		}

		Approval ap = new Approval();
		ApprovalParamHttp apParam;

		try {
			apParam = gson.fromJson(jsonString, ApprovalParamHttp.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 400 Bad Request
			return Response.status(Status.BAD_REQUEST)
					.entity(message.error(e.getMessage())).build();
		}

		try {
			Integer appUserId = tokenSession.findByToken(token).getUser()
					.getId();
			Token tokenA = tokenSession.findByToken(token);
			ApprovalProcessType approvalProcessType = approvalProcessTypeSession
					.find(apParam.approvalProcessTypeId);

			ap.setName(apParam.approvalName);
			ap.setApprovalType(approvalTypeSession.find(apParam.approvalTypeId));
			ap.setUserId(appUserId);
			ap.setCreated(new Date());
			// create Approval
			ap = approvalSession.create(ap, tokenA);

			for (ApprovalLevelForm appLevelForm : apParam.listOfApprovalLevelForm) {
				Organisasi organisasi = mapOrganisasi
						.get(appLevelForm.departemenId);
				ApprovalLevel approvalLevel = new ApprovalLevel();
				approvalLevel.setApproval(ap);
				approvalLevel.setGroup(organisasi);
				if (appLevelForm.userId != null && appLevelForm.userId != 0) {
					approvalLevel
							.setUser(userSession.find(appLevelForm.userId));
				} else {
					approvalLevel.setUser(null);
				}
				approvalLevel.setSequence(appLevelForm.sequence);
				approvalLevel.setThreshold(appLevelForm.threshold);
				approvalLevel.setUserId(appUserId);
				approvalLevel.setCreated(new Date());
				// Create ApprovalLevel
				approvalLevel = approvalLevelSession.create(approvalLevel,
						tokenA);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
		// kirim 201 Created (The request has been fulfilled, resulting in the
		// creation of a new resource)
		return Response.status(Status.CREATED).entity(ap).build();
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	@Path("/edit")
	@POST
	public Response edit(String jsonString,
			@HeaderParam("Authorization") String token) {

		ApprovalParamHttp apParam;
		try {
			apParam = gson.fromJson(jsonString, ApprovalParamHttp.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 400 Bad Request
			return Response.status(Status.BAD_REQUEST)
					.entity(message.error(e.getMessage())).build();
		}

		Approval ap;

		try {
			ap = approvalSession.find(apParam.approvalId);
			List<ApprovalLevel> lal = approvalLevelSession.findByApproval(ap);
			Integer appUserId = tokenSession.findByToken(token).getUser()
					.getId();
			Token tokenA = tokenSession.findByToken(token);

			ApprovalProcessType approvalProcessType = approvalProcessTypeSession
					.find(apParam.approvalProcessTypeId);

			ap.setName(apParam.approvalName);
			ap.setApprovalType(approvalTypeSession.find(apParam.approvalTypeId));
			ap.setUserId(appUserId);
			ap.setUpdated(new Date());
			// edit Approval
			ap = approvalSession.update(ap, tokenA);

			Map<Integer, Organisasi> mapOrganisasi = new HashMap<Integer, Organisasi>();
			for (Organisasi o : organisasiSession.findAll()) {
				mapOrganisasi.put(o.getId(), o);
			}

			for (ApprovalLevelForm appLevelForm : apParam.listOfApprovalLevelForm) {
				Organisasi organisasi = mapOrganisasi
						.get(appLevelForm.departemenId);

				ApprovalLevel approvalLevel = new ApprovalLevel();
				if (appLevelForm.id != null) {
					approvalLevel = approvalLevelSession.find(appLevelForm.id);
					Iterator itAppLevel = lal.iterator();
					while (itAppLevel.hasNext()) {
						ApprovalLevel apl1 = (ApprovalLevel) itAppLevel.next();
						if (approvalLevel.getId() == apl1.getId()) {
							itAppLevel.remove();
						}
					}
					approvalLevel.setApproval(ap);
					approvalLevel.setGroup(organisasi);
					if (appLevelForm.userId != null && appLevelForm.userId != 0) {
						approvalLevel.setUser(userSession
								.find(appLevelForm.userId));
					} else {
						approvalLevel.setUser(null);
					}
					approvalLevel.setSequence(appLevelForm.sequence);
					approvalLevel.setThreshold(appLevelForm.threshold);
					approvalLevel.setUserId(appUserId);
					approvalLevel.setUpdated(new Date());
					// Update ApprovalLevel bila ditemukan
					approvalLevel = approvalLevelSession.update(approvalLevel,
							tokenA);
				} else {
					approvalLevel.setApproval(ap);
					approvalLevel.setGroup(organisasi);
					if (appLevelForm.userId != null && appLevelForm.userId != 0) {
						approvalLevel.setUser(userSession
								.find(appLevelForm.userId));
					} else {
						approvalLevel.setUser(null);
					}
					approvalLevel.setSequence(appLevelForm.sequence);
					approvalLevel.setThreshold(appLevelForm.threshold);
					approvalLevel.setUserId(appUserId);
					approvalLevel.setCreated(new Date());
					// Create ApprovalLevel bila belum ada
					approvalLevel = approvalLevelSession.create(approvalLevel,
							tokenA);
				}

			}

			// Hapus semua approval level yang tidak lagi digunakan setelah
			// diedit
			for (ApprovalLevel apl1 : lal) {
				approvalLevelSession.delete(apl1.getId(), tokenA);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
		// kirim 201 Created (The request has been fulfilled, resulting in the
		// creation of a new resource)
		return Response.status(201).entity(ap).build();
	}

	@Path("/delete")
	@POST
	public Approval delete(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {

		Approval approval = approvalSession.getById(id);
		
		List<ApprovalLevel> approvalLevelList = approvalLevelSession.findByApproval(approval);
		for(ApprovalLevel approvalLevel : approvalLevelList){
			approvalLevelSession.delete(approvalLevel.getId(), tokenSession.findByToken(token));
		}
		approvalSession.delete(id, tokenSession.findByToken(token));
		
		return approval;
	}
	
	

	// Class helper untuk parameter dari http client

	class ApprovalParamHttp {
		private Integer approvalId;
		private String approvalName;
		private Integer approvalProcessTypeId;
		protected Integer approvalTypeId;
		private Collection<ApprovalLevelForm> listOfApprovalLevelForm;

		public ApprovalParamHttp(Integer approvalId, String approvalName,
				Integer approvalProcessTypeId, Integer approvalTypeId,
				Collection<ApprovalLevelForm> listOfApprovalLevelForm) {
			if (approvalId != 0) {
				this.approvalId = approvalId;
			}
			this.approvalName = approvalName;
			this.approvalProcessTypeId = approvalProcessTypeId;
			this.approvalTypeId = approvalTypeId;
			this.listOfApprovalLevelForm = listOfApprovalLevelForm;
		}

		public ApprovalParamHttp(Integer approvalId) {
			if (approvalId != 0) {
				this.approvalId = approvalId;
			}
		}
	}

	class ApprovalLevelForm {
		private Integer id;
		private Integer sequence;
		private Integer userId;
		private Double threshold;
		private Integer departemenId;

		public ApprovalLevelForm(Integer id, Integer sequence, Integer userId,
				Integer departemenId, Double threshold) {
			if (id != null && id != 0) {
				this.id = id;
			}
			this.sequence = sequence;
			this.userId = userId;
			this.threshold = threshold;
			this.departemenId = departemenId;
		}

		public ApprovalLevelForm(Integer sequence, Integer userId,
				Integer departemenId, Double threshold) {
			this.sequence = sequence;
			this.userId = userId;
			this.threshold = threshold;
			this.departemenId = departemenId;
		}
	}
}
