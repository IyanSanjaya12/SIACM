package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T1_AKSI")
@NamedQueries({
		@NamedQuery(name = "Aksi.find", query = "SELECT a FROM Aksi a WHERE a.isDelete = 0"),
		@NamedQuery(name = "Aksi.findPublicList", query = "SELECT a FROM Aksi a WHERE a.isDelete = 0 AND a.isPublic = 1"),
		@NamedQuery(name = "Aksi.findListByPath", query = "SELECT a FROM Aksi a WHERE a.isDelete = 0 AND a.path =:path"),
		@NamedQuery(name = "Aksi.findListByRole", query = "SELECT distinct a FROM Aksi a, RoleMenu rm, MenuAksi ma WHERE a.isDelete = 0 AND rm.isDelete=0 AND ma.isDelete=0 AND a.id = ma.aksi.id AND ma.menu.id=rm.menu.id  AND rm.role.id=:roleId ") })

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_AKSI", initialValue = 1, allocationSize = 1)
public class Aksi {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AKSI_ID")
	private Integer id;
	
	@Column(name = "PATH", length = 250)
	private String path;

	@Column(name = "ISPUBLIC")
	private Boolean isPublic;
	
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
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
