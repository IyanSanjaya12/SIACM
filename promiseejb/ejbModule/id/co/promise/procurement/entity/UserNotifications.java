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
@Table(name = "T2_USER_NOTIFICATION")
@NamedQueries({
	@NamedQuery(name = "UserNotifications.find",
			query = "SELECT x FROM UserNotifications x WHERE x.isDelete = 0")
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_USER_NOTIFICATION", initialValue = 1, allocationSize = 1)
public class UserNotifications {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_NOTIFICATION_ID")
	private Integer id;	
	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	User user;
	@Column(name="MESSAGE", length=100)
	String message;
	@Column(name="LINK")
	String link;
	@Column(name="IS_READ")
	Boolean isRead;
	
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Boolean isRead() {
		return isRead;
	}
	public void setRead(Boolean isRead) {
		this.isRead = isRead;
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
