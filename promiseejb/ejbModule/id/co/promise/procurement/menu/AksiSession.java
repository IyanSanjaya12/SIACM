package id.co.promise.procurement.menu;

import id.co.promise.procurement.entity.Aksi;
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
public class AksiSession extends AbstractFacadeWithAudit<Aksi> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public AksiSession() {
		super(Aksi.class);
	}

	public Aksi getAksi(int id) {
		return super.find(id);
	}

	public List<Aksi> getAksiList() {
		Query q = em.createNamedQuery("Aksi.find");
		return q.getResultList();
	}

	public List<Aksi> getPublicAksiList() {
		Query q = em.createNamedQuery("Aksi.findPublicList");
		return q.getResultList();
	}

	public List<Aksi> getAksiListByRole(Integer roleId) {
		Query q = em.createNamedQuery("Aksi.findListByRole").setParameter("roleId", roleId);
		return q.getResultList();
	}

	public List<Aksi> getAksiListByPath(String path) {
		Query q = em.createNamedQuery("Aksi.findListByPath").setParameter("path", path);
		return q.getResultList();
	}

	public Aksi insertAksi(Aksi a, Token token) {
		a.setCreated(new Date());
		a.setIsDelete(0);
		super.create(a, AuditHelper.OPERATION_CREATE, token);
		return a;
	}

	public Aksi updateAksi(Aksi a, Token token) {
		a.setUpdated(new Date());
		super.edit(a, AuditHelper.OPERATION_UPDATE, token);
		return a;
	}

	public Aksi deleteAksi(int aksiId, Token token) {
		Aksi a = super.find(aksiId);
		a.setIsDelete(1);
		a.setDeleted(new Date());
		super.edit(a, AuditHelper.OPERATION_DELETE, token);
		return a;
	}

	public Aksi deleteRowAksi(int aksiId, Token token) {
		Aksi a = super.find(aksiId);
		super.remove(a, AuditHelper.OPERATION_ROW_DELETE, token);
		return a;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	public long countFindByToken(String keyword) {
		String queryStr = "SELECT COUNT(o) FROM Aksi o WHERE o.isDelete=0 AND "
				+ "o.path like :keyword";
		Query query = em.createQuery(queryStr);
		query.setParameter("keyword", keyword);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}

	public long countTotalFindByToken(Token tokenObj) {
		String queryStr = "SELECT COUNT(o) FROM Aksi o WHERE o.isDelete=0";
		Query query = em.createQuery(queryStr);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}

	public Object findByToken(Integer start, Integer length, String keyword, Token tokenObj, Integer columnOrder,
			String tipeOrder) {
		String queryUser = "SELECT o FROM Aksi o WHERE o.isDelete=0 AND " 
				+ "o.path like :keyword";
		
		String[] columnToView = {"nomor","path","isPublic"};
		if (columnOrder > 0) {
			queryUser+=" ORDER BY o. "+columnToView[columnOrder] + " " + tipeOrder;
		} else {
			queryUser+=" ORDER BY o.id desc ";
		}
		
		Query query = em.createQuery(queryUser);	
		query.setParameter("keyword", keyword);
		query.setFirstResult(start);
		query.setMaxResults(length);
		List<Aksi> aksiList = query.getResultList();
		return aksiList;
	}

	public List<Aksi> searchPath(String path, int max) {
		Query q = em.createQuery("SELECT o FROM Aksi o WHERE o.isDelete=0 AND o.path like :path");
		q.setParameter("path", "%"+path+"%");
		q.setMaxResults(max);
		return q.getResultList();
	}
}
