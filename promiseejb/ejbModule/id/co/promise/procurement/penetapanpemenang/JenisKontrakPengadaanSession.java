package id.co.promise.procurement.penetapanpemenang;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.JenisKontrakPengadaan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class JenisKontrakPengadaanSession extends AbstractFacadeWithAudit<JenisKontrakPengadaan> {
	@PersistenceContext(unitName = "promisePU")
	EntityManager em;@Override
	protected EntityManager getEntityManager() {
		return em;
	}	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	public JenisKontrakPengadaanSession(){
		super(JenisKontrakPengadaan.class);
	}
	
	public List<JenisKontrakPengadaan> findAll(){
		Query q = em.createNamedQuery("JenisKontrakPengadaan.findAll");
		return q.getResultList();
	}	
	
	public List<JenisKontrakPengadaan> findByPengadaan(int pengadaanId){
		Query q = em.createNamedQuery("JenisKontrakPengadaan.findByPengadaan")
				.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}
	
	public JenisKontrakPengadaan createJenisKontrakPengadaan(JenisKontrakPengadaan jkp, Token token){
		jkp.setIsDelete(0);
		jkp.setCreated(new Date());
		jkp.setUserId(token.getUser().getId());
		super.create(jkp, AuditHelper.OPERATION_CREATE, token);
		return jkp;
	}
	

	public JenisKontrakPengadaan updatefJenisKontrakPengadaan(JenisKontrakPengadaan jkp, Token token){
		jkp.setUpdated(new Date());
		super.edit(jkp, AuditHelper.OPERATION_UPDATE, token);
		return jkp;
	}
}
