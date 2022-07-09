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
@Table(name = "T3_UNBLACKLIST_FILE")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_UNBLACKLIST_FILE", initialValue = 1, allocationSize = 1)
public class UnblacklistVendorFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UNBLACKLIST_FILE_ID")
	private Integer unblacklistFileId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UNBLACKLIST_FILE_CREATED")
	private Date unblacklistFileCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UNBLACKLIST_FILE_UPDATED")
	private Date unblacklistFileUpdated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UNBLACKLIST_FILE_DELETED")
	private Date unblacklistFileDeleted;

	@Column(name = "UNBLACKLIST_FILE_USERID")
	private Integer unblacklistFileUserId;

	@ColumnDefault( value = "0" )
	@Column(name = "UNBLACKLIST_FILE_ISDELETE")
	private Integer unblacklistFileIsDelete;

	@ManyToOne
	@JoinColumn(name = "UNBLACKLIST_V_ID", referencedColumnName = "UNBLACKLIST_V_ID")  
	private UnblacklistVendor unblacklistVendor;
	
	@Column(name = "UNBLACKLIST_FILE")
	private String unblacklistFile;

	public Integer getUnblacklistFileId() {
		return unblacklistFileId;
	}

	public void setUnblacklistFileId(Integer unblacklistFileId) {
		this.unblacklistFileId = unblacklistFileId;
	}

	public Date getUnblacklistFileCreated() {
		return unblacklistFileCreated;
	}

	public void setUnblacklistFileCreated(Date unblacklistFileCreated) {
		this.unblacklistFileCreated = unblacklistFileCreated;
	}

	public Date getUnblacklistFileUpdated() {
		return unblacklistFileUpdated;
	}

	public void setUnblacklistFileUpdated(Date unblacklistFileUpdated) {
		this.unblacklistFileUpdated = unblacklistFileUpdated;
	}

	public Date getUnblacklistFileDeleted() {
		return unblacklistFileDeleted;
	}

	public void setUnblacklistFileDeleted(Date unblacklistFileDeleted) {
		this.unblacklistFileDeleted = unblacklistFileDeleted;
	}

	public Integer getUnblacklistFileUserId() {
		return unblacklistFileUserId;
	}

	public void setUnblacklistFileUserId(Integer unblacklistFileUserId) {
		this.unblacklistFileUserId = unblacklistFileUserId;
	}

	public Integer getUnblacklistFileIsDelete() {
		return unblacklistFileIsDelete;
	}

	public void setUnblacklistFileIsDelete(Integer unblacklistFileIsDelete) {
		this.unblacklistFileIsDelete = unblacklistFileIsDelete;
	}

	public UnblacklistVendor getUnblacklistVendor() {
		return unblacklistVendor;
	}

	public void setUnblacklistVendor(UnblacklistVendor unblacklistVendor) {
		this.unblacklistVendor = unblacklistVendor;
	}

	public String getUnblacklistFile() {
		return unblacklistFile;
	}

	public void setUnblacklistFile(String unblacklistFile) {
		this.unblacklistFile = unblacklistFile;
	}    
	
}