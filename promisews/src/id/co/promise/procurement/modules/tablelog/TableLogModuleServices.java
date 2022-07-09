package id.co.promise.procurement.modules.tablelog;

import id.co.promise.procurement.audit.TableLogSession;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.scheduler.BackupTableLogSchedulerServices;
import id.co.promise.procurement.security.UserSession;

import java.sql.SQLException;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path(value = "/procurement/modules/tablelog/tableLogModulesServices")
@Produces(MediaType.APPLICATION_JSON)
public class TableLogModuleServices {

	@EJB
	TableLogSession tableLogSession;

	@EJB
	UserSession userSession;
	
	@EJB
	BackupTableLogSchedulerServices backupTableLogSchedulerServices;
	
	@Path("/findMonthAndYear/{month}/{year}/{userId}")
	@GET
	public List<TableLogDTO> findMonthAndYear(@PathParam("userId")int userId, 
			@PathParam("month")int month,
			@PathParam("year")int year) {
//		List<TableLog> tableLogList = new ArrayList<TableLog>();
		try {
			backupTableLogSchedulerServices.moveTableLogToCsv(month, year, userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tableLogSession.findLogByMonthAndYearAndUserId(month, year, userId);
	}
	
	@Path("/getDataIndexTableLogByTableUser")
	@GET
	public List<User> getDataIndexTableLogByTableUser() {
		List<User> listDataUserFromSession = new ArrayList<User>();
		
		listDataUserFromSession = userSession.getDataIndexTableLogByTableUser();
		return listDataUserFromSession;
	}
	
	@Path("/getListTableLogFromFileCsv")
	@POST
	public Response getListTableLogFromFileCsv (
			@FormParam("month")Integer month, 
			@FormParam("year")Integer year, 
			@FormParam("userid")Integer userid, 
			@FormParam("start") Integer start,
			@FormParam("length")Integer length, 
			@FormParam("draw")Integer draw,
			@FormParam("search[value]") String keyword,
			@HeaderParam("Authorization") String token) throws SQLException {
		
		List<TableLogDTO> listTableLog = tableLogSession.readDataFromFileCsv(month, year, userid, length, start, keyword);
		
		Integer size = tableLogSession.readCountDataFromFileCsv(month, year, userid, length, start, keyword);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("draw", draw);
		result.put("data", listTableLog);
		result.put("recordsTotal", size);
		result.put("recordsFiltered", size);
		
		return Response.ok(result).build();
	}
	
	@Path("/getDataTableLogByTableLogId")
	@POST
	public TableLogDTO getDataTableLogByTableLogId(
			@FormParam("month")Integer month, 
			@FormParam("year")Integer year, 
			@FormParam("userid")Integer userid, 
			@FormParam("idTableLog") Integer idTableLog) {
		
		return tableLogSession.readDataFromFileCsvByIdTableLog(month, year, userid, idTableLog);
	}
	
	
	
}
