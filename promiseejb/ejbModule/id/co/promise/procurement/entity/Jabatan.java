package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "T1_JABATAN")
@NamedQueries({
		@NamedQuery(name = "Jabatan.find", query = "SELECT jabatan FROM Jabatan jabatan WHERE jabatan.isDelete = 0"),
		@NamedQuery(name = "Jabatan.getByOrganisasiIdList", query = "SELECT jabatan FROM Jabatan Jabatan WHERE jabatan.isDelete = 0 and jabatan.id in "
				+ " (select roleUser.user.jabatan.id from RoleUser roleUser where roleUser.isDelete = 0 and roleUser.organisasi IN (:organisasiList) ) "),
		@NamedQuery(name = "Jabatan.findNama", query = "select jabatan FROM Jabatan jabatan where jabatan.isDelete=0 and jabatan.nama = :nama"),
		@NamedQuery(name = "Jabatan.getJabatanAndOrganisasiByOrganisasiIdList", query = "SELECT Count(roleUser.user.jabatan) , roleUser.user.jabatan.nama,roleUser.user.jabatan.id ,roleUser.organisasi.nama,roleUser.organisasi.id "
				+ " FROM RoleUser roleUser WHERE roleUser.user.jabatan.isDelete = 0 and roleUser.role.appCode = :appCode and roleUser.organisasi in (:organisasiList) "
				+ " group by roleUser.user.jabatan.nama,roleUser.user.jabatan.id ,roleUser.organisasi.nama,roleUser.organisasi.id , roleUser.user.jabatan"),
		@NamedQuery(name = "Jabatan.getJabatanAndOrganisasiByOrganisasiIdListAndAdditional", query = "SELECT Count(roleUser.user.jabatan) , roleUser.user.jabatan.nama, roleUser.user.jabatan.id, roleUser.organisasi.nama, roleUser.organisasi.id "
				+ " FROM RoleUser roleUser WHERE roleUser.user.jabatan.isDelete = 0 and roleUser.role.appCode = :appCode and roleUser.organisasi in (:organisasiList) "
				+ "OR roleUser.user.jabatan IN (SELECT appOrgJabatan.jabatan FROM ApprovalOrgJabatan appOrgJabatan WHERE appOrgJabatan.organisasi = :organisasi OR appOrgJabatan.isAllApproval = 1) "
				+ " group by roleUser.user.jabatan.nama, roleUser.user.jabatan.id, roleUser.organisasi.nama, roleUser.organisasi.id, roleUser.user.jabatan"),
		@NamedQuery(name = "Jabatan.getAdditionalApproval", query = "SELECT Count(roleUser.user.jabatan) , roleUser.user.jabatan.nama, roleUser.user.jabatan.id, roleUser.organisasi.nama, roleUser.organisasi.id "
				+ " FROM RoleUser roleUser WHERE roleUser.user.jabatan.isDelete = 0 "
				+ "AND roleUser.user.jabatan IN (SELECT appOrgJabatan.jabatan FROM ApprovalOrgJabatan appOrgJabatan WHERE appOrgJabatan.organisasi = :organisasi OR appOrgJabatan.isAllApproval = 1) "
				+ " group by roleUser.user.jabatan.nama, roleUser.user.jabatan.id, roleUser.organisasi.nama, roleUser.organisasi.id, roleUser.user.jabatan"),
		@NamedQuery(name = "Jabatan.getJabatanAndOrganisasiByOrganisasiIdList2", query = "SELECT roleUser.user.jabatan ,roleUser.organisasi  "
						+ " FROM RoleUser roleUser WHERE roleUser.user.jabatan.isDelete = 0 and roleUser.organisasi in (:organisasiList) order by roleUser.organisasi.id "),
		@NamedQuery(name = "Jabatan.getNotRegisteredList", query = "select jabatan FROM Jabatan jabatan where jabatan.isDelete=0 and jabatan.organisasi.id =:organisasiId "
				+ "and jabatan.id not in (select roleJabatan.jabatan.id from RoleJabatan roleJabatan where roleJabatan.isDelete = 0)"),
		@NamedQuery(name = "Jabatan.getJabatanByJabatanIdEoffice", query = "select jabatan FROM Jabatan jabatan where jabatan.isDelete=0 and jabatan.jabatanIdEoffice = :jabatanIdEoffice"),
		@NamedQuery(name = "Jabatan.getJabatanListByOrganisasi", query = "SELECT jabatan FROM Jabatan jabatan "
				+ "WHERE jabatan.isDelete = 0 "
				+ "AND jabatan.organisasi.id =:organisasiId ")
		
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_JABATAN", initialValue = 1, allocationSize = 1)
public class Jabatan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "JABATAN_ID")
	private Integer id;
	
	@Column(name = "NAMA", length=250)
	private String nama;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED")
	private Date updated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED")
	private Date deleted;
	
	@Column(name="USERID")
	private Integer userId;
	
	@Column(name="ISDELETE")
	private Integer isDelete;
	
	@Column(name="JABATAN_ID_EOFFICE")
	private Integer jabatanIdEoffice;
	
	@OneToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName = "ORGANISASI_ID")
	private Organisasi organisasi;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getJabatanIdEoffice() {
		return jabatanIdEoffice;
	}

	public void setJabatanIdEoffice(Integer jabatanIdEoffice) {
		this.jabatanIdEoffice = jabatanIdEoffice;
	}

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
	}
	
}
