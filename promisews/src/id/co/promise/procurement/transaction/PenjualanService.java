package id.co.promise.procurement.transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.siacm.Barang;
import id.co.promise.procurement.entity.siacm.Bonus;
import id.co.promise.procurement.entity.siacm.Penjualan;
import id.co.promise.procurement.entity.siacm.PenjualanDTO;
import id.co.promise.procurement.entity.siacm.PenjualanDetail;
import id.co.promise.procurement.master.BarangSession;
import id.co.promise.procurement.master.BonusSession;
import id.co.promise.procurement.security.TokenSession;


@Stateless
@Path(value = "/transaction/penjualan")
@Produces(MediaType.APPLICATION_JSON)
public class PenjualanService {
	@EJB
	PenjualanSession penjualanSession;
	
	@EJB
	PenjualanDetailSession penjualanDetailSession;
	
	@EJB
	BarangSession barangSession;
	
	@EJB
	TokenSession tokenSession;
	
	@EJB
	BonusSession bonusSession;
	
	@Path("/getPenjualanList")
	@GET
	public List<Penjualan> getPenjualanList() {
		return penjualanSession.getpenjualanList();
	}
	@Path("/getLaporanPenjualanList")
	@POST
	public List<Penjualan> getLaporanPenjualanList(
			@HeaderParam("Authorization") String token,
			@FormParam("startDate") Date startDate, 
			@FormParam("endDate") Date endDate) {
		Calendar calender = Calendar.getInstance();
		if (endDate != null) {
			calender.setTime(endDate);
			calender.add(Calendar.DATE, 1);
		}

		Date currentDatePlusOne = calender.getTime();
		return penjualanSession.getLaporanPenjualanList(startDate, currentDatePlusOne);
	}
	@Path("/getListNotSelectedByPenjualanId")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getListNotSelectedByPenjualanId( @HeaderParam("Authorization") String token, Integer penjualanId) {
		List <PenjualanDetail> penjualanDetailList = penjualanDetailSession.getListByPenjualanId(penjualanId);
		List<Integer> selectList= new ArrayList<>();
		for (PenjualanDetail penjualanDetail : penjualanDetailList ) {
			selectList.add(penjualanDetail.getBarang().getId());
		}
		
		List<Barang> barangList = barangSession.getListNotSelect(selectList);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("barangList", barangList);
		map.put("selectList", selectList);
		map.put("penjualanDetailList", penjualanDetailList);

		
		return Response.ok(map).build();
	}
	
	@Path("/getListNotSelectedByIdList")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getListNotSelectedByIdList(List<Integer> selectList, @HeaderParam("Authorization") String token) {
		List<Barang> barangList = barangSession.getListNotSelect(selectList);
		return Response.ok(barangList).build();
	}
	
	
	@SuppressWarnings("deprecation")
	@Path("/getGenerateCode")
	@GET
	public Response getGenerateCode(@HeaderParam("Authorization") String token) {
		Date currentDate = new Date();
    	String pattern = "ddMMyyy";
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    	String date = simpleDateFormat.format(currentDate);
    	String code = "KD"+date+"-"+(currentDate.getHours())+""+(currentDate.getMinutes())+""+(currentDate.getSeconds());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		return Response.ok(map).build();
	}
	
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(PenjualanDTO penjualanDto, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			Penjualan penjualan = penjualanSession.insert(penjualanDto.getPenjualan(), token);
			for(PenjualanDetail penjualanDetail : penjualanDto.getPenjualanDetailList()) {
				Barang barang = new Barang();
				barang = barangSession.getbarang(penjualanDetail.getBarang().getId());
				penjualanDetail.setPenjualan(penjualan);
				penjualanDetail.setHargaBeli(barang.getHargaBeli());
				penjualanDetailSession.insert(penjualanDetail, token);
				barang.setJumlah(barang.getJumlah() - penjualanDetail.getQuantity());
				if(barang != null) {
					barangSession.update(barang, token);					
				}
			}
			Bonus bonusKepalaMekanik = new Bonus();
			Bonus bonusMekanik = new Bonus();
			Bonus bonusPembantuMekanik = new Bonus();
			if(penjualan.getKaryawan() != null) {
				bonusMekanik.setPenjualan(penjualan);
				bonusMekanik.setKaryawan(penjualan.getKaryawan());
				bonusMekanik.setPersentase(new Double(3.0));
				bonusMekanik.setNilaiBonus(penjualan.getHargaSetelahDiskon()*(bonusMekanik.getPersentase()/100));//3%
				bonusSession.insert(bonusMekanik, token);
			}
			if(penjualan.getKepalaMekanik() != null) {
				bonusKepalaMekanik.setPenjualan(penjualan);
				bonusKepalaMekanik.setKaryawan(penjualan.getKepalaMekanik());
				bonusKepalaMekanik.setPersentase(new Double(5.0));
				bonusKepalaMekanik.setNilaiBonus(penjualan.getHargaSetelahDiskon()*(bonusKepalaMekanik.getPersentase()/100));//5%
				bonusSession.insert(bonusKepalaMekanik, token);
			}
			if(penjualan.getPerbantuan() != null) {
				bonusPembantuMekanik.setPenjualan(penjualan);
				bonusPembantuMekanik.setKaryawan(penjualan.getPerbantuan());
				bonusPembantuMekanik.setPersentase(new Double(2.5));
				bonusPembantuMekanik.setNilaiBonus(penjualan.getHargaSetelahDiskon()*(bonusPembantuMekanik.getPersentase()/100));//2.5%
				bonusSession.insert(bonusPembantuMekanik, token);
			}
			return Response.ok(penjualanDto).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
}
