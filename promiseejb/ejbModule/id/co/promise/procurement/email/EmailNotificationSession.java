package id.co.promise.procurement.email;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogContractDetail;
import id.co.promise.procurement.catalog.session.CatalogContractDetailSession;
import id.co.promise.procurement.deliveryreceived.DeliveryReceivedSession;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.DeliveryReceived;
import id.co.promise.procurement.entity.EmailNotification;
import id.co.promise.procurement.entity.EmailNotificationStatus;
import id.co.promise.procurement.entity.InvoicePayment;
import id.co.promise.procurement.entity.MasterEmailNotification;
import id.co.promise.procurement.entity.MasterSMTPServer;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.SertifikatVendor;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorProfile;
import id.co.promise.procurement.inisialisasi.PendaftaranVendorSession;
import id.co.promise.procurement.master.AnggotaPanitiaSession;
import id.co.promise.procurement.master.EmailPurchaseOrderDTO;
import id.co.promise.procurement.master.MasterEmailNotificationSession;
import id.co.promise.procurement.master.ParameterSession;
import id.co.promise.procurement.master.PejabatPelaksanaPengadaanSession;
import id.co.promise.procurement.master.TimPanitiaSession;
import id.co.promise.procurement.purchaseorder.DeliveryReceivedDetailDTO;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.KeyGenHelper;
import id.co.promise.procurement.utils.ParamContext;
import id.co.promise.procurement.vendor.VendorProfileSession;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@LocalBean
public class EmailNotificationSession extends AbstractFacade<EmailNotification> {
	final static Logger logger = Logger.getLogger(EmailNotificationSession.class);
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	@EJB
	private EmailNotificationStatusSession emailNotificationStatusSession;
	@EJB
	private MasterEmailNotificationSession masterEmailNotificationSession;
	@EJB
	private PendaftaranVendorSession pendaftaranVendorSession;
	@EJB
	private TimPanitiaSession timPanitiaSession;
	@EJB
	private PejabatPelaksanaPengadaanSession pejabatPelaksanaPengadaanSession;
	@EJB
	private AnggotaPanitiaSession anggotaPanSession;
	@EJB
	private MasterSMTPServerSession smtpSession;
	@EJB
	private PurchaseOrderSession purchaseOrderSession;
	@EJB
	private PurchaseOrderItemSession purchaseOrderItemSession;
	@EJB
	private VendorProfileSession vendorProfileSession;
	@EJB
	private UserSession userSession;
	@EJB
	private ParameterSession parameterSession;
	@EJB
	private VendorSession vendorSession;
	@EJB
	private DeliveryReceivedSession deliveryReceivedSession;
	@EJB
	private CatalogContractDetailSession catalogContractDetailSession;
	@EJB
	private PurchaseRequestItemSession purchaseRequestItemSession;
	
	public EmailNotificationSession() {
		super(EmailNotification.class);
	}

