package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T2_M_EMAIL_NOTIF")
@NamedQueries({
		@NamedQuery(name = "MasterEmailNotification.find", query = "SELECT m FROM MasterEmailNotification m WHERE m.isDelete = 0"),
		@NamedQuery(name = "MasterEmailNotification.findByTahapanAndRole", query = "SELECT m FROM MasterEmailNotification m WHERE m.isDelete = 0 AND m.role.id=:roleId AND m.tahapan.id=:tahapanId"),
		@NamedQuery(name = "MasterEmailNotification.findNama", query = "SELECT masterEmailNotification FROM MasterEmailNotification masterEmailNotification WHERE masterEmailNotification.isDelete = 0 AND masterEmailNotification.nama = :nama")
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_M_EMAIL_NOTIF", initialValue = 1, allocationSize = 1)
public class MasterEmailNotification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MASTEREMAILNOTIFY_ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "TAHAPAN_ID", referencedColumnName = "TAHAPAN_ID")
	private Tahapan tahapan;
	
	@ManyToOne
	@JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")
	private Role role;
	
	@Column(name = "NOTIFIKASI_TGL_SESUDAH")
	private int notifikasiTglSesudah;
	
	@Column(name = "NOTIFIKASI_TGL_SEBELUM")
	private int notifikasiTglSebelum;
	
	@Column(name = "TEMPLATE_EMAIL", length = 2000)
	private String templateEmail;
	
	@Column(name = "NAMA")
	private String nama;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED")
	private Date deleted;

	@Column(name = "USERID")
	private Integer userId;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Tahapan getTahapan() {
		return tahapan;
	}

	public void setTahapan(Tahapan tahapan) {
		this.tahapan = tahapan;
	}

	public int getNotifikasiTglSesudah() {
		return notifikasiTglSesudah;
	}

	public void setNotifikasiTglSesudah(int notifikasiTglSesudah) {
		this.notifikasiTglSesudah = notifikasiTglSesudah;
	}

	public int getNotifikasiTglSebelum() {
		return notifikasiTglSebelum;
	}

	public void setNotifikasiTglSebelum(int notifikasiTglSebelum) {
		this.notifikasiTglSebelum = notifikasiTglSebelum;
	}

	public String getTemplateEmail() {
		return templateEmail;
	}

	public void setTemplateEmail(String templateEmail) {
		this.templateEmail = templateEmail;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}
}
