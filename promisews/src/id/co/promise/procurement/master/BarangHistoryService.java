package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.siacm.Barang;
import id.co.promise.procurement.entity.siacm.BarangHistory;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/master/barangHistory")
@Produces(MediaType.APPLICATION_JSON)
public class BarangHistoryService {
	@EJB
	BarangHistorySession barangHistorySession;
	@EJB
	TokenSession tokenSession;
	@EJB
	BarangSession barangSession;
	
	@Path("/getBarangHistoryList")
	@GET
	public List<BarangHistory> getBarangHistoryList() {
		return barangHistorySession.getbarangHistoryList();
	}
	
	@Path("/getBarangHistoryRevisiList")
	@GET
	public List<BarangHistory> getBarangHistoryRevisiList() {
		return barangHistorySession.getBarangHistoryRevisiList();
	}
	
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(BarangHistory barangHistory, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			Barang barang = new Barang();
			if(barangHistory.getStatus() == 1) {//approve
				barang.setHarga(barangHistory.getHarga());
				barang.setHargaBeli(barangHistory.getHargaBeli());
				barang.setJumlah(barangHistory.getJumlah());
				barang.setKode(barangHistory.getKode());
				barang.setMobil(barangHistory.getMobil());
				barang.setNama(barangHistory.getNama());
				barang.setStokMinimal(barangHistory.getStokMinimal());
				barang.setStatusBarang(barangHistory.getStatusBarang());
				if(barangHistory.getBarang() != null) {
					barang.setId(barangHistory.getBarang().getId());
					barang.setIsApproval(0);//approved
					barangSession.update(barang, token);
				}else {
					barangSession.insert(barang, token);
				}
				//delete history
				
				barangHistorySession.delete(barangHistory.getId(), token);
			}else {//revisi
				if(barangHistory.getBarang()!=null) {
					barang = barangHistory.getBarang();
					barang.setIsApproval(2);//revisi
					barangSession.update(barang, token);
				}
				barangHistorySession.update(barangHistory, token);
			}
			
			return Response.ok(barangHistory).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(BarangHistory barangHistory, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			barangHistorySession.delete(barangHistory.getId(), token);
			
			return Response.ok(barangHistory).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
