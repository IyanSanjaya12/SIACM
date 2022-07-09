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
@Table(name = "T2_ROLE_USER")

@NamedQueries({
		@NamedQuery(name = "RoleUser.find", query = "SELECT roleUser FROM RoleUser roleUser WHERE roleUser.isDelete = 0"),
		@NamedQuery(name = "RoleUser.findByRoleId", query = "SELECT r FROM RoleUser r WHERE r.isDelete = 0 AND r.role.id = 1"), // 1:
																																// role
																																// panitia
		@NamedQuery(name = "RoleUser.findByRoleIdAndName", query = "SELECT r FROM RoleUser r WHERE r.isDelete = 0 AND r.role.id = :roleId AND r.user.namaPengguna LIKE :name"), // 1:
																																												// role
																																												// panitia
		@NamedQuery(name = "RoleUser.findByNameNotVendor", query = "SELECT r FROM RoleUser r WHERE r.isDelete = 0 AND r.role.id <> 2 AND r.user.namaPengguna LIKE :name"), // 1:
																																											// role
		@NamedQuery(name = "RoleUser.findByUserIdAndRoleAppCode", query = "SELECT ru FROM RoleUser ru WHERE ru.isDelete=0 AND ru.user.id = :userId AND ru.role.appCode=:roleAppCode"),
		// panitia
		@NamedQuery(name = "RoleUser.findByUserId", query = "SELECT ru FROM RoleUser ru WHERE ru.isDelete=0 AND ru.user.id =:userId"),
		@NamedQuery(name = "RoleUser.findRoleByOrganisasi", query = "SELECT ru.role FROM RoleUser ru WHERE ru.isDelete=0 AND ru.organisasi =:organisasi"),
		@NamedQuery(name = "RoleUser.findByToken", query = "SELECT ru FROM RoleUser ru, Token t WHERE ru.isDelete=0 AND ru.user.id = t.user.id and t.token=:token"),
		@NamedQuery(name = "RoleUser.findByUserNamePartial", query = "SELECT b FROM User a, RoleUser b, Organisasi c WHERE a.isDelete=0 AND a.namaPengguna LIKE :username "
				+ "AND b.user = a AND b.organisasi = c"),
		@NamedQuery(name = "RoleUser.deletebyUserId", query = "delete from RoleUser roleUser where roleUser.user.id =:userId"),
		@NamedQuery(name = "RoleUser.findByTokenAndAppCode", query = "SELECT ru FROM RoleUser ru, Token t, User u WHERE ru.isDelete=0 AND ru.user.id = u.id AND t.user.id=u.id and t.token=:token and ru.role.appCode=:appCode"),
		@NamedQuery(name = "RoleUser.findByOrganisasiAndRoleAppCode", query = "SELECT ru FROM RoleUser ru WHERE ru.isDelete=0 AND ru.organisasi.id = :orgId AND ru.role.appCode=:roleAppCode"),
		@NamedQuery(name = "RoleUser.findRoleUserByRoleAndOrganisasi", query = "SELECT ru FROM RoleUser ru WHERE ru.isDelete=0 AND ru.organisasi.id = :organisasiId AND ru.role.id=:roleId"), 
		@NamedQuery(name = "RoleUser.findRoleUserByOrganisasi", query = "SELECT ru FROM RoleUser ru WHERE ru.isDelete=0 AND ru.organisasi.id = :organisasiId"),
		@NamedQuery(name = "RoleUser.findByJabatanIdAndOrganisasiId",query="select roleUser from RoleUser roleUser where roleUser.organisasi.id =:organisasiId and roleUser.isDelete=0 and roleUser.user.jabatan.id =:jabatanId "),
		@NamedQuery(name = "RoleUser.findByUserIdAndOrganisasiId",query="select roleUser from RoleUser roleUser where roleUser.organisasi.id =:organisasiId and roleUser.isDelete=0 and roleUser.user.id =:userId "),
		@NamedQuery(name = "RoleUser.findByParentUserIdAndRoleId", query = "SELECT roleUser FROM RoleUser roleUser WHERE roleUser.isDelete = 0 "
				+ "AND roleUser.user.id =:parentUserId AND roleUser.role.id in(:roleIdList) ")
		
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_ROLE_USER", initialValue = 1, allocationSize = 1)
public class RoleUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROLE_USER_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	private User user;

	@ManyToOne
	@JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")
	private Role role;

	@ManyToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName = "ORGANISASI_ID")
	private Organisasi organisasi;

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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
	}

}
