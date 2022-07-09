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
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T2_BLACKLIST_V")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_BLACKLIST_V", initialValue = 1, allocationSize = 1)
public class BlacklistVendor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BLACKLIST_V_ID")
	private Integer blacklistVendorId;

	@Lob
	@Column(name = "VENDOR_KETERANGAN")
	private String blacklistVendorKeterangan;

	@Lob
	@Column(name = "VENDOR_KETERANGAN_VND")
	private String blacklistVendorKeteranganVnd;
	
	@Column(name = "VENDOR_JML_WAKTU")
	private Integer blacklistVendorJmlWaktu;

	/**
	 * D = Days M = Months Y = Years
	 */

	@Column(name = "VENDOR_JK_WAKTU")
	private String blacklistVendorJkWaktu;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VENDOR_CREATED")
	private Date blacklistVendorCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VENDOR_UPDATED")
	private Date blacklistVendorUpdated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VENDOR_DELETED")
	private Date blacklistVendorDeleted;

	@Column(name = "VENDOR_USERID")
	private Integer blacklistVendorUserId;

	@ColumnDefault( value = "0" )
	@Column(name = "VENDOR_ISDELETE")
	private Integer blacklistVendorIsDelete;

	@ManyToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")  
	private Vendor vendor;

	@Column(name = "VENDOR_TGL_AWAL")
	private Date blacklistVendorTglAwal;

	@Column(name = "VENDOR_TGL_AKHIR")
	private Date blacklistVendorTglAkhir;

	/**
	 * 0 = baru dibuat/butuh approval
	 * 1 = disetujui
	 * 2 = ditolak
	 * 3 = sudah berakhir
	 * 4 = unblacklist manual
	 */
	
	@Column(name = "VENDOR_STATUS")
	private Integer blacklistVendorStatus;
	
	@Column(name = "VENDOR_FILE")
	private String blacklistVendorFile;

	public Integer getBlacklistVendorId() {
		return blacklistVendorId;
	}

	public void setBlacklistVendorId(Integer blacklistVendorId) {
		this.blacklistVendorId = blacklistVendorId;
	}

	public String getBlacklistVendorKeterangan() {
		return blacklistVendorKeterangan;
	}

	public void setBlacklistVendorKeterangan(String blacklistVendorKeterangan) {
		this.blacklistVendorKeterangan = blacklistVendorKeterangan;
	}

	public String getBlacklistVendorKeteranganVnd() {
		return blacklistVendorKeteranganVnd;
	}

	public void setBlacklistVendorKeteranganVnd(String blacklistVendorKeteranganVnd) {
		this.blacklistVendorKeteranganVnd = blacklistVendorKeteranganVnd;
	}

	public Integer getBlacklistVendorJmlWaktu() {
		return blacklistVendorJmlWaktu;
	}

	public void setBlacklistVendorJmlWaktu(Integer blacklistVendorJmlWaktu) {
		this.blacklistVendorJmlWaktu = blacklistVendorJmlWaktu;
	}

	public String getBlacklistVendorJkWaktu() {
		return blacklistVendorJkWaktu;
	}

	public void setBlacklistVendorJkWaktu(String blacklistVendorJkWaktu) {
		this.blacklistVendorJkWaktu = blacklistVendorJkWaktu;
	}

	public Date getBlacklistVendorCreated() {
		return blacklistVendorCreated;
	}

	public void setBlacklistVendorCreated(Date blacklistVendorCreated) {
		this.blacklistVendorCreated = blacklistVendorCreated;
	}

	public Date getBlacklistVendorUpdated() {
		return blacklistVendorUpdated;
	}

	public void setBlacklistVendorUpdated(Date blacklistVendorUpdated) {
		this.blacklistVendorUpdated = blacklistVendorUpdated;
	}

	public Date getBlacklistVendorDeleted() {
		return blacklistVendorDeleted;
	}

	public void setBlacklistVendorDeleted(Date blacklistVendorDeleted) {
		this.blacklistVendorDeleted = blacklistVendorDeleted;
	}

	public Integer getBlacklistVendorUserId() {
		return blacklistVendorUserId;
	}

	public void setBlacklistVendorUserId(Integer blacklistVendorUserId) {
		this.blacklistVendorUserId = blacklistVendorUserId;
	}

	public Integer getBlacklistVendorIsDelete() {
		return blacklistVendorIsDelete;
	}

	public void setBlacklistVendorIsDelete(Integer blacklistVendorIsDelete) {
		this.blacklistVendorIsDelete = blacklistVendorIsDelete;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Integer getBlacklistVendorStatus() {
		return blacklistVendorStatus;
	}

	public void setBlacklistVendorStatus(Integer blacklistVendorStatus) {
		this.blacklistVendorStatus = blacklistVendorStatus;
	}

	public String getBlacklistVendorFile() {
		return blacklistVendorFile;
	}

	public void setBlacklistVendorFile(String blacklistVendorFile) {
		this.blacklistVendorFile = blacklistVendorFile;
	}

	public Date getBlacklistVendorTglAwal() {
		return blacklistVendorTglAwal;
	}

	public void setBlacklistVendorTglAwal(Date blacklistVendorTglAwal) {
		this.blacklistVendorTglAwal = blacklistVendorTglAwal;
	}

	public Date getBlacklistVendorTglAkhir() {
		return blacklistVendorTglAkhir;
	}

	public void setBlacklistVendorTglAkhir(Date blacklistVendorTglAkhir) {
		this.blacklistVendorTglAkhir = blacklistVendorTglAkhir;
	}
	
}
