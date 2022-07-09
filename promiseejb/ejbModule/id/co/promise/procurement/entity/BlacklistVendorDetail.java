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
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T3_BLACKLIST_DTL")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_BLACKLIST_DTL", initialValue = 1, allocationSize = 1)
public class BlacklistVendorDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BLACKLIST_DTL_ID")
	private Integer blacklistDtlId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BLACKLIST_DTL_CREATED")
	private Date blacklistDtlCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BLACKLIST_DTL_UPDATED")
	private Date blacklistDtlUpdated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BLACKLIST_DTL_DELETED")
	private Date blacklistDtlDeleted;

	@Column(name = "BLACKLIST_DTL_USERID")
	private Integer blacklistDtlUserId;

	@ColumnDefault( value = "0" )
	@Column(name = "BLACKLIST_DTL_ISDELETE")
	private Integer blacklistDtlIsDelete;

	@ManyToOne
	@JoinColumn(name = "BLACKLIST_V_ID", referencedColumnName = "BLACKLIST_V_ID")  
	private BlacklistVendor blacklistVendor; 

	@ManyToOne
	@JoinColumn(name = "ALASAN_BLACKLIST_ID", referencedColumnName = "ALASAN_BLACKLIST_ID")  
	private AlasanBlacklist alasanBlacklist;

	@Lob 
	@Column(name = "BLACKLIST_VENDOR_KETERANGAN")
	private String blacklistDtlKeterangan;	
	
	@Column(name = "BLACKLIST_VENDOR_JML_WAKTU")
	private Integer blacklistDtlJmlWaktu;
	
	/**
	 * D = Days
	 * M = Months
	 * Y = Years
	 */
	
	@Column(name = "BLACKLIST_VENDOR_JK_WAKTU")
	private String blacklistDtlJkWaktu;
	
	@Transient
	private Integer selected;

	public Integer getBlacklistDtlId() {
		return blacklistDtlId;
	}

	public void setBlacklistDtlId(Integer blacklistDtlId) {
		this.blacklistDtlId = blacklistDtlId;
	}

	public Date getBlacklistDtlCreated() {
		return blacklistDtlCreated;
	}

	public void setBlacklistDtlCreated(Date blacklistDtlCreated) {
		this.blacklistDtlCreated = blacklistDtlCreated;
	}

	public Date getBlacklistDtlUpdated() {
		return blacklistDtlUpdated;
	}

	public void setBlacklistDtlUpdated(Date blacklistDtlUpdated) {
		this.blacklistDtlUpdated = blacklistDtlUpdated;
	}

	public Date getBlacklistDtlDeleted() {
		return blacklistDtlDeleted;
	}

	public void setBlacklistDtlDeleted(Date blacklistDtlDeleted) {
		this.blacklistDtlDeleted = blacklistDtlDeleted;
	}

	public Integer getBlacklistDtlUserId() {
		return blacklistDtlUserId;
	}

	public void setBlacklistDtlUserId(Integer blacklistDtlUserId) {
		this.blacklistDtlUserId = blacklistDtlUserId;
	}

	public Integer getBlacklistDtlIsDelete() {
		return blacklistDtlIsDelete;
	}

	public void setBlacklistDtlIsDelete(Integer blacklistDtlIsDelete) {
		this.blacklistDtlIsDelete = blacklistDtlIsDelete;
	}

	public BlacklistVendor getBlacklistVendor() {
		return blacklistVendor;
	}

	public void setBlacklistVendor(BlacklistVendor blacklistVendor) {
		this.blacklistVendor = blacklistVendor;
	}

	public AlasanBlacklist getAlasanBlacklist() {
		return alasanBlacklist;
	}

	public void setAlasanBlacklist(AlasanBlacklist alasanBlacklist) {
		this.alasanBlacklist = alasanBlacklist;
	}

	public String getBlacklistDtlKeterangan() {
		return blacklistDtlKeterangan;
	}

	public void setBlacklistDtlKeterangan(String blacklistDtlKeterangan) {
		this.blacklistDtlKeterangan = blacklistDtlKeterangan;
	}

	public Integer getBlacklistDtlJmlWaktu() {
		return blacklistDtlJmlWaktu;
	}

	public void setBlacklistDtlJmlWaktu(Integer blacklistDtlJmlWaktu) {
		this.blacklistDtlJmlWaktu = blacklistDtlJmlWaktu;
	}

	public String getBlacklistDtlJkWaktu() {
		return blacklistDtlJkWaktu;
	}

	public void setBlacklistDtlJkWaktu(String blacklistDtlJkWaktu) {
		this.blacklistDtlJkWaktu = blacklistDtlJkWaktu;
	}

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}
	
	
}