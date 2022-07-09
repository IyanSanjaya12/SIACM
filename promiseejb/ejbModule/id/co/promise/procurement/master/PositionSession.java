package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.siacm.Position;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class PositionSession extends AbstractFacadeWithAudit<Position>{
	public PositionSession() {
		super(Position.class);
	}

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	public Position getPosition(int id) {
		return super.find(id);
	}
	
	public Position getposition(int id) {
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Position> getpositionList(){
		Query query = em.createQuery("SELECT position FROM Position position WHERE position.isDelete = 0");
		return query.getResultList();
	}
	
	public Position insert(Position position, Token token) {
		position.setCreated(new Date());
		position.setIsDelete(0);
		super.create(position, AuditHelper.OPERATION_CREATE, token);
		return position;
	}

	public Position update(Position position, Token token) {
		position.setUpdated(new Date());
		super.edit(position, AuditHelper.OPERATION_UPDATE, token);
		return position;
	}

	public Position delete(int id, Token token) {
		Position position = super.find(id);
		position.setIsDelete(1);
		position.setDeleted(new Date());
		super.edit(position, AuditHelper.OPERATION_DELETE, token);
		return position;
	}
}
