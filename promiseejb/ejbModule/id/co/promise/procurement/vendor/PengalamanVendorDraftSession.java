package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.PengalamanVendor;
import id.co.promise.procurement.entity.PengalamanVendorDraft;
import id.co.promise.procurement.entity.SegmentasiVendorDraft;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Wilayah;
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
public class PengalamanVendorDraftSession extends AbstractFacadeWithAudit<PengalamanVendorDraft> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public PengalamanVendorDraftSession(){
		super(PengalamanVendorDraft.class);
	}
	
	@SuppressWarnings("unchecked")
	public PengalamanVendorDraft getPengalamanVendorDraftByVendorIdAndPengalamanId(Integer vendorId, Integer pengalamanVendorId){
		Query q = em.createNamedQuery("PengalamanVendorDraft.findByPengalamanIdAndVendorId");
		q.setParameter("vendorId", vendorId);
		q.setParameter("pengalamanVendorId", pengalamanVendorId);
		
		List<PengalamanVendorDraft> pengalamanVendorDraft = q.getResultList();
		if (pengalamanVendorDraft != null && pengalamanVendorDraft.size() > 0) {
			return pengalamanVendorDraft.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public PengalamanVendorDraft getPengalamanVendorDraftByVendor(Integer vendorId){
		Query q = em.createNamedQuery("PengalamanVendorDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		List<PengalamanVendorDraft> pengalamanVendorDraft = q.getResultList();
		if (pengalamanVendorDraft != null && pengalamanVendorDraft.size() > 0) {
			return pengalamanVendorDraft.get(0);
		}
		return null;
	}
	
	public List<PengalamanVendorDraft> getPengalamanVendorDraftById(int vendorId) {
		Query q = em.createNamedQuery("PengalamanVendorDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	
	
	public List<PengalamanVendorDraft> getPengalamanVendorDraftByVendorIdAndTipePengalaman(int vendorId, String tipePengalaman){
		Query q = em.createNamedQuery("PengalamanVendorDraft.findByVendorAndTipePengalaman");
		q.setParameter("vendorId", vendorId);
		q.setParameter("tipePengalaman", tipePengalaman);
		return q.getResultList();
	}
	
	public Boolean checkNamaPengalamaan(String namaPekerjaan, String tipePengalaman, String toDo, Integer pengalamanVendorDraftId) {
		Query q = em.createNamedQuery("PengalamanVendorDraft.findNama");
		q.setParameter("namaPekerjaan", namaPekerjaan);
		q.setParameter("tipePengalaman", tipePengalaman);
		List<PengalamanVendorDraft> pengalamanVendorDraftList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("add")) {
			if (pengalamanVendorDraftList != null && pengalamanVendorDraftList.size() > 0) {
				
				isSave = false;
				
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("edit")) {
			if (pengalamanVendorDraftList != null && pengalamanVendorDraftList.size() > 0) {
				if (pengalamanVendorDraftId.equals(pengalamanVendorDraftList.get(0).getId())) {
					isSave = true;
				} else {
					isSave = false;
				}
			} else {
				isSave = true;
			}
		}

		return isSave;

	}


	public PengalamanVendorDraft insertPengalamanVendorDraft(PengalamanVendorDraft pengalamanVendorDraft, Token token) {
		pengalamanVendorDraft.setCreated(new Date());
		pengalamanVendorDraft.setIsDelete(0);
		super.create(pengalamanVendorDraft, AuditHelper.OPERATION_CREATE, token);
		return pengalamanVendorDraft;
	}

	public PengalamanVendorDraft updatePengalamanVendorDraft(PengalamanVendorDraft pengalamanVendorDraft, Token token) {
		pengalamanVendorDraft.setUpdated(new Date());
		super.edit(pengalamanVendorDraft, AuditHelper.OPERATION_UPDATE, token);
		return pengalamanVendorDraft;
	}

	public PengalamanVendorDraft deletePengalamanVendorDraft(int id, Token token) {
		PengalamanVendorDraft pengalamanVendorDraft = super.find(id);
		pengalamanVendorDraft.setIsDelete(1);
		pengalamanVendorDraft.setDeleted(new Date());

		super.edit(pengalamanVendorDraft, AuditHelper.OPERATION_DELETE, token);
		return pengalamanVendorDraft;
	}

	public PengalamanVendorDraft deleteRowPengalamanVendorDraft(int id, Token token) {
		PengalamanVendorDraft pengalamanVendorDraft = super.find(id);
		super.remove(pengalamanVendorDraft, AuditHelper.OPERATION_ROW_DELETE, token);
		return pengalamanVendorDraft;
	}
	
	public void deletePengalamanVendorDraftByVendorId(int vendorId){
		Query q = em.createNamedQuery("PengalamanVendorDraft.deleteByVendorId");
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
