package id.co.promise.procurement.approval;

import java.util.ArrayList;
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

import org.json.JSONException;

import id.co.promise.procurement.entity.ApprovalTahapan;
import id.co.promise.procurement.entity.ApprovalTahapanDetail;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.master.ApprovalTahapanDTO;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.TahapanSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.Constant;

@Stateless
@Path(value = "/procurement/approval/approval-tahapan")
@Produces(MediaType.APPLICATION_JSON)
public class ApprovalTahapanService {
	
	@EJB
	private ApprovalTahapanSession approvalTahapanSession;
	
	@EJB
	private ApprovalTahapanDetailSession approvalTahapanDetailSession;
	
	@EJB
	private OrganisasiSession organisasiSession;
	
	@EJB
	private ApprovalSession approvalSession;
	
	@EJB
	private ApprovalLevelSession approvalLevelSession;
	
	@EJB
	private ApprovalProcessTypeSession approvalProcessTypeSession;
	
	@EJB
	private TahapanSession tahapanSession;
	
	@EJB
	TokenSession tokenSession;
	
	@Path("/get-list")
	@GET
	public List<ApprovalTahapanDTO> getApprovalTahapanList(@HeaderParam("Authorization") String token){
		
		Token tokenObj = tokenSession.findByToken(token);
		Organisasi organisasi = organisasiSession.getOrganisasiByToken(tokenObj);
		List<ApprovalTahapan> approvalTahapanList = approvalTahapanSession.getList();
		List<ApprovalTahapanDTO> approvalTahapanDTOList = new ArrayList();
		for (ApprovalTahapan approvalTahapan : approvalTahapanList ) {
			ApprovalTahapanDTO approvalTahapanDTO = new ApprovalTahapanDTO();
			approvalTahapanDTO.setTahapan(approvalTahapan.getTahapan());
			approvalTahapanDTO.setUnit(approvalTahapan.getOrganisasi());
			approvalTahapanDTO.setId(approvalTahapan.getId());
			ApprovalTahapanDetail approvalTahapanDetail = approvalTahapanDetailSession.getApprovalTahapanByTahapan(approvalTahapan.getTahapan());
			String Tipe = Constant.TYPE_THRESHOLD_PUSAT;
			if (approvalTahapanDetail.getTipe()==Constant.TYPE_THRESHOLD_CABANG_VALUE) {
				Tipe =Constant.TYPE_THRESHOLD_CABANG;
			}
			approvalTahapanDTO.setTipe(Tipe);
			
			Organisasi OrganisasiParent = organisasiSession.getAfcoOrganisasiByOrganisasiId(approvalTahapan.getOrganisasi().getId());
			
			approvalTahapanDTO.setOrganisasi(OrganisasiParent);
			approvalTahapanDTOList.add(approvalTahapanDTO);
		}
		
		
		
	    return approvalTahapanDTOList;
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(ApprovalTahapanDTO approvalTahapanDTO,
			@HeaderParam("Authorization") String token) throws JSONException {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";
		Token tokenObj = tokenSession.findByToken(token);
		Organisasi organisasi = organisasiSession.getOrganisasiByToken(tokenObj);
		Boolean isSaveTahapanApproval = approvalTahapanSession.checkApprovalTahapan(approvalTahapanDTO.getTahapan(), approvalTahapanDTO.getOrganisasi(), toDo, approvalTahapanDTO.getTahapan().getId());
		
		if (!isSaveTahapanApproval) {
			String errorTahapanApproval = "Tahapan : "  + approvalTahapanDTO.getTahapan().getNama() + ", Sudah Ada Di Organisasi : "+ approvalTahapanDTO.getOrganisasi().getNama();
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorTahapanApproval", errorTahapanApproval);
		}

		if (isSaveTahapanApproval) {
			ApprovalTahapan approvalTahapan = new ApprovalTahapan();
			approvalTahapan.setTahapan(approvalTahapanDTO.getTahapan());
			approvalTahapan.setOrganisasi(approvalTahapanDTO.getUnit());
			approvalTahapanSession.insert(approvalTahapan, tokenSession.findByToken(token));
			for ( ApprovalTahapanDetail approvalTahapanDetail : approvalTahapanDTO.getApprovalTahapanDetailList() ) {
				ApprovalTahapanDetail approvalTahapanDetailNew = new ApprovalTahapanDetail();
				approvalTahapanDetailNew.setEndRange(approvalTahapanDetail.getEndRange());
				approvalTahapanDetailNew.setApproval(approvalTahapanDetail.getApproval());
				approvalTahapanDetailNew.setApprovalTahapan(approvalTahapan);
				/*
				 * Integer type = null; if (approvalTahapanDTO.getTipe() != null) { // jika
				 * mempunyai tipe, berti dia memiliki threshold type =
				 * Constant.TYPE_THRESHOLD_PUSAT_VALUE;
				 * if(approvalTahapanDTO.getTipe().equalsIgnoreCase(Constant.
				 * TYPE_THRESHOLD_CABANG)) { type = Constant.TYPE_THRESHOLD_CABANG_VALUE; } }
				 */
				
				/* approvalTahapanDetailNew.setTipe(type); */
				approvalTahapanDetailSession.insert(approvalTahapanDetailNew, tokenSession.findByToken(token));
			}			
		}
		map.put("ApprovalTahapanDTO", approvalTahapanDTO);

		return map;
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Path("/update")
	@POST
	public Map update(ApprovalTahapanDTO approvalTahapanDTO,
			@HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";
		Boolean isSaveTahapanApproval = approvalTahapanSession.checkApprovalTahapan(approvalTahapanDTO.getTahapan(), approvalTahapanDTO.getOrganisasi(), toDo, approvalTahapanDTO.getId());
		Token tokenObj = tokenSession.findByToken(token);
		Organisasi organisasi = organisasiSession.getOrganisasiByToken(tokenObj);
		if (!isSaveTahapanApproval) {
			String errorTahapanApproval = "Approval Sudah Ada Dengan Tahapan "  + approvalTahapanDTO.getTahapan().getNama();
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorTahapanApproval", errorTahapanApproval);
		}

		if (isSaveTahapanApproval) {
			
			ApprovalTahapan approvalTahapan = new ApprovalTahapan();
			approvalTahapan.setTahapan(approvalTahapanDTO.getTahapan());
			approvalTahapan.setOrganisasi(organisasi);
			approvalTahapan.setId(approvalTahapanDTO.getId());
			approvalTahapanDetailSession.deleteByApprovalTahapanId(approvalTahapanDTO.getId());
			for ( ApprovalTahapanDetail approvalTahapanDetail : approvalTahapanDTO.getApprovalTahapanDetailList() ) {
				ApprovalTahapanDetail approvalTahapanDetailNew = new ApprovalTahapanDetail();
				approvalTahapanDetailNew.setThreshold(approvalTahapanDetail.getThreshold());
				approvalTahapanDetailNew.setApproval(approvalTahapanDetail.getApproval());
				approvalTahapanDetailNew.setApprovalTahapan(approvalTahapan);
				approvalTahapanDetailNew.setEndRange(approvalTahapanDetail.getEndRange());
				approvalTahapanDetailSession.insert(approvalTahapanDetailNew, tokenSession.findByToken(token));
			}	
		}

		map.put("ApprovalTahapanDTO", approvalTahapanDTO);

		return map;
	}
	
	
	@Path("/delete")
	@POST
	public ApprovalTahapan deleteApprovalTahapan(
			@FormParam("id")int id,
			@HeaderParam("Authorization") String token){

		approvalTahapanDetailSession.deleteByApprovalTahapanId(id);
		return approvalTahapanSession.delete(id, tokenSession.findByToken(token));
	}
	
	
}
