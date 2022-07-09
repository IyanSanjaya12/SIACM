package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.BuktiKepemilikan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class BuktiKepemilikanSession extends AbstractFacadeWithAudit<BuktiKepemilikan> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public BuktiKepemilikanSession() {
		super(BuktiKepemilikan.class);
	}

	public BuktiKepemilikan getBuktiKepemilikan(int id){
		return super.find(id);
	}
	
	public List<BuktiKepemilikan> getBuktiKepemilikanList(){
		Query q = em.createNamedQuery("BuktiKepemilikan.find");
		return q.getResultList();
	}
	
	public BuktiKepemilikan insertBuktiKepemilikan(BuktiKepemilikan bk, Token token){
		bk.setCreated(new Date());
		bk.setIsDelete(0);
		super.create(bk, AuditHelper.OPERATION_CREATE, token);
		return bk;
	}
	
	public BuktiKepemilikan updateBuktiKepemilikan(BuktiKepemilikan bk, Token token){
		bk.setUpdated(new Date());
		super.edit(bk, AuditHelper.OPERATION_UPDATE, token);
		return bk;				
	}
	
	public BuktiKepemilikan deleteBuktiKepemilikan(int id, Token token){
		BuktiKepemilikan bk = super.find(id);
		bk.setIsDelete(1);
		bk.setDeleted(new Date());
		super.edit(bk, AuditHelper.OPERATION_DELETE, token);
		return bk;
	}
	
	public BuktiKepemilikan deleteRowBuktiKepemilikan(int id, Token token){
		BuktiKepemilikan bk = super.find(id);
		super.remove(bk, AuditHelper.OPERATION_ROW_DELETE, token);
		return bk;
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
