package id.co.promise.procurement.menu;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.Aksi;
import id.co.promise.procurement.entity.MenuAksi;
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
public class MenuAksiSession extends AbstractFacadeWithAudit<MenuAksi> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public MenuAksiSession() {
		super(MenuAksi.class);
	}
	
	public MenuAksi getMenuAksi(int id){
		return super.find(id);
	}
	
	public List<MenuAksi> getMenuAksiList(){
		Query q = em.createNamedQuery("MenuAksi.find");
		return q.getResultList();
	}
	
	public List<MenuAksi> getMenuAksiListByMenuAksi(Integer menuId, Integer aksiId){
		Query q = em.createNamedQuery("MenuAksi.findListByMenuAksi")
				.setParameter("menuId", menuId).setParameter("aksiId", aksiId);
		return q.getResultList();
	}
		
	public MenuAksi insertMenuAksi(MenuAksi ma, Token token){
		ma.setCreated(new Date());
		ma.setIsDelete(0);
		super.create(ma, AuditHelper.OPERATION_CREATE, token);
		return ma;
	}
	
	public MenuAksi updateMenuAksi(MenuAksi ma, Token token){
		ma.setUpdated(new Date());
		super.edit(ma, AuditHelper.OPERATION_UPDATE, token);
		return ma;
	}
	
	public MenuAksi deleteMenuAksi(int menuAksiId, Token token) {
		MenuAksi ma = super.find(menuAksiId);
		ma.setIsDelete(1);
		ma.setDeleted(new Date());
		super.edit(ma, AuditHelper.OPERATION_DELETE, token);
		return ma;
	}
	
	public MenuAksi deleteRowMenuAksi(int menuAksiId, Token token) {
		MenuAksi ma = super.find(menuAksiId);
			super.remove(ma, AuditHelper.OPERATION_ROW_DELETE, token);
		return ma;
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

	public long countFindByToken(String keyword) {
		String queryStr = "SELECT COUNT(o) FROM MenuAksi o WHERE o.isDelete=0 AND "
				+ "(o.menu.nama like :keyword OR o.aksi.path like :keyword)";
		Query query = em.createQuery(queryStr);
		query.setParameter("keyword", keyword);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}

	public long countTotalFindByToken(Token tokenObj) {
		String queryStr = "SELECT COUNT(o) FROM MenuAksi o WHERE o.isDelete=0";
		Query query = em.createQuery(queryStr);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}

	public Object findByToken(Integer start, Integer length, String keyword, Token tokenObj, Integer columnOrder,
			String tipeOrder) {
		String queryUser = "SELECT o FROM MenuAksi o WHERE o.isDelete=0 AND " 
				+ "(o.menu.nama like :keyword "
				+ "OR o.aksi.path like :keyword) ";
		
		String[] columnToView = {"nomor","menu.nama", "aksi.path"};
		if (columnOrder > 0) {
			queryUser+=" ORDER BY o. "+columnToView[columnOrder] + " " + tipeOrder;
		} else {
			queryUser+=" ORDER BY o.id desc ";
		}
		
		Query query = em.createQuery(queryUser);	
		query.setParameter("keyword", keyword);
		query.setFirstResult(start);
		query.setMaxResults(length);
		List<MenuAksi> aksiList = query.getResultList();
		return aksiList;
	}

	
}
