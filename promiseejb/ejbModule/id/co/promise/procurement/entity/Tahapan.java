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

@Entity
@NamedQueries({
	@NamedQuery(name="Tahapan.getList", query="SELECT tahapan FROM Tahapan tahapan WHERE tahapan.isDelete = 0"),
	@NamedQuery(name="Tahapan.findByNama", query="SELECT tahapan FROM Tahapan tahapan WHERE tahapan.isDelete = 0 AND tahapan.nama = :nama"),
	@NamedQuery(name="Tahapan.getUnregisteredList", query="SELECT tahapan FROM Tahapan tahapan WHERE tahapan.isDelete = 0 AND tahapan.id "
			+ "not in (select approvalTahapan.tahapan.id from ApprovalTahapan approvalTahapan where approvalTahapan.isDelete = 0 and approvalTahapan.organisasi =:organisasi )")
})
@Table(name = "T1_TAHAPAN")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_TAHAPAN", initialValue = 1, allocationSize = 1)
public class Tahapan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TAHAPAN_ID")
	private Integer id;

	@Column(name = "NAMA")
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
	
	@Column(name="IS_THRESHOLD")
	private Integer isThreshold;

	public Integer getIsThreshold() {
		return isThreshold;
	}

	public void setIsThreshold(Integer isThreshold) {
		this.isThreshold = isThreshold;
	}

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
}
