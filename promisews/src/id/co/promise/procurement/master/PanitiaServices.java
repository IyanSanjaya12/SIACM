package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.AnggotaPanitia;
import id.co.promise.procurement.entity.Panitia;
import id.co.promise.procurement.entity.PejabatPelaksanaPengadaan;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.TimPanitia;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

@Stateless
@Path(value = "/procurement/master/panitiaServices")
@Produces(MediaType.APPLICATION_JSON)
public class PanitiaServices {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@EJB
	private PanitiaSession panitiaSession;
	@EJB
	private OrganisasiSession organisasiSession;
	@EJB
	private AnggotaPanitiaSession anggotaPanitiaSession;
	@EJB
	private PejabatPelaksanaPengadaanSession pejabatPelaksanaPengadaanSession;
	@EJB
	private RoleUserSession roleUserSession;
	
	@EJB
	private TimPanitiaSession timPanitiaSession;
	@EJB
	TokenSession tokenSession;

	@Path("/getPanitiaById/{id}")
	@GET
	public Panitia getPanitia(@PathParam("id") int id) {
		return panitiaSession.getPanitia(id);
	}

	@Path("/getPanitiaByUserId/{userId}")
	@GET
	public Panitia getPanitiaByUserId(@PathParam("userId") int userId) {
		return panitiaSession.getPanitiaByUser(userId);
	}

	@Path("/getListPanitia")
	@GET
	public List<Panitia> getListPanitia() {
		return panitiaSession.getPanitiaList();
	}
	
	@Path("/getAllListPanitia")
	@GET
	public List<Object> getAllListPanitia() {
		return panitiaSession.getAllPanitiaList();
	}
	
	
	
	
	@Path("/getPanitiaList")
	@GET
	public List<PanitiaDTO>  GetPanitiaListDtl() {

		List<Panitia> panitiaList = panitiaSession.getPanitiaList();
		
		List<PanitiaDTO> panitiaDTOList = new ArrayList<PanitiaDTO>();
		for(Panitia panitia : panitiaList){
			PanitiaDTO panitiaDTO= new PanitiaDTO();
			List <TimPanitia> timPanitiaList =timPanitiaSession.getPanitiaPengadaanByPanitiaList(panitia.getId());
			List <PejabatPelaksanaPengadaan> pejabatPelaksanaPengadaanList = pejabatPelaksanaPengadaanSession.getPelaksanaPengadaanByPanitiaList(panitia.getId());
			
			panitiaDTO.setPanitia(panitia);
			if(timPanitiaList.size()!=0)
			{
				panitiaDTO.setNama(timPanitiaList.get(0).getNama());
				panitiaDTO.setTimPanitia(timPanitiaList.get(0));
				panitiaDTO.setAnggotaPanitiaList(anggotaPanitiaSession.getAnggotaPanitiaByTimPanitiaList(timPanitiaList.get(0).getId()));
				panitiaDTO.setType("Tim");
				
				
			}else if(pejabatPelaksanaPengadaanList.size()!=0){
				panitiaDTO.setNama(pejabatPelaksanaPengadaanList.get(0).getNama());
				panitiaDTO.setPejabatPelaksanaPengadaan(pejabatPelaksanaPengadaanList.get(0));	
				panitiaDTO.setType("Pejabat");
			}
			panitiaDTOList.add(panitiaDTO);
		}
		
		return panitiaDTOList;
	}
	
	

	@Path("/getListRoleUser")
	@GET
	public List<RoleUser> getRoleUserList() {
		return roleUserSession.getRoleUserList();
	}


	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public void insertPanitiaDTO(PanitiaDTO panitiaDTO, @HeaderParam("Authorization") String token) {
		
			if(panitiaDTO.getPejabatPelaksanaPengadaan()==null){
				Panitia panitia = panitiaDTO.getPanitia();
				//insert panitia
				panitia.setCabang(organisasiSession.find(panitiaDTO.getPanitia().getCabang().getId()));
				panitia.setDivisi(organisasiSession.find(panitiaDTO.getPanitia().getDivisi().getId()));		
				panitiaSession.insertPanitia(panitia, tokenSession.findByToken(token));
				
				//insert tim panitia
				TimPanitia timPanitia= panitiaDTO.getTimPanitia();
				timPanitia.setPanitia(panitia);
				timPanitiaSession.insertPanitiaPengadaan(timPanitia, tokenSession.findByToken(token));
				
				//insert anggota panitia
				
				for(AnggotaPanitia anggotaPanitia : panitiaDTO.getAnggotaPanitiaList())
				{
					anggotaPanitia.setTimPanitia(timPanitia);
					anggotaPanitiaSession.insertAnggotaPanitia(anggotaPanitia, tokenSession.findByToken(token));
				}
			}else
			{
				Panitia panitia = panitiaDTO.getPanitia();
				//insert panitia
				panitia.setCabang(organisasiSession.find(panitiaDTO.getPanitia().getCabang().getId()));
				panitia.setDivisi(organisasiSession.find(panitiaDTO.getPanitia().getDivisi().getId()));		
				panitiaSession.insertPanitia(panitia, tokenSession.findByToken(token));
				//insert PejabatPelaksanaPengadaan
				
				PejabatPelaksanaPengadaan pejabatPelaksanaPengadaan=panitiaDTO.getPejabatPelaksanaPengadaan();
				pejabatPelaksanaPengadaan.setPanitia(panitia);
				pejabatPelaksanaPengadaanSession.insertPelaksanaPengadaan(pejabatPelaksanaPengadaan, tokenSession.findByToken(token));
				
			}

	}

