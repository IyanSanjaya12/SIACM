package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.SertifikatVendor;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.VendorProfile;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class SertifikatVendorSession extends AbstractFacadeWithAudit<SertifikatVendor> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public SertifikatVendorSession(){
		super(SertifikatVendor.class);
	} 
	
	public List<SertifikatVendor> getSertifikatVendorByVendorId(int vendorId){
		Query q = em.createNamedQuery("SertifikatVendor.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}

	public SertifikatVendor insertSertifikatVendor(SertifikatVendor SertifikatVendor, Token token) {
		SertifikatVendor.setCreated(new Date());
		SertifikatVendor.setIsDelete(0);
		super.create(SertifikatVendor, AuditHelper.OPERATION_CREATE, token);
		return SertifikatVendor;
	}

	public SertifikatVendor updateSertifikatVendor(SertifikatVendor SertifikatVendor, Token token) {
		SertifikatVendor.setUpdated(new Date());
		super.edit(SertifikatVendor, AuditHelper.OPERATION_UPDATE, token);
		return SertifikatVendor;
	}

	public SertifikatVendor deleteSertifikatVendor(int id, Token token) {
		SertifikatVendor SertifikatVendor = super.find(id);
		SertifikatVendor.setIsDelete(1);
		SertifikatVendor.setDeleted(new Date());

		super.edit(SertifikatVendor, AuditHelper.OPERATION_DELETE, token);
		return SertifikatVendor;
	}

	public SertifikatVendor deleteRowSertifikatVendor(int id, Token token) {
		SertifikatVendor SertifikatVendor = super.find(id);
		super.remove(SertifikatVendor, AuditHelper.OPERATION_ROW_DELETE, token);
		return SertifikatVendor;
	}

	@SuppressWarnings("unchecked")
	public SertifikatVendor getSertifikatVendorUserId(Integer id) {
		Query q = em.createQuery("SELECT t1 FROM SertifikatVendor t1 WHERE t1.vendor.user = :id");
		q.setParameter("id", id);
		List<SertifikatVendor> sertifikatVendorList = q.getResultList();
		if(sertifikatVendorList == null){
			return null;
		}else{
			if(sertifikatVendorList.size() > 0)
				return sertifikatVendorList.get(0);
			else
				return null;
		}
	}

	@SuppressWarnings("unchecked")
	public SertifikatVendor getSertifikatVendorByVendor(Integer id) {
		Query q = em.createQuery("SELECT t1 FROM SertifikatVendor t1 WHERE t1.vendor.id = :id");
		q.setParameter("id", id);
		List<SertifikatVendor> sertifikatVendorList = q.getResultList();
		if(sertifikatVendorList == null){
			return null;
		}else{
			if(sertifikatVendorList.size() > 0)
				return sertifikatVendorList.get(0);
			else
				return null;
		}
	}

	private String columnSortList(int index){
		String[] colomnTbl = new String[7];
//		colomnTbl[0] = "T1.vendor.nama";
		colomnTbl[0] = "T1.vendor.id";
		colomnTbl[1] = "T1.vendor.nama";
		colomnTbl[2] = "T1.vendor.email";
		colomnTbl[3] = "T1.nomor";
		colomnTbl[4] = "T1.tanggalMulai";
		colomnTbl[5] = "T1.tanggalBerakhir";
		colomnTbl[6] = "T1.status";
		
		return colomnTbl[index].toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<SertifikatVendor> getSertifikatVendorPaggingList(
			Integer iDisplayStart, Integer iDisplayLength, String keyword,
			Integer column, String sort, String nama, String status) {
		
		String query = "SELECT T1 "
				+ "FROM SertifikatVendor T1 WHERE T1.isDelete = :isDelete AND T1.vendor.isDelete = :isDelete "
				+ "AND (T1.vendor.nama LIKE :keyword OR T1.vendor.email LIKE :keyword OR T1.nomor LIKE :keyword) ";
		
		if(nama != null){
			if(!nama.equals("")){
				query += "AND T1.vendor.nama LIKE :nama ";
			}
		}
		
		if(status != null){
			if(!status.equals("")){
				query += "AND T1.status = :status ";
			}
		}
		
		query += "ORDER BY " + this.columnSortList(column) + " " + sort;
		
		Query q  = em.createQuery(query)
				.setParameter("isDelete", 0) 
				.setParameter("keyword", "%" + keyword + "%");
		
		if(nama != null){
			if(!nama.equals("")){
				q.setParameter("nama", "%" + nama + "%") ;
			}
		}
		
		if(status != null){
			if(!status.equals("")){
				q.setParameter("status", Integer.valueOf(status));
			}
		}
				
		return q.setFirstResult(iDisplayStart)
				.setMaxResults(iDisplayLength)
				.getResultList();
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Integer getSertifikatVendorPaggingSize(String keyword, String nama, String status) {

		String query ="SELECT COUNT(T1) "
				+ "FROM SertifikatVendor T1 WHERE T1.isDelete = :isDelete AND T1.vendor.isDelete = :isDelete "
				+ "AND (T1.vendor.nama LIKE :keyword OR T1.vendor.email LIKE :keyword OR T1.nomor LIKE :keyword) ";
		
		if(nama != null){
			if(!nama.equals("")){
				query += "AND T1.vendor.nama LIKE :nama ";
			}
		}
		
		if(status != null){
			if(!status.equals("")){
				query += "AND T1.status = :status ";
			}
		}
		
		Query q  = em.createQuery(query)
				.setParameter("isDelete", 0) 
				.setParameter("keyword", "%" + keyword + "%");
		
		if(nama != null){
			if(!nama.equals("")){
				q.setParameter("nama", "%" + nama + "%") ;
			}
		}
		
		if(status != null){
			if(!status.equals("")){
				q.setParameter("status", Integer.valueOf(status));
			}
		}
		 
		List list = q.getResultList();
		
		Integer result = 0;
		for (Object p : list) {
			result = Integer.valueOf(p.toString());
		}
		return result;
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
