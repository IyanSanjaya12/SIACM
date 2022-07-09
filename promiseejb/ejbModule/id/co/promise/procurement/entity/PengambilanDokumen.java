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

/**
 * @author nanang
 *
 */
@Entity
@Table(name = "T5_PENGAMBILAN_DOKUMEN")
@NamedQueries({
		@NamedQuery(name = "PengambilanDok.getList", query = "SELECT x FROM PengambilanDokumen x WHERE x.isDelete = 0"),
		@NamedQuery(name = "PengambilanDok.getByPengadaan", query = "SELECT x FROM PengambilanDokumen x WHERE x.isDelete = 0 and x.pengadaan.id =:pengadaanId"),
		@NamedQuery(name = "PengambilanDok.getByPengadaanVendor", query = "SELECT x FROM PengambilanDokumen x WHERE x.isDelete = 0 and x.pengadaan.id =:pengadaanId and x.vendor.id=:vendorId") })

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_PENGAMBILAN_DOKUMEN", initialValue = 1, allocationSize = 1)
public class PengambilanDokumen {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PENGAMBILAN_DOKUMEN_ID")
	private Integer id;

	@Column(name = "TGL_PENGAMBILAN_DOKUMEN")
	private Date tglAmbil;

	@ManyToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")
	private Pengadaan pengadaan;

	@ManyToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;

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

	public Date getTglAmbil() {
		return tglAmbil;
	}

	public void setTglAmbil(Date tglAmbil) {
		this.tglAmbil = tglAmbil;
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
