package id.co.promise.procurement.pengumumanhasilevladmintek;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.PengumumanHasilEvaluasiAdminTeknis;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;
@Stateless
@LocalBean
public class PengumumanHasilEvaluasiAdminTeknisSession extends AbstractFacadeWithAudit<PengumumanHasilEvaluasiAdminTeknis> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public PengumumanHasilEvaluasiAdminTeknisSession() {
		super(PengumumanHasilEvaluasiAdminTeknis.class);
	}
	
	public PengumumanHasilEvaluasiAdminTeknis findByPengadaan(int pengadaanId){
		Query q = em.createNamedQuery("PengumumanHasilEvaluasiAdminTeknis.findByPengadaan");
		return (PengumumanHasilEvaluasiAdminTeknis) q.getSingleResult();
	}
	
	public PengumumanHasilEvaluasiAdminTeknis insertPengumuman(PengumumanHasilEvaluasiAdminTeknis pheat, Token token){
		pheat.setCreated(new Date());
		pheat.setIsDelete(0);
		super.create(pheat, AuditHelper.OPERATION_CREATE, token);
		return pheat;
	}
	
	public PengumumanHasilEvaluasiAdminTeknis editPengumuman(PengumumanHasilEvaluasiAdminTeknis pheat, Token token){
		pheat.setUpdated(new Date());
		super.edit(pheat, AuditHelper.OPERATION_CREATE, token);
		return pheat;
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
