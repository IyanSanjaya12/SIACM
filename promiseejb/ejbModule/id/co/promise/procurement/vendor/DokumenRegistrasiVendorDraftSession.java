package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.DokumenRegistrasiVendor;
import id.co.promise.procurement.entity.DokumenRegistrasiVendorDraft;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.VendorSKD;
import id.co.promise.procurement.entity.VendorSKDDraft;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Singleton
@LocalBean
public class DokumenRegistrasiVendorDraftSession extends AbstractFacadeWithAudit<DokumenRegistrasiVendorDraft> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB private DokumenRegistrasiVendorSession dokumenRegistrasiVendorSession;
	@EJB private VendorSKDSession vendorSKDSession;	
	@EJB private VendorSKDDraftSession vendorSKDDraftSession;
	
	public DokumenRegistrasiVendorDraftSession(){
		super(DokumenRegistrasiVendorDraft.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<DokumenRegistrasiVendorDraft> getDokumenRegistrasiVendorDraftList(){
		Query q = em.createNamedQuery("DokumenRegistrasiVendorDraft.findAll");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<DokumenRegistrasiVendorDraft> getDokumenRegistrasiVendorDraftByVendorId(int vendorId){
		Query q = em.createNamedQuery("DokumenRegistrasiVendorDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		List<DokumenRegistrasiVendorDraft> dokumenRegistrasiVendorDraft= q.getResultList();
		return dokumenRegistrasiVendorDraft;
	}
	
	@SuppressWarnings("unchecked")
	public List<DokumenRegistrasiVendorDraft> getDokumenRegistrasiVendorDraftByVendorIdAndSubject(int vendorId, String subject){
		Query q = em.createNamedQuery("DokumenRegistrasiVendorDraft.findByVendorAndSubject");
		q.setParameter("vendorId", vendorId);
		q.setParameter("subject", subject);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public DokumenRegistrasiVendorDraft getDokumenRegistrasiVendorDraftByVendorAndSubject(int vendorId, String subject){
		Query q = em.createNamedQuery("DokumenRegistrasiVendorDraft.findByVendorAndSubject");
		q.setParameter("vendorId", vendorId);
		q.setParameter("subject", subject);
		List<DokumenRegistrasiVendorDraft> dokumenRegistrasiVendorDraft = q.getResultList();
		if (dokumenRegistrasiVendorDraft != null && dokumenRegistrasiVendorDraft.size() > 0) {
			return dokumenRegistrasiVendorDraft.get(0);
		}
		return null;
	}
	
	public void DuplicateDokumenRegistrasiVendorDraft(int vendorId,Token token) {
		List <DokumenRegistrasiVendorDraft> dokumenRegistrasiVendorDraftList=getDokumenRegistrasiVendorDraftByVendorId(vendorId);
		if(dokumenRegistrasiVendorDraftList.size() <= 0){
			List<DokumenRegistrasiVendor> dokumenRegistrasiVendorList=dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorId(vendorId);	
			for(DokumenRegistrasiVendor dokumenRegistrasiVendor : dokumenRegistrasiVendorList){
				DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft = new DokumenRegistrasiVendorDraft();
				dokumenRegistrasiVendorDraft.setCreated(dokumenRegistrasiVendor.getCreated());
				dokumenRegistrasiVendorDraft.setDokumenRegistrasiVendor(dokumenRegistrasiVendor);
				dokumenRegistrasiVendorDraft.setFileName(dokumenRegistrasiVendor.getFileName());
				dokumenRegistrasiVendorDraft.setFileSize(dokumenRegistrasiVendor.getFileSize());
				dokumenRegistrasiVendorDraft.setNamaDokumen(dokumenRegistrasiVendor.getNamaDokumen());
				dokumenRegistrasiVendorDraft.setPathFile(dokumenRegistrasiVendor.getPathFile());
				dokumenRegistrasiVendorDraft.setStatusCeklist(dokumenRegistrasiVendor.getStatusCeklist());
				dokumenRegistrasiVendorDraft.setSubject(dokumenRegistrasiVendor.getSubject());
				dokumenRegistrasiVendorDraft.setTanggalBerakhir(dokumenRegistrasiVendor.getTanggalBerakhir());
				dokumenRegistrasiVendorDraft.setTanggalTerbit(dokumenRegistrasiVendor.getTanggalTerbit());
				dokumenRegistrasiVendorDraft.setVendor(dokumenRegistrasiVendor.getVendor());
				dokumenRegistrasiVendorDraft.setIsDuplicate(1);
				insertDokumenRegistrasiVendorDraft(dokumenRegistrasiVendorDraft,token);
			}
			/*duplikat data skd*/
			List <VendorSKDDraft> vendorSKDDraftList=vendorSKDDraftSession.getVendorSkdDraftByVendorId(vendorId);
			if(vendorSKDDraftList.size()<=0){
				VendorSKD vendorSKD= vendorSKDSession.getVendorSkdByVendor(vendorId);
				if(vendorSKD != null){
					VendorSKDDraft vendorSKDDraft = new VendorSKDDraft();
					vendorSKDDraft.setCreated(vendorSKD.getCreated());
					vendorSKDDraft.setAlamat(vendorSKD.getAlamat());
					vendorSKDDraft.setFileName(vendorSKD.getFileName());
					vendorSKDDraft.setFileSize(vendorSKD.getFileSize());
					vendorSKDDraft.setNamaDokumen(vendorSKD.getNamaDokumen());
					vendorSKDDraft.setPathFile(vendorSKD.getPathFile());
					vendorSKDDraft.setStatusCeklist(vendorSKD.getStatusCeklist());
					vendorSKDDraft.setTanggalBerakhir(vendorSKD.getTanggalBerakhir());
					vendorSKDDraft.setTanggalTerbit(vendorSKD.getTanggalTerbit());
					vendorSKDDraft.setVendorSKD(vendorSKD);
					vendorSKDDraft.setVendor(vendorSKD.getVendor());
					vendorSKDDraftSession.insertVendorSKDDraft(vendorSKDDraft, token);					
				}
			}			
		}		
	}
	
	public DokumenRegistrasiVendorDraft insertDokumenRegistrasiVendorDraft(DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft, Token token) {
		dokumenRegistrasiVendorDraft.setCreated(new Date());
		dokumenRegistrasiVendorDraft.setIsDelete(0);
		super.create(dokumenRegistrasiVendorDraft, AuditHelper.OPERATION_CREATE, token);
		return dokumenRegistrasiVendorDraft;
	}

	public DokumenRegistrasiVendorDraft updateDokumenRegistrasiVendorDraft(DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft, Token token) {
		dokumenRegistrasiVendorDraft.setUpdated(new Date());
		super.edit(dokumenRegistrasiVendorDraft, AuditHelper.OPERATION_UPDATE, token);
		return dokumenRegistrasiVendorDraft;
	}
	
	@SuppressWarnings("unchecked")
	public DokumenRegistrasiVendorDraft getDokumenRegistrasiVendorDraftByVendorIdAndDokumenRegistrasiVendor(Integer vendorId, Integer dokumenRegistrasiVendorId) {
		Query q = em.createNamedQuery("DokumenRegistrasiVendorDraft.findByVendorAndDokumenVendor");
		q.setParameter("vendorId", vendorId);
		q.setParameter("dokumenRegistrasiVendorId", dokumenRegistrasiVendorId);
    
		List<DokumenRegistrasiVendorDraft> dokumenRegistrasiVendorDraft= q.getResultList();
		if (dokumenRegistrasiVendorDraft!= null && dokumenRegistrasiVendorDraft.size() > 0) {
				return dokumenRegistrasiVendorDraft.get(0);
		}
			return null;
		}
	
	public DokumenRegistrasiVendorDraft deleteDokumenRegistrasiVendorDraft(int id, Token token) {
		DokumenRegistrasiVendorDraft dokumenRegistrasiVendor = super.find(id);
		dokumenRegistrasiVendor.setIsDelete(1);
		dokumenRegistrasiVendor.setDeleted(new Date());

		super.edit(dokumenRegistrasiVendor, AuditHelper.OPERATION_DELETE, token);
		return dokumenRegistrasiVendor;
	}

	public DokumenRegistrasiVendorDraft deleteRowDokumenRegistrasiVendorDraft(int id, Token token) {
		DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft = super.find(id);
		super.remove(dokumenRegistrasiVendorDraft, AuditHelper.OPERATION_ROW_DELETE, token);
		return dokumenRegistrasiVendorDraft;
	}
	
	public DokumenRegistrasiVendorDraft deleteRowDokumenRegistrasiVendorDraftByVendor(int vendorId, Token token) {
		Query q = em.createNamedQuery("DokumenRegistrasiVendorDraft.deleteByVendor");
		q.setParameter("vendorId", vendorId);
		q.executeUpdate();
		return null;
	}
	
	public List<DokumenRegistrasiVendorDraft> deleteDokumenRegistrasiVendorDraftByVendorId(int vendorId){
		Query q = em.createNamedQuery("DokumenRegistrasiVendorDraft.deleteDokumenRegistrasiVendorDraft");
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
