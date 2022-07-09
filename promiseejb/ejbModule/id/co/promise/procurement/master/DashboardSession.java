package id.co.promise.procurement.master;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Dashboard;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;

@Stateless
@LocalBean
public class DashboardSession extends AbstractFacadeWithAudit<Dashboard> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public DashboardSession(){
		super(Dashboard.class);
	}
	
	public Dashboard getDashboard(int id){
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Dashboard> getDashboardList(){
		Query q = em.createNamedQuery("Dashboard.find");
		return q.getResultList();
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}
	
	@SuppressWarnings("unchecked")
	public DashboardDTO getItemCatalogReport(Date startDate, Date endDate, Integer organizationId, List<Integer> dataIdList){
		Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy");  
        DateFormat dateFormatRange = new SimpleDateFormat("dd-MM-yyyy");
        String strYear = dateFormat.format(date);  
		DashboardDTO dashboardDTO = new DashboardDTO("Grafik Top 10 Kategori Katalog");
		List<String> labels = new ArrayList<String>();
		List<String> dataLabels = new ArrayList<String>();
		List<List<Double>> data = new ArrayList<List<Double>>();
		List<Object[]> dataQuery = new ArrayList<Object[]>();
		Query q = null;
		
		String query = "SELECT COUNT (catalogCategory) , catalogCategory.category.translateInd  FROM CatalogCategory catalogCategory "
				+ " where catalogCategory.catalog.catalogKontrak.isDelete = 0 and catalogCategory.isDelete = 0 ";
		
		if (dataIdList.size() > 0) {
			query = query + "and catalogCategory.category.id in (:categoryIdList) ";
		}
		

		if (startDate != null && endDate != null) {

			query += "and ((catalogCategory.catalog.catalogKontrak.tglMulailKontrak >= :startDate and catalogCategory.catalog.catalogKontrak.tglMulailKontrak <= :endDate ) "
					+ "or (catalogCategory.catalog.catalogKontrak.tglAkhirKontrak >= :startDate and catalogCategory.catalog.catalogKontrak.tglAkhirKontrak <= :endDate ) "
					+ "or (catalogCategory.catalog.catalogKontrak.tglMulailKontrak <= :startDate and catalogCategory.catalog.catalogKontrak.tglAkhirKontrak >= :startDate ) "
					+ "or (catalogCategory.catalog.catalogKontrak.tglMulailKontrak <= :endDate and catalogCategory.catalog.catalogKontrak.tglAkhirKontrak >= :endDate )) ";
			
		} else {
			query += "and (current_date between catalogCategory.catalog.catalogKontrak.tglMulailKontrak and catalogCategory.catalog.catalogKontrak.tglAkhirKontrak) ";
		}
		
		/* if (organizationId != null) {
			query = query + "and catalogCategory.catalog.item.id in (select io.item.id from ItemOrganisasi io where io.organisasi.id =:organizationId ) ";
		} */

		query = query + "group by catalogCategory.category.translateInd ";
			q = em.createQuery(query);

		if (dataIdList.size() > 0) {
			q.setParameter("categoryIdList", dataIdList);
		}
		/*if (organizationId != null) {
			q.setParameter("organizationId", organizationId);
		} */
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}

		
		try {
			dataQuery = q.getResultList();
			if (startDate != null && endDate != null) {
				labels.add(dateFormatRange.format(startDate) +" - "+ dateFormatRange.format(endDate));
			}else {
				labels.add("Current Years(" + strYear + ")");
			}
			List<Double>totalCount = new ArrayList<Double>();
			for(Object[] object : dataQuery){
				dataLabels.add(object[1].toString());
				totalCount.add(Double.parseDouble(object[0].toString()));
			}
			data.add(totalCount);
			dashboardDTO.setData(data);
			dashboardDTO.setLabels(labels);
			dashboardDTO.setDataLabels(dataLabels);
		}catch (Exception e) {
			System.err.println("No Data Found");
		}
		return dashboardDTO;
	}
	
	@SuppressWarnings("unchecked")
	public DashboardDTO getVendorReport(Date startDate, Date endDate, List<Integer>  subBidangUsahaIdList){
		Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        DateFormat dateFormatRange = new SimpleDateFormat("dd-MM-yyyy");
        String strYear = dateFormat.format(date);  
		DashboardDTO dashboardDTO = new DashboardDTO("Grafik Bidang Usaha Penyedia");
		List<String> labels = new ArrayList<String>();
		List<String> dataLabels = new ArrayList<String>();
		List<List<Double>> data = new ArrayList<List<Double>>();
		List<Object[]> dataQuery = new ArrayList<Object[]>();
//		Query q = em.createNamedQuery("SegmentasiVendor.getDashboardData");
//		q.setParameter("subBidangUsahaIdList",subBidangUsahaIdList);
		
		Query q = null;
		
		String query = "SELECT COUNT (segmentasiVendor) , segmentasiVendor.subBidangUsaha.nama  FROM SegmentasiVendor segmentasiVendor "
				+ " where  segmentasiVendor.vendor.isDelete = 0  "
				+ " and segmentasiVendor.isDelete = 0 and segmentasiVendor.subBidangUsaha.isDelete = 0 ";
		
		if (subBidangUsahaIdList.size() > 0) {
			query = query + "and segmentasiVendor.subBidangUsaha.id in (:subBidangUsahaIdList) ";
		}
		

		if (startDate != null && endDate != null) {


			query += "And segmentasiVendor.vendor.id in (select catalogKontrak.vendor.id from CatalogKontrak catalogKontrak where " + 
					"((catalogKontrak.tglMulailKontrak >= :startDate and catalogKontrak.tglMulailKontrak <= :endDate ) " 
					+ "or (catalogKontrak.tglAkhirKontrak >= :startDate and catalogKontrak.tglAkhirKontrak <= :endDate ) "
					+ "or (catalogKontrak.tglMulailKontrak <= :startDate and catalogKontrak.tglAkhirKontrak >= :startDate ) "
					+ "or (catalogKontrak.tglMulailKontrak <= :endDate and catalogKontrak.tglAkhirKontrak >= :endDate )) and catalogKontrak.isDelete = 0 ) ";
			
		} else {
			query += "And segmentasiVendor.vendor.id in (select catalogKontrak.vendor.id from CatalogKontrak catalogKontrak where " + 
					"(current_date between catalogKontrak.tglMulailKontrak and catalogKontrak.tglAkhirKontrak ) and catalogKontrak.isDelete = 0 ) ";
		}
		query = query + "group by segmentasiVendor.subBidangUsaha.nama ";
		q = em.createQuery(query);
		
		if (subBidangUsahaIdList.size() > 0) {
			q.setParameter("subBidangUsahaIdList", subBidangUsahaIdList);
		}

		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		
		try {
			dataQuery = q.getResultList();
			if (startDate != null && endDate != null) {
				labels.add(dateFormatRange.format(startDate) +" - "+ dateFormatRange.format(endDate));
			}else {
				labels.add("Current Years(" + strYear + ")");
			}
			List<Double>totalCount = new ArrayList<Double>();
			for(Object[] object : dataQuery){
				dataLabels.add(object[1].toString());
				totalCount.add(Double.parseDouble(object[0].toString()));
			}
			data.add(totalCount);
			dashboardDTO.setData(data);
			dashboardDTO.setLabels(labels);
			dashboardDTO.setDataLabels(dataLabels);
		}catch(Exception e) {
			System.err.println("No Data Found");
		}	
		return dashboardDTO;
	}
	
	@SuppressWarnings("unchecked")
	public DashboardDTO getPembelianPerPeriodeReport(Integer organizationId, Integer year) throws ParseException{
		DashboardDTO dashboardDTO = new DashboardDTO("Grafik Pembelian Per Tahun");
		List<String> labels = new ArrayList<String>();
		List<String> dataLabels = new ArrayList<String>();
		List<List<Double>> data = new ArrayList<List<Double>>();
		List<Object[]> dataQuery = new ArrayList<Object[]>();
		Integer totalMonth = 12;
		Integer thisYear = Calendar.getInstance().get(Calendar.YEAR);
		Integer thisMonth = Calendar.getInstance().get(Calendar.MONTH);

		//Query q = em.createNamedQuery("PurchaseOrder.getSumPerMonth");
		
		Query q = null;
		
		//String query = " SELECT coalesce(sum(o.purchaseOrder.totalCost), 0) FROM DeliveryReceived o WHERE o.isDelete = 0 AND "
				//+ " (o.purchaseOrder.created between :startDate and :endDate) ";
		
		String query = " SELECT coalesce(sum(o.totalCost), 0) FROM PurchaseOrder o WHERE o.status = 'PO Complete' AND o.isDelete = 0 AND "
				+ " (o.created between :startDate and :endDate) ";
		
		if (organizationId != null) {
			query = query + "and o.purchaseRequest.organisasi.id =:organizationId ";
		}
		q = em.createQuery(query);
		
		if(year.equals(thisYear)){
			totalMonth = thisMonth + 1;
		}else if (year > thisYear) {
			totalMonth = 0;
		} 
//		else if (year < thisYear) {
//			Integer selisih = thisYear - year;
//			totalMonth = (totalMonth*selisih)+thisMonth + 1;
//		}
		String pattern = "dd/MM/yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		dataLabels.add("totalPembelian");
		for(Integer i = 1 ; i <= totalMonth; i++) {
				labels.add(new DateFormatSymbols().getMonths()[i-1]);
				String startMonthTemp = "0"+i;
				if (i >= 10) {
					startMonthTemp = i.toString();
				}
				String startMonth = startMonthTemp;
				Integer endMonthInt = null;
				String endMonthTemp = "0"+(i+1);
				if(i>=9) {
					endMonthInt = i+1;
					endMonthTemp = endMonthInt.toString();
				} 
				
				String endMonth = endMonthTemp;
				String startDateStr = "01/"+startMonth+"/"+year ;
				String endDateStr = i== 12 ? "01/01/"+(year+1) : "01/"+endMonth+"/"+year ;
				Date startDate=simpleDateFormat.parse(startDateStr);
				Date endDate=simpleDateFormat.parse(endDateStr);
				q.setParameter("startDate", startDate);
				q.setParameter("endDate", endDate);
				if (organizationId != null) {
					q.setParameter("organizationId", organizationId);
				}
				data.add(q.getResultList());
		}	
		dashboardDTO.setData(data);
		dashboardDTO.setLabels(labels);
		dashboardDTO.setDataLabels(dataLabels);
		return dashboardDTO;
	}
	
	@SuppressWarnings("unchecked")
	public DashboardDTO getKinerjaPenyediaReport() throws ParseException{
		DashboardDTO dashboardDTO = new DashboardDTO("Grafik Kinerja Penyedia");
		List<String> dataLabels = new ArrayList<String>();	
		List<String> labels = new ArrayList<String>(Arrays.asList("Sangat Kurang","Kurang","Cukup","Baik","Sangat Baik","Sangat Memuaskan"));
		List<List<Double>> data = new ArrayList<List<Double>>();
		dataLabels.add("Current");
		List<Long> dataQuery = new ArrayList<Long>();
		List<Double> start = new ArrayList<Double>(Arrays.asList(new Double(0),new Double(1),new Double(2),new Double(3),new Double(4),new Double(5)));
		List<Double> end = new ArrayList<Double>(Arrays.asList(new Double(1),new Double(2),new Double(3),new Double(4),new Double(5),new Double(6)));
		Query q = em.createNamedQuery("Vendor.getVendorRatingReport");
		for(Integer i = 0 ; i < labels.size(); i++){
			List<Double> totalData = new ArrayList<Double>();
			q.setParameter("start", start.get(i));
			q.setParameter("end", end.get(i));
			dataQuery = q.getResultList();
			totalData.add(Double.parseDouble(dataQuery.get(0).toString()));
			data.add(totalData);
		}
		
		dashboardDTO.setData(data);
		dashboardDTO.setLabels(labels);
		dashboardDTO.setDataLabels(dataLabels);
		return dashboardDTO;
	}
	
	@SuppressWarnings("unchecked")
	public DashboardDTO getProsesPembelianReport(Integer organizationId, Date startDate , Date endDate) throws ParseException{
		DashboardDTO dashboardDTO = new DashboardDTO("Grafik Proses Pembelian");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
        String strStartDate= dateFormat.format(startDate);
        String strEndDate= dateFormat.format(endDate);
		List<String> queryName = null;
		
		if (organizationId != null) {
			queryName = Arrays.asList("PurchaseOrder.getCountByDateAndOrganizationId","DeliveryReceived.getCountByDateAndOrganizationId","InvoicePayment.getCountByDateAndOrganizationId");
		} else {
			queryName = new ArrayList<String>(Arrays.asList("PurchaseOrder.getCountByDate","DeliveryReceived.getCountByDate","InvoicePayment.getCountByDate"));
		}
		
		List<String> dataLabels = new ArrayList<String>();
		List<String> labels = new ArrayList<String>(Arrays.asList("Jumlah PO","Jumlah Receipt","Jumlah Pembayaran"));
		List<List<Double>> data = new ArrayList<List<Double>>();
		dataLabels.add(strStartDate+"-"+strEndDate);
		List<Long> dataQuery = new ArrayList<Long>();
		for(String qName : queryName){
			List<Double> totalData = new ArrayList<Double>();
			Query q = em.createNamedQuery(qName);
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
			if (organizationId != null) {
				q.setParameter("organizationId", organizationId);
			}
			dataQuery = q.getResultList();
			totalData.add(Double.parseDouble(dataQuery.get(0).toString()));
			data.add(totalData);
		}
		dashboardDTO.setData(data);
		dashboardDTO.setLabels(labels);
		dashboardDTO.setDataLabels(dataLabels);
		return dashboardDTO;
	}
	@SuppressWarnings("unchecked")
	public DashboardDTO getMasaKontrakCatalogReport(Date endDate, Date endDatePlus21, Integer organizationId) throws ParseException{
		
		if (endDate == null) {
			endDate = new Date();
		}
		DashboardDTO dashboardDTO = new DashboardDTO("Grafik Masa Kontrak Catalog");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        List<String> queryName = null;
       // if ( organizationId !=null ) { // karena sudah tidaj menggunakan table item organisasi
        	//queryName = new ArrayList<String>(Arrays.asList("CatalogKontrak.getNonActiveByOrganizationId","CatalogKontrak.getAlmostExpiredByOrganizationId","CatalogKontrak.getOver21DaysByOrganizationId"));
       // } else {
        	queryName = new ArrayList<String>(Arrays.asList("CatalogKontrak.getNonActive","CatalogKontrak.getAlmostExpired","CatalogKontrak.getOver21Days"));
       // }
		List<String> dataLabels = new ArrayList<String>();
		List<String> labels = new ArrayList<String>(Arrays.asList("Berakhir","Mendekati Berakhir < 21 HK","Aktif > 21HK"));
		List<List<Double>> data = new ArrayList<List<Double>>();
		
		List<Long> dataQuery = new ArrayList<Long>();
		for(String qName : queryName){
			List<Double> totalData = new ArrayList<Double>();
			Query q = em.createNamedQuery(qName);
			q.setParameter("endDatePlus21", endDatePlus21);
			q.setParameter("endDate", endDate);
			/* if ( organizationId !=null ) {
				 q.setParameter("organizationId", organizationId);
			 } */
			dataQuery = q.getResultList();
			totalData.add(Double.parseDouble(dataQuery.get(0).toString()));
			data.add(totalData);
		}
		dataLabels.add("Current");
		dashboardDTO.setData(data);
		dashboardDTO.setLabels(labels);
		dashboardDTO.setDataLabels(dataLabels);
		return dashboardDTO;
	}
	
}
