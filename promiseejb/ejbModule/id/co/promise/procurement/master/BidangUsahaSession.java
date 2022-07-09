package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.BidangUsaha;
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
public class BidangUsahaSession extends AbstractFacadeWithAudit<BidangUsaha> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public BidangUsahaSession(){
		super(BidangUsaha.class);
	}
	
	public BidangUsaha getBidangUsaha(int id){
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<BidangUsaha> getBidangUsahaList(){
		Query q = em.createNamedQuery("BidangUsaha.find");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<BidangUsaha> getBidangUsahaForTreeList() {
		Query query = em.createQuery("select bu from BidangUsaha bu where bu.isDelete = 0 order by bu.parentId, bu.nama ");
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<BidangUsaha> getBidangUsahaByParentIdList(int id){
		Query q = em.createNamedQuery("BidangUsaha.findByParentId");
		q.setParameter("parentId", id);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkNamaBidangUsaha(String nama, String toDo, Integer bidangUsahaId) {
		Query q = em.createNamedQuery("BidangUsaha.findByNama");
		q.setParameter("nama", nama);
		List<BidangUsaha> bidangUsahaList = q.getResultList();
	  
		Boolean isSave = false;
		if(toDo.equalsIgnoreCase("insert")) {
			if(bidangUsahaList != null && bidangUsahaList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}
	   
		} else if (toDo.equalsIgnoreCase("update")) {
			if(bidangUsahaList != null && bidangUsahaList.size() > 0) {
				if(bidangUsahaId.equals(bidangUsahaList.get(0).getId())) {
					isSave = true;
				} else {
					isSave = false;
				}
			} else {
				isSave = true;
			}
		}
	  
		return isSave;
	  
	}
	
	public BidangUsaha insertBidangUsaha(BidangUsaha bu, Token token){
		bu.setCreated(new Date());
		bu.setIsDelete(0);
		super.create(bu, AuditHelper.OPERATION_CREATE, token);
		return bu;
	}
	
	public BidangUsaha updateBidangUsaha(BidangUsaha bu, Token token){
		bu.setUpdated(new Date());
		super.edit(bu, AuditHelper.OPERATION_UPDATE, token);
		return bu;
	}
	
	public BidangUsaha deleteBidangUsaha(int id, Token token){
		BidangUsaha bu = super.find(id);
		bu.setIsDelete(1);
		bu.setDeleted(new Date());
		super.edit(bu, AuditHelper.OPERATION_DELETE, token);
		return bu;
	}
	
	public BidangUsaha deleteRowBidangUsaha(int id, Token token){
		BidangUsaha bu = super.find(id);
		super.remove(bu, AuditHelper.OPERATION_ROW_DELETE, token);
		return bu;
	}
	
	@SuppressWarnings("unchecked")
	public BidangUsaha getBidangUsahaByNama(String nama) {
		Query q = em.createQuery("SELECT bidangUsaha FROM BidangUsaha bidangUsaha WHERE bidangUsaha.isDelete = 0 AND bidangUsaha.nama = :nama");
		q.setParameter("nama", nama);
		
		List<BidangUsaha> bidangUsahaList = q.getResultList();
		if (bidangUsahaList != null && bidangUsahaList.size() > 0) {
			return bidangUsahaList.get(0);
		}
		
		return null;
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public Boolean hasChildren(List<BidangUsaha> listBidangUsaha, int id){
		for (BidangUsaha x : listBidangUsaha){
			if (x.getParentId() == id){
				return true;
			}
		}
		return false;
	}
	
	public String parentBidangUsahaStruktur(List<BidangUsaha> listBidangUsaha, int id){
		String str = "[";
		int indeks = 0;
		for (BidangUsaha x : listBidangUsaha){
			if(x.getIsDelete() == 0){
				if(x.getParentId() == id){
					if(indeks>0){
						str += ",";
					}
					str += "{\"id\":"+x.getId()+",\"label\":\""+x.getNama()
							+"\",\"parentId\":\""+x.getParentId()+"\",\"Created\":\""+x.getCreated()+"\"";
					if (hasChildren(listBidangUsaha, x.getId())){
						str += ",\"children\":";
						str += parentBidangUsahaStruktur(listBidangUsaha, x.getId());
					}
					str += "}";
					indeks++;
				}
			}
		}
		str += "]";
		return str;
	}
	
	public String bidangUsahaGetAll(){
		String str = "{\"data\":";
		str += parentBidangUsahaStruktur(findAll(), 0);
		str += "}";
		return str;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
}
