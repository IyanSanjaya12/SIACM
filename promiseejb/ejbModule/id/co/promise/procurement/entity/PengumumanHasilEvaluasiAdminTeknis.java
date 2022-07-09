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
@NamedQueries({
		@NamedQuery(name = "PengumumanHasilEvaluasiAdminTeknis.find", query = "SELECT o FROM PengumumanHasilEvaluasiAdminTeknis o WHERE o.isDelete = 0"),
		@NamedQuery(name = "PengumumanHasilEvaluasiAdminTeknis.findByPengadaan", query = "SELECT o FROM PengumumanHasilEvaluasiAdminTeknis o WHERE o.isDelete = 0 AND o.pengadaan.id=:pengadaanId") })
@Table(name = "T5_P_H_E_A_T")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_P_H_E_A_T", initialValue = 1, allocationSize = 1)
public class PengumumanHasilEvaluasiAdminTeknis {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PENGUMUMAN_HSLEVLADMTEK_ID")
	private int id;
	@Column(name="NOMOR")
	private String nomor;
	@Temporal(TemporalType.DATE)
	@Column(name = "TANGGAL_PENGUMUMAN")
	private Date tanggalPengumuman;
	@ManyToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")
	private Pengadaan pengadaan;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
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
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNomor() {
		return nomor;
	}
	public void setNomor(String nomor) {
		this.nomor = nomor;
	}
	public Date getTanggalPengumuman() {
		return tanggalPengumuman;
	}
	public void setTanggalPengumuman(Date tanggalPengumuman) {
		this.tanggalPengumuman = tanggalPengumuman;
	}
	public Pengadaan getPengadaan() {
		return pengadaan;
	}
	public void setPengadaan(Pengadaan pengadaan) {
		this.pengadaan = pengadaan;
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

}
