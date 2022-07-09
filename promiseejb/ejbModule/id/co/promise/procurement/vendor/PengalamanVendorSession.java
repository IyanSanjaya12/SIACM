package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.PengalamanVendor;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.VendorPIC;
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
public class PengalamanVendorSession extends AbstractFacadeWithAudit<PengalamanVendor> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public PengalamanVendorSession(){
		super(PengalamanVendor.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<PengalamanVendor> getPengalamanVendorByVendorId(int vendorId){
		Query q = em.createNamedQuery("PengalamanVendor.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public PengalamanVendor getPengalamanVendorByVendor(Integer vendorId){
		Query q = em.createNamedQuery("PengalamanVendor.findByVendor");
		q.setParameter("vendorId", vendorId);
		List<PengalamanVendor> pengalamanVendor = q.getResultList();
		if (pengalamanVendor != null && pengalamanVendor.size() > 0) {
			return pengalamanVendor.get(0);
		}
		return null;
	}
	
	public List<PengalamanVendor> getPengalamanVendorById(int vendorId) {
		Query q = em.createNamedQuery("PengalamanVendor.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	public List<PengalamanVendor> getPengalamanVendorByVendorIdAndTipePengalaman(int vendorId, String tipePengalaman){
		Query q = em.createNamedQuery("PengalamanVendor.findByVendorAndTipePengalaman");
		q.setParameter("vendorId", vendorId);
		q.setParameter("tipePengalaman", tipePengalaman);
		return q.getResultList();
	}
	
	public Boolean checkNamaPengalamaan(String namaPekerjaan, String tipePengalaman, String toDo, Integer pengalamanVendorId) {
		Query q = em.createNamedQuery("PengalamanVendor.findNama");
		q.setParameter("namaPekerjaan", namaPekerjaan);
		q.setParameter("tipePengalaman", tipePengalaman);
		List<PengalamanVendor> pengalamanVendorList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (pengalamanVendorList != null && pengalamanVendorList.size() > 0) {
				
				isSave = false;
				
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (pengalamanVendorList != null && pengalamanVendorList.size() > 0) {
				if (pengalamanVendorId.equals(pengalamanVendorList.get(0).getId())) {
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


	public PengalamanVendor insertPengalamanVendor(PengalamanVendor pengalamanVendor, Token token) {
		pengalamanVendor.setCreated(new Date());
		pengalamanVendor.setIsDelete(0);
		super.create(pengalamanVendor, AuditHelper.OPERATION_CREATE, token);
		return pengalamanVendor;
	}

	public PengalamanVendor updatePengalamanVendor(PengalamanVendor pengalamanVendor, Token token) {
		pengalamanVendor.setUpdated(new Date());
		super.edit(pengalamanVendor, AuditHelper.OPERATION_UPDATE, token);
		return pengalamanVendor;
	}

	public PengalamanVendor deletePengalamanVendor(int id, Token token) {
		PengalamanVendor pengalamanVendor = super.find(id);
		pengalamanVendor.setIsDelete(1);
		pengalamanVendor.setDeleted(new Date());

		super.edit(pengalamanVendor, AuditHelper.OPERATION_DELETE, token);
		return pengalamanVendor;
	}

	public PengalamanVendor deleteRowPengalamanVendor(int id, Token token) {
		PengalamanVendor pengalamanVendor = super.find(id);
		super.remove(pengalamanVendor, AuditHelper.OPERATION_ROW_DELETE, token);
		return pengalamanVendor;
	}
	
	public void deletePengalamanVendorByVendorId(int vendorId){
		Query q = em.createNamedQuery("PengalamanVendor.deleteByVendorId");
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
