package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.DokumenRegistrasiVendor;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Singleton
@LocalBean
public class DokumenRegistrasiVendorSession extends AbstractFacadeWithAudit<DokumenRegistrasiVendor> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public DokumenRegistrasiVendorSession(){
		super(DokumenRegistrasiVendor.class);
	}

	@SuppressWarnings("unchecked")
	public List<DokumenRegistrasiVendor> getDokumenRegistrasiVendorByVendorId(int vendorId){
		Query q = em.createNamedQuery("DokumenRegistrasiVendor.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<DokumenRegistrasiVendor> getDokumenRegistrasiVendorByVendorIdAndSubject(int vendorId, String subject){
		Query q = em.createNamedQuery("DokumenRegistrasiVendor.findByVendorAndSubject");
		q.setParameter("vendorId", vendorId);
		q.setParameter("subject", subject);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<DokumenRegistrasiVendor> getHistoryDokumenRegistrasiVendor(int vendorId, String subject){
		Query q = em.createNamedQuery("DokumenRegistrasiVendor.findHistoryDokumenVendor");
		q.setParameter("vendorId", vendorId);
		q.setParameter("subject", subject);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public ValidDTO checkNomorDokumen(DokumenRegistrasiVendor draftDocument){
		ValidDTO validDTO = new ValidDTO();
		int vendorId = draftDocument.getVendor().getId();
		String isExpired;
		String subject = draftDocument.getSubject();
		Boolean valid = true;
		Query q = em.createNamedQuery("DokumenRegistrasiVendor.findHistoryDokumenVendor");
		q.setParameter("vendorId", vendorId);
		q.setParameter("subject", subject);
		List<DokumenRegistrasiVendor> historyDocumentList =q.getResultList();
		Query query = em.createNamedQuery("DokumenRegistrasiVendor.findByVendorAndSubject");
		query.setParameter("vendorId", vendorId);
		query.setParameter("subject", subject);
		DokumenRegistrasiVendor currentDocument =(DokumenRegistrasiVendor) query.getSingleResult();
		if(currentDocument.getTanggalBerakhir().compareTo(new Date())<0){
			isExpired = "Expired";
		}else{
			isExpired = "";
		}
		for(DokumenRegistrasiVendor historyDokumen : historyDocumentList){//cek nama sudah di pakai blom
			if(historyDokumen.getNamaDokumen().equals(draftDocument.getNamaDokumen())){
				valid=false;
				validDTO.setInfo("Doc Exist");
				break;
			}
		}	
		if(!isExpired.equals("")){//kalo expired nama dokumen mesti di ganti			
			if(currentDocument.getNamaDokumen().equals(draftDocument.getNamaDokumen()))
			{	
				valid=false;
				validDTO.setInfo("Change Doc");
			}
		}
		
		if(valid){
			if(isExpired.equals("")){
				if(!currentDocument.getNamaDokumen().equals(draftDocument.getNamaDokumen())){
					if(draftDocument.getTanggalTerbit().compareTo(currentDocument.getTanggalBerakhir())<0){
						validDTO.setInfo("Doc Masi Berlaku");
					}
				}	
			}
			
		}
		
		validDTO.setValid(valid);
		return validDTO;
	}
	
	@SuppressWarnings("unchecked")
	public DokumenRegistrasiVendor getDokumenRegistrasiVendorByVendorAndSubject(int vendorId, String subject){
		Query q = em.createNamedQuery("DokumenRegistrasiVendor.findByVendorAndSubject");
		q.setParameter("vendorId", vendorId);
		q.setParameter("subject", subject);
		List<DokumenRegistrasiVendor> dokumenRegistrasiVendor = q.getResultList();
		if (dokumenRegistrasiVendor != null && dokumenRegistrasiVendor.size() > 0) {
			return dokumenRegistrasiVendor.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<DokumenRegistrasiVendor> getAllByVendorAndSubject(int vendorId, String subject){
		Query q = em.createNamedQuery("DokumenRegistrasiVendor.findAllByVendorAndSubject");
		q.setParameter("vendorId", vendorId);
		q.setParameter("subject", subject);
		List<DokumenRegistrasiVendor> dokumenRegistrasiVendor = q.getResultList();		
		return dokumenRegistrasiVendor;
	}
	
	public DokumenRegistrasiVendor insertDokumenRegistrasiVendor(DokumenRegistrasiVendor dokumenRegistrasiVendor, Token token) {
		dokumenRegistrasiVendor.setCreated(new Date());
		dokumenRegistrasiVendor.setIsDelete(0);
		super.create(dokumenRegistrasiVendor, AuditHelper.OPERATION_CREATE, token);
		return dokumenRegistrasiVendor;
	}

	public DokumenRegistrasiVendor updateDokumenRegistrasiVendor(DokumenRegistrasiVendor dokumenRegistrasiVendor, Token token) {
		dokumenRegistrasiVendor.setUpdated(new Date());
		super.edit(dokumenRegistrasiVendor, AuditHelper.OPERATION_UPDATE, token);
		return dokumenRegistrasiVendor;
	}

	public DokumenRegistrasiVendor deleteDokumenRegistrasiVendor(int id, Token token) {
		DokumenRegistrasiVendor dokumenRegistrasiVendor = super.find(id);
		dokumenRegistrasiVendor.setIsDelete(1);
		dokumenRegistrasiVendor.setDeleted(new Date());

		super.edit(dokumenRegistrasiVendor, AuditHelper.OPERATION_DELETE, token);
		return dokumenRegistrasiVendor;
	}

	public DokumenRegistrasiVendor deleteRowDokumenRegistrasiVendor(int id, Token token) {
		DokumenRegistrasiVendor dokumenRegistrasiVendor = super.find(id);
		super.remove(dokumenRegistrasiVendor, AuditHelper.OPERATION_ROW_DELETE, token);
		return dokumenRegistrasiVendor;
	}


	private String columnRekananDokumenExpiredPaggingList(int index){
		String[] colomnTbl = new String[5];
		colomnTbl[0] = "t1.id";
		colomnTbl[1] = "t1.vendor.nama";
		colomnTbl[2] = "t1.subject";
		colomnTbl[3] = "t1.tanggalBerakhir";
		colomnTbl[4] = "t1.id";
		
		return colomnTbl[index].toString();
	}

	
	@SuppressWarnings("unchecked")
	public List<DokumenRegistrasiVendor> getRekananDokumenExpiredPaggingList(
			int iDisplayStart, int iDisplayLength, String keyword, int column, String sort) {
		
		Calendar cals = Calendar.getInstance();
        cals.add(Calendar.DATE, 30); //menghitung hari setelah hari ini
        Date tanggalSelanjutnya = cals.getTime(); //membuat variabel yang bertipe Date yang menyimpan  hari setelah hari ini
        
        String query = "SELECT t1 FROM DokumenRegistrasiVendor t1 " +
				"WHERE t1.tanggalBerakhir IS NOT NULL AND t1.tanggalBerakhir < :vDokTglSelanjutnya " +
				"AND t1.isDelete = 0 " +  
				"AND t1.vendor.isDelete = 0 " +  
				"AND ( " +
				"t1.vendor.nama LIKE :keyword OR " +
				"t1.namaDokumen  LIKE :keyword)" +
				"ORDER BY "+ this.columnRekananDokumenExpiredPaggingList(column)+" "+ (column == 0 ? "DESC " : sort);
		
        Query q = em.createQuery(query);
		return q.setParameter("vDokTglSelanjutnya", tanggalSelanjutnya)
				.setParameter("keyword", "%" + keyword + "%")
				.setFirstResult(iDisplayStart)
				.setMaxResults(iDisplayLength)
				.getResultList();
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Integer getRekananDokumenExpiredPaggingSize(String keyword) {
		
		Calendar cals = Calendar.getInstance();
        cals.add(Calendar.DATE, 30); //menghitung hari setelah hari ini
        Date tanggalSelanjutnya = cals.getTime(); //membuat variabel yang bertipe Date yang menyimpan  hari setelah hari ini

		String query = "SELECT COUNT(t1) FROM DokumenRegistrasiVendor t1 " +
				"WHERE t1.tanggalBerakhir IS NOT NULL AND t1.tanggalBerakhir < :vDokTglSelanjutnya " +
				"AND t1.isDelete = 0 " +  
				"AND t1.vendor.isDelete = 0 " +  
				"AND ( " +
				"t1.vendor.nama LIKE :keyword OR " +
				"t1.namaDokumen  LIKE :keyword)";
		Query q = em.createQuery(query);
		List list = q.setParameter("vDokTglSelanjutnya", tanggalSelanjutnya)
				.setParameter("keyword", "%" + keyword + "%")
				.getResultList();
		
		Integer result = 0;
		for (Object p : list) {
			result = Integer.valueOf(p.toString());
		}
		
		return result;
	}
	
	public List<DokumenRegistrasiVendor> deleteDokumenRegistrasiVendorByVendorId(int vendorId){
		Query q = em.createNamedQuery("DokumenRegistrasiVendor.deleteDokumenRegistrasiVendor");
		q.setParameter("vendorId", vendorId);
		q.executeUpdate();
		return null;
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
