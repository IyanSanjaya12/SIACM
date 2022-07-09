package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.RoleJabatan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class RoleJabatanSession extends AbstractFacadeWithAudit<RoleJabatan> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public RoleJabatanSession() {
		super(RoleJabatan.class);
	}
	
	public RoleJabatan getRoleJabatan(int id){
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleJabatan> getList(){
		Query q = em.createNamedQuery("RoleJabatan.find");
		return q.getResultList();
	}
	
	public RoleJabatan insert(RoleJabatan rj, Token token){
		rj.setCreated(new Date());
		rj.setIsDelete(0);
		super.create(rj, AuditHelper.OPERATION_CREATE, token);
		return rj;
	}
	
	public RoleJabatan update(RoleJabatan rj, Token token){
		rj.setUpdated(new Date());
		super.edit(rj, AuditHelper.OPERATION_UPDATE, token);
		return rj;
	}
	
	public RoleJabatan delete(int roleJabatanId, Token token) {
		RoleJabatan rj = super.find(roleJabatanId);
		rj.setIsDelete(1);
		rj.setDeleted(new Date());
		super.edit(rj, AuditHelper.OPERATION_DELETE, token);
		return rj;
	}
	
	public RoleJabatan deleteRow(int roleJabatanId, Token token) {
		RoleJabatan rj = super.find(roleJabatanId);
		super.remove(rj, AuditHelper.OPERATION_ROW_DELETE, token);
		return rj;
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}
	
	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	public List<RoleJabatan> getListByJabatanId(Integer jabatanId) {
		Query q = em.createNamedQuery("RoleJabatan.getListByJabatanId");
		q.setParameter("jabatanId", jabatanId);
		return q.getResultList();
	}
}
