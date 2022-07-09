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
@Table(name = "T1_ROLE")
@NamedQueries({ 
	@NamedQuery(name = "Role.find", query = "SELECT r FROM Role r WHERE r.isDelete = 0"),
	@NamedQuery(name = "Role.findNonVendor", query = "SELECT role FROM Role role WHERE role.isDelete = 0 AND NOT role.id IN(2,10)"),
	@NamedQuery(name="Role.findByAppCode", query="SELECT role FROM Role role WHERE role.isDelete = 0 AND role.appCode = :appCode"),
	@NamedQuery(name = "Role.findByAppCodePm", query = "SELECT role FROM Role role WHERE role.isDelete = 0 and role.isSso = 1 AND role.appCode = 'PM'"),
	@NamedQuery(name = "Role.findByAppCodeCm", query = "SELECT role FROM Role role WHERE role.isDelete = 0 and role.isSso = 1 AND role.appCode = 'CM'"),
	@NamedQuery(name="Role.findByKode", query="SELECT role FROM Role role WHERE role.isDelete = 0 AND role.code = :code")				
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_ROLE", initialValue = 1, allocationSize = 1)
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROLE_ID")
	private Integer id;

	@Column(name = "PARENT_ID")
	private Integer parentId;

	@Column(name = "NAMA")
	private String nama;

	@Column(name = "KODE")
	private String code;

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

	@Column(name = "APP_CODE ", length = 2)
	private String appCode;
	
	@ManyToOne
	@JoinColumn(name ="DASHBOARD_ID", referencedColumnName = "DASHBOARD_ID")
	private Dashboard dashboard;
	
	@Column(name = "IS_SSO")
	private Integer isSso;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
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

	public Dashboard getDashboard() {
		return dashboard;
	}

	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
	}

	public Integer getIsSso() {
		return isSso;
	}

	public void setIsSso(Integer isSso) {
		this.isSso = isSso;
	}
}
