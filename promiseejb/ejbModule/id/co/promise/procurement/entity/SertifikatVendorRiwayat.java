package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T4_SERTIF_V_RIWAYAT") 
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T4_SERTIF_V_RIWAYAT", initialValue = 1, allocationSize = 1)
public class SertifikatVendorRiwayat {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SERTIFIKAT_VENDOR_RIWAYAT_ID")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "SERTIFIKAT_VENDOR_ID", referencedColumnName = "SERTIFIKAT_VENDOR_ID")
	private SertifikatVendor sertifikatVendor;

	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	@Column(name = "NOMOR")
	private String nomor;
	
	@Column(name = "STATUS")
	private Integer status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TANGGAL_MULAI")
	private Date tanggalMulai;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TANGGAL_BERAKHIR")
	private Date tanggalBerakhir; 
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SertifikatVendor getSertifikatVendor() {
		return sertifikatVendor;
	}

	public void setSertifikatVendor(SertifikatVendor sertifikatVendor) {
		this.sertifikatVendor = sertifikatVendor;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getNomor() {
		return nomor;
	}

	public void setNomor(String nomor) {
		this.nomor = nomor;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getTanggalMulai() {
		return tanggalMulai;
	}

	public void setTanggalMulai(Date tanggalMulai) {
		this.tanggalMulai = tanggalMulai;
	}

	public Date getTanggalBerakhir() {
		return tanggalBerakhir;
	}

	public void setTanggalBerakhir(Date tanggalBerakhir) {
		this.tanggalBerakhir = tanggalBerakhir;
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
