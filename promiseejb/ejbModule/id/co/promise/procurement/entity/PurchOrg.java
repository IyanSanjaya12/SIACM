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
@Table(name = "T2_PURCH_ORG")
@NamedQueries({
	@NamedQuery(name="PurchOrg.getByOrganisasiId", query="select purchOrg from PurchOrg purchOrg where purchOrg.isDelete=0 and purchOrg.organisasi.id = :organisasiId"),
	@NamedQuery(name="PurchOrg.getAllCode", query="select purchOrg from PurchOrg purchOrg where purchOrg.isDelete=0 GROUP BY purchOrg.code"),
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_PURCH_ORG", initialValue = 1, allocationSize = 1)

public class PurchOrg {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PURCH_ORG_ID")
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED")
	private Date deleted;
	
	@Column(name = "USERID")
	private Integer userId;
	
	@Column(name = "ISDELETE", columnDefinition="tinyint(1) default 0")
	private Integer isDelete;
	
	@Column(name = "CODE", length = 100)
	private String code;
	
	@ManyToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName = "ORGANISASI_ID")
	private Organisasi organisasi;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
	}
	
}