package id.co.promise.procurement.master;

import java.util.Date;
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.Constant;

@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
@Path(value = "/procurement/master/satuan")
@Produces(MediaType.APPLICATION_JSON)
public class SatuanService {

	@EJB
	private SatuanSession satuanSession;

	@EJB
	TokenSession tokenSession;
	
	@EJB ProcedureSession procedureSession;
	
	@Resource private UserTransaction userTransaction;

	@Path("/get-list")
	@GET
	public List<Satuan> getList() {
		return satuanSession.getSatuanList();
	}


	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(Satuan satuan, @HeaderParam("Authorization") String token) throws Exception, Throwable {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";

		Boolean isSaveNama = satuanSession.checkNamaSatuan(satuan.getNama(), toDo, satuan.getId());
		userTransaction.begin();

		if (!isSaveNama) {
			String errorNama = "Nama Satuan Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (isSaveNama) {
			satuan.setCreated(new Date());
			satuan.setUserId(0);
			satuanSession.insertSatuan(satuan, tokenSession.findByToken(token));
			userTransaction.commit();
			procedureSession.execute(Constant.DO_SYNC_UNIT, satuan.getId());	

		}

		map.put("satuan", satuan);
		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/update")
	@POST
	public Map update(Satuan satuan, @HeaderParam("Authorization") String token) throws Throwable, Exception {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";

		Boolean isSaveNama = satuanSession.checkNamaSatuan(satuan.getNama(), toDo, satuan.getId());
		userTransaction.begin();
		
		if (!isSaveNama) {
			String errorNama = "Nama Satuan Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (isSaveNama) {
			satuan.setUpdated(new Date());
			satuanSession.updateSatuan(satuan, tokenSession.findByToken(token));
			userTransaction.commit();
			procedureSession.execute(Constant.DO_SYNC_UNIT, satuan.getId());	
		}

		map.put("satuan", satuan);
		return map;
	}

	@Path("/delete")
	@POST
	public Satuan delete(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return satuanSession.deleteSatuan(id, tokenSession.findByToken(token));
	}

}
