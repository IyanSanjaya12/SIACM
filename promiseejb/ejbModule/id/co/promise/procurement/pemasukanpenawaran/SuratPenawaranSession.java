package id.co.promise.procurement.pemasukanpenawaran;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.SuratPenawaran;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class SuratPenawaranSession extends AbstractFacadeWithAudit<SuratPenawaran>{

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public SuratPenawaranSession(){
		super(SuratPenawaran.class);
	}
	
	public SuratPenawaran getSuratPenawaran(int id){
		return super.find(id);
	}
	
	public List<SuratPenawaran> getSuratPenawaranList(){
		Query q = em.createNamedQuery("SuratPenawaran.find");
		return q.getResultList();
	}
	
	public List<SuratPenawaran> getListSuratPenawaranByPengadaan(int pengadaanId){
		Query q = em.createNamedQuery("SuratPenawaran.findByPengadaanId");
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}
	
	public List<SuratPenawaran> getSuratPenawaranByPengadaanNilaiJaminanList(int pengadaanId){
		Query q = em.createNamedQuery("SuratPenawaran.findByPengadaanIdNilaiJaminan");
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}
	
	public List<SuratPenawaran> getListSuratPenawaranByVendorAndPengadaan(int vendorId, int pengadaanId){
		Query q = em.createNamedQuery("SuratPenawaran.findByVendorIdAndPengadaanId");
		q.setParameter("vendorId", vendorId);
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}
	
	public SuratPenawaran insertSuratPenawaran(SuratPenawaran suratPenawaran, Token token){
		suratPenawaran.setCreated(new Date());
		suratPenawaran.setIsDelete(0);
		suratPenawaran.setUserId(token.getUser().getId());
		super.create(suratPenawaran, AuditHelper.OPERATION_CREATE, token);
		return suratPenawaran;
	}
	
	public SuratPenawaran updateSuratPenawaran(SuratPenawaran suratPenawaran, Token token){
		suratPenawaran.setUpdated(new Date());
		super.edit(suratPenawaran, AuditHelper.OPERATION_UPDATE, token);
		return suratPenawaran;
	}
	
	public SuratPenawaran deleteSuratPenawaran(int id, Token token){
		SuratPenawaran suratPenawaran = super.find(id);
		suratPenawaran.setIsDelete(1);
		suratPenawaran.setDeleted(new Date());
		super.edit(suratPenawaran, AuditHelper.OPERATION_DELETE, token);
		return suratPenawaran;
	}
	
	public SuratPenawaran deleteRowSuratPenawaran(int id, Token token){
		SuratPenawaran suratPenawaran = super.find(id);
		super.remove(suratPenawaran, AuditHelper.OPERATION_ROW_DELETE, token);
		return suratPenawaran;
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}
}
