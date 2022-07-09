package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.SertifikatVendorRiwayat;
import id.co.promise.procurement.entity.Token;
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
public class SertifikatVendorRiwayatSession extends AbstractFacadeWithAudit<SertifikatVendorRiwayat> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public SertifikatVendorRiwayatSession(){
		super(SertifikatVendorRiwayat.class);
	} 

	public SertifikatVendorRiwayat insertSertifikatVendorRiwayat(SertifikatVendorRiwayat SertifikatVendorRiwayat, Token token) {
		SertifikatVendorRiwayat.setCreated(new Date());
		SertifikatVendorRiwayat.setIsDelete(0);
		super.create(SertifikatVendorRiwayat, AuditHelper.OPERATION_CREATE, token);
		return SertifikatVendorRiwayat;
	}

	public SertifikatVendorRiwayat updateSertifikatVendorRiwayat(SertifikatVendorRiwayat SertifikatVendorRiwayat, Token token) {
		SertifikatVendorRiwayat.setUpdated(new Date());
		super.edit(SertifikatVendorRiwayat, AuditHelper.OPERATION_UPDATE, token);
		return SertifikatVendorRiwayat;
	}

	public SertifikatVendorRiwayat deleteSertifikatVendorRiwayat(int id, Token token) {
		SertifikatVendorRiwayat SertifikatVendorRiwayat = super.find(id);
		SertifikatVendorRiwayat.setIsDelete(1);
		SertifikatVendorRiwayat.setDeleted(new Date());

		super.edit(SertifikatVendorRiwayat, AuditHelper.OPERATION_DELETE, token);
		return SertifikatVendorRiwayat;
	}

	public SertifikatVendorRiwayat deleteRowSertifikatVendorRiwayat(int id, Token token) {
		SertifikatVendorRiwayat SertifikatVendorRiwayat = super.find(id);
		super.remove(SertifikatVendorRiwayat, AuditHelper.OPERATION_ROW_DELETE, token);
		return SertifikatVendorRiwayat;
	}

	@SuppressWarnings("unchecked")
	public List<SertifikatVendorRiwayat> getSertifikatVendorRiwayatByVendor(Integer id) {
		Query q = em.createQuery("SELECT t1 FROM SertifikatVendorRiwayat t1 WHERE t1.vendor.id = :id ORDER BY t1.id DESC ");
		q.setParameter("id", id);
		List<SertifikatVendorRiwayat> SertifikatVendorRiwayatList = q.getResultList();
		if(SertifikatVendorRiwayatList == null){
			return null;
		}else{
			if(SertifikatVendorRiwayatList.size() > 0)
				return SertifikatVendorRiwayatList;
			else
				return null;
		}
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
