package id.co.promise.procurement.inisialisasi;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.ItemPengadaan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Singleton
@LocalBean
public class ItemPengadaanSession extends
		AbstractFacadeWithAudit<ItemPengadaan> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public ItemPengadaanSession() {
		super(ItemPengadaan.class);
	}

	public ItemPengadaan getItemPengadaan(int id) {
		return super.find(id);
	}

	public List<ItemPengadaan> getItemPengadanByPengadaanId(int pengadaanId) {
		Query q = em.createNamedQuery("ItemPengadaan.findByPengadaanId");
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}
	
	public ItemPengadaan getByPengadaanIdAndItemId(int pengadaanId, int itemId){
		Query q = em.createNamedQuery("ItemPengadaan.findByPengadaanIdAndItemId");
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("itemId", itemId);
		List<ItemPengadaan> itemPengadaanList = q.getResultList();
		if(itemPengadaanList.size()>0){
			return itemPengadaanList.get(0);
		} else {
			return new ItemPengadaan();
		}
	}

	public List<ItemPengadaan> getItemPengadaanMaterialByPengadaanId(
			int pengadaanId) {
		Query q = em
				.createNamedQuery("ItemPengadaan.findTipeItemByPengadaanId");
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("itemTypeId", 1);
		return q.getResultList();
	}

	public List<ItemPengadaan> getItemPengadaanJasaByPengadaanId(int pengadaanId) {
		Query q = em
				.createNamedQuery("ItemPengadaan.findTipeItemByPengadaanId");
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("itemTypeId", 2);
		return q.getResultList();
	}
	
	public double getItemPengadaanTotalHPSByPengadaan(int pengadaanId){
		Query q = em.createNamedQuery("ItemPengadaan.getByPengadaan").setParameter("pengadaanId", pengadaanId);
		double rs = Double.parseDouble(q.getSingleResult().toString());
		return rs;
	}

	public List getTotalNilaiHPSPengadanByOrganisasiId(
			List<Integer> divisiIdList, Date periodeAwal, Date periodeAkhir,
			int jenisPengadaan, int metodePengadaan, int mataUang) {
		String queryString = "SELECT SUM(itemPengadaan.totalHPS) "
				+ "FROM ItemPengadaan itemPengadaan "
				+ "WHERE itemPengadaan.isDelete = 0 AND itemPengadaan.pengadaan.isDelete = 0 AND itemPengadaan.pengadaan.panitia.divisi.id IN (:divisiIdList) ";

		if (periodeAwal != null) {
			queryString += "AND itemPengadaan.pengadaan.tanggalNotaDinas >= :periodeAwal ";
		}
		if (periodeAkhir != null) {
			queryString += "AND itemPengadaan.pengadaan.tanggalNotaDinas <= :periodeAkhir ";
		}
		if (jenisPengadaan > 0) {
			queryString += "AND itemPengadaan.pengadaan.jenisPengadaan.id = :jenisPengadaan ";
		}
		if (metodePengadaan > 0) {
			queryString += "AND itemPengadaan.pengadaan.metodePengadaan.id = :metodePengadaan ";
		}
		if (mataUang > 0) {
			queryString += "AND itemPengadaan.pengadaan.mataUang.id = :mataUang ";
		}
		Query q = em.createQuery(queryString).setParameter("divisiIdList",
				divisiIdList);
		if (periodeAwal != null) {
			q.setParameter("periodeAwal", periodeAwal);
		}
		if (periodeAkhir != null) {
			q.setParameter("periodeAkhir", periodeAkhir);
		}
		if (jenisPengadaan > 0) {
			q.setParameter("jenisPengadaan", jenisPengadaan);
		}
		if (metodePengadaan > 0) {
			q.setParameter("metodePengadaan", metodePengadaan);
		}
		if (mataUang > 0) {
			q.setParameter("mataUang", mataUang);
		}
		return q.getResultList();
	}

	public List<ItemPengadaan> getItemPengadaanList() {
		Query q = em.createNamedQuery("ItemPengadaan.find");
		return q.getResultList();
	}

	public ItemPengadaan insertItemPengadaan(ItemPengadaan itemPengadaan,
			Token token) {
		itemPengadaan.setCreated(new Date());
		itemPengadaan.setIsDelete(0);
		super.create(itemPengadaan, AuditHelper.OPERATION_CREATE, token);
		return itemPengadaan;
	}

	public ItemPengadaan updateItemPengadaan(ItemPengadaan itemPengadaan,
			Token token) {
		itemPengadaan.setUpdated(new Date());
		super.edit(itemPengadaan, AuditHelper.OPERATION_UPDATE, token);
		return itemPengadaan;
	}

	public ItemPengadaan deleteItemPengadaan(int id, Token token) {
		ItemPengadaan itemPengadaan = super.find(id);
		itemPengadaan.setIsDelete(1);
		itemPengadaan.setDeleted(new Date());
		super.edit(itemPengadaan, AuditHelper.OPERATION_DELETE, token);
		return itemPengadaan;
	}

	public ItemPengadaan deleteRowItemPengadaan(int id, Token token) {
		ItemPengadaan itemPengadaan = super.find(id);
		super.remove(itemPengadaan, AuditHelper.OPERATION_ROW_DELETE, token);
		return itemPengadaan;
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