	@SuppressWarnings("unchecked")
	@Asynchronous
	public void emailSender() throws AddressException, MessagingException {
		MasterSMTPServer smtp = smtpSession.findAll().get(0);
		String host = smtp.getHost();// Gmail
		int port = smtp.getPort();
		String serviceUsername = smtp.getUsername();
		String servicePassword = smtp.getPassword();// Our Gmail password

		Properties props = new Properties();
		props.put("mail.smtp.user", serviceUsername);
		props.put("mail.smtp.password", servicePassword);
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.debug", "true");
		// props.put("mail.debug", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", port);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		// Destination of the email
		String from = smtp.getUsername();
		// Auth
		SmtpAuthenticator smtpAuth = new SmtpAuthenticator(serviceUsername, servicePassword);
		// Creating a javax.Session with the our prope
		Session session = Session.getInstance(props, smtpAuth);
		Query q = em.createNamedQuery("EmailNotification.findByStatusNew");
		List<EmailNotification> enList = q.getResultList();
		if (enList.size() > 30) {
			for (int i = 0; i < 30; i++) {
				EmailNotification en = enList.get(i);
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(en.getEmailTo()));
				message.setSubject(en.getSubject());
				message.setContent(en.getMessage(), "text/html; charset=utf-8");
				Transport.send(message);
				en.setStatusEmailSending(1);
				en.setUpdated(new Date());
				super.edit(en);
				logger.info("sending email to " + en.getEmailTo() + " success");
			}
		}else {
			for (int i = 0; i < enList.size(); i++) {
				EmailNotification en = enList.get(i);
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(en.getEmailTo()));
				message.setSubject(en.getSubject());
				message.setContent(en.getMessage(), "text/html; charset=utf-8");
				Transport.send(message);
				en.setStatusEmailSending(1);
				en.setUpdated(new Date());
				super.edit(en);
				logger.info("sending email to " + en.getEmailTo() + " success");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Asynchronous
	public void emailSenderTest(String emailTo, String subject, String contentEmail) throws AddressException, MessagingException {
		try {
			String host = "smtp.gmail.com";// Gmail
			int port = 587;
			String serviceUsername = "mailsender@mmi-pt.com";
			String servicePassword = "ProMIS3!";// Our Gmail password

			Properties props = new Properties();
			props.put("mail.smtp.user", serviceUsername);
			props.put("mail.smtp.password", servicePassword);
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.debug", "true");
			// props.put("mail.debug", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.socketFactory.port", port);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");

			// Destination of the email
			String from = "mailsender@mmi-pt.com";
			// Auth
			SmtpAuthenticator smtpAuth = new SmtpAuthenticator(serviceUsername, servicePassword);
			// Creating a javax.Session with the our prope
			Session session = Session.getInstance(props, smtpAuth);
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
			message.setSubject(subject);
			message.setContent(contentEmail, "text/html; charset=utf-8");
			Transport.send(message);
			logger.info("sending email to " + emailTo + " success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("sending email to " + emailTo + " failed. Cause : "+e.getMessage()+e.getCause());
		}
	}

	public void getEmailGenerator() throws ParseException {
		/*EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			List<MasterEmailNotification> listMEN = masterEmailNotificationSession.findAll();
			if (listMEN.size() > 0) {
				for (int i = 0; i < listMEN.size(); i++) {
					MasterEmailNotification men = listMEN.get(i);
					if (men.getRole().getId() == 1) { // Panitia
						// Notifikasi Jadwal Pengadaan
						SimpleDateFormat formatDateOnly = new SimpleDateFormat("dd/MM/yyyy");
						Date date = new Date(formatDateOnly.parse(formatDateOnly.format(new Date())).getTime()
								+ ((long) men.getNotifikasiTglSebelum() * 1000 * 60 * 60 * 24));
						List<JadwalPengadaan> jadwalPengadaanList = jadwalPengadaanSession.getByDateNotify(date);
						for (int j = 0; j < jadwalPengadaanList.size(); j++) {
							Pengadaan pengadaan = jadwalPengadaanList.get(j).getPengadaan();
							long countCheckMail = emailNotifyProsesPengadaan
									.getEmailNotifyProsesPengadaan(pengadaan.getId(), men.getId());
							if (countCheckMail == 0) {
								EmailNotification en = new EmailNotification();
								en.setCreated(new Date());
								TimPanitia tp = timPanitiaSession.getPanitiaPengadaanId(pengadaan.getPanitia().getId());
								String namaPanitia = "";
								if (tp != null) {
									namaPanitia = tp.getNama();
									List<AnggotaPanitia> anggotaPanList = anggotaPanSession
											.getAnggotaPanitiaByTimPanitiaList(tp.getId());
									String emailTo = "";
									for (int k = 0; k < anggotaPanList.size(); k++) {
										if (k == 0)
											emailTo = anggotaPanList.get(k).getPic().getUser().getEmail();
										else
											emailTo += "," + anggotaPanList.get(k).getPic().getUser().getEmail();
									}
									en.setEmailTo(emailTo);
								} else {
									List<PejabatPelaksanaPengadaan> pppList = pejabatPelaksanaPengadaanSession
											.getPelaksanaPengadaanByPanitiaList(pengadaan.getPanitia().getId());
									if (pppList.size() > 0) {
										PejabatPelaksanaPengadaan ppp = pppList.get(0);
										namaPanitia = ppp.getNama();
										if (ppp != null)
											en.setEmailTo(ppp.getPic().getUser().getEmail());
									}
								}
								en.setSubject(men.getNama());
								String tanggalNotifikasi = "";
								SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy");
								tanggalNotifikasi = formatter.format(date);
								en.setMessage(men.getTemplateEmail().replaceAll("\\{Panitia\\.nama\\}", namaPanitia)
										.replaceAll("\\{Pengadaan\\.namaPengadaan\\}", pengadaan.getNamaPengadaan())
										.replaceAll("\\{Pengadaan\\.nomorNotaDinas\\}", pengadaan.getNomorNotaDinas())
										.replaceAll("\\{JadwalPengadaan\\.tanggal\\}", tanggalNotifikasi));

								en.setStatusEmailSending(0);
								en.setSendingDate(new Date());
								en.setIsDelete(0);
								en.setMasterEmailNotification(men);
								if (en.getEmailTo() != null) {
									super.create(en);

									EmailNotificationProsesPengadaan enp = new EmailNotificationProsesPengadaan();
									enp.setCreated(new Date());
									enp.setEmailNotification(en);
									enp.setPengadaan(pengadaan);
									enp.setIsDelete(0);
									emailNotifyProsesPengadaan.create(enp);
								} else {
									System.err.println("Generate Email error: No Email To! PENGADAAN_ID="+pengadaan.getId());
								}
							}
						}

					} else if (men.getRole().getId() == 2) {// vendor

					}
				}
			}
		}*/
	}

	public Boolean getMailLoginRecoverResetPassword(String emailHash) {
		try {
			String email = KeyGenHelper.finalDecrypt(emailHash);
			User user = userSession.getUserByEmail(email);
			if (user != null) {
				// cek setting email aktif / not
				EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
				if (ens.isStatusAktif()) {
					// 33 = Recover User Login by email
					MasterEmailNotification men = masterEmailNotificationSession.find(9);
					if (men != null) {
						String radomPassword = KeyGenHelper.finalEncrypt((new Date()).toString()).substring(0, 8);
						user.setPassword(KeyGenHelper.finalEncrypt(radomPassword));
						userSession.edit(user);
						EmailNotification en = new EmailNotification();
						en.setCreated(new Date());
						en.setEmailTo(email);
						en.setSubject(men.getNama());

						en.setMessage(men.getTemplateEmail().replaceAll("\\{User\\.email\\}", user.getEmail())
								.replaceAll("\\{User\\.namaPengguna\\}", user.getNamaPengguna())
								.replaceAll("\\{User\\.password\\}", radomPassword));

						en.setStatusEmailSending(0);
						en.setSendingDate(new Date());
						en.setIsDelete(0);
						en.setMasterEmailNotification(men);

						try {
							super.create(en);
							return true;
						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}

					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public Boolean getMailLoginRecover(String email) {
		User user = userSession.getUserByEmail(email);
		if (user != null) {
			// cek setting email aktif / not
			EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
			if (ens.isStatusAktif()) {
				// 33 = Recover User Login by email
				MasterEmailNotification men = masterEmailNotificationSession.find(8);
				if (men != null) {
					EmailNotification en = new EmailNotification();
					en.setCreated(new Date());
					en.setEmailTo(email);
					en.setSubject(men.getNama());
					String emailHash = "";
					try {
						emailHash = KeyGenHelper.finalEncrypt(email);
					} catch (Exception e) {
						e.printStackTrace();
					}
					String urlLoginRecover = parameterSession.getParameterByName("BASE_IP_ADDRESS").getNilai()
							+ "/#/page/recover-execute/" + emailHash;

					en.setMessage(men.getTemplateEmail().replaceAll("\\{User\\.namaPengguna\\}", user.getNamaPengguna())
							.replaceAll("\\{URL\\.userLoginRecovery\\}", urlLoginRecover));

					en.setStatusEmailSending(0);
					en.setSendingDate(new Date());
					en.setIsDelete(0);
					en.setMasterEmailNotification(men);
					try {
						super.create(en);
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				} else {
					System.err.println("Email Notification No Template");
					return false;
				}

			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public void getMailGeneratorRegistrasiVendor(VendorProfile vendorProfile) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.find(5); // registrasi
																					// vendor
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(vendorProfile.getEmail());
			en.setSubject(men.getNama());

			User user = userSession.getUser(vendorProfile.getVendor().getUser());
			String password = "";
			try {
				password = KeyGenHelper.finalDecrypt(user.getPassword());
			} catch (Exception e) {
				e.printStackTrace();
			}

			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\{VendorProfile\\.namaPerusahaan\\}", vendorProfile.getNamaPerusahaan())
					.replaceAll("\\{VendorProfile\\.email\\}", vendorProfile.getEmail())
					.replaceAll("\\{VendorProfile\\.password\\}", password)
					.replaceAll("\\{VendorProfile\\.alamat\\}", vendorProfile.getAlamat())
					.replaceAll("\\{VendorProfile\\.faksimile\\}", vendorProfile.getFaksimile())
					.replaceAll("\\{VendorProfile\\.telephone\\}", vendorProfile.getTelephone())
					.replaceAll("\\{VendorProfile\\.provinsi\\}", vendorProfile.getProvinsi())
					.replaceAll("\\{VendorProfile\\.kota\\}", vendorProfile.getKota())
					.replaceAll("\\{VendorProfile\\.kodePos\\}", vendorProfile.getKodePos())
					.replaceAll("\\{VendorProfile\\.tipePerusahaan\\}", vendorProfile.getTipePerusahaan())
					.replaceAll("\\{User\\.username\\}", user.getUsername())
					.replaceAll("\\{User\\.password}", password));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}

	public void getMailGeneratorApprovalVendor(VendorProfile vendorProfile) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.find(6); // Approval
																					// vendor
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(vendorProfile.getEmail());
			en.setSubject(men.getNama());

			User user = userSession.getUser(vendorProfile.getVendor().getUser());
			String password = "";
			try {
				password = KeyGenHelper.finalDecrypt(user.getPassword());
			} catch (Exception e) {
				e.printStackTrace();
			}

			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\{VendorProfile\\.namaPerusahaan\\}", vendorProfile.getNamaPerusahaan())
					.replaceAll("\\{VendorProfile\\.alamat\\}", vendorProfile.getAlamat())
					.replaceAll("\\{VendorProfile\\.faksimile\\}", vendorProfile.getFaksimile())
					.replaceAll("\\{VendorProfile\\.telephone\\}", vendorProfile.getTelephone())
					.replaceAll("\\{VendorProfile\\.provinsi\\}", vendorProfile.getProvinsi())
					.replaceAll("\\{VendorProfile\\.kota\\}", vendorProfile.getKota())
					.replaceAll("\\{VendorProfile\\.kodePos\\}", vendorProfile.getKodePos())
					.replaceAll("\\{VendorProfile\\.tipePerusahaan\\}", vendorProfile.getTipePerusahaan())
					.replaceAll("\\{User\\.username\\}", user.getUsername())
					.replaceAll("\\{User\\.password\\}", password));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	public void getMailGeneratorAddEditManageCatalog(Catalog catalog, User user, String aksi) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.find(14); //Pemberitahuan Penambahan / Perubahan Katalog
																					
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(user.getEmail());
			en.setSubject(men.getNama());	

			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\$NO_KONTRAK", catalog.getCatalogKontrak().getNomorKontrak())
					.replaceAll("\\$KODE_ITEM", ((catalog.getKodeProduk() != null) ? catalog.getKodeProduk() : "-") )
					.replaceAll("\\$NAMA_ITEM", ((catalog.getNamaIND() != null) ? catalog.getNamaIND() : "-"))
					.replaceAll("\\$DESKRIPSI", ((catalog.getCatalogContractDetail().getItemDesc() != null) ? catalog.getCatalogContractDetail().getItemDesc() : "-"))
					.replaceAll("\\$AKSI", aksi));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	public void getMailGeneratorApprovalMidleAndLast(Catalog catalog, Integer status, List<ApprovalProcess> appProcessList) {
		String note = "-";
		String noteApp = "-";
		try {
			EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
			if (ens.isStatusAktif()) {
				MasterEmailNotification men = masterEmailNotificationSession.find(15); //Pemberitahuan Persetujuan Item Katalog
				if(appProcessList.size() > 1) {
					noteApp = appProcessList.get(1).getKeterangan();
				}else {
					note = appProcessList.get(0).getKeterangan();					
				}
				for(ApprovalProcess app : appProcessList) {//to Approver
					EmailNotification en = new EmailNotification();
					en.setCreated(new Date());
					en.setEmailTo(app.getUser().getEmail());
					en.setSubject(men.getNama());
					
					String statusText = "Disetujui";
					if (status != 1 ) {
						statusText = "Ditolak";
					}
					en.setMessage(men.getTemplateEmail()
							.replaceAll("\\$NO_KONTRAK", catalog.getCatalogKontrak().getNomorKontrak())
							.replaceAll("\\$KODE_ITEM", ((catalog.getKodeProduk() != null) ? catalog.getKodeProduk() : "-"))
							.replaceAll("\\$NAMA_ITEM", ((catalog.getNamaIND() != null) ? catalog.getNamaIND() : "-"))
							.replaceAll("\\$DESKRIPSI", ((catalog.getCatalogContractDetail().getItemDesc() != null) ? catalog.getCatalogContractDetail().getItemDesc() : "-"))
							.replaceAll("\\$STATUS", statusText)
							.replaceAll("\\$NOTES", note)
							.replaceAll("\\$CATATAN", noteApp));

					en.setStatusEmailSending(0);
					en.setSendingDate(new Date());
					en.setIsDelete(0);
					en.setMasterEmailNotification(men);
					super.create(en);
				}
				//to Vendor
				EmailNotification en = new EmailNotification();
				en.setCreated(new Date());
				en.setEmailTo(catalog.getVendor().getEmail());
				en.setSubject(men.getNama());
				
				String statusText = "Disetujui";
				if (status != 1 ) {
					statusText = "Ditolak";
				}
				en.setMessage(men.getTemplateEmail()
						.replaceAll("\\$NO_KONTRAK", catalog.getCatalogKontrak().getNomorKontrak())
						.replaceAll("\\$KODE_ITEM", ((catalog.getKodeProduk() != null) ? catalog.getKodeProduk() : "-"))
						.replaceAll("\\$NAMA_ITEM", ((catalog.getNamaIND() != null) ? catalog.getNamaIND() : "-"))
						.replaceAll("\\$DESKRIPSI", ((catalog.getCatalogContractDetail().getItemDesc() != null) ? catalog.getCatalogContractDetail().getItemDesc() : "-"))
						.replaceAll("\\$STATUS", statusText)
						.replaceAll("\\$NOTES", note)
						.replaceAll("\\$CATATAN", noteApp));

				en.setStatusEmailSending(0);
				en.setSendingDate(new Date());
				en.setIsDelete(0);
				en.setMasterEmailNotification(men);
				super.create(en);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	public void getMailGeneratorRejectVendor(VendorProfile vendorProfile) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);

		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.find(10); // Reject
																					// Vendor
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(vendorProfile.getEmail());
			en.setSubject(men.getNama());

			User user = userSession.getUser(vendorProfile.getVendor().getUser());
			String password = "";
			try {
				password = KeyGenHelper.finalDecrypt(user.getPassword());
			} catch (Exception e) {
				e.printStackTrace();
			}

			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\{VendorProfile\\.namaPerusahaan\\}", vendorProfile.getNamaPerusahaan())
					.replaceAll("\\{VendorProfile\\.alamat\\}", vendorProfile.getAlamat())
					.replaceAll("\\{VendorProfile\\.faksimile\\}", vendorProfile.getFaksimile())
					.replaceAll("\\{VendorProfile\\.telephone\\}", vendorProfile.getTelephone())
					.replaceAll("\\{VendorProfile\\.provinsi\\}", vendorProfile.getProvinsi())
					.replaceAll("\\{VendorProfile\\.kota\\}", vendorProfile.getKota())
					.replaceAll("\\{VendorProfile\\.kodePos\\}", vendorProfile.getKodePos())
					.replaceAll("\\{VendorProfile\\.tipePerusahaan\\}", vendorProfile.getTipePerusahaan())
					.replaceAll("\\{User\\.username\\}", user.getUsername())
					.replaceAll("\\{User\\.namaPengguna\\}", user.getNamaPengguna())
					.replaceAll("\\{User\\.password\\}", password));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	public void getMailGeneratorEditVendorForApproval(Vendor vendor,User userApproval) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			VendorProfile vendorProfile=vendorProfileSession.getVendorProfileByVendorId(vendor.getId()).get(0);
			MasterEmailNotification men = masterEmailNotificationSession.find(110); // Edit vendor sisi approval
																					
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(userApproval.getEmail());
			en.setSubject(men.getNama());
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\{Approval\\.nama\\}", userApproval.getNamaPengguna())			
					.replaceAll("\\{VendorProfile\\.namaPerusahaan\\}", vendorProfile.getNamaPerusahaan())
					.replaceAll("\\{VendorProfile\\.namaAfco\\}", vendorProfile.getUnitTerdaftar())
					.replaceAll("\\{VendorProfile\\.alamat\\}", vendorProfile.getAlamat())
					.replaceAll("\\{VendorProfile\\.faksimile\\}", vendorProfile.getFaksimile())
					.replaceAll("\\{VendorProfile\\.telephone\\}", vendorProfile.getTelephone())
					.replaceAll("\\{VendorProfile\\.provinsi\\}", vendorProfile.getProvinsi())
					.replaceAll("\\{VendorProfile\\.kota\\}", vendorProfile.getKota())
					.replaceAll("\\{VendorProfile\\.kodePos\\}", vendorProfile.getKodePos())
					.replaceAll("\\{VendorProfile\\.tipePerusahaan\\}", vendorProfile.getTipePerusahaan()));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	public void getMailGeneratorEditVendorEndSuccess(Vendor vendor) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			VendorProfile vendorProfile=vendorProfileSession.getVendorProfileByVendorId(vendor.getId()).get(0);
			MasterEmailNotification men = masterEmailNotificationSession.find(112); // Edit vendor sisi vendor
																					
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(vendor.getEmail());
			en.setSubject(men.getNama());
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\{VendorProfile\\.namaPerusahaan\\}", vendorProfile.getNamaPerusahaan())
					.replaceAll("\\{VendorProfile\\.namaAfco\\}", vendorProfile.getUnitTerdaftar())
					.replaceAll("\\{VendorProfile\\.alamat\\}", vendorProfile.getAlamat())
					.replaceAll("\\{VendorProfile\\.faksimile\\}", vendorProfile.getFaksimile())
					.replaceAll("\\{VendorProfile\\.telephone\\}", vendorProfile.getTelephone())
					.replaceAll("\\{VendorProfile\\.provinsi\\}", vendorProfile.getProvinsi())
					.replaceAll("\\{VendorProfile\\.kota\\}", vendorProfile.getKota())
					.replaceAll("\\{VendorProfile\\.kodePos\\}", vendorProfile.getKodePos())
					.replaceAll("\\{VendorProfile\\.tipePerusahaan\\}", vendorProfile.getTipePerusahaan()));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	public void getMailGeneratorEditVendorEndReject(Vendor vendor) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			VendorProfile vendorProfile=vendorProfileSession.getVendorProfileByVendorId(vendor.getId()).get(0);
			MasterEmailNotification men = masterEmailNotificationSession.find(113); // Edit vendor sisi vendor
																					
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(vendor.getEmail());
			en.setSubject(men.getNama());
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\{VendorProfile\\.namaPerusahaan\\}", vendorProfile.getNamaPerusahaan())
					.replaceAll("\\{VendorProfile\\.namaAfco\\}", vendorProfile.getUnitTerdaftar())
					.replaceAll("\\{VendorProfile\\.alamat\\}", vendorProfile.getAlamat())
					.replaceAll("\\{VendorProfile\\.faksimile\\}", vendorProfile.getFaksimile())
					.replaceAll("\\{VendorProfile\\.telephone\\}", vendorProfile.getTelephone())
					.replaceAll("\\{VendorProfile\\.provinsi\\}", vendorProfile.getProvinsi())
					.replaceAll("\\{VendorProfile\\.kota\\}", vendorProfile.getKota())
					.replaceAll("\\{VendorProfile\\.kodePos\\}", vendorProfile.getKodePos())
					.replaceAll("\\{VendorProfile\\.tipePerusahaan\\}", vendorProfile.getTipePerusahaan()));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}

	public void getMailGeneratorApprovalPO(int poID) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.find(7); // Approval
																					// vendor
			// PurchaseOrder
			PurchaseOrder po = purchaseOrderSession.getPurchaseOrder(poID);
			if (po != null) {
				// Vendor
				List<PurchaseOrderItem> poItemList = purchaseOrderItemSession.getPurchaseOrderItemByPoId(po.getId());
				if (poItemList.size() > 0) {
					Vendor vendor = poItemList.get(0).getVendor();
					List<VendorProfile> vendorProfileList = vendorProfileSession
							.getVendorProfileByVendorId(vendor.getId());
					if (vendorProfileList.size() > 0) {
						VendorProfile vendorProfile = vendorProfileList.get(0);
						EmailNotification en = new EmailNotification();
						en.setCreated(new Date());
						en.setEmailTo(vendorProfile.getEmail());
						en.setSubject(men.getNama());
						if (po.getPengadaan() == null) {
							en.setMessage(men.getTemplateEmail()
									.replaceAll("\\{VendorProfile\\.namaPerusahaan\\}",
											vendorProfile.getNamaPerusahaan())
									.replaceAll("\\{PurchaseOrder\\.poNumber\\}", po.getPoNumber())
									.replaceAll("\\{Pengadaan\\.namaPengadaan\\}","")
									.replaceAll("\\{Pengadaan\\.nomorNotaDinas\\}","")
									.replaceAll("\\{Panitia\\.nama\\}", "Admin"));
						} else {
									
							en.setMessage(men.getTemplateEmail()
									.replaceAll("\\{VendorProfile\\.namaPerusahaan\\}",
											vendorProfile.getNamaPerusahaan())
									.replaceAll("\\{PurchaseOrder\\.poNumber\\}", po.getPoNumber())
									.replaceAll("\\{Pengadaan\\.namaPengadaan\\}", po.getPengadaan().getNamaPengadaan())
									.replaceAll("\\{Pengadaan\\.nomorNotaDinas\\}",
											po.getPengadaan().getNomorNotaDinas())
									.replaceAll("\\{Panitia\\.nama\\}",
											po.getPengadaan().getPanitia().getDivisi().getNama()));
						}

						en.setStatusEmailSending(0);
						en.setSendingDate(new Date());
						en.setIsDelete(0);
						en.setMasterEmailNotification(men);
						super.create(en);
					}

				}
			}

		}
	}
	
	public void getMailGeneratorPerpanjanganSertifikat(SertifikatVendor sertifikatVendor, VendorProfile vendorProfile) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);

		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.find(105); // Perpanjangan
																					// Sertifikat
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(vendorProfile.getEmail());
			en.setSubject(men.getNama());
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String sertifikatVendorTanggal = sdf.format(sertifikatVendor.getUpdated());
			
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\{VendorProfile\\.namaPerusahaan\\}", vendorProfile.getNamaPerusahaan())
					.replaceAll("\\{SertifikatVendor\\.tanggal\\}", sertifikatVendorTanggal));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
		
	}
	
	public void getMailGeneratorCreateCOVendorAndUserCreated(User user,Integer vendorId, String soNumber) {
		Vendor vendor = vendorSession.getVendorById(vendorId);
		List<VendorProfile> vendorProfileList = vendorProfileSession.getVendorProfileByVendorId(vendorId);
		VendorProfile vendorProfile = vendorProfileList.get(0);
		
		
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.find(106); // Penambahan
																					// CO
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(vendor.getEmail());
			en.setSubject(men.getNama());

			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\{Vendor\\.nama\\}", vendor.getNama())
					.replaceAll("\\{Nomor\\.bookingOrder\\}", soNumber)
					.replaceAll("\\{VendorProfile\\.tipePerusahaan\\}", vendorProfile.getTipePerusahaan())
					.replaceAll("\\{VendorProfile\\.namaPerusahaan\\}", vendorProfile.getNamaPerusahaan()));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
			
			
			//set email notifto user created
			
			MasterEmailNotification menforuser = masterEmailNotificationSession.find(107); // created bo user
			// CO
			EmailNotification enUser = new EmailNotification();
			enUser.setCreated(new Date());
			enUser.setEmailTo(user.getEmail());
			enUser.setSubject(menforuser.getNama());
			
			enUser.setMessage(menforuser.getTemplateEmail()
			.replaceAll("\\{Vendor\\.nama\\}", vendor.getNama())
			.replaceAll("\\{Nomor\\.bookingOrder\\}", soNumber));
			
			enUser.setStatusEmailSending(0);
			enUser.setSendingDate(new Date());
			enUser.setIsDelete(0);
			enUser.setMasterEmailNotification(menforuser);
			super.create(enUser);
		}
	}
	
	public void getMailGeneratorBlacklistVendor(User user, VendorProfile vendorProfile) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);

		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.find(1000); // Blacklist
																					// Vendor
			
			if(men == null) {
				men = masterEmailNotificationSession.getMasterEmailNotificationByNama("Blacklist Vendor");
			}
			
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(user.getEmail());
			en.setSubject(men.getNama());
			
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\{User\\.username\\}", user.getUsername())
					.replaceAll("\\{User\\.email\\}", user.getEmail())
					.replaceAll("\\{VendorProfile\\.tipePerusahaan\\}", vendorProfile.getTipePerusahaan())
					.replaceAll("\\{VendorProfile\\.namaPerusahaan\\}", vendorProfile.getNamaPerusahaan()));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	public void getMailGeneratorUnblacklistVendor(User user, VendorProfile vendorProfile) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);

		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.find(1001); // Unblacklist
																					// Vendor
			
			if(men == null) {
				men = masterEmailNotificationSession.getMasterEmailNotificationByNama("Unblacklist Vendor");
			}
			
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(user.getEmail());
			en.setSubject(men.getNama());
			
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\{User\\.email\\}", user.getEmail())
					.replaceAll("\\{VendorProfile\\.tipePerusahaan\\}", vendorProfile.getTipePerusahaan())
					.replaceAll("\\{VendorProfile\\.namaPerusahaan\\}", vendorProfile.getNamaPerusahaan()));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
		
	}
	
	public void getMailGeneratorUserGeneratePassword(User user, String password) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);

		if (ens.isStatusAktif()) {
			
			MasterEmailNotification men = masterEmailNotificationSession.getMasterEmailNotificationByNama(Constant.NOTIFICATION_TEMPLATE_GENERATE_PASSWORD);
			
			
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(user.getEmail());
			en.setSubject(men.getNama());
			
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\{user\\.name\\}", user.getNamaPengguna())
					.replaceAll("\\{user\\.password\\}", password));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
		
	}
	
	public void getMailGeneratorSyncCatalog(List<String> insertedSKUList,List<String> updatedSKUList,Integer totalData,Integer insertSuccess,Integer updateSuccess ) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		 Calendar cal = Calendar.getInstance();
	     SimpleDateFormat sdf = new SimpleDateFormat("HH");
	     Integer jam =Integer.parseInt(sdf.format(cal.getTime()));
	     String sesi = "Sync Malam";
	     if(jam < 18){
	    	 sesi = "Sync Pagi";
	     }
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.find(114); // Sync Catalog
			
			if(men == null) {
				men = masterEmailNotificationSession.getMasterEmailNotificationByNama("Sync Catalog Email");
			}
			
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(ParamContext.getParameterStringByName("EMAIL_SYNC_CATALOG_RECEIVER"));
			en.setSubject(men.getNama());
			
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\{sync\\.totalData\\}", totalData.toString())
					.replaceAll("\\{sync\\.sesiSync\\}", sesi)
					.replaceAll("\\{sync\\.insertSuccess\\}", insertSuccess.toString())
					.replaceAll("\\{sync\\.listInsertedSKU\\}", insertedSKUList.toString())
					.replaceAll("\\{sync\\.updateSuccess\\}", updateSuccess.toString())
					.replaceAll("\\{sync\\.listUpdatedSKU\\}", updatedSKUList.toString()));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
		
	}
	
	public void getMailBookingOrderToEbs(PurchaseRequest purchaseRequest, String email) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.getMasterEmailNotificationByNama(Constant.RILIS_BOOKING_ORDER_TO_EBS);
			User user = userSession.getUser(purchaseRequest.getRequestorUserId());
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(email);
			en.setSubject(men.getNama());
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\$NO_BO", ((purchaseRequest.getBoNumber() != null) ? purchaseRequest.getBoNumber() : "-"))
					.replaceAll("\\$PR_NUMBER", ((purchaseRequest.getPrnumber() != null) ? purchaseRequest.getPrnumber() : "-"))
					.replaceAll("\\$NAMA_JABATAN", ((user.getJabatan().getNama() != null) ? user.getJabatan().getNama() : "-")));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	public void getMailDeliveryReceiptToEbs(DeliveryReceived deliveryReceived, String email) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.getMasterEmailNotificationByNama(Constant.RILIS_DELIVERY_RECEIPT_TO_EBS);
			User user = userSession.getUser(deliveryReceived.getRequestorUserId());
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(email);
			en.setSubject(men.getNama());
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\$PO_NUMBER", ((deliveryReceived.getPurchaseOrder().getPoNumber() != null) ? deliveryReceived.getPurchaseOrder().getPoNumber() : "-"))
					.replaceAll("\\$DR_NUMBER", ((deliveryReceived.getDeliveryReceiptNum() != null) ? deliveryReceived.getDeliveryReceiptNum() : "-"))
					.replaceAll("\\$NAMA_JABATAN", ((user.getJabatan().getNama() != null) ? user.getJabatan().getNama() : "-")));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	public void getMailPurchaseOrderToEbs(PurchaseOrder purchaseOrder, String email, String namaOrg) throws ParseException {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.getMasterEmailNotificationByNama(Constant.RILIS_PURCHASE_ORDER_TO_EBS);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			DecimalFormat formatDecimal = (DecimalFormat) DecimalFormat.getCurrencyInstance();
 	        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
 	        formatRp.setCurrencySymbol(" ");
	        formatRp.setMonetaryDecimalSeparator(',');
	        formatRp.setGroupingSeparator('.');
	        formatDecimal.setDecimalFormatSymbols(formatRp); 
	        String rows = "";
	        Integer no = 1;
	        double tampTotal= 0D;
	        
			List<Object[]> objList = purchaseOrderSession.getPurchaseOrderByPOId(purchaseOrder.getId());
			List<EmailPurchaseOrderDTO> EmailPurchaseOrderDTOList = new ArrayList<EmailPurchaseOrderDTO>();
			if(objList.size() > 0) {
				for(Object[] obj : objList) {
					EmailPurchaseOrderDTO emailPurchaseOrderDTO = new EmailPurchaseOrderDTO();
					emailPurchaseOrderDTO.setPurchaseRequest((PurchaseRequest)obj[0]);
					emailPurchaseOrderDTO.setPurchaseOrder((PurchaseOrder)obj[1]);
					emailPurchaseOrderDTO.setDeliveryReceived((DeliveryReceived)obj[2]);
					emailPurchaseOrderDTO.setPurchaseOrderItem((PurchaseOrderItem)obj[3]);
					emailPurchaseOrderDTO.setVendor((Vendor)obj[4]);
					emailPurchaseOrderDTO.setItem((Item)obj[5]);
					emailPurchaseOrderDTO.setCatalog((Catalog)obj[6]);
					emailPurchaseOrderDTO.setSatuan((Satuan)obj[7]);
					emailPurchaseOrderDTO.setAddressBook((AddressBook)obj[8]);
					EmailPurchaseOrderDTOList.add(emailPurchaseOrderDTO);
				}
			}
	        
			for(EmailPurchaseOrderDTO emailPurchaseOrderDTOTamp: EmailPurchaseOrderDTOList) {
				Integer jmlDiterima = 0;
				DeliveryReceived deliveryReceived = deliveryReceivedSession.getDeliveryReceivedByPoIdSingle(emailPurchaseOrderDTOTamp.getPurchaseOrder().getId());
				if(deliveryReceived != null) {
					double tampQuantityPR = emailPurchaseOrderDTOTamp.getPurchaseOrderItem().getQuantityPurchaseRequest();
					jmlDiterima = (int) tampQuantityPR;
				}
				double unitPrice = emailPurchaseOrderDTOTamp.getPurchaseOrderItem().getUnitPrice();
				double totalUnitPrice = emailPurchaseOrderDTOTamp.getPurchaseOrderItem().getTotalUnitPrices();
				rows += "<tr>"
		                  + "<td style=\"text-align: center;\">" + no + "</strong></td>"
		                  + "<td style=\"text-align: center;\">" + emailPurchaseOrderDTOTamp.getCatalog().getNamaIND() + "</td>"
		                  + "<td style=\"text-align: center;\">" + emailPurchaseOrderDTOTamp.getItem().getKode() + "</td>"
		                  + "<td style=\"text-align: center;\">" + emailPurchaseOrderDTOTamp.getSatuan().getNama() + "</td>"
		                  + "<td style=\"text-align: center;\">" + emailPurchaseOrderDTOTamp.getPurchaseOrderItem().getQuantityPurchaseRequest() + "</td>"
		                  + "<td style=\"text-align: center;\">" + jmlDiterima + "</td>"
		                  + "<td style=\"text-align: center;\">" + formatDecimal.format(unitPrice)+ "</td>"
		                  + "<td style=\"text-align: center;\">" + formatDecimal.format(totalUnitPrice)+ "</td>"
		                + "</tr>";
				tampTotal += totalUnitPrice;
		         no++;
			}
			
			List<Integer> slaDeliveryTimeList = purchaseRequestItemSession.groupByPurchaseRequestId(purchaseOrder.getPurchaseRequest().getId());
			Integer slaDeliveryTime   = slaDeliveryTimeList.size()>0?slaDeliveryTimeList.get(0):0;
			for(Integer slaDel : slaDeliveryTimeList) {
				if(slaDel > slaDeliveryTime) {
					slaDeliveryTime = slaDel;
				}
			}
			
			Integer sla   		= slaDeliveryTime;
			String poDate 		= sdf.format(purchaseOrder.getPurchaseOrderDate());
			String alamat 		= purchaseOrder.getAddressBook().getStreetAddress();
			double tampOngkir	= purchaseOrder.getPurchaseRequest().getTotalHargaOngkir();
			double tampPpn	  	= (tampTotal + tampOngkir) * 10 /100;
			double tampGrandTotal= tampTotal + tampOngkir + tampPpn;
			
			String total 		= "<td>" + formatDecimal.format(tampTotal) + "</strong></td>";
			String ongkir 		= "<td>" + formatDecimal.format(tampOngkir)  + "</strong></td>";
			String ppn 			= "<td>" + formatDecimal.format(tampPpn) + "</strong></td>";
			String grandTotal 	= "<td>" + formatDecimal.format(tampGrandTotal) + "</strong></td>";
				
			calendar.setTime(sdf.parse(poDate)); 
			calendar.add(Calendar.DAY_OF_MONTH, sla); 
			String estimatedDate = sdf.format(calendar.getTime());  
			
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(email);
			en.setSubject(men.getNama());
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\$NOMOR_PO", ((purchaseOrder.getPoNumber() != null) ? purchaseOrder.getPoNumber() : "-"))
					.replaceAll("\\$TANGGAL_PO", ((purchaseOrder.getPurchaseOrderDate() != null) ? sdf.format(purchaseOrder.getPurchaseOrderDate()) : "-"))
					.replaceAll("\\$ORGANISASI", ((namaOrg != null) ? namaOrg : "-"))
					.replaceAll("\\$NAMA_PENYEDIA", ((purchaseOrder.getVendorName() != null) ? purchaseOrder.getVendorName() : "-"))
					.replaceAll("\\$STATUS_PO", ((purchaseOrder.getStatus() != null) ? purchaseOrder.getStatus() : "-"))
					.replaceAll("\\$STATUS_PO_EBS", ((purchaseOrder.getStatusEbs() != null) ? purchaseOrder.getStatusEbs() : "-"))
					.replaceAll("\\$ROWS", (rows))
					.replaceAll("\\$TOTAL", (total))
					.replaceAll("\\$ONGKOS_KIRIM", (ongkir))
					.replaceAll("\\$PPN", (ppn))
					.replaceAll("\\$GRAND_TOTAL", (grandTotal))
					.replaceAll("\\$ESTIMATED_DATE", ((estimatedDate != null) ? estimatedDate : "-"))
					.replaceAll("\\$DELIVERY_TO", ((alamat != null) ? alamat : "-")));
			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	public void getMailBookingOrderToVendor(PurchaseRequest purchaseRequest, User user, String email) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.getMasterEmailNotificationByNama(Constant.NOTIF_PURCHASE_REQUEST_TO_VENDOR);
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(email);
			en.setSubject(men.getNama());
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\$NO_BO", ((purchaseRequest.getBoNumber() != null) ? purchaseRequest.getBoNumber() : "-"))
					.replaceAll("\\$NAMA_JABATAN", ((user.getNamaPengguna() != null) ? user.getNamaPengguna() : "-")));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}

	public void getMailPembayaranSelesai(PurchaseOrder purchaseOrder, DeliveryReceived deliveryReceived, InvoicePayment invoicePayment, String email) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.getMasterEmailNotificationByNama(Constant.PEMBERITAHUAN_PEMBAYARAN_SELESAI);
			User user = userSession.getUser(deliveryReceived.getRequestorUserId());
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(email);
			en.setSubject(men.getNama());
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\$PO_NUMBER", ((purchaseOrder.getPoNumber() != null) ? purchaseOrder.getPoNumber() : "-"))
					.replaceAll("\\$DR_NUMBER", ((deliveryReceived.getDeliveryReceiptNum() != null) ? deliveryReceived.getDeliveryReceiptNum() : "-"))
					.replaceAll("\\$INVOICE_NO", ((invoicePayment.getInvoiceNumber() != null) ? invoicePayment.getInvoiceNumber() : "-"))
					.replaceAll("\\$NAMA_JABATAN", ((user.getJabatan().getNama() != null) ? user.getJabatan().getNama() : "-")));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	public void getMailPenerbitanInvoice(PurchaseOrder purchaseOrder, DeliveryReceived deliveryReceived,  InvoicePayment invoicePayment, String email) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.getMasterEmailNotificationByNama(Constant.PEMBERITAHUAN_PENERBITAN_INVOICE);
			User user = userSession.getUser(deliveryReceived.getRequestorUserId());
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(email);
			en.setSubject(men.getNama());
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\$PO_NUMBER", ((purchaseOrder.getPoNumber() != null) ? purchaseOrder.getPoNumber() : "-"))
					.replaceAll("\\$DR_NUMBER", ((deliveryReceived.getDeliveryReceiptNum() != null) ? deliveryReceived.getDeliveryReceiptNum() : "-"))
					.replaceAll("\\$INVOICE_NO", ((invoicePayment.getInvoiceNumber() != null) ? invoicePayment.getInvoiceNumber() : "-"))
					.replaceAll("\\$NAMA_JABATAN", ((user.getJabatan().getNama() != null) ? user.getJabatan().getNama() : "-")));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	public void getMailPersetujuanBO(PurchaseRequest purchaseRequest, String statusEmail, String note, String vendor, String email) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.getMasterEmailNotificationByNama(Constant.PEMBERITAHUAN_PERSETUJUAN_BOOKING_ORDER);
			EmailNotification en = new EmailNotification();
			User user = userSession.getUser(purchaseRequest.getRequestorUserId());
			
			en.setCreated(new Date());
			en.setEmailTo(email);
			en.setSubject(men.getNama());
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\$NO_BO", ((purchaseRequest.getBoNumber() != null) ? purchaseRequest.getBoNumber() : "-"))
					.replaceAll("\\$STATUS", (statusEmail))
					.replaceAll("\\$NAMA_VENDOR", (vendor))
					.replaceAll("\\$NOTES", (note))
					.replaceAll("\\$NAMA_JABATAN", ((user.getNamaPengguna() != null) ? user.getNamaPengguna() : "-"))); 

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	public void getMailBerhasilDaftar(String username, String email, String password, User user, String cabang) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.getMasterEmailNotificationByNama(Constant.PEMBERITAHUAN_BERHASIL_MENDAFTAR);
			EmailNotification en = new EmailNotification();
			en.setCreated(new Date());
			en.setEmailTo(email);
			en.setSubject(men.getNama());
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\$JABATAN", ((user.getJabatan().getNama() != null) ? user.getJabatan().getNama() : "-"))
					.replaceAll("\\$CABANG", ((cabang != null) ? cabang : "-"))
					.replaceAll("\\$NAMA_PENGGUNA", ((user.getNamaPengguna() != null) ? user.getNamaPengguna() : "-"))
					.replaceAll("\\$USERNAME", ((username != null) ? username : "-"))
					.replaceAll("\\$PASSWORD", (password)));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	public void getMailTest() {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.getMasterEmailNotificationByNama(Constant.PEMBERITAHUAN_PERSETUJUAN_BOOKING_ORDER);
			EmailNotification en = new EmailNotification();
			
			en.setCreated(new Date());
			en.setEmailTo("yudi.ismiaji@mmi-pt.com");
			en.setSubject(men.getNama());
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\$NO_BO", "Tes 123")
					.replaceAll("\\$STATUS", "Tes")
					.replaceAll("\\$NAMA_VENDOR", "Vendor Tes")
					.replaceAll("\\$NOTES", "Catatan Tes")
					.replaceAll("\\$NAMA_JABATAN", "Jabatan Tes"));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	
	public void getMailDeliveryReceived(Integer drId, String email) {
		EmailNotificationStatus ens = emailNotificationStatusSession.find(1);
		if (ens.isStatusAktif()) {
			MasterEmailNotification men = masterEmailNotificationSession.getMasterEmailNotificationByNama(Constant.NOTIF_EMAIL_DELIVERY_RECEIVED);
			DeliveryReceived deliveryReceived = deliveryReceivedSession.getDeliveryReceived(drId);
			User user = userSession.getUser(deliveryReceived.getRequestorUserId());
			EmailNotification en = new EmailNotification();
			SimpleDateFormat formatDateOnly = new SimpleDateFormat("dd-MM-yyyy");
			Date estimatedDate = new Date();
			DecimalFormat formatDecimal = (DecimalFormat) DecimalFormat.getCurrencyInstance();
 	        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
// 	        formatRp.setCurrencySymbol("Rp. ");
	        formatRp.setMonetaryDecimalSeparator('.');
	        formatRp.setGroupingSeparator(',');
	        formatDecimal.setDecimalFormatSymbols(formatRp);
			Calendar calender = Calendar.getInstance();
			calender.setTime(deliveryReceived.getPurchaseOrder().getPurchaseOrderDate());
			
			List<Integer> slaDeliveryTimeList = purchaseRequestItemSession.groupByPurchaseRequestId(deliveryReceived.getPurchaseOrder().getPurchaseRequest().getId());
			Integer slaDeliveryTime   = slaDeliveryTimeList.size()>0?slaDeliveryTimeList.get(0):0;
			for(Integer slaDel : slaDeliveryTimeList) {
				if(slaDel > slaDeliveryTime) {
					slaDeliveryTime = slaDel;
				}
			}
			
			calender.add(Calendar.DATE, slaDeliveryTime);
			estimatedDate = calender.getTime();
			List<DeliveryReceivedDetailDTO> drdList = purchaseOrderSession.getDeliveryReceivedDetailReceivedList(deliveryReceived.getPurchaseOrder().getId(), deliveryReceived.getId());
			String rows = "";
	        Integer no = 1;
	        Double total = 0D;
	        for(DeliveryReceivedDetailDTO deliveryReceivedDetailDTO : drdList) {
	        	CatalogContractDetail catalogContractDetail = catalogContractDetailSession.getCatalogContractDetailByItem(deliveryReceivedDetailDTO.getPurchaseOrderItem().getItem());
		        total += deliveryReceivedDetailDTO.getPurchaseOrderItem().getTotalUnitPrices();
		        rows += "<tr>"
	                  + "<td style=\"text-align: center;\">" + no + "</strong></td>"
	                  + "<td style=\"text-align: center;\">" + catalogContractDetail.getItemDesc() + "</td>"
	                  + "<td style=\"text-align: center;\">" + deliveryReceivedDetailDTO.getPurchaseOrderItem().getItem().getKode() + "</td>"
	                  + "<td style=\"text-align: center;\">" + deliveryReceivedDetailDTO.getPurchaseOrderItem().getItem().getSatuanId().getNama() + "</td>"
	                  + "<td style=\"text-align: center;\">" + deliveryReceivedDetailDTO.getPurchaseOrderItem().getQuantityPurchaseRequest() + "</td>"
	                  + "<td style=\"text-align: center;\">" + deliveryReceivedDetailDTO.getDikirim() + "</td>"
	                  + "<td style=\"text-align: center;\">" + formatDecimal.format(deliveryReceivedDetailDTO.getPurchaseOrderItem().getUnitPrice()) + "</td>"
	                  + "<td style=\"text-align: center;\">" + formatDecimal.format(deliveryReceivedDetailDTO.getPurchaseOrderItem().getTotalUnitPrices()) + "</td>"
	                + "</tr>";
	           no++;
	        }
	        Double ongkir = deliveryReceived.getPurchaseOrder().getPurchaseRequest().getTotalHargaOngkir();
	        Double ppn = ((total+ongkir)*10)/100;
	        Double grand = total+ongkir+ppn;
	        String foots = "<tr>"
	                  + "<th colspan=\"7\" style=\"text-align: right\"><span>TOTAL </span></th>"
	                  + "<th style=\"text-align: right\">" + formatDecimal.format(total) + "</th>"
	                  +"</tr>"
	                  + "<tr>"
	                  + "<th colspan=\"7\" style=\"text-align: right\"><span>Ogkos Kirim </span></th>"
	                  + "<th style=\"text-align: right\">" + formatDecimal.format(ongkir) + "</th>"
	                  +"</tr>"
	                  + "<tr>"
	                  + "<th colspan=\"7\" style=\"text-align: right\"><span>PPN </span></th>"
	                  + "<th style=\"text-align: right\">" + formatDecimal.format(ppn) + "</th>"
	                  +"</tr>"
	                  + "<tr>"
	                  + "<th colspan=\"7\" style=\"text-align: right\"><span>Grand Total </span></th>"
	                  + "<th style=\"text-align: right\">" + formatDecimal.format(grand) + "</th>"
	                  +"</tr>";
			en.setCreated(new Date());
			en.setEmailTo(email);
			en.setSubject(men.getNama());
			en.setMessage(men.getTemplateEmail()
					.replaceAll("\\$NOMOR_PO", ((deliveryReceived.getPurchaseOrder().getPoNumber() != null) ? deliveryReceived.getPurchaseOrder().getPoNumber() : "-"))
					.replaceAll("\\$TANGGAL_PO", ((deliveryReceived.getPurchaseOrder().getPurchaseOrderDate() != null) ? formatDateOnly.format(deliveryReceived.getPurchaseOrder().getPurchaseOrderDate()) : "-"))
					.replaceAll("\\$NAMA_PENYEDIA", ((deliveryReceived.getPurchaseOrder().getVendorName() != null) ? deliveryReceived.getPurchaseOrder().getVendorName() : "-"))
					.replaceAll("\\$NOMOR_DR", ((deliveryReceived.getDeliveryReceiptNum() != null) ? deliveryReceived.getDeliveryReceiptNum() : "-"))
					.replaceAll("\\$TANGGAL_DR", ((deliveryReceived.getPurchaseOrder().getDeliveryTime() != null) ? formatDateOnly.format(deliveryReceived.getPurchaseOrder().getDeliveryTime()) : "-"))
					.replaceAll("\\$NOMOR_SRT_JLN", ((deliveryReceived.getDeliveryOrderNum() != null) ? deliveryReceived.getDeliveryOrderNum() : "-"))
					.replaceAll("\\$TANGGAL_SRT_JLN", ((deliveryReceived.getDeliveryOrderDate() != null) ? formatDateOnly.format(deliveryReceived.getDeliveryOrderDate()) : "-"))
					.replaceAll("\\$ROWS", (rows))
					.replaceAll("\\$FOOTS", (foots))
					.replaceAll("\\$ESTIMATED_DATE", ((estimatedDate != null) ? formatDateOnly.format(estimatedDate) : "-"))
					// KAI - 20210204 - #20867
					//	.replaceAll("\\$DELIVERY_TO", ((deliveryReceived.getPurchaseOrder().getPurchaseRequest().getAlamat() != null) ? deliveryReceived.getPurchaseOrder().getPurchaseRequest().getAlamat() : "-"))
					.replaceAll("\\$TANGGAL_TERIMA", ((deliveryReceived.getDateReceived() != null) ? formatDateOnly.format(deliveryReceived.getDateReceived()) : "-"))
					.replaceAll("\\$NAMA_USER_INTERNAL", ((user.getNamaPengguna() != null) ? user.getNamaPengguna() : "-"))
					.replaceAll("\\$NIPP", ((user.getUsername() != null) ? user.getUsername() : "-")));

			en.setStatusEmailSending(0);
			en.setSendingDate(new Date());
			en.setIsDelete(0);
			en.setMasterEmailNotification(men);
			super.create(en);
		}
	}
	
	/*penambahan modul KAI 22/12/2020 [21152]*/
	@SuppressWarnings("unchecked")
	public List<EmailNotification> getList(){
		Query query = em.createNamedQuery("EmailNotification.getList");
		return (List<EmailNotification>)query.getResultList();
	}
	/*penambahan modul KAI 22/12/2020 [21152]*/
	@Asynchronous
	public void sendEmail(String emailTo, String subject, String contentEmail) throws AddressException, MessagingException {
		try {
			MasterSMTPServer smtp = smtpSession.findAll().get(0);
			String host = smtp.getHost();// Gmail
			int port = smtp.getPort();
			String serviceUsername = smtp.getUsername();
			String servicePassword = smtp.getPassword();// Our Gmail password

			Properties props = new Properties();
			props.put("mail.smtp.user", serviceUsername);
			props.put("mail.smtp.password", servicePassword);
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.debug", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.socketFactory.port", port);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");

			// Destination of the email
			String from = smtp.getUsername();
			// Auth
			SmtpAuthenticator smtpAuth = new SmtpAuthenticator(serviceUsername, servicePassword);
			// Creating a javax.Session with the our prope
			Session session = Session.getInstance(props, smtpAuth);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
			message.setSubject(subject);
			message.setContent(contentEmail, "text/html; charset=utf-8");
			Transport.send(message);
			logger.info("sending email to " + emailTo + " success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("sending email to " + emailTo + " failed. Cause : "+e.getMessage()+e.getCause());
		}
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
