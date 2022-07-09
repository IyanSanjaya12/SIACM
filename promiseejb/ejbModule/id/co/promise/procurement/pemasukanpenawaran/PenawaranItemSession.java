package id.co.promise.procurement.pemasukanpenawaran;

import id.co.promise.procurement.entity.PenawaranItem;
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
public class PenawaranItemSession extends AbstractFacadeWithAudit<PenawaranItem>{

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public PenawaranItemSession(){
		super(PenawaranItem.class);
	}
	
	public PenawaranItem getPenawaranItem(int id){
		return super.find(id);
	}
	
	public List<PenawaranItem> getPenawaranItemList(){
		Query q = em.createNamedQuery("PenawaranItem.find");
		return q.getResultList();
	}
	
	public List<PenawaranItem> getPenawaranItemListByItemAndPengadaan(int itemId, int pengadaanId){
		Query q = em.createNamedQuery("PenawaranItem.findByItemIdAndPengadaanId");
		q.setParameter("itemId", itemId);
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}
	
	public List<PenawaranItem> getListPenawaranItemByVendorAndPengadaan(int vendorId, int pengadaanId){
		Query q = em.createNamedQuery("PenawaranItem.findByVendorIdAndPengadaanId");
		q.setParameter("vendorId", vendorId);
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}
	
	
	public PenawaranItem insertPenawaranItem(PenawaranItem penawaranItem, Token token){
		penawaranItem.setCreated(new Date());
		penawaranItem.setIsDelete(0);
		super.create(penawaranItem, AuditHelper.OPERATION_CREATE, token);
		return penawaranItem;
	}
	
	public PenawaranItem updatePenawaranItem(PenawaranItem penawaranItem, Token token){
		penawaranItem.setUpdated(new Date());
		super.edit(penawaranItem, AuditHelper.OPERATION_UPDATE, token);
		return penawaranItem;
	}
	
	public PenawaranItem deletePenawaranItem(int id, Token token){
		PenawaranItem penawaranItem = super.find(id);
		penawaranItem.setIsDelete(1);
		penawaranItem.setDeleted(new Date());
		super.edit(penawaranItem, AuditHelper.OPERATION_DELETE, token);
		return penawaranItem;
	}
	
	public PenawaranItem deleteRowPenawaranItem(int id, Token token){
		PenawaranItem penawaranItem = super.find(id);
		super.remove(penawaranItem, AuditHelper.OPERATION_ROW_DELETE, token);
		return penawaranItem;
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
