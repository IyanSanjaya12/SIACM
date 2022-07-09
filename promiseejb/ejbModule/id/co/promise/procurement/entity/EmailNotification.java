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
@Table(name = "T2_EMAIL_NOTIFICATION")
@NamedQueries({
		/*penambahan modul KAI 22/12/2020 [21152]*/
		@NamedQuery(name = "EmailNotification.getList", query = "SELECT m FROM EmailNotification m WHERE m.isDelete = 0 ORDER BY m.id DESC"),
		@NamedQuery(name = "EmailNotification.findByStatusNew", query = "SELECT m FROM EmailNotification m WHERE m.isDelete = 0 AND m.statusEmailSending = 0 order by m.id DESC") })
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_EMAIL_NOTIFICATION", initialValue = 1, allocationSize = 1)
public class EmailNotification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMAILNOTIFY_ID")
	private Integer id;
	
	@Column(name = "EMAIL_TO")
	private String emailTo;
	
	@Column(name = "EMAIL_FORM")
	private String emailForm;
	
	@Column(name = "SUBJECT")
	private String subject;
	
	@Column(name = "MESSAGE", length=1000)
	private String message;
	
	@Column(name = "ATTACHMENT")
	private String attachment;
	
	@Column(name = "STATUS_EMAIL_SEND")
	private int statusEmailSending;
	
	@ManyToOne
	@JoinColumn(name = "MASTEREMAILNOTIFY_ID", referencedColumnName = "MASTEREMAILNOTIFY_ID")
	private MasterEmailNotification masterEmailNotification;
	
	@Column(name = "SENDING_DATE")
	private Date sendingDate;

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

	public Date getSendingDate() {
		return sendingDate;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public void setSendingDate(Date sendingDate) {
		this.sendingDate = sendingDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailForm() {
		return emailForm;
	}

	public void setEmailForm(String emailForm) {
		this.emailForm = emailForm;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public int getStatusEmailSending() {
		return statusEmailSending;
	}

	public void setStatusEmailSending(int statusEmailSending) {
		this.statusEmailSending = statusEmailSending;
	}

	public MasterEmailNotification getMasterEmailNotification() {
		return masterEmailNotification;
	}

	public void setMasterEmailNotification(
			MasterEmailNotification masterEmailNotification) {
		this.masterEmailNotification = masterEmailNotification;
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

}
