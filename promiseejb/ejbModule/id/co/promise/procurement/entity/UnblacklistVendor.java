package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T2_UNBLACKLIST_V")
@NamedQueries({
	@NamedQuery(name="UnblacklistVendor.deleteByVendorId", query="UPDATE UnblacklistVendor unblacklistVendor set unblacklistVendor.unblacklistVendorIsDelete = 1 WHERE unblacklistVendor.vendor.id =:vendorId")
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_UNBLACKLIST_V", initialValue = 1, allocationSize = 1)
public class UnblacklistVendor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UNBLACKLIST_V_ID")
	private Integer unblacklistVendorId;

	@Lob
	@Column(name = "VENDOR_KETERANGAN")
	private String unblacklistVendorKeterangan;

	@Lob
	@Column(name = "VENDOR_KETERANGAN_VND")
	private String unblacklistVendorKeteranganVnd; 

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VENDOR_CREATED")
	private Date unblacklistVendorCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VENDOR_UPDATED")
	private Date unblacklistVendorUpdated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VENDOR_DELETED")
	private Date unblacklistVendorDeleted;

	@Column(name = "VENDOR_USERID")
	private Integer unblacklistVendorUserId;

	@ColumnDefault( value = "0" )
	@Column(name = "VENDOR_ISDELETE")
	private Integer unblacklistVendorIsDelete;

	@ManyToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")  
	private Vendor vendor;

	@Column(name = "VENDOR_TGL_AWAL")
	private Date unblacklistVendorTglAwal;

	/**
	 * 0 = baru dibuat/butuh approval
	 * 1 = disetujui
	 * 2 = ditolak
	 */
	
	@Column(name = "VENDOR_STATUS")
	private Integer unblacklistVendorStatus; 

	@ManyToOne
	@JoinColumn(name = "BLACKLIST_V_ID", referencedColumnName = "BLACKLIST_V_ID")  
	private BlacklistVendor blacklistVendor;

	public Integer getUnblacklistVendorId() {
		return unblacklistVendorId;
	}

	public void setUnblacklistVendorId(Integer unblacklistVendorId) {
		this.unblacklistVendorId = unblacklistVendorId;
	}

	public String getUnblacklistVendorKeterangan() {
		return unblacklistVendorKeterangan;
	}

	public void setUnblacklistVendorKeterangan(String unblacklistVendorKeterangan) {
		this.unblacklistVendorKeterangan = unblacklistVendorKeterangan;
	}

	public String getUnblacklistVendorKeteranganVnd() {
		return unblacklistVendorKeteranganVnd;
	}

	public void setUnblacklistVendorKeteranganVnd(String unblacklistVendorKeteranganVnd) {
		this.unblacklistVendorKeteranganVnd = unblacklistVendorKeteranganVnd;
	}

	public Date getUnblacklistVendorCreated() {
		return unblacklistVendorCreated;
	}

	public void setUnblacklistVendorCreated(Date unblacklistVendorCreated) {
		this.unblacklistVendorCreated = unblacklistVendorCreated;
	}

	public Date getUnblacklistVendorUpdated() {
		return unblacklistVendorUpdated;
	}

	public void setUnblacklistVendorUpdated(Date unblacklistVendorUpdated) {
		this.unblacklistVendorUpdated = unblacklistVendorUpdated;
	}

	public Date getUnblacklistVendorDeleted() {
		return unblacklistVendorDeleted;
	}

	public void setUnblacklistVendorDeleted(Date unblacklistVendorDeleted) {
		this.unblacklistVendorDeleted = unblacklistVendorDeleted;
	}

	public Integer getUnblacklistVendorUserId() {
		return unblacklistVendorUserId;
	}

	public void setUnblacklistVendorUserId(Integer unblacklistVendorUserId) {
		this.unblacklistVendorUserId = unblacklistVendorUserId;
	}

	public Integer getUnblacklistVendorIsDelete() {
		return unblacklistVendorIsDelete;
	}

	public void setUnblacklistVendorIsDelete(Integer unblacklistVendorIsDelete) {
		this.unblacklistVendorIsDelete = unblacklistVendorIsDelete;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Date getUnblacklistVendorTglAwal() {
		return unblacklistVendorTglAwal;
	}

	public void setUnblacklistVendorTglAwal(Date unblacklistVendorTglAwal) {
		this.unblacklistVendorTglAwal = unblacklistVendorTglAwal;
	}

	public Integer getUnblacklistVendorStatus() {
		return unblacklistVendorStatus;
	}

	public void setUnblacklistVendorStatus(Integer unblacklistVendorStatus) {
		this.unblacklistVendorStatus = unblacklistVendorStatus;
	}

	public BlacklistVendor getBlacklistVendor() {
		return blacklistVendor;
	}

	public void setBlacklistVendor(BlacklistVendor blacklistVendor) {
		this.blacklistVendor = blacklistVendor;
	} 
	
	
}
