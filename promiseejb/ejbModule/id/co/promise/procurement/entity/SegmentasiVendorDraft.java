package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T3_SEGMENTASI_V_DRAFT")
@NamedQueries({
	@NamedQuery(name="SegmentasiVendorDraft.findByVendorAndSegmentasiVendor",
        	query="SELECT segmentasiVendorDraft FROM SegmentasiVendorDraft segmentasiVendorDraft WHERE segmentasiVendorDraft.vendor.id =:vendorId "
        			+ "and segmentasiVendorDraft.segmentasiVendor.id=:segmentasiVendorId and segmentasivendorDraft.isDelete = 0"),
    @NamedQuery(name="SegmentasiVendorDraft.findVendorBySubBidangUsaha",
        	query="SELECT segmentasiVendorDraft FROM SegmentasiVendorDraft segmentasiVendorDraft WHERE segmentasiVendorDraft.subBidangUsaha.id =:subBidangUsahaId"),
    @NamedQuery(name="SegmentasiVendorDraft.findByVendor",
        	query="SELECT segmentasiVendorDraft FROM SegmentasiVendorDraft segmentasiVendorDraft WHERE segmentasiVendorDraft.vendor.id =:vendorId and segmentasiVendorDraft.isDelete = 0"),
    @NamedQuery(name="SegmentasiVendorDraft.findBySegmentasiVendorId",
    		query="SELECT segmentasiVendorDraft FROM SegmentasiVendorDraft segmentasiVendorDraft WHERE segmentasiVendorDraft.segmentasiVendor IS NULL "),
    @NamedQuery(name="SegmentasiVendorDraft.deleteByVendorId",
         	query="UPDATE SegmentasiVendorDraft segmentasiVendorDraft set segmentasiVendorDraft.isDelete = 1 WHERE segmentasiVendorDraft.vendor.id =:vendorId ")
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_SEGMENTASI_V_DRAFT", initialValue = 1, allocationSize = 1)
public class SegmentasiVendorDraft {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEGMENTASI_VENDOR_DRAFT_ID")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;

	@OneToOne
	@JoinColumn(name = "SUB_BIDANG_USAHA_ID", referencedColumnName = "SUB_BIDANG_USAHA_ID")
	private SubBidangUsaha subBidangUsaha;
	
	@OneToOne
	@JoinColumn(name = "SEGMENTASI_VENDOR_ID", referencedColumnName = "SEGMENTASI_VENDOR_ID")
	private SegmentasiVendor segmentasiVendor;
	
	@Column(name = "ASOSIASI")
	private String asosiasi;
	
	@Column(name = "NOMOR")
	private String nomor;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TANGGAL_MULAI")
	private Date tanggalMulai;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TANGGAL_BERAKHIR")
	private Date tanggalBerakhir;

	@OneToOne
	@JoinColumn(name = "JABATAN_ID", referencedColumnName = "JABATAN_ID")
	private Jabatan jabatan;
	
	@Column(name = "EMAIL")
	private String email;
	
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
	
	@Transient
	private Integer totalRow;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public SubBidangUsaha getSubBidangUsaha() {
		return subBidangUsaha;
	}

	public void setSubBidangUsaha(SubBidangUsaha subBidangUsaha) {
		this.subBidangUsaha = subBidangUsaha;
	}

	public String getAsosiasi() {
		return asosiasi;
	}

	public void setAsosiasi(String asosiasi) {
		this.asosiasi = asosiasi;
	}

	public String getNomor() {
		return nomor;
	}

	public void setNomor(String nomor) {
		this.nomor = nomor;
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

	public Jabatan getJabatan() {
		return jabatan;
	}

	public void setJabatan(Jabatan jabatan) {
		this.jabatan = jabatan;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Integer getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(Integer totalRow) {
		this.totalRow = totalRow;
	}

	public SegmentasiVendor getSegmentasiVendor() {
		return segmentasiVendor;
	}

	public void setSegmentasiVendor(SegmentasiVendor segmentasiVendor) {
		this.segmentasiVendor = segmentasiVendor;
	}
	
	
	
}
