package id.co.promise.procurement.kontrakmanajemen;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.AnggotaPanitia;
import id.co.promise.procurement.entity.Kontrak;
import id.co.promise.procurement.entity.PejabatPelaksanaPengadaan;
import id.co.promise.procurement.entity.Pengadaan;
import id.co.promise.procurement.entity.PerolehanPengadaanSatuan;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.PurchaseRequestPengadaan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.inisialisasi.PurchaseRequestPengadaanSession;
import id.co.promise.procurement.master.AnggotaPanitiaSession;
import id.co.promise.procurement.master.PejabatPelaksanaPengadaanSession;
import id.co.promise.procurement.perolehan.PerolehanPengadaanSatuanSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class KontrakSession extends AbstractFacadeWithAudit<Kontrak> {
	@EJB
	private AnggotaPanitiaSession anggotaPanitiaSession;
	@EJB
	private PejabatPelaksanaPengadaanSession pejabatPelaksanaSession;
	@EJB
	private PurchaseRequestSession prSession;
	@EJB
	private PurchaseRequestPengadaanSession prPengadaanSession;
	@EJB
	private PurchaseRequestItemSession prItemSession;
	@EJB
	private PerolehanPengadaanSatuanSession perolehanPengadaanSession;

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public KontrakSession() {
		super(Kontrak.class);
	}

	public Kontrak getKontrak(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<Kontrak> getKontrakList() {
		Query q = em.createNamedQuery("Kontrak.find");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Kontrak> getLastKontrakList() {
		Query q = em.createNamedQuery("Kontrak.findLastKontrak");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Kontrak> getKontrakListByPengadaanId(int pengadaanId) {
		Query q = em.createNamedQuery("Kontrak.findKontrakByPengadaanId");
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Kontrak> getKontrakListByVendorId(int vendorId) {
		Query q = em.createNamedQuery("Kontrak.findKontrakByVendorId");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}

	public Kontrak insertKontrak(Kontrak kontrak, Token token) {
		kontrak.setCreated(new Date());
		kontrak.setIsDelete(0);
		super.create(kontrak, AuditHelper.OPERATION_CREATE, token);
		return kontrak;
	}

	public Kontrak updateKontrak(Kontrak kontrak, Token token) {
		kontrak.setUpdated(new Date());
		super.edit(kontrak, AuditHelper.OPERATION_UPDATE, token);
		return kontrak;
	}

	public Kontrak deleteKontrak(int id, Token token) {
		Kontrak kontrak = super.find(id);
		kontrak.setIsDelete(1);
		kontrak.setDeleted(new Date());

		super.edit(kontrak, AuditHelper.OPERATION_DELETE, token);
		return kontrak;
	}

	public Kontrak deleteRowKontrak(int id, Token token) {
		Kontrak kontrak = super.find(id);
		super.remove(kontrak, AuditHelper.OPERATION_ROW_DELETE, token);
		return kontrak;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	@SuppressWarnings("unchecked")
	public List<Kontrak> getListByVendor(Integer vendorId) {
		Query q = em.createQuery(
				"SELECT k FROM Kontrak k WHERE k.isDelete = 0 and k.vendor.id = :vendorId ORDER BY k.id ASC");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();

	}

	/**
	 * Insert element at front of the sequence.
	 * 
	 * @param pElement
	 *            the element to add
	 * @return the element is inserted as the first element of the sequence
	 * @exception java.lang.NullPointerException
	 *                if element is detected null
	 */
	@SuppressWarnings("unchecked")
	public List<Kontrak> getLastKontrakList(Token token) {
		int userId = token.getUser().getId();
		// int tahapanId = 1;// pengadaan - inisialisasi
		// return getPengadaanListNonJadwal(tahapanId, userId);
		List<AnggotaPanitia> anggotaPanitiaList = anggotaPanitiaSession.getAnggotaPanitiaByRoleUserList(userId);

		Integer panitiaId;
		if (anggotaPanitiaList != null && anggotaPanitiaList.size() > 0) {
			AnggotaPanitia anggotaPanitia = anggotaPanitiaList.get(0);
			panitiaId = anggotaPanitia.getTimPanitia().getPanitia().getId();
			Query q = em.createNamedQuery("Kontrak.findKontrakByPanitiaId");
			q.setParameter("panitiaId", panitiaId);
			return q.getResultList();
		} else {
			List<PejabatPelaksanaPengadaan> listPejabat = pejabatPelaksanaSession
					.getPejabatPelaksanaByRoleUserList(userId);
			if (listPejabat.size() > 0) {
				PejabatPelaksanaPengadaan pelaksanaPengadaan = listPejabat.get(0);
				panitiaId = pelaksanaPengadaan.getPanitia().getId();
				Query q = em.createNamedQuery("Kontrak.findKontrakByPanitiaId");
				q.setParameter("panitiaId", panitiaId);
				return q.getResultList();
			} else {
				return new ArrayList<Kontrak>();
			}
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Kontrak> getKontrakListByVendorForPerformance(Integer vendorId) {
		Query qm = em.createQuery(
				"SELECT vp.kontrak.id FROM PenilaianVendor vp WHERE vp.vPerfIsDelete=0 AND vp.vendor.id = :vendorId ");
		qm.setParameter("vendorId", vendorId);
		List<Integer> IntegerList = qm.getResultList();
		if (IntegerList == null) {
			IntegerList = new ArrayList();
			IntegerList.add(0);
		}
		if (IntegerList.size() == 0) {
			IntegerList.add(0);
		}

		Query q = em.createQuery("SELECT k FROM Kontrak k " + "WHERE k.id NOT IN (:arr)" + "AND k.isDelete = 0 "
				+ "AND k.vendor.id = :vendorId) " + "ORDER BY k.id ASC");
		q.setParameter("vendorId", vendorId);
		q.setParameter("arr", IntegerList);
		return q.getResultList();

	}

	@SuppressWarnings({ "unchecked" })
	public List<Object[]> getKontrakListByVendor(Integer vendorId) {
		Query q = em.createQuery(
				"SELECT k,  (SELECT AVG(p.vPerfNilaiAkhir) FROM PenilaianVendor p WHERE p.vPerfIsDelete = 0 AND p.kontrak.id = k.id ) AS vp FROM Kontrak k WHERE k.isDelete = 0 AND k.vendor.id = :vendorId order by k.id DESC");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();

	}

	/**
	 * Insert element at front of the sequence.
	 * 
	 * @param pElement
	 *            the element to add
	 * @return the element is inserted as the first element of the sequence
	 * @exception java.lang.NullPointerException
	 *                if element is detected null
	 */
	public String getSyncronizeToPR(int kontrakId, Token token) {
		String result = "{\"msg\":\"null\"}";
		// Get data KOntrak
		Kontrak kontrak = getKontrak(kontrakId);
		// cek perencanaan
		List<PurchaseRequestPengadaan> pr = prPengadaanSession.getListByPengadaanId(kontrak.getPengadaan().getId());
		if (pr.size() > 0) {
			for (PurchaseRequestPengadaan purchaseRequestPengadaan : pr) {
				// get PR Line
				List<PurchaseRequestItem> prItemList = prItemSession.getPurchaseRequestItemByPurchaseRequestId(
						purchaseRequestPengadaan.getPurchaseRequest().getId());
				if (prItemList.size() > 0) {
					for (PurchaseRequestItem purchaseRequestItem : prItemList) {
						// get Item Perolehan Pengadaan
						PerolehanPengadaanSatuan perolehanItemPengadaan = perolehanPengadaanSession
								.findByPengadaanVendorAndItem(kontrak.getPengadaan().getId(),
										kontrak.getVendor().getId(), purchaseRequestItem.getItem().getId());

						if (perolehanItemPengadaan != null) {
							purchaseRequestItem.setVendor(kontrak.getVendor());
							purchaseRequestItem.setVendorname(kontrak.getVendor().getNama());
							purchaseRequestItem.setPrice(perolehanItemPengadaan.getNilaiSatuan());
							purchaseRequestItem.setTotal(perolehanItemPengadaan.getNilaiTotal());
							prItemSession.edit(purchaseRequestItem, AuditHelper.OPERATION_UPDATE, token);
						}
					}
					result = "{\"msg\":\"Syncronize to Purchase Request Number "
							+ purchaseRequestPengadaan.getPurchaseRequest().getPrnumber() + " is Success.\"}";
				} else {
					result = "{\"msgError\":\"Syncronize Error, Purchase Request Item Tidak ada!\"}";
				}

			}

		} else {
			result = "{\"msgError\":\"Syncronize Error, Purchase Request Tidak ada!\"}";
		}

		return result;
	}

	/**
	 * Insert element at front of the sequence.
	 * 
	 * @param pElement
	 *            the element to add
	 * @return the element is inserted as the first element of the sequence
	 * @exception java.lang.NullPointerException
	 *                if element is detected null
	 */

	public String getSyncronizeToPRByPengadaanSatuan(int pengadaanId, Token token) {
		String result = "{\"msg\":\"null\"}";
		// cek perencanaan
		List<PurchaseRequestPengadaan> pr = prPengadaanSession.getListByPengadaanId(pengadaanId);
		if (pr.size() > 0) {
			for (PurchaseRequestPengadaan purchaseRequestPengadaan : pr) {
				// get PR Line
				List<PurchaseRequestItem> prItemList = prItemSession.getPurchaseRequestItemByPurchaseRequestId(
						purchaseRequestPengadaan.getPurchaseRequest().getId());
				if (prItemList.size() > 0) {
					for (PurchaseRequestItem purchaseRequestItem : prItemList) {
						// get Item Perolehan Pengadaan

						List<PerolehanPengadaanSatuan> perolehanItemPengadaanList = perolehanPengadaanSession
								.findByPengadaanAndItem(pengadaanId, purchaseRequestItem.getItem().getId());
						for (PerolehanPengadaanSatuan perolehanPengadaanSatuan : perolehanItemPengadaanList) {

						}
						/*
						 * if(perolehanItemPengadaan !=null){
						 * purchaseRequestItem.setVendor(penetapanPengadaan.
						 * getVendor());
						 * purchaseRequestItem.setVendorname(penetapanPengadaan.
						 * getVendor().getNama());
						 * purchaseRequestItem.setPrice(perolehanItemPengadaan.
						 * getNilaiSatuan());
						 * purchaseRequestItem.setTotal(perolehanItemPengadaan.
						 * getNilaiTotal());
						 * prItemSession.edit(purchaseRequestItem,
						 * AuditHelper.OPERATION_UPDATE, token); }
						 */
					}
					result = "{\"msg\":\"Syncronize to Purchase Request Number "
							+ purchaseRequestPengadaan.getPurchaseRequest().getPrnumber() + " is Success.\"}";
				} else {
					result = "{\"msgError\":\"Syncronize Error, Purchase Request Item Tidak ada!\"}";
				}

			}

		} else {
			result = "{\"msgError\":\"Syncronize Error, Purchase Request Tidak ada!\"}";
		}

		return result;
	}

	/**
	 * Insert element at front of the sequence.
	 * 
	 * @param pElement
	 *            the element to add
	 * @return the element is inserted as the first element of the sequence
	 * @exception java.lang.NullPointerException
	 *                if element is detected null
	 */
	@SuppressWarnings("unchecked")
	public List<Pengadaan> getNewKontrakList() {
		Query q = em.createNamedQuery("Kontrak.findNewKontrak");
		return q.getResultList();
	}

	/**
	 * Insert element at front of the sequence.
	 * 
	 * @param pElement
	 *            the element to add
	 * @return the element is inserted as the first element of the sequence
	 * @exception java.lang.NullPointerException
	 *                if element is detected null
	 */
	@SuppressWarnings("unchecked")
	public List<PengadaanVendorDTO> getNewKontrakListWithSearch(Integer start, Integer length, String keyword,
			Integer columnOrder, String tipeOrder) {

		String queryUser = "SELECT p,ppt.vendor FROM PenetapanPemenangTotal ppt join ppt.pengadaan p WHERE p.isDelete=0 AND p.tahapanPengadaan.tahapan.id=21 AND p.id NOT IN (SELECT k.pengadaan.id FROM Kontrak k) AND "
				+ "(p.nomorNotaDinas like :keyword OR p.namaPengadaan like :keyword OR ppt.vendor.nama like :keyword) ";

		String[] columnToView = { "nomor", "nomorNotaDinas", "namaPengadaan", "nama" };
		if (columnOrder > 0) {
			queryUser += " ORDER BY p. " + columnToView[columnOrder] + " " + tipeOrder;
		} else {
			queryUser += " ORDER BY p.id asc ";
		}

		Query query = em.createQuery(queryUser);
		query.setParameter("keyword", keyword);
		query.setFirstResult(start);
		query.setMaxResults(length);
		Iterator results = query.getResultList().iterator();
		List<PengadaanVendorDTO> pvDTO = new ArrayList<PengadaanVendorDTO>();
		while (results.hasNext()) {
			Object[] tuple = (Object[]) results.next();
			PengadaanVendorDTO tempPV = new PengadaanVendorDTO();
			Pengadaan tempPengadaan = (Pengadaan) tuple[0];
			tempPV.setPengadaan(tempPengadaan);
			if (tempPengadaan.getJenisPenawaran().getId() == 1) {
				tempPV.setVendor((Vendor) tuple[1]);
			}
			pvDTO.add(tempPV);
		}
		return pvDTO;
	}

	public long countFiltered(String keyword) {
		String queryCountUser = "SELECT COUNT(p) FROM PenetapanPemenangTotal ppt join ppt.pengadaan p WHERE p.isDelete=0 AND p.tahapanPengadaan.tahapan.id=21 AND p.id NOT IN (SELECT k.pengadaan.id FROM Kontrak k) AND "
				+ "(p.nomorNotaDinas like :keyword OR p.namaPengadaan like :keyword OR ppt.vendor.nama like :keyword) ";
		Query query = em.createQuery(queryCountUser);
		query.setParameter("keyword", keyword);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}

	public long countTotal() {
		String queryCountUser = "SELECT COUNT(p) FROM PenetapanPemenangTotal ppt join ppt.pengadaan p WHERE p.isDelete=0 AND p.tahapanPengadaan.tahapan.id=21 AND p.id NOT IN (SELECT k.pengadaan.id FROM Kontrak k) ";

		Query query = em.createQuery(queryCountUser);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}
}
