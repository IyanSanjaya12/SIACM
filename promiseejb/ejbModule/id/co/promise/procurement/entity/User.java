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
@Table(name = "T1_USER")
@NamedQueries({
		@NamedQuery(name = "User.find", query = "SELECT x FROM User x WHERE x.isDelete = 0"),
		@NamedQuery(name = "User.findNonVendor", query = "SELECT user FROM User user WHERE user.isDelete = 0 AND user.id IN (SELECT roleUser.user.id FROM RoleUser roleUser WHERE roleUser.isDelete = 0 AND NOT (roleUser.role.id=2 OR roleUser.role.id=10))"),
		@NamedQuery(name = "User.findByEmail", query = "SELECT user FROM User user WHERE user.email = :email AND user.isDelete=0"),
		@NamedQuery(name = "User.getUserByUsername", query = "SELECT user FROM User user WHERE user.username =:username ORDER BY id DESC"),
		@NamedQuery(name = "User.getUserByJabatanId", query = "SELECT user FROM User user WHERE user.jabatan.id =:jabatanId ORDER BY id DESC"),
		@NamedQuery(name = "User.findByUsername",query="select user from User user where user.isDelete=0 and user.username =:username"),
		@NamedQuery(name = "User.findByTokenUserId", query = "SELECT user FROM User user WHERE user.isDelete=0 AND user.id =:userId "),
		@NamedQuery(name = "User.getUserListByOrganisasiIdList", query = "SELECT DISTINCT user.namaPengguna, user.id, organisasi.nama, organisasi.id "
				+ " FROM User user LEFT JOIN RoleUser roleUser ON roleUser.user.id = user.id LEFT JOIN Organisasi organisasi ON organisasi.id = roleUser.organisasi.id WHERE user.id IN (SELECT roleUser.user.id FROM RoleUser roleUser WHERE roleUser.organisasi IN (:organisasiList) ) ")
		})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_USER", initialValue = 1, allocationSize = 1)
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "JABATAN_ID", referencedColumnName = "JABATAN_ID")
	private Jabatan jabatan;

	@Column(name = "USERNAME", length = 50)
	private String username;

	@Column(name = "PASSWORD", length = 100)
	private String password;

	@Column(name = "NAMA_PENGGUNA")
	private String namaPengguna;
	
	@Column(name = "ID_USER_EOFFICE")
	private String idUserEOffice;

	@Column(name = "EMAIL", length = 100)
	private String email;

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

	@Column(name = "LOGIN_TIME")
	private Date loginTime;
	
	@Column(name = "TERKUNCI")
	private Integer terkunci; 
	
	@Column(name = "BLACKLIST")
	private Integer blacklist;
	
	@Column(name = "REJECT")
	private Integer reject; 
	
	@Column(name = "LAST_GENERATE_PASSWORD")
	private Date lastGeneratePassword; 
	
	@Column(name = "FLAG_LOGIN_EBS")
	private Integer flagLoginEbs; 
	
	@Column(name = "ID_PERUSAHAAN")
	private Integer idPerusahaan;

	@Column(name = "PARENT_USER_ID")
	private Integer parentUserId;
	
	@Column(name = "IS_PLH")
	private Integer isPlh;
	
	@Column(name = "KODE")
	private String kode;
	
	public String getIdUserEOffice() {
		return idUserEOffice;
	}

	public void setIdUserEOffice(String idUserEOffice) {
		this.idUserEOffice = idUserEOffice;
	}

	public Integer getTerkunci() {
		return terkunci;
	}

	public Date getLastGeneratePassword() {
		return lastGeneratePassword;
	}



	public void setLastGeneratePassword(Date lastGeneratePassword) {
		this.lastGeneratePassword = lastGeneratePassword;
	}



	public void setTerkunci(Integer terkunci) {
		this.terkunci = terkunci; 
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getNamaPengguna() {
		return namaPengguna;
	}

	public void setNamaPengguna(String namaPengguna) {
		this.namaPengguna = namaPengguna;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(Integer blacklist) {
		this.blacklist = blacklist;
	}

	public Integer getReject() {
		return reject;
	}

	public void setReject(Integer reject) {
		this.reject = reject;
	}

	public Jabatan getJabatan() {
		return jabatan;
	}

	public void setJabatan(Jabatan jabatan) {
		this.jabatan = jabatan;
	}

	public Integer getFlagLoginEbs() {
		return flagLoginEbs;
	}

	public void setFlagLoginEbs(Integer flagLoginEbs) {
		this.flagLoginEbs = flagLoginEbs;
	}

	public Integer getIdPerusahaan() {
		return idPerusahaan;
	}

	public void setIdPerusahaan(Integer idPerusahaan) {
		this.idPerusahaan = idPerusahaan;
	}

	public Integer getParentUserId() {
		return parentUserId;
	}

	public void setParentUserId(Integer parentUserId) {
		this.parentUserId = parentUserId;
	}

	public Integer getIsPlh() {
		return isPlh;
	}

	public void setIsPlh(Integer isPlh) {
		this.isPlh = isPlh;
	}

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}
	
}
