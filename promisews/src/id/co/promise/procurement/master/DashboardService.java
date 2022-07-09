package id.co.promise.procurement.master;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.catalog.entity.Category;
import id.co.promise.procurement.catalog.session.CategorySession;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.SubBidangUsaha;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.Constant;

@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
@Path(value = "/procurement/dashboard/")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardService {

	@EJB
	private SatuanSession satuanSession;

	@EJB
	TokenSession tokenSession;
	
	@EJB ProcedureSession procedureSession;
	
	@Resource private UserTransaction userTransaction;
	
	@EJB
	CategorySession categorySession;
	
	@EJB
	DashboardSession dashboardSession;
	
	@EJB
	SubBidangUsahaSession subBidangUsahaSession;
	
	@EJB
	HariLiburSession hariLiburSession;
	
	@EJB
	RoleUserSession roleUserSession;

	@SuppressWarnings({ "rawtypes", "unused" })
	@Path("firstLoad")
	@GET
	public Map firstLoad(@HeaderParam("Authorization") String token) throws ParseException {
		Date endDatePlus21 =hariLiburSession.getTotalWorkingDays(21);
		Integer year = Calendar.getInstance().get(Calendar.YEAR);
		String pattern = "dd/MM/yyyy";
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String firstDateStr = "01/01/"+year;
		Date firstDate=simpleDateFormat.parse(firstDateStr);
		List<Integer> categoryIdList = new ArrayList<Integer>();
		List<Integer> subBidangUsahaIdList = new ArrayList<Integer>();
		List<Category> categoryList = categorySession.getActiveCategory();
		List<SubBidangUsaha> SBUList = subBidangUsahaSession.getActiveSubBidangUsaha();
		Integer a = 0;
		Integer b = 0;
		for(Category cat :categoryList){
			if(a < 10) {
				categoryIdList.add(cat.getId());
				a++;
			}
		}
		for(SubBidangUsaha SBU :SBUList){
			if(b < 10) {
				subBidangUsahaIdList.add(SBU.getId());
				b++;
			}
		}
		
		DashboardParamDTO dashboardParamDTO =new DashboardParamDTO();
		/*perubahan KAI 25 Januari 2021 [21377]*/
		boolean isVendor = false;
		dashboardParamDTO.setDataIdList(categoryIdList);
		Token tokenObj = tokenSession.findByToken(token);
		RoleUser roleUser = roleUserSession.getByToken(tokenObj);
		if(roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			isVendor = true;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("SBUList", SBUList);
		map.put("categoryList", categoryList);
		map.put("monitoringCatalog",dashboardSession.getItemCatalogReport(null, null, null, categoryIdList ));
		map.put("monitoringVendor",dashboardSession.getVendorReport(null, null, subBidangUsahaIdList));
		map.put("monitoringPembelian",dashboardSession.getPembelianPerPeriodeReport(null, year));
		map.put("monitoringKinerjaPenyedia",dashboardSession.getKinerjaPenyediaReport());
		map.put("monitoringProsesPembelian", dashboardSession.getProsesPembelianReport(null,firstDate, new Date()));
		map.put("monitoringMasaKontrakCatalog", dashboardSession.getMasaKontrakCatalogReport(null, endDatePlus21, null));
		map.put("isVendor", isVendor);/*perubahan KAI 25 Januari 2021 [21377]*/
		return map;
	}
	
	@Path("getFilter")
	@POST
	public Map getFilter(DashboardParamDTO dashboardParamDTO) throws ParseException {
		Date endDatePlus21 =hariLiburSession.getTotalWorkingDays(21);
		Integer year = Calendar.getInstance().get(Calendar.YEAR); 
		if (dashboardParamDTO.getYear() != null) {
			year = Integer.parseInt(dashboardParamDTO.getYear());
		}
		String pattern = "dd/MM/yyyy";
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String firstDateStr = "01/01/"+year;
		String lastDateStr = "31/12/"+year;
		Date firstDate=simpleDateFormat.parse(firstDateStr);
		Date lastDate=simpleDateFormat.parse(lastDateStr);
		List<Integer> categoryIdList = new ArrayList<Integer>();
		List<Integer> subBidangUsahaIdList = new ArrayList<Integer>();
		List<Category> categoryList = categorySession.getActiveCategory();
		List<SubBidangUsaha> SBUList = subBidangUsahaSession.getActiveSubBidangUsaha();
		Integer a = 0;
		Integer b = 0;
		for(Category cat :categoryList){
			if(a < 10) {
				categoryIdList.add(cat.getId());
				a++;
			}
		}
		for(SubBidangUsaha SBU :SBUList){
			if(b < 10) {
				subBidangUsahaIdList.add(SBU.getId());
				b++;
			}
		}
		
		//DashboardParamDTO dashboardParamDTO =new DashboardParamDTO();
		dashboardParamDTO.setDataIdList(categoryIdList);
		
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("SBUList", SBUList);
		map.put("categoryList", categoryList);
		map.put("monitoringCatalog",dashboardSession.getItemCatalogReport(firstDate, lastDate, dashboardParamDTO.getOrganizationId(), categoryIdList ));
		map.put("monitoringVendor",dashboardSession.getVendorReport(firstDate, lastDate, subBidangUsahaIdList));
		map.put("monitoringPembelian",dashboardSession.getPembelianPerPeriodeReport(dashboardParamDTO.getOrganizationId(), year));
		map.put("monitoringKinerjaPenyedia",dashboardSession.getKinerjaPenyediaReport());
		map.put("monitoringProsesPembelian", dashboardSession.getProsesPembelianReport(dashboardParamDTO.getOrganizationId(),firstDate, lastDate));
		map.put("monitoringMasaKontrakCatalog", dashboardSession.getMasaKontrakCatalogReport(null, endDatePlus21, dashboardParamDTO.getOrganizationId()));
		return map;
	}
	
	
	@Path("cekDataByYear")
	@POST
	public Map cekDataByYear(List<String> yearList) throws ParseException {
		
		List<String> yearListNotEmptyData = new ArrayList();
		for (String years : yearList) {
		Boolean isEmpty = true ;	
		
		Date endDatePlus21 =hariLiburSession.getTotalWorkingDays(21);
		Integer year = Calendar.getInstance().get(Calendar.YEAR); 
		year = Integer.parseInt(years);

		String pattern = "dd/MM/yyyy";
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String firstDateStr = "01/01/"+year;
		String lastDateStr = "31/12/"+year;
		Date firstDate=simpleDateFormat.parse(firstDateStr);
		Date lastDate=simpleDateFormat.parse(lastDateStr);
		List<Integer> categoryIdList = new ArrayList<Integer>();
		List<Integer> subBidangUsahaIdList = new ArrayList<Integer>();
		List<Category> categoryList = categorySession.getActiveCategory();
		List<SubBidangUsaha> SBUList = subBidangUsahaSession.getActiveSubBidangUsaha();
		Integer a = 0;
		Integer b = 0;
		for(Category cat :categoryList){
			if(a < 10) {
				categoryIdList.add(cat.getId());
				a++;
			}
		}
		for(SubBidangUsaha SBU :SBUList){
			if(b < 10) {
				subBidangUsahaIdList.add(SBU.getId());
				b++;
			}
		}
		
		 
		DashboardDTO dashboardItemCatalogReport = dashboardSession.getItemCatalogReport(firstDate, lastDate, null, categoryIdList);
		DashboardDTO dashboardPembelianPerPeriodeReport = dashboardSession.getPembelianPerPeriodeReport(null, year);
		DashboardDTO dashboardVendorReport = dashboardSession.getVendorReport(firstDate, lastDate, subBidangUsahaIdList);
		DashboardDTO dashboardProsesPembelianReport = dashboardSession.getProsesPembelianReport(null,firstDate, lastDate);
		
		isEmpty = cekEmpty(dashboardItemCatalogReport.getData(), isEmpty);
		isEmpty = cekEmpty(dashboardPembelianPerPeriodeReport.getData(), isEmpty);
		isEmpty = cekEmpty(dashboardVendorReport.getData(), isEmpty);
		isEmpty = cekEmpty(dashboardProsesPembelianReport.getData(), isEmpty);
			if (!isEmpty) {
				yearListNotEmptyData.add(years);
			}
		
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("yearList", yearListNotEmptyData);
	
		return map;
	}
	
	public Boolean cekEmpty(List<List<Double>> data, Boolean isEmpty) {
		if (isEmpty)
		for (List<Double> dataList : data) {
			if (dataList.size() > 0 ) {
				for (Double dataObj : dataList) {
					if (dataObj != 0 ) {
						isEmpty = false;
						return isEmpty;
					}
	
				}
			}
		}
		
		
		return isEmpty;
	}
	
	@Path("getItemCatalogReport")
	@POST
	public Map getItemCatalogReport(DashboardParamDTO dashboardParamDTO) throws ParseException{
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		Date startDate = null;
		Date endDate = null;
		if (dashboardParamDTO.getStartDate() != null )
		    startDate=new SimpleDateFormat("dd/MM/yyyy").parse(dashboardParamDTO.getStartDate());
		if (dashboardParamDTO.getEndDate() != null )
		    endDate=new SimpleDateFormat("dd/MM/yyyy").parse(dashboardParamDTO.getEndDate());
		
		map.put("monitoringCatalog",dashboardSession.getItemCatalogReport(startDate, endDate, dashboardParamDTO.getOrganizationId(), dashboardParamDTO.getDataIdList() ));
		return map;
	}
	
	@Path("getVendorReport")
	@POST
	public Map getVendorReport(DashboardParamDTO dashboardParamDTO) throws ParseException{
		 List<Integer> subBidangUsahaIdList= new ArrayList();
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		Date startDate = null;
		Date endDate = null;
		if (dashboardParamDTO.getStartDate() != null )
		    startDate=new SimpleDateFormat("dd/MM/yyyy").parse(dashboardParamDTO.getStartDate());
		if (dashboardParamDTO.getEndDate() != null )
		    endDate=new SimpleDateFormat("dd/MM/yyyy").parse(dashboardParamDTO.getEndDate());
		
		map.put("monitoringVendor",dashboardSession.getVendorReport(startDate, endDate, dashboardParamDTO.getDataIdList() ));
		return map;
	}
	
	@Path("getPembelianPerPeriodeReport")
	@POST
	public Map getPembelianPerPeriodeReport(DashboardParamDTO dashboardParamDTO) throws ParseException{
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		Integer intYear = null;
		if ( dashboardParamDTO.getYear() !=null ) {
			intYear = Integer.parseInt(dashboardParamDTO.getYear().trim());
		}
		map.put("monitoringPembelian",dashboardSession.getPembelianPerPeriodeReport(dashboardParamDTO.getOrganizationId(), intYear));
		return map;
	}
	
	@Path("getKinerjaPenyediaReport")
	@POST
	public Map getKinerjaPenyediaReport() throws ParseException{
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("monitoringKinerjaPenyedia",dashboardSession.getKinerjaPenyediaReport());
		return map;
	}
	
	@Path("getProsesPembelianReport")
	@POST
	public Map getProsesPembelianReport(DashboardParamDTO dashboardParamDTO) throws ParseException{
		Date startDate = null;
		Date endDate = null;
		if (dashboardParamDTO.getStartDate() != null )
		    startDate=new SimpleDateFormat("dd/MM/yyyy").parse(dashboardParamDTO.getStartDate());
		if (dashboardParamDTO.getEndDate() != null )
		    endDate=new SimpleDateFormat("dd/MM/yyyy").parse(dashboardParamDTO.getEndDate());
		
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("monitoringProsesPembelian",dashboardSession.getProsesPembelianReport(dashboardParamDTO.getOrganizationId(), startDate,endDate ));
		return map;
	}
	
	@Path("getMasaKontrakCatalogReport")
	@POST
	public Map getMasaKontrakCatalogReport(DashboardParamDTO dashboardParamDTO) throws ParseException{
		Date endDate = new Date();
		if (dashboardParamDTO.getEndDate() != null)
			{
				endDate=new SimpleDateFormat("dd/MM/yyyy").parse(dashboardParamDTO.getEndDate());
			}
		Date endDatePlus21 = hariLiburSession.getWorkingDays(endDate, 21);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("monitoringMasaKontrakCatalog",dashboardSession.getMasaKontrakCatalogReport(endDate, endDatePlus21, dashboardParamDTO.getOrganizationId()));
		return map;
	}

}
