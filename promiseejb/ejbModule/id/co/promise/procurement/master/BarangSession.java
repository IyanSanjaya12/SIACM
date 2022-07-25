package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.siacm.Barang;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class BarangSession extends AbstractFacadeWithAudit<Barang>{
	
	public BarangSession() {
		super(Barang.class);
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
	
	public Barang getbarang(int id) {
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Barang> getbarangList(){
		Query query = em.createQuery("SELECT barang FROM Barang barang WHERE barang.isDelete = 0");
		return query.getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<Barang> getbarangListMinimum(){
		Query query = em.createQuery("SELECT barang FROM Barang barang WHERE barang.isDelete = 0 AND barang.jumlah <= (barang.stokMinimal+1)");
		return query.getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<Barang> getListNotSelect(List<Integer> idList){
		Query query = em.createQuery("SELECT barang FROM Barang barang WHERE barang.isDelete = 0 AND barang.id NOT IN (:selectList)");
		if(idList.size() > 0) {
			query.setParameter("selectList", idList);			
		}else {
			query.setParameter("selectList", Constant.ZERO_VALUES);
		}
		return query.getResultList();
	}
	
	public Barang insert(Barang barang, Token token) {
		barang.setCreated(new Date());
		barang.setIsDelete(0);
		super.create(barang, AuditHelper.OPERATION_CREATE, token);
		return barang;
	}

	public Barang update(Barang barang, Token token) {
		barang.setIsDelete(0);
		barang.setUpdated(new Date());
		super.edit(barang, AuditHelper.OPERATION_UPDATE, token);
		return barang;
	}

	public Barang delete(int id, Token token) {
		Barang barang = super.find(id);
		barang.setIsDelete(1);
		barang.setDeleted(new Date());
		super.edit(barang, AuditHelper.OPERATION_DELETE, token);
		return barang;
	}
}
