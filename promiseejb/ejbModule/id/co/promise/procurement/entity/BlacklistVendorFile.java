package id.co.promise.procurement.entity;

import java.util.Date;

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

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T3_BLACKLIST_FILE")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_BLACKLIST_FILE", initialValue = 1, allocationSize = 1)
public class BlacklistVendorFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BLACKLIST_FILE_ID")
	private Integer blacklistFileId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BLACKLIST_FILE_CREATED")
	private Date blacklistFileCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BLACKLIST_FILE_UPDATED")
	private Date blacklistFileUpdated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BLACKLIST_FILE_DELETED")
	private Date blacklistFileDeleted;

	@Column(name = "BLACKLIST_FILE_USERID")
	private Integer blacklistFileUserId;

	@ColumnDefault( value = "0" )
	@Column(name = "BLACKLIST_FILE_ISDELETE")
	private Integer blacklistFileIsDelete;

	@ManyToOne
	@JoinColumn(name = "BLACKLIST_V_ID", referencedColumnName = "BLACKLIST_V_ID")  
	private BlacklistVendor blacklistVendor;   
	
	@Column(name = "BLACKLIST_FILE")
	private String blacklistFile;

	public Integer getBlacklistFileId() {
		return blacklistFileId;
	}

	public void setBlacklistFileId(Integer blacklistFileId) {
		this.blacklistFileId = blacklistFileId;
	}

	public Date getBlacklistFileCreated() {
		return blacklistFileCreated;
	}

	public void setBlacklistFileCreated(Date blacklistFileCreated) {
		this.blacklistFileCreated = blacklistFileCreated;
	}

	public Date getBlacklistFileUpdated() {
		return blacklistFileUpdated;
	}

	public void setBlacklistFileUpdated(Date blacklistFileUpdated) {
		this.blacklistFileUpdated = blacklistFileUpdated;
	}

	public Date getBlacklistFileDeleted() {
		return blacklistFileDeleted;
	}

	public void setBlacklistFileDeleted(Date blacklistFileDeleted) {
		this.blacklistFileDeleted = blacklistFileDeleted;
	}

	public Integer getBlacklistFileUserId() {
		return blacklistFileUserId;
	}

	public void setBlacklistFileUserId(Integer blacklistFileUserId) {
		this.blacklistFileUserId = blacklistFileUserId;
	}

	public Integer getBlacklistFileIsDelete() {
		return blacklistFileIsDelete;
	}

	public void setBlacklistFileIsDelete(Integer blacklistFileIsDelete) {
		this.blacklistFileIsDelete = blacklistFileIsDelete;
	}

	public BlacklistVendor getBlacklistVendor() {
		return blacklistVendor;
	}

	public void setBlacklistVendor(BlacklistVendor blacklistVendor) {
		this.blacklistVendor = blacklistVendor;
	}

	public String getBlacklistFile() {
		return blacklistFile;
	}

	public void setBlacklistFile(String blacklistFile) {
		this.blacklistFile = blacklistFile;
	}

	
	
	
}