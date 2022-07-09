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

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T2_AFCO")
@NamedQueries({
	@NamedQuery(name="Afco.find", query="SELECT afco FROM Afco afco WHERE afco = afco and afco.isDelete=0"),
    @NamedQuery(name="Afco.findByName", query="SELECT afco FROM Afco afco WHERE afco.name LIKE :name"),
    @NamedQuery(name="Afco.findByUserId", query="SELECT afco FROM Afco afco WHERE afco.userId = :userId"),    
    @NamedQuery(name="Afco.findByOrganisasiId", query="SELECT afco FROM Afco afco WHERE afco.organisasi.id = :organisasiId and afco.isDelete=0"), 
    @NamedQuery(name="Afco.findListByOrganisasiId", query="SELECT afco FROM Afco afco WHERE  afco.isDelete=0 AND afco.organisasi.id IN :organisasiIdList"), 
    @NamedQuery(name="Afco.findByOrganisasiParentId", query="SELECT afco FROM Afco afco WHERE afco.organisasi.parentId = :organisasiId"),    
    @NamedQuery(name="Afco.findByCompanyName", query="SELECT afco FROM Afco afco WHERE afco.companyName like :companyName and afco.isDelete=0")
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", valueColumnName = "TABLE_SEQ_VALUE", 
pkColumnValue = "T2_AFCO", initialValue = 1, allocationSize = 1)
public class Afco {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AFCO_ID")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName = "ORGANISASI_ID")
	private Organisasi organisasi;
	
	@Column(name="NAME")
	private String name;

	@Column(name="COMPANY_NAME")
	private String companyName;
	
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
	
	@Column(name="SINGKATAN")
	private String singkatan;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getSingkatan() {
		return singkatan;
	}

	public void setSingkatan(String singkatan) {
		this.singkatan = singkatan;
	}
	
}

