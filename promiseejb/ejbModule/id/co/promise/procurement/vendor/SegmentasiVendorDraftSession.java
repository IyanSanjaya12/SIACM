package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.HariLibur;
import id.co.promise.procurement.entity.SegmentasiVendor;
import id.co.promise.procurement.entity.SegmentasiVendorDraft;
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
public class SegmentasiVendorDraftSession extends AbstractFacadeWithAudit<SegmentasiVendorDraft> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB VendorSession vendorSession;
	
	public SegmentasiVendorDraftSession(){
		super(SegmentasiVendorDraft.class);
	}
	
	public SegmentasiVendorDraft getSegmentasiVendorDraft(int id){
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	 public List<SegmentasiVendorDraft> getSegmentasiVendorDraftByVendorId(int vendorId){
		Query q = em.createNamedQuery("SegmentasiVendorDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public SegmentasiVendorDraft getSegmentasiVendorDraftByVendor(Integer vendorId){
		Query q = em.createNamedQuery("SegmentasiVendorDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<SegmentasiVendorDraft> segmentasiVendorDraft = q.getResultList();
		if (segmentasiVendorDraft != null && segmentasiVendorDraft.size() > 0) {
			return segmentasiVendorDraft.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegmentasiVendorDraft> getSegmentasiVendorDraftBySegmentasiVendorId(){
		Query q = em.createNamedQuery("SegmentasiVendorDraft.findBySegmentasiVendorId");
		//q.setParameter("segmentasiVendorId", segmentasiVendorId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public SegmentasiVendorDraft getSegmentasiVendorDraftByVendorIdAndSegmentasiVendor(Integer vendorId, Integer segmentasiVendorId) {
		Query q = em.createNamedQuery("SegmentasiVendorDraft.findByVendorAndSegmentasiVendor");
		q.setParameter("vendorId", vendorId);
		q.setParameter("segmentasiVendorId", segmentasiVendorId);
		
		List<SegmentasiVendorDraft> segmentasiVendorDraft = q.getResultList();
		if (segmentasiVendorDraft != null && segmentasiVendorDraft.size() > 0) {
			return segmentasiVendorDraft.get(0);
		}
		
		return null;
	}
	
	/*@SuppressWarnings("unchecked")
	public VendorListDTO getVendorBySubBidangUsahaId(String subBidUshList, Integer maxBaris, Integer halamanKe, Integer vendorExclude){
		List<Vendor> vendorList = new ArrayList<Vendor>();
		List<SegmentasiVendorDraft> resultList = new ArrayList<SegmentasiVendorDraft>();
		
		
		if(subBidUshList.equals("")) {
			subBidUshList = "0";
		}
		
		String queryString = "SELECT DISTINCT vendor.VENDOR_ID FROM promise_t2_vendor vendor "
				+ "LEFT JOIN promise_t3_segmentasi_vendor_draft sv ON vendor.VENDOR_ID = sv.VENDOR_ID "
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
	}*/
	
	public SegmentasiVendorDraft insertSegmentasiVendorDraft(SegmentasiVendorDraft segmentasiVendorDraft, Token token) {
		segmentasiVendorDraft.setCreated(new Date());
		segmentasiVendorDraft.setIsDelete(0);
		super.create(segmentasiVendorDraft, AuditHelper.OPERATION_CREATE, token);
		return segmentasiVendorDraft;
	}

	public SegmentasiVendorDraft updateSegmentasiVendorDraft(SegmentasiVendorDraft segmentasiVendorDraft, Token token) {
		segmentasiVendorDraft.setUpdated(new Date());
		super.edit(segmentasiVendorDraft, AuditHelper.OPERATION_UPDATE, token);
		return segmentasiVendorDraft;
	}

	public SegmentasiVendorDraft deleteSegmentasiVendorDraft(int id, Token token) {
		SegmentasiVendorDraft segmentasiVendorDraft = super.find(id);
		segmentasiVendorDraft.setIsDelete(1);
		segmentasiVendorDraft.setDeleted(new Date());

		super.edit(segmentasiVendorDraft, AuditHelper.OPERATION_DELETE, token);
		return segmentasiVendorDraft;
	}

	public SegmentasiVendorDraft deleteRowSegmentasiVendorDraft(int id, Token token) {
		SegmentasiVendorDraft segmentasiVendorDraft = super.find(id);
		super.remove(segmentasiVendorDraft, AuditHelper.OPERATION_ROW_DELETE, token);
		return segmentasiVendorDraft;
	}
	
	@SuppressWarnings("unchecked")
	 public void deleteSegmentasiVendorDraftByVendorId(int vendorId){
		Query q = em.createNamedQuery("SegmentasiVendorDraft.deleteByVendorId");
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
