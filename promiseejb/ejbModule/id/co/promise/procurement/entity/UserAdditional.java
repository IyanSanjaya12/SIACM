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

@Entity
@Table(name = "T2_USER_ADDITIONAL")
@NamedQueries({
	@NamedQuery(name = "UserAdditional.findByNippPlh", query = "SELECT userAdditional FROM UserAdditional userAdditional WHERE userAdditional.nippPlh =:nippPlh AND userAdditional.isDelete = 0 "),
	@NamedQuery(name = "UserAdditional.findUserAdditionalMax", query = "SELECT userAdditional FROM UserAdditional userAdditional WHERE userAdditional.isDelete = 0 "
			+ "AND userAdditional.userAdditionalId IN(SELECT MAX(userAdditional.userAdditionalId) FROM UserAdditional userAdditional)")
	
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_USER_ADDITIONAL", initialValue = 1, allocationSize = 1)
public class UserAdditional {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ADDITIONAL_ID")
	private Integer userAdditionalId;

	@Column(name = "NIPP_PLH", length = 255)
	private String nippPlh;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	private User user;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="START_DATE")
	private Date startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="END_DATE")
	private Date endDate;
	
	@Column(name = "NOTA_DINAS", length = 255)
	private String notaDinas;
	
	@Column(name = "IS_ACTIVE")
	private Integer isActive;
	
	@ManyToOne
	@JoinColumn(name = "JABATAN_ID", referencedColumnName = "JABATAN_ID")
	private Jabatan jabatan;
	
	@ManyToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName = "ORGANISASI_ID")
	private Organisasi organisasi;
	
	@Column(name="ISDELETE")
	private Integer isDelete;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED")
	private Date updated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED")
	private Date deleted;
	
	public Integer getUserAdditionalId() {
		return userAdditionalId;
	}

	public void setUserAdditionalId(Integer userAdditionalId) {
		this.userAdditionalId = userAdditionalId;
	}

	public String getNippPlh() {
		return nippPlh;
	}

	public void setNippPlh(String nippPlh) {
		this.nippPlh = nippPlh;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getNotaDinas() {
		return notaDinas;
	}

	public void setNotaDinas(String notaDinas) {
		this.notaDinas = notaDinas;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Jabatan getJabatan() {
		return jabatan;
	}

	public void setJabatan(Jabatan jabatan) {
		this.jabatan = jabatan;
	}

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
	}
	
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
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

}
