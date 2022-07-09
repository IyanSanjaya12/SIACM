package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.BankVendor;
import id.co.promise.procurement.entity.SegmentasiVendor;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Singleton
@LocalBean
public class SegmentasiVendorSession extends AbstractFacadeWithAudit<SegmentasiVendor> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB VendorSession vendorSession;
	
	public SegmentasiVendorSession(){
		super(SegmentasiVendor.class);
	}
	
	public SegmentasiVendor getSegmentasiVendor(int id){
		return super.find(id);
	}
	
	public List<SegmentasiVendor> getSegmentasiVendorByVendorId(int vendorId){
		Query q = em.createNamedQuery("SegmentasiVendor.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public SegmentasiVendor getSegmentasiVendorByVendor(Integer vendorId){
		Query q = em.createNamedQuery("SegmentasiVendor.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<SegmentasiVendor> segmentasiVendor = q.getResultList();
		if (segmentasiVendor != null && segmentasiVendor.size() > 0) {
			return segmentasiVendor.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public VendorListDTO getVendorBySubBidangUsahaId(String subBidUshList, Integer maxBaris, Integer halamanKe, Integer vendorExclude){
		List<Vendor> vendorList = new ArrayList<Vendor>();
		List<SegmentasiVendor> resultList = new ArrayList<SegmentasiVendor>();
		
		
		if(subBidUshList.equals("")) {
			subBidUshList = "0";
		}
		
		String queryString = "SELECT DISTINCT vendor.VENDOR_ID FROM promise_t2_vendor vendor "
				+ "LEFT JOIN promise_t3_segmentasi_vendor sv ON vendor.VENDOR_ID = sv.VENDOR_ID "
				+ "LEFT JOIN promise_t2_sub_bidang_usaha sbu ON sv.SUB_BIDANG_USAHA_ID = sbu.SUB_BIDANG_USAHA_ID "
				+ "WHERE sv.ISDELETE = 0 AND vendor.ISDELETE = 0";
		if(subBidUshList != null) {
			queryString += " AND sv.SUB_BIDANG_USAHA_ID IN("+subBidUshList+") AND vendor.VENDOR_ID <> "+vendorExclude;
		}
		
		
		queryString += "GROUP BY vendor.VENDOR_ID ORDER BY vendor.VENDOR_ID" ;
		int startRow = 0;
		if(maxBaris != null && halamanKe != null) {
			startRow = (halamanKe-1)*maxBaris;
			//queryString += " LIMIT " + maxBaris + " OFFSET " + (((halamanKe - 1) * maxBaris));
		}

		//Query query = getEntityManager().createNativeQuery(queryString, Vendor.class);
		
		Query query = em.createNativeQuery(queryString);
		//vendorList = query.setFirstResult(startRow).setMaxResults(maxBaris).getResultList();
		
		List<Integer> arrObjList = query.setFirstResult(startRow)
				.setMaxResults(maxBaris)
				.getResultList();
		
		Integer totalRow = arrObjList.size();

		for (Integer obj : arrObjList) {
			Vendor vendorNew = vendorSession.find(obj);
			vendorList.add(vendorNew);
		}
		
		VendorListDTO vendorListDTO = new VendorListDTO();
		vendorListDTO.setVendorList(vendorList);
		vendorListDTO.setTotalRow(totalRow);
		return vendorListDTO;
	}

	public SegmentasiVendor insertSegmentasiVendor(SegmentasiVendor segmentasiVendor, Token token) {
		segmentasiVendor.setCreated(new Date());
		segmentasiVendor.setIsDelete(0);
		super.create(segmentasiVendor, AuditHelper.OPERATION_CREATE, token);
		return segmentasiVendor;
	}

	public SegmentasiVendor updateSegmentasiVendor(SegmentasiVendor segmentasiVendor, Token token) {
		segmentasiVendor.setUpdated(new Date());
		super.edit(segmentasiVendor, AuditHelper.OPERATION_UPDATE, token);
		return segmentasiVendor;
	}

	public SegmentasiVendor deleteSegmentasiVendor(int id, Token token) {
		SegmentasiVendor segmentasiVendor = super.find(id);
		segmentasiVendor.setIsDelete(1);
		segmentasiVendor.setDeleted(new Date());

		super.edit(segmentasiVendor, AuditHelper.OPERATION_DELETE, token);
		return segmentasiVendor;
	}

	public SegmentasiVendor deleteRowSegmentasiVendor(int id, Token token) {
		SegmentasiVendor segmentasiVendor = super.find(id);
		super.remove(segmentasiVendor, AuditHelper.OPERATION_ROW_DELETE, token);
		return segmentasiVendor;
	}
	
	public void deleteSegmentasiVendorByVendorId(int vendorId){
		Query q = em.createNamedQuery("SegmentasiVendor.deleteByVendorId");
		q.setParameter("vendorId", vendorId);
		q.executeUpdate();
		
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
}