	@SuppressWarnings("rawtypes")
	@Path("/update")
	@POST
	public void updatePanitiaDTO(PanitiaDTO panitiaDTO, @HeaderParam("Authorization") String token) {
		
			if(panitiaDTO.getPejabatPelaksanaPengadaan()==null){
				Panitia panitia = panitiaDTO.getPanitia();
				panitia.setCabang(organisasiSession.find(panitiaDTO.getPanitia().getCabang().getId()));
				panitia.setDivisi(organisasiSession.find(panitiaDTO.getPanitia().getDivisi().getId()));
				panitiaSession.updatePanitia(panitia, tokenSession.findByToken(token));
				
				//insert tim panitia
				TimPanitia timPanitia= panitiaDTO.getTimPanitia();
				timPanitia.setPanitia(panitia);
				timPanitiaSession.updatePanitiaPengadaan(timPanitia, tokenSession.findByToken(token));
				
				//insert anggota panitia
				
				for(AnggotaPanitia anggotaPanitia: anggotaPanitiaSession.getAnggotaPanitiaByTimPanitiaList(timPanitia.getId()))
				{
					anggotaPanitiaSession.deleteAnggotaPanitia(anggotaPanitia.getId(), tokenSession.findByToken(token));
				}
				
				for(AnggotaPanitia anggotaPanitia : panitiaDTO.getAnggotaPanitiaList())
				{
					anggotaPanitia.setTimPanitia(timPanitia);
					anggotaPanitia.setId(null);
					anggotaPanitiaSession.insertAnggotaPanitia(anggotaPanitia, tokenSession.findByToken(token));
				}
			}else{
				Panitia panitia = panitiaDTO.getPanitia();
				//insert panitia
				panitia.setCabang(organisasiSession.find(panitiaDTO.getPanitia().getCabang().getId()));
				panitia.setDivisi(organisasiSession.find(panitiaDTO.getPanitia().getDivisi().getId()));		
				panitiaSession.updatePanitia(panitia, tokenSession.findByToken(token));
				//insert PejabatPelaksanaPengadaan
				
				PejabatPelaksanaPengadaan pejabatPelaksanaPengadaan=panitiaDTO.getPejabatPelaksanaPengadaan();
				pejabatPelaksanaPengadaan.setPanitia(panitia);
				pejabatPelaksanaPengadaanSession.updatePelaksanaPengadaan(pejabatPelaksanaPengadaan, tokenSession.findByToken(token));
			}
			
		
		
	}
	
	@Path("/delete")
	@POST
	public Panitia deletePanitia(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		
		List <TimPanitia> timPanitiaList =timPanitiaSession.getPanitiaPengadaanByPanitiaList(id);
		List <PejabatPelaksanaPengadaan> pejabatPelaksanaPengadaanList = pejabatPelaksanaPengadaanSession.getPelaksanaPengadaanByPanitiaList(id);
		
		if(timPanitiaList.size()!=0)
		{
			List<AnggotaPanitia> anggotaPanitiaList= anggotaPanitiaSession.getAnggotaPanitiaByTimPanitiaList(timPanitiaList.get(0).getId());
			
			for(AnggotaPanitia anggotaPanitia:anggotaPanitiaList)
			{
				anggotaPanitiaSession.deleteAnggotaPanitia(anggotaPanitia.getId(), tokenSession.findByToken(token));
			}
			timPanitiaSession.deletePanitiaPengadaan(timPanitiaList.get(0).getId(), tokenSession.findByToken(token));
			
		}
		
		if(pejabatPelaksanaPengadaanList.size()!=0){
			pejabatPelaksanaPengadaanSession.deletePelaksanaPengadaan(pejabatPelaksanaPengadaanList.get(0).getId(), tokenSession.findByToken(token));
		}
		
		panitiaSession.deletePanitia(id, tokenSession.findByToken(token));
		
		return null;
	}

	@Path("/deleteRow/{id}")
	@GET
	public Panitia deleteRowPanitia(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return panitiaSession.deleteRowPanitia(id, tokenSession.findByToken(token));
	}
}
