package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.AlasanBlacklist;
import id.co.promise.procurement.entity.AlokasiAnggaran;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class AlasanBlacklistSession extends AbstractFacadeWithAudit<AlasanBlacklist> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public AlasanBlacklistSession() {
		super(AlasanBlacklist.class);
	}

	public AlasanBlacklist getAlasanBlacklist(int id){
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<AlasanBlacklist> getAlasanBlacklistList(){
		Query q = em.createQuery("SELECT t1 FROM AlasanBlacklist t1 WHERE t1.alasanBlacklistIsDelete = :alasanBlacklistIsDelete ");
		return q.setParameter("alasanBlacklistIsDelete", 0).getResultList();
	}
	
	private String columnPurchaseRequestItemPaggingList(int index){
		String[] colomnTbl = new String[8];
		colomnTbl[0] = "T1.ALASAN_BLACKLIST_ID";
		colomnTbl[1] = "T1.ALASAN_BLACKLIST_KETERANGAN";
		colomnTbl[2] = "jumlahHari";
		return colomnTbl[index].toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getAlasanBlacklistByPagination (
			Integer iDisplayStart, Integer iDisplayLength, String keyword,
			Integer column, String sort) {
		
		String query = 
			"SELECT T1.ALASAN_BLACKLIST_ID, T1.ALASAN_BLACKLIST_KETERANGAN, T1.ALASAN_BLACKLIST_JML_WAKTU, T1.ALASAN_BLACKLIST_JK_WAKTU ,CASE WHEN T1.ALASAN_BLACKLIST_JK_WAKTU = 'Y' THEN T1.ALASAN_BLACKLIST_JML_WAKTU * 365 "
		+ "WHEN T1.ALASAN_BLACKLIST_JK_WAKTU = 'M' THEN T1.ALASAN_BLACKLIST_JML_WAKTU * 30 "
		+ "WHEN T1.ALASAN_BLACKLIST_JK_WAKTU = 'D' THEN T1.ALASAN_BLACKLIST_JML_WAKTU * 1 "
		+ "ELSE NULL END AS jumlahHari from promise_t1_alasan_blacklist T1 WHERE T1.ALASAN_BLACKLIST_ISDELETE = :isDeleted AND (T1.ALASAN_BLACKLIST_KETERANGAN LIKE :keyword OR T1.ALASAN_BLACKLIST_JML_WAKTU  LIKE :keyword)"
        + "ORDER BY " + this.columnPurchaseRequestItemPaggingList(column) + " " + sort;
			
		Query q  = em.createNativeQuery(query)
				.setParameter("isDeleted", 0)
				.setParameter("keyword", "%" + keyword + "%");
		 
		List<Object[]> result = (List<Object[]>) q.setFirstResult(iDisplayStart)
				.setMaxResults(iDisplayLength)
				.getResultList();
		
		return result;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Integer getAlasanBlacklistPaggingSize(String keyword) {
		
		String query = 
				"SELECT COUNT(T1.ALASAN_BLACKLIST_ID) from promise_t1_alasan_blacklist T1 WHERE T1.ALASAN_BLACKLIST_ISDELETE = :isDeleted AND "
			+ "(T1.ALASAN_BLACKLIST_KETERANGAN LIKE :keyword OR T1.ALASAN_BLACKLIST_JML_WAKTU LIKE :keyword)";
		
		Query q  = em.createNativeQuery(query)
				.setParameter("isDeleted", 0)
				.setParameter("keyword", "%" + keyword + "%");
		 
		List list = q.getResultList();
		
		Integer result = 0;
		for (Object p : list) {
			result = Integer.valueOf(p.toString());
		}
		return result;
	}
	
	
	public AlasanBlacklist insertAlasanBlacklist(AlasanBlacklist alasanBlacklist, Token token){
		alasanBlacklist.setAlasanBlacklistCreated(new Date());
		alasanBlacklist.setAlasanBlacklistIsDelete(0);
		super.create(alasanBlacklist, AuditHelper.OPERATION_CREATE, token);
		return alasanBlacklist;
	}
	
	public AlasanBlacklist updateAlasanBlacklist(AlasanBlacklist alasanBlacklist, Token token){
		alasanBlacklist.setAlasanBlacklistUpdated(new Date());
		super.edit(alasanBlacklist, AuditHelper.OPERATION_UPDATE, token);
		return alasanBlacklist;				
	}
	
	public AlasanBlacklist deleteAlasanBlacklist(int id, Token token){
		AlasanBlacklist alasanBlacklist = super.find(id);
		alasanBlacklist.setAlasanBlacklistIsDelete(1);
		alasanBlacklist.setAlasanBlacklistDeleted(new Date());
		super.edit(alasanBlacklist, AuditHelper.OPERATION_DELETE, token);
		return alasanBlacklist;
	}
	
	public AlasanBlacklist deleteRowAlasanBlacklist(int id, Token token){
		AlasanBlacklist alasanBlacklist = super.find(id);
		super.remove(alasanBlacklist, AuditHelper.OPERATION_ROW_DELETE, token);
		return alasanBlacklist;
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
