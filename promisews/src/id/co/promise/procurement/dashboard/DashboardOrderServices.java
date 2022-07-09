package id.co.promise.procurement.dashboard;

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

import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@Path(value = "/procurement/DashboardOrderServices")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardOrderServices {
	
	@EJB
	TokenSession tokenSession;
	@EJB
	VendorSession vendorSession;
	
	@Path("/countByMonth")
	@GET
	public Response countByMonth(@HeaderParam("Authorization") String token) {
		
		List<DashboardOrderDTO> list = vendorSession.countByMonth(tokenSession.findByToken(token));
		Map<String, Object> map1 	  = new HashMap<String, Object>();
		List<Object> listData1 		  = new ArrayList<Object>();
		
		Map<String, Object> map2 	  = new HashMap<String, Object>();
		List<Object> listData2 		  = new ArrayList<Object>();
		
		for(DashboardOrderDTO dashboard : list){
			SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
			String dateInString = "01-"+dashboard.getMonth()+"-"+dashboard.getYear()+" 00:00:00";
			Date date;
			try {
				date = sdf.parse(dateInString);
				long time = date.getTime();
				List<Object> listVendor = new ArrayList<Object>();
				listVendor.add(time-5 * 24 * 60 * 60 * 1000L*1);
				listVendor.add(dashboard.getVendor());
				listData1.add(listVendor);
				
				List<Object> listCalonVendor = new ArrayList<Object>();
				listCalonVendor.add(time+5 * 24 * 60 * 60 * 1000L*1);
				listCalonVendor.add(dashboard.getCalonVendor());
				listData2.add(listCalonVendor);
				
			
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.serverError().build();
			}
			
			
		}
		
		map1.put("label", "Vendor");
		map1.put("color", "#8d1c9e");
		map1.put("data", listData1);
		
		map2.put("label", "Calon Vendor");
		map2.put("color", "#6639b6");
		map2.put("data", listData2);
		
	
		
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		listData.add(map1);
		listData.add(map2);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", listData);
		result.put("minyear", list.get(0).getYear());
		result.put("minmonth", list.get(0).getMonth());
		result.put("maxyear", list.get(list.size()-1).getYear());
		result.put("maxmonth", list.get(list.size()-1).getMonth());		
		
		return Response.ok(result).build();
	}
	
	@Path("/countByYear")
	@GET
	public Response countByYear(@HeaderParam("Authorization") String token) {
		
		List<DashboardOrderDTO> list = vendorSession.countByYear(tokenSession.findByToken(token));
		Map<String, Object> map1 	  = new HashMap<String, Object>();
		List<Object> listData1 		  = new ArrayList<Object>();
		
		Map<String, Object> map2 	  = new HashMap<String, Object>();
		List<Object> listData2 		  = new ArrayList<Object>();
		
		for(DashboardOrderDTO dashboard : list){
			SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
			String dateInString = "01-01-"+dashboard.getYear()+" 00:00:00";
			Date date;
			try {
				date = sdf.parse(dateInString);
				long time = date.getTime();
				List<Object> listVendor = new ArrayList<Object>();
				listVendor.add(time-2 * 30 * 24 * 60 * 60 * 1000L*1);
				listVendor.add(dashboard.getVendor());
				listData1.add(listVendor);
				
				List<Object> listCalonVendor = new ArrayList<Object>();
				listCalonVendor.add(time+2 * 30 * 24 * 60 * 60 * 1000L*1);
				listCalonVendor.add(dashboard.getCalonVendor());
				listData2.add(listCalonVendor);
				
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.serverError().build();
			}
			
			
		}
		
		map1.put("label", "Vendor");
		map1.put("color", "#8d1c9e");
		map1.put("data", listData1);
		
		map2.put("label", "Calon Vendor");
		map2.put("color", "#6639b6");
		map2.put("data", listData2);
		
	
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		listData.add(map1);
		listData.add(map2);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", listData);
		result.put("minyear", list.get(0).getYear());
		result.put("maxyear", list.get(list.size()-1).getYear());	
		
		return Response.ok(result).build();
	}
	
	@Path("/countByWeek")
	@GET
	public Response countByWeek(@HeaderParam("Authorization") String token) {
		
		List<DashboardOrderDTO> list = vendorSession.countByWeek(tokenSession.findByToken(token));
		Map<String, Object> map1 	  = new HashMap<String, Object>();
		List<Object> listData1 		  = new ArrayList<Object>();
		
		Map<String, Object> map2 	  = new HashMap<String, Object>();
		List<Object> listData2 		  = new ArrayList<Object>();
		
		for(DashboardOrderDTO dashboard : list){
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-ww-u");
			String dateInString = ""+dashboard.getYear()+"-"+(dashboard.getWeek()+1)+"-1";
			Date date;
			try {
				date = sdf.parse(dateInString);
				long time = date.getTime();
				List<Object> listVendor = new ArrayList<Object>();
				listVendor.add(time- 24 * 60 * 60 * 1000L*1);
				listVendor.add(dashboard.getVendor());
				listData1.add(listVendor);
				
				List<Object> listCalonVendor = new ArrayList<Object>();
				listCalonVendor.add(time+24 * 60 * 60 * 1000L*1);
				listCalonVendor.add(dashboard.getCalonVendor());
				listData2.add(listCalonVendor);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.serverError().build();
			}
			
			
		}
		
		map1.put("label", "Vendor");
		map1.put("color", "#8d1c9e");
		map1.put("data", listData1);
		
		map2.put("label", "Calon Vendor");
		map2.put("color", "#6639b6");
		map2.put("data", listData2);
		
		
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		listData.add(map1);
		listData.add(map2);
		
		Map<String, Object> result = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-ww-u");
		List<Long> times = new ArrayList<Long>();
		String dateMinInString = ""+list.get(0).getYear()+"-"+(list.get(0).getWeek()+1)+"-1";
		String dateMaxInString = ""+list.get(list.size()-1).getYear()+"-"+(list.get(list.size()-1).getWeek()+1)+"-1";
		Date date;
		for (DashboardOrderDTO dashboard : list){
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
	
	@Path("/countApprovalVendor")
	@GET
	public Response countApprovalVendor(@HeaderParam("Authorization") String token) {
		
		Map<String, Long> result = new HashMap<String, Long>();
		
		result = vendorSession.countStatusApproval(tokenSession.findByToken(token));
		return Response.ok(result).build();
	}
	
	@Path("/countApprovalByUser")
	@GET
	public Response countApprovalByUser(@HeaderParam("Authorization") String token) {
		
		Map<String, Integer> result = new HashMap<String, Integer>();
		
		result = vendorSession.countApprovalByUser(tokenSession.findByToken(token));
		return Response.ok(result).build();
	}
	
	@Path("/countVendor")
	@GET
	public Response countVendor() {
		
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("vendor",  vendorSession.countByStatus(1));
		result.put("calon",  vendorSession.countByStatus(0));
		return Response.ok(result).build();
	}
}
