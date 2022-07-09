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
@Table(name = "T5_KLARIFIKASI_H_S")
@NamedQueries({
		@NamedQuery(name = "KlarifikasiHargaSatuan.find", query = "SELECT x FROM KlarifikasiHargaSatuan x WHERE x.isDelete = 0"),
		@NamedQuery(name = "KlarifikasiHargaSatuan.findByPengadaan", query = "SELECT x FROM KlarifikasiHargaSatuan x WHERE x.isDelete =0 AND x.pengadaan.id = :pengadaanId"),
		@NamedQuery(name = "KlarifikasiHargaSatuan.findByVendor", query = "SELECT x FROM KlarifikasiHargaSatuan x WHERE x.isDelete =0 AND x.vendor.id=:vendorId"),
		@NamedQuery(name = "KlarifikasiHargaSatuan.findByVendorAndPengadaanAndItem", query = "SELECT x FROM KlarifikasiHargaSatuan x WHERE x.isDelete =0 AND x.vendor.id=:vendorId and x.pengadaan.id=:pengadaanId and x.item.id=:itemId"),
		@NamedQuery(name = "KlarifikasiHargaSatuan.findByPengadaanAndItem", query = "SELECT x FROM KlarifikasiHargaSatuan x WHERE x.isDelete =0 AND x.pengadaan.id=:pengadaanId and x.item.id=:itemId") })

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_KLARIFIKASI_H_S", initialValue = 1, allocationSize = 1)
public class KlarifikasiHargaSatuan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "KLARIFIKASI_HARGA_SATUAN_ID")
	private Integer id;

	@Column(name = "FINAL_CHECK")
	private Boolean finalCheck;

	@ManyToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")
	private Pengadaan pengadaan;

	@ManyToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	@ManyToOne
	@JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID")
	private Item item;

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

	public Boolean getFinalCheck() {
		return finalCheck;
	}

	public void setFinalCheck(Boolean finalCheck) {
		this.finalCheck = finalCheck;
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

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
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
