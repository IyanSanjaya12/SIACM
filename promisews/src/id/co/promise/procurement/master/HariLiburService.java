package id.co.promise.procurement.master;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import id.co.promise.procurement.entity.HariLibur;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.Constant;

@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
@Path(value = "/procurement/master/hari-libur")
@Produces(MediaType.APPLICATION_JSON)
public class HariLiburService {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@EJB
	private HariLiburSession hariLiburSession;
	@EJB
	TokenSession tokenSession;
	
	@EJB ProcedureSession procedureSession;
	
	@Resource private UserTransaction userTransaction;

	@Path("/get-list")
	@GET
	public List<HariLibur> getHariLiburList() {
		return hariLiburSession.getHariLiburList();
	}
	
	@Path("/get-list-by-id/{id}")
	@GET
	public List<HariLibur> getHariLiburListById(@PathParam("id")Integer id){
		return hariLiburSession.getHariLiburListById(id);
	}

	/* /insert lama
	@Path("/insertHariLibur")
	@POST
	public HariLibur insertHariLibur(@FormParam("nama") String nama,
			@FormParam("deskripsi") String deskripsi,
			@FormParam("tanggal") String tanggal,
			@HeaderParam("Authorization") String token// yyyy-MM-DD
	) {
		HariLibur hariLibur = new HariLibur();
		hariLibur.setNama(nama);
		hariLibur.setDeskripsi(deskripsi);
		try {
			hariLibur.setTanggal(sdf.parse(tanggal));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return hariLiburSession.createHariLibur(hariLibur,
				tokenSession.findByToken(token));
	}

	//update lama
	@Path("/updateHariLibur")
	@POST
	public HariLibur updateHariLibur(@FormParam("id") int id,
			@FormParam("nama") String nama,
			@FormParam("deskripsi") String deskripsi,
			@FormParam("tanggal") String tanggal // yyyy-MM-DD
			, @HeaderParam("Authorization") String token) {

		HariLibur hariLibur = hariLiburSession.getHariLiburById(id);
		hariLibur.setNama(nama);
		hariLibur.setDeskripsi(deskripsi);
		try {
			hariLibur.setTanggal(sdf.parse(tanggal));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return hariLiburSession.editHariLibur(hariLibur,
				tokenSession.findByToken(token));
	}
	 */
	
	//insert tambahan untuk master hari libur
	
	@SuppressWarnings({"rawtypes"})
	@Path("/insert")
	@POST
	public Map insert(HariLibur harilibur,
			@HeaderParam("Authorization") String token) throws Exception{
		  
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";
		Boolean isSaveNama = hariLiburSession.checkNamaHariLibur(harilibur.getNama(), toDo, harilibur.getId());
		userTransaction.begin();
		if(!isSaveNama) {
			String errorNama = "Nama Hari Libur Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}
	 
		if(isSaveNama) {
			hariLiburSession.insertHariLibur(harilibur, tokenSession.findByToken(token));
			userTransaction.commit();
			procedureSession.execute(Constant.DO_SYNC_HOLIDAY, harilibur.getId());
		}
	  
		map.put("hariLibur", harilibur);
	  
		return map;
	}
	
	//update tambahan  untuk master hari libur
	@SuppressWarnings({"rawtypes"})
	@Path("/update")
	@POST
	public Map update(HariLibur harilibur,
			@HeaderParam("Authorization") String token) throws Exception {
	  
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";

		
		Boolean isSaveNama = hariLiburSession.checkNamaHariLibur(harilibur.getNama(), toDo, harilibur.getId());
		userTransaction.begin();
		if(!isSaveNama) {
			String errorNama = "Nama Hari Libur Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}
	 
		if(isSaveNama) {
			hariLiburSession.updateHariLibur(harilibur, tokenSession.findByToken(token));
			userTransaction.commit();
			procedureSession.execute(Constant.DO_SYNC_HOLIDAY, harilibur.getId());
		}
	  
		map.put("hariLibur", harilibur);
	  
		return map;
	}
	
	// delete untuk master hari libur
	@Path("/delete")
	@POST
	public HariLibur deleteHariLibur(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return hariLiburSession.deleteHariLibur(id, tokenSession.findByToken(token));
	}
	
	//get harilibur by date
	/*@Path("/getHariLiburByDate/{tanggal}")
	@GET
	public List<HariLibur> getHariLiburByDate(@PathParam("tanggal")String tanggal){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Date dateObj = null;
		try {
			dateObj = sdf.parse(tanggal);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hariLiburSession.getHariLiburByDate(dateObj);
	}
	
	
	@Path("/getHariLiburByNama/{nama}")
	@GET
	public HariLibur getHariLiburByNama(@PathParam("nama") String nama) {
		return hariLiburSession.getHariLiburByNama(nama);
	}*/
	
	@Path("/get-by-pagination")
	@POST 
	public Response getHariLiburByPagination(
			@FormParam("search[value]") String keyword,
			@FormParam("start") Integer start,
			@FormParam("length") Integer length,
			@FormParam("draw") Integer draw,
			@FormParam("order[0][column]") Integer columnOrder,
			@FormParam("order[0][dir]") String tipeOrder,
			@HeaderParam("Authorization") String token
			){
		Token objToken = tokenSession.findByToken(token);
		//Integer userId = objToken.getUser().getId();
		
		String tempKeyword = "%" + keyword + "%";
		long countData = hariLiburSession.getHariLiburListCount(tempKeyword);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("draw", draw);
		result.put("recordsTotal", countData);
		result.put("recordsFiltered", countData);		
		result.put("data", hariLiburSession.getHariLiburListWithPagination(start, length, tempKeyword, columnOrder, tipeOrder));
		
		return Response.ok(result).build();
	}

}
