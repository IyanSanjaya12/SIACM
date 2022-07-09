/*
* File: DashboardVendorServices.java
* This class is created to handle all processing involved
* in a shopping cart.
*
* Copyright (c) 2017 Mitra Mandiri Informatika
* Jl. Tebet Raya no. 11 B Jakarta Selatan
* All Rights Reserved.
*
* This software is the confidential and proprietary
* information of Mitra Mandiri Informatika ("Confidential
* Information").
*
* You shall not disclose such Confidential Information and
* shall use it only in accordance with the terms of the
* license agreement you entered into with MMI.
*
* Date 				Author 			Version 	Changes
* Feb 1, 2017 3:07:28 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.dashboard;

import id.co.promise.procurement.alokasianggaran.AlokasiAnggaranSession;
import id.co.promise.procurement.alokasianggaran.DashboardAnggaranDTO;
import id.co.promise.procurement.entity.AlokasiAnggaran;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.security.TokenSession;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Mamat
 *
 */

@Stateless
@Path(value = "/procurement/DashboardAnggaranServices")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardAnggaranServices {
	@EJB
	private AlokasiAnggaranSession alokasiAnggaranSession;
	@EJB
	private TokenSession tokenSession;
	
	@Path("/getAllAnggaran")
	@GET
	public Response getAllAnggaran(@HeaderParam("Authorization")String token){
		
		Map<String, Object> result = new HashMap<String, Object>();
		Token objToken = tokenSession.findByToken(token);
		int userId = objToken.getUser().getId();
		List<AlokasiAnggaran> list = alokasiAnggaranSession.getList(userId);
		BigDecimal plannedBudget 	= BigDecimal.ZERO;
		BigDecimal availableBudget 	= BigDecimal.ZERO;
		BigDecimal usedBudget 		= BigDecimal.ZERO;
		BigDecimal bookedBudget 	= BigDecimal.ZERO;
		
		if(list !=null && list.size() > 0){
			
			
			for(AlokasiAnggaran anggaran : list){
				if(anggaran.getJumlah() !=null){
					plannedBudget = plannedBudget.add(new BigDecimal(anggaran.getJumlah(),MathContext.DECIMAL64)).setScale(2, RoundingMode.HALF_EVEN); //set planned budget.
				}
				if(anggaran.getSisaAnggaran() !=null){
					availableBudget = availableBudget.add(new BigDecimal(anggaran.getSisaAnggaran(),MathContext.DECIMAL64)).setScale(2, RoundingMode.HALF_EVEN); //set available budget.
				}
				if(anggaran.getBookingAnggaran() != null){
					bookedBudget = bookedBudget.add(new BigDecimal(anggaran.getBookingAnggaran(),MathContext.DECIMAL64)).setScale(2, RoundingMode.HALF_EVEN); //set booked budget.
				}
				
			}
			
			/*perhitungan awal*/
			//usedBudget = plannedBudget.subtract(availableBudget).setScale(2);
			
			/*booked budget mempengaruhi usedBudget*/
			usedBudget = (plannedBudget.subtract(availableBudget).setScale(2)).subtract(bookedBudget).setScale(2, RoundingMode.HALF_EVEN); //terpakai=(total-sisa)-booked
			
		
		}
		result.put("plannedBudget", plannedBudget);
		result.put("bookedBudget", bookedBudget);
		result.put("usedBudget",usedBudget);
		result.put("availableBudget",availableBudget);
		return Response.ok(result).build();
	}
	
	@Path("/getAnggaranByYear")
	@GET
	public Response getAnggaranByYear(@HeaderParam("Authorization")String token){
		
		Token objToken = tokenSession.findByToken(token);
		int userId = objToken.getUser().getId();
		
		List<DashboardAnggaranDTO> list = alokasiAnggaranSession.getAnggaranByYear(userId);
		
		Map<String, Object> map1 	  = new HashMap<String, Object>();
		List<Object> listData1 		  = new ArrayList<Object>();
		
		Map<String, Object> map2 	  = new HashMap<String, Object>();
		List<Object> listData2 		  = new ArrayList<Object>();
		
		Map<String, Object> map3 	  = new HashMap<String, Object>();
		List<Object> listData3 		  = new ArrayList<Object>();
		
		Map<String, Object> map4 	  = new HashMap<String, Object>();
		List<Object> listData4 		  = new ArrayList<Object>();
		
	
		for(DashboardAnggaranDTO dashboard : list){
			SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
			String dateInString = "01-01-"+dashboard.getYear()+" 00:00:00";
			Date date;
			try {
				date = sdf.parse(dateInString);
				long time = date.getTime();
				List<Object> listTotal = new ArrayList<Object>();
				listTotal.add(time+5000000000L*3/2);
				listTotal.add(dashboard.getPlannedBudget());
				listData1.add(listTotal);
				
				List<Object> listBooked = new ArrayList<Object>();
				listBooked.add(time+5000000000L*1/2);
				listBooked.add(dashboard.getBookedBudget());
				listData2.add(listBooked);
				
				List<Object> listUsed = new ArrayList<Object>();
				listUsed.add(time-5000000000L*1/2);
				listUsed.add(dashboard.getUsedBudget());
				listData3.add(listUsed);
				
				List<Object> listAvailable = new ArrayList<Object>();
				listAvailable.add(time-5000000000L*3/2);
				listAvailable.add(dashboard.getAvailableBudget());
				listData4.add(listAvailable);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.serverError().build();
			}
			
			
		}
		
		map1.put("label", "Total Anggaran");
		map1.put("color", "#4caf50");
		map1.put("data", listData1);
		
		map2.put("label", "Booked");
		map2.put("color", "#00a65a");
		map2.put("data", listData2);
		
		map3.put("label", "Terpakai");
		map3.put("color", "#fe9700");
		map3.put("data", listData3);
		
		map4.put("label", "Sisa");
		map4.put("color", "#f34235");
		map4.put("data", listData4);
		
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		listData.add(map1);
		listData.add(map2);
		listData.add(map3);
		listData.add(map4);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", listData);
		result.put("minyear", list.get(0).getYear());
		result.put("maxyear", list.get(list.size()-1).getYear());		
		
		return Response.ok(result).build();
		
		
	}
	
	@Path("/getAnggaranByMonth")
	@GET
	public Response getAnggaranByMonth(@HeaderParam("Authorization")String token){
		
		Token objToken = tokenSession.findByToken(token);
		int userId = objToken.getUser().getId();
		
		List<DashboardAnggaranDTO> list = alokasiAnggaranSession.getAnggaranByMonth(userId);
		
		Map<String, Object> map1 	  = new HashMap<String, Object>();
		List<Object> listData1 		  = new ArrayList<Object>();
		
		Map<String, Object> map2 	  = new HashMap<String, Object>();
		List<Object> listData2 		  = new ArrayList<Object>();
		
		Map<String, Object> map3 	  = new HashMap<String, Object>();
		List<Object> listData3 		  = new ArrayList<Object>();
		
		Map<String, Object> map4 	  = new HashMap<String, Object>();
		List<Object> listData4 		  = new ArrayList<Object>();
		
	
		for(DashboardAnggaranDTO dashboard : list){
			SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
			String dateInString = "01-"+dashboard.getMonth()+"-"+dashboard.getYear()+" 00:00:00";
			Date date;
			try {
				date = sdf.parse(dateInString);
				long time = date.getTime();
				List<Object> listTotal = new ArrayList<Object>();
				//listTotal.add(time+5000000000L*3/2);
				listTotal.add(dashboard.getPlannedBudget());
				listData1.add(listTotal);
				
				List<Object> listBooked = new ArrayList<Object>();
				//listBooked.add(time+5000000000L*1/2);
				listBooked.add(dashboard.getBookedBudget());
				listData2.add(listBooked);
				
				List<Object> listUsed = new ArrayList<Object>();
				//listUsed.add(time-5000000000L*1/2);
				listUsed.add(dashboard.getUsedBudget());
				listData3.add(listUsed);
				
				List<Object> listAvailable = new ArrayList<Object>();
				//listAvailable.add(time-5000000000L*3/2);
				listAvailable.add(dashboard.getAvailableBudget());
				listData4.add(listAvailable);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.serverError().build();
			}
			
			
		}
		
		map1.put("label", "Total Anggaran");
		map1.put("color", "#4caf50");
		map1.put("data", listData1);
		
		map2.put("label", "Booked");
		map2.put("color", "#00a65a");
		map2.put("data", listData2);
		
		map3.put("label", "Terpakai");
		map3.put("color", "#fe9700");
		map3.put("data", listData3);
		
		map4.put("label", "Sisa");
		map4.put("color", "#f34235");
		map4.put("data", listData4);
		
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		listData.add(map1);
		listData.add(map2);
		listData.add(map3);
		listData.add(map4);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", listData);
		result.put("minyear", list.get(0).getYear());
		result.put("minmonth", list.get(0).getMonth());
		result.put("maxyear", list.get(list.size()-1).getYear());
		result.put("maxmonth", list.get(list.size()-1).getMonth());		
		
		return Response.ok(result).build();
		
		
	}
	
	@Path("/getAnggaranByWeek")
	@GET
	public Response getAnggaranByWeek(@HeaderParam("Authorization")String token){
		
		Token objToken = tokenSession.findByToken(token);
		int userId = objToken.getUser().getId();
		
		List<DashboardAnggaranDTO> list = alokasiAnggaranSession.getAnggaranByWeek(userId);
		
		Map<String, Object> map1 	  = new HashMap<String, Object>();
		List<Object> listData1 		  = new ArrayList<Object>();
		
		Map<String, Object> map2 	  = new HashMap<String, Object>();
		List<Object> listData2 		  = new ArrayList<Object>();
		
		Map<String, Object> map3 	  = new HashMap<String, Object>();
		List<Object> listData3 		  = new ArrayList<Object>();
		
		Map<String, Object> map4 	  = new HashMap<String, Object>();
		List<Object> listData4 		  = new ArrayList<Object>();
		
	
		for(DashboardAnggaranDTO dashboard : list){
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-ww-u");
			String dateInString = ""+dashboard.getYear()+"-"+(dashboard.getWeek()+1)+"-1";
			Date date;
			try {
				date = sdf.parse(dateInString);
				long time = date.getTime();
				List<Object> listTotal = new ArrayList<Object>();
				//listTotal.add(time+5000000000L*3/2);
				listTotal.add(dashboard.getPlannedBudget());
				listData1.add(listTotal);
				
				List<Object> listBooked = new ArrayList<Object>();
				//listBooked.add(time+5000000000L*1/2);
				listBooked.add(dashboard.getBookedBudget());
				listData2.add(listBooked);
				
				List<Object> listUsed = new ArrayList<Object>();
				//listUsed.add(time-5000000000L*1/2);
				listUsed.add(dashboard.getUsedBudget());
				listData3.add(listUsed);
				
				List<Object> listAvailable = new ArrayList<Object>();
				//listAvailable.add(time-5000000000L*3/2);
				listAvailable.add(dashboard.getAvailableBudget());
				listData4.add(listAvailable);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.serverError().build();
			}
			
			
		}
		
		map1.put("label", "Total Anggaran");
		map1.put("color", "#4caf50");
		map1.put("data", listData1);
		
		map2.put("label", "Booked");
		map2.put("color", "#00a65a");
		map2.put("data", listData2);
		
		map3.put("label", "Terpakai");
		map3.put("color", "#fe9700");
		map3.put("data", listData3);
		
		map4.put("label", "Sisa");
		map4.put("color", "#f34235");
		map4.put("data", listData4);
		
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		listData.add(map1);
		listData.add(map2);
		listData.add(map3);
		listData.add(map4);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-ww-u");
		List<Long> times = new ArrayList<Long>();
		String dateMinInString = ""+list.get(0).getYear()+"-"+(list.get(0).getWeek()+1)+"-1";
		String dateMaxInString = ""+list.get(list.size()-1).getYear()+"-"+(list.get(list.size()-1).getWeek()+1)+"-1";
		Date date;
		for (DashboardAnggaranDTO dashboard : list){
			try {
				date = sdf.parse(dashboard.getYear()+"-"+(dashboard.getWeek()+1)+"-1");
				times.add(date.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			date = sdf.parse(dateMinInString);
			result.put("mindate", date.getTime());
			date = sdf.parse(dateMaxInString);
			result.put("maxdate", date.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result.put("times", times);
		result.put("data", listData);	
		
		return Response.ok(result).build();
		
	}
	
	
	
}
