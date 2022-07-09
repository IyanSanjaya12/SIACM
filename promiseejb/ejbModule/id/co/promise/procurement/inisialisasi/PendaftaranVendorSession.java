package id.co.promise.procurement.inisialisasi;

import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.email.EmailNotificationStatusSession;
import id.co.promise.procurement.entity.EmailNotification;
import id.co.promise.procurement.entity.EmailNotificationStatus;
import id.co.promise.procurement.entity.ItemPengadaan;
import id.co.promise.procurement.entity.MasterEmailNotification;
import id.co.promise.procurement.entity.PejabatPelaksanaPengadaan;
import id.co.promise.procurement.entity.PendaftaranVendor;
import id.co.promise.procurement.entity.Pengadaan;
import id.co.promise.procurement.entity.PerolehanPengadaanTotal;
import id.co.promise.procurement.entity.TimPanitia;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.MasterEmailNotificationSession;
import id.co.promise.procurement.master.PejabatPelaksanaPengadaanSession;
import id.co.promise.procurement.master.TimPanitiaSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.vendor.VendorSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Singleton
@LocalBean
public class PendaftaranVendorSession extends AbstractFacadeWithAudit<PendaftaranVendor> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	@EJB
	EmailNotificationStatusSession emailNotificationStatusSession;
	@EJB
	EmailNotificationSession emailNotificationSession;
	@EJB
	MasterEmailNotificationSession masterEmailNotificationSession;
	@EJB
	VendorSession vendorSession;
	@EJB
	private ItemPengadaanSession itemPengadaanSession;
	@EJB
	TimPanitiaSession timPanitiaSession;
	@EJB
	PejabatPelaksanaPengadaanSession pejabatPelaksanaPengadaanSession;

	public PendaftaranVendorSession() {
		super(PendaftaranVendor.class);
	}

	public PendaftaranVendor getPendaftaranVendor(int id) {
		return super.find(id);
	}
	
	public List<PendaftaranVendor> getPendaftaranVendorByVendorUserIdTahapan(int userId, int tahapanId){
		Query q = em.createNamedQuery("PendaftaranVendor.findByVendorTahapan")
				.setParameter("userId", userId)
				.setParameter("tahapanId", tahapanId);
		return q.getResultList();
	}

	public List<PendaftaranVendor> getPendaftaranVendorByVendorUserId(int userId) {
		Query q = em.createNamedQuery("PendaftaranVendor.findByVendorUserId");
		q.setParameter("userId", userId);
		return q.getResultList();
	}
	
	/*public List<PendaftaranVendor> getSedangMengikutiPengadaanByVendorUserId(int userId) {
		Query q = em.createNamedQuery("PendaftaranVendor.sedangMengikutiFindByVendorUserId");
		q.setParameter("userId", userId);
		return q.getResultList();
	}*/

	public List<PendaftaranVendor> getPendaftaranVendorList() {
		Query q = em.createNamedQuery("PendaftaranVendor.find");
		return q.getResultList();
	}

	public List<PendaftaranVendor> getPendaftaranVendorByPengadaan(
			int pengadaanId) {
		Query q = em.createNamedQuery("PendaftaranVendor.findByPengadaanId");
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}
	
	//fungsi baru
	//getPernahMengikuti untuk menampilkan di tabel view modul rekaphistoryvendor
	@SuppressWarnings("unchecked")
	public List<PerolehanPengadaanTotal> getPernahMengikutiPengadaanByVendorUserId(int userId) {
		String qPendaftaranByUserId = "SELECT perolehanPengadaanTotal FROM PerolehanPengadaanTotal perolehanPengadaanTotal WHERE perolehanPengadaanTotal.isDelete = 0 AND "
				+ "perolehanPengadaanTotal.vendor.user = :userId AND perolehanPengadaanTotal.pengadaan.tahapanPengadaan.tahapan.id = 21 AND NOT perolehanPengadaanTotal.pengadaan.tahapanPengadaan.tahapan.id = 22 ";
		Query q = getEntityManager().createQuery(qPendaftaranByUserId);
		q.setParameter("userId", userId);
		//System.out.println("cuma sampe di sini............");
		List<PerolehanPengadaanTotal> perolehanPengadaanTotalList = q.getResultList();
		for (int i=0 ; i < perolehanPengadaanTotalList.size();i++) {
			PerolehanPengadaanTotal newPerolehanPengadaanTotal = perolehanPengadaanTotalList.get(i);
        	//get itemPengadaanList
        	List<ItemPengadaan> itemPengadaanList = itemPengadaanSession.getItemPengadanByPengadaanId(newPerolehanPengadaanTotal.getPengadaan().getId());
        	newPerolehanPengadaanTotal.setItemPengadaanList(itemPengadaanList);
        	
        	double sumTotalHPS= 0;
			for (int j = 0; j < itemPengadaanList.size(); j++) {
				ItemPengadaan newItemPengadaan = itemPengadaanList.get(j);
				sumTotalHPS = (newItemPengadaan.getTotalHPS());
				newPerolehanPengadaanTotal.setTotalHPS(sumTotalHPS);
			}
			
			perolehanPengadaanTotalList.set(i, newPerolehanPengadaanTotal);
		}
		return perolehanPengadaanTotalList;
		
	}
	
	//getSedangMengikuti untuk menampilkan di tabel view modul rekaphistoryvendor
		@SuppressWarnings("unchecked")
		public List<PendaftaranVendor> getSedangMengikutiPengadaanByVendorUserId(int userId) {
			String qSedangMengikiutiByUserId = "SELECT pendaftaranVendor FROM PendaftaranVendor pendaftaranVendor WHERE pendaftaranVendor.isDelete = 0 AND "
					+ "pendaftaranVendor.vendor.user = :userId  AND NOT pendaftaranVendor.pengadaan.tahapanPengadaan.tahapan.id = 20 "
					+ "AND NOT pendaftaranVendor.pengadaan.tahapanPengadaan.tahapan.id = 21 "
					+ "AND NOT pendaftaranVendor.pengadaan.tahapanPengadaan.tahapan.id = 22";
			Query q = getEntityManager().createQuery(qSedangMengikiutiByUserId);
			q.setParameter("userId", userId);
			List<PendaftaranVendor> pendaftaranVendorList = q.getResultList();
		
			for (int i=0 ; i < pendaftaranVendorList.size();i++) {
				PendaftaranVendor newPendaftaranVendor = pendaftaranVendorList.get(i);
	        	//get itemPengadaanList
				int pengadaanId= newPendaftaranVendor.getPengadaan().getId();
	        	List<ItemPengadaan> itemPengadaanList = itemPengadaanSession.getItemPengadanByPengadaanId(pengadaanId);
	        	
	        	newPendaftaranVendor.setItemPengadaanList(itemPengadaanList);
	        	
	        		double sumTotalHPS= 0;
					for (int j = 0; j < itemPengadaanList.size(); j++) {
						ItemPengadaan newItemPengadaan = itemPengadaanList.get(j);
						sumTotalHPS = (newItemPengadaan.getTotalHPS());
						newPendaftaranVendor.setTotalHPS(sumTotalHPS);
					}
	        	
	        	pendaftaranVendorList.set(i, newPendaftaranVendor);
			}	
			return pendaftaranVendorList;
		}


	public List<PendaftaranVendor> getPendaftaranVendorByVendorDateTahapan(
			int userId, int tahapanId) {
		Query q = em
				.createNamedQuery("PendaftaranVendor.findByVendorDateTahapan");
		q.setParameter("tahapanId", tahapanId);
		q.setParameter("userId", userId);
		q.setParameter("dateNow", new Date());
		return q.getResultList();
	}

	public PendaftaranVendor getPendaftaranVendorByVendorUserIdAndPengadaanId(
			int userId, int pengadaanId) {
		Query q = em
				.createNamedQuery("PendaftaranVendor.findByVendorUserIdAndPengadaanId");
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("userId", userId);
		try {
			return (PendaftaranVendor) q.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<PendaftaranVendor> findByVendorIdAndPengadaanId(
			int vendorId, int pengadaanId) {
		Query q = em
				.createNamedQuery("PendaftaranVendor.findByVendorIdAndPengadaanId");
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("vendorId", vendorId);
		try {
			return q.getResultList();
		} catch (Exception e) {
			return new ArrayList<PendaftaranVendor>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PendaftaranVendor> getPendaftaranVendorByVendorId(int vendorId){
		String stringQuery = "SELECT t1 FROM PendaftaranVendor t1 WHERE t1.isDelete = :isDelete AND t1.vendor.id= :vendorId";
		Query query = em.createQuery(stringQuery);
		query.setParameter("isDelete", 0);
		query.setParameter("vendorId", vendorId);
		return query.getResultList();
	}

	public PendaftaranVendor insertPendaftaranVendor(PendaftaranVendor pv, Token token) {
		pv.setCreated(new Date());
		pv.setIsDelete(0);
		super.create(pv, AuditHelper.OPERATION_CREATE, token);
		
		// Email Notification
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.find(1); //1. pendaftaran pengadaan konfirmasi
			if (men != null) {
				Vendor vendor = vendorSession.getVendor(pv.getVendor().getId());
				String namaPanitia = "";
				TimPanitia tp = timPanitiaSession.getPanitiaPengadaanId(pv
						.getPengadaan().getId());
				if (tp != null) {
					namaPanitia = tp.getNama();
				} else {
					PejabatPelaksanaPengadaan ppp = pejabatPelaksanaPengadaanSession
							.getPelaksanaPengadaanId(pv.getPengadaan().getId());
					if (ppp != null) {
						namaPanitia = ppp.getNama();
					}
				}
				EmailNotification en = new EmailNotification();
				en.setCreated(new Date());
				en.setEmailTo(vendor.getEmail());
				en.setSubject(men.getNama());
				en.setStatusEmailSending(0);
				en.setIsDelete(0);
				en.setSendingDate(new Date());
				emailNotificationSession.create(en);
			}
		}

		return pv;
	}

	public PendaftaranVendor updatePendaftaranVendor(PendaftaranVendor pv, Token token) {
		pv.setUpdated(new Date());
		super.edit(pv, AuditHelper.OPERATION_UPDATE, token);
		return pv;
	}

	public PendaftaranVendor deletePendaftaranVendor(int id, Token token) {
		PendaftaranVendor pv = super.find(id);
		pv.setIsDelete(1);
		pv.setDeleted(new Date());
		super.edit(pv, AuditHelper.OPERATION_DELETE, token);
		return pv;
	}

	public PendaftaranVendor deleteRowPendaftaranVendor(int id, Token token) {
		PendaftaranVendor pv = super.find(id);
		super.remove(pv, AuditHelper.OPERATION_ROW_DELETE, token);
		return pv;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}
	
	@SuppressWarnings("unchecked")
	public PendaftaraanVendorListPagination getPendaftaranVendorWithPagination(Integer userId, Integer startIndex, Integer endIndex, String search) {
		//System.out.println("TEST6");
		JsonElement jelement = new JsonParser().parse(search);
		JsonObject jobject = jelement.getAsJsonObject();

		JsonArray jarray = jobject.getAsJsonArray("filter");
		String where = "";
		for (int i = 0; i < jarray.size(); i++) {
			JsonObject joWhere = jarray.get(i).getAsJsonObject();
			String fixValue = joWhere.get("value").toString().replaceAll("\"", "");
			if (fixValue.equalsIgnoreCase("undefined")){
				fixValue = "";
			}
			
			where += " AND o." + joWhere.get("key").toString().replaceAll("\"", "") + " like '%" + fixValue + "%'";
		}
		
		String sort = " ";
		try {
			//sort = " ORDER BY o." + jobject.get("sort").toString().replaceAll("\"", "") + " DESC, o.pengadaan.updated DESC" ;
			sort = " ORDER BY o." + jobject.get("sort").toString().replaceAll("\"", "") + " DESC, o.pengadaan.updated DESC" ;
		} catch (Exception e) {

		}
		
		Integer tahapanId = Integer.parseInt(jobject.get("pengadaan.tahapanPengadaan").toString());
		if(tahapanId > 0) {
			where += " AND o.pengadaan.tahapanPengadaan.tahapan.id = "+tahapanId;
		} else {
//			where += " AND o.pengadaan.tahapanPengadaan.tahapan.id <> 21 ";
			where += " AND o.pengadaan.tahapanPengadaan.tahapan.id <> 10010000";
		}
		
		//
		where += " AND o.vendor.user = "+userId;
		Query q = em.createQuery("SELECT COUNT(o) FROM PendaftaranVendor o WHERE o.pengadaan.isDelete=0 AND o.isDelete = 0 " + where );
		int totalData = Integer.parseInt(q.getSingleResult().toString());
		
		//+ where + sort
		q = em.createQuery("SELECT o FROM PendaftaranVendor o WHERE o.pengadaan.isDelete=0 AND o.isDelete = 0 " + where + sort);
		int[] range = { startIndex - 1, endIndex - 1 };
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);
		List<PendaftaranVendor> listPendaftaraanVendor = q.getResultList();
		
		return new PendaftaraanVendorListPagination(userId, totalData, startIndex, endIndex, listPendaftaraanVendor);
	}


}
