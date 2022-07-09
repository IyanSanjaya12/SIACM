package id.co.promise.procurement.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T5_PENDAFTARAN_VENDOR")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_PENDAFTARAN_VENDOR", initialValue = 1, allocationSize = 1)
public class PendaftaranVendor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PENDAFTARAN_VENDOR_ID")
	private Integer id;

	@Column(name = "TANGGAL_DAFTAR")
	private Date tanggalDaftar;

	@Column(name = "NOMOR_PENDAFTARAN", length = 100)
	private String nomorPendaftaran;

	@ManyToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")
	private Pengadaan pengadaan;

	@ManyToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	@Transient
	private List<ItemPengadaan> itemPengadaanList;
	
	@Transient
	private Double totalHPS;

	public Double getTotalHPS() {
		return totalHPS;
	}

	public void setTotalHPS(Double totalHPS) {
		this.totalHPS = totalHPS;
	}

	public List<ItemPengadaan> getItemPengadaanList() {
		return itemPengadaanList;
	}

	public void setItemPengadaanList(List<ItemPengadaan> itemPengadaanList) {
		this.itemPengadaanList = itemPengadaanList;
	}

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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public Pengadaan getPengadaan() {
		return pengadaan;
	}

	public void setPengadaan(Pengadaan pengadaan) {
		this.pengadaan = pengadaan;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
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

	public String getNomorPendaftaran() {
		return nomorPendaftaran;
	}

	public void setNomorPendaftaran(String nomorPendaftaran) {
		this.nomorPendaftaran = nomorPendaftaran;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getTanggalDaftar() {
		return tanggalDaftar;
	}

	public void setTanggalDaftar(Date tanggalDaftar) {
		this.tanggalDaftar = tanggalDaftar;
	}
}
