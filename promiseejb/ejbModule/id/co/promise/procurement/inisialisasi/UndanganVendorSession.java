package id.co.promise.procurement.inisialisasi;

import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.email.EmailNotificationStatusSession;
import id.co.promise.procurement.entity.EmailNotification;
import id.co.promise.procurement.entity.EmailNotificationStatus;
import id.co.promise.procurement.entity.MasterEmailNotification;
import id.co.promise.procurement.entity.PejabatPelaksanaPengadaan;
import id.co.promise.procurement.entity.TimPanitia;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.UndanganVendor;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.MasterEmailNotificationSession;
import id.co.promise.procurement.master.PejabatPelaksanaPengadaanSession;
import id.co.promise.procurement.master.TimPanitiaSession;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.vendor.VendorSession;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Singleton
@LocalBean
public class UndanganVendorSession extends AbstractFacadeWithAudit<UndanganVendor> {
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
	TimPanitiaSession timPanitiaSession;
	@EJB
	PejabatPelaksanaPengadaanSession pejabatPelaksanaPengadaanSession;

	public UndanganVendorSession() {
		super(UndanganVendor.class);
	}

	public UndanganVendor getUndanganVendor(int id) {
		return super.find(id);
	}

	public int countByVendorUserId(int userId) {
		Query q = em.createNamedQuery("UndanganVendor.countByVendorUserId")
				.setParameter("userId", userId)
				.setParameter("dateNow", new Date());
		Object obj = q.getSingleResult();
		return obj != null ? Integer.parseInt(obj.toString()) : 0;
	}

	public List<UndanganVendor> getUndanganVendorList() {
		Query q = em.createNamedQuery("UndanganVendor.find");
		return q.getResultList();
	}

	public List<UndanganVendor> getUndanganVendorListByVendorUserid(int userId) {
		Query q = em.createNamedQuery("UndanganVendor.findByVendorUserId");
		q.setParameter("userId", userId);
		return q.getResultList();
	}

	public List<UndanganVendor> getUndanganVendorListByPengadaanId(
			int pengadaanId) {
		Query q = em.createNamedQuery("UndanganVendor.findByPengadaanId");
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}
	
	public List<UndanganVendor> getUndanganByPengadaanIdAndVendorId(
			int pengadaanId, int vendorId) {
		Query q = em.createNamedQuery("UndanganVendor.findByPengadaanIdAndVendorId");
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	

	public List<UndanganVendor> getListByVendorDateTahapan(int userId,
			int tahapanId) {
		Query q = em.createNamedQuery("UndanganVendor.findByVendorDateTahapan");
		q.setParameter("userId", userId);
		q.setParameter("dateNow", new Date());
		q.setParameter("tahapanId", tahapanId);
		return q.getResultList();
	}

	public UndanganVendor insertUndanganVendor(UndanganVendor undanganVendor, Token token) {
		undanganVendor.setCreated(new Date());
		undanganVendor.setIsDelete(0);
		super.create(undanganVendor, AuditHelper.OPERATION_CREATE, token);

		// Email Notification
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification menList = masterEmailNotificationSession.find(2);//2 Undangan Pengadaan
			if (menList != null) {
				Vendor vendor = vendorSession.getVendor(undanganVendor
						.getVendor().getId());
				String namaPanitia = "";
				TimPanitia tp = timPanitiaSession
						.getPanitiaPengadaanId(undanganVendor.getPengadaan()
								.getPanitia().getId());
				if (tp != null) {
					namaPanitia = tp.getNama();
				} else {
					List<PejabatPelaksanaPengadaan> ppp = pejabatPelaksanaPengadaanSession
							.getPelaksanaPengadaanByPanitiaList(
									undanganVendor.getPengadaan().getPanitia()
											.getId());
					if (ppp.size()>0) {
						namaPanitia = ppp.get(0).getNama();
					} else {
						namaPanitia = token.getUser().getNamaPengguna();
					}
					
				}
				EmailNotification en = new EmailNotification();
				en.setCreated(new Date());
				en.setEmailTo(vendor.getEmail());
				en.setSubject(menList.getNama());
				String message = menList.getTemplateEmail();
				message = message.replaceAll("\\{Vendor\\.nama\\}",
						vendor.getNama());
				message = message.replaceAll("\\{Pengadaan\\.namaPengadaan\\}",
						undanganVendor.getPengadaan().getNamaPengadaan());
				message = message.replaceAll("\\{Panitia\\.nama\\}",
						namaPanitia).replaceAll(
						"\\{Pengadaan\\.nomorNotaDinas\\}",
						undanganVendor.getPengadaan().getNomorNotaDinas());
				en.setMessage(message);
				en.setStatusEmailSending(0);
				en.setIsDelete(0);
				en.setSendingDate(new Date());
				emailNotificationSession.create(en);
			}
		}
		return undanganVendor;
	}

	public UndanganVendor updateUndanganVendor(UndanganVendor undanganVendor, Token token) {
		undanganVendor.setUpdated(new Date());
		super.edit(undanganVendor, AuditHelper.OPERATION_UPDATE, token);
		return undanganVendor;
	}

	public UndanganVendor deleteUndanganVendor(int id, Token token) {
		UndanganVendor undanganVendor = super.find(id);
		undanganVendor.setIsDelete(1);
		undanganVendor.setDeleted(new Date());
		super.edit(undanganVendor, AuditHelper.OPERATION_DELETE, token);
		return undanganVendor;
	}

	public UndanganVendor deleteRowUndanganVendor(int id, Token token) {
		UndanganVendor undanganVendor = super.find(id);
		super.remove(undanganVendor, AuditHelper.OPERATION_ROW_DELETE, token);
		return undanganVendor;
	}
	

	public UndanganVendor deleteRowUndanganVendorByPengadaanVendor(int pengadaanId, int vendorId, Token token) {
		List<UndanganVendor> undanganVendorList = getUndanganByPengadaanIdAndVendorId(pengadaanId, vendorId);
		if(undanganVendorList.size()>0) {
			UndanganVendor undanganVendor = new UndanganVendor();
			for (UndanganVendor undanganVendorObj : undanganVendorList) {;
				super.remove(undanganVendorObj, AuditHelper.OPERATION_ROW_DELETE, token);
				undanganVendor = undanganVendorObj;
			}
			
			return undanganVendor;
		} else {
			return null;
		}
		
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}
	
	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

}
