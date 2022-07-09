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
	@NamedQuery(name = "KualifikasiVendor.find",
			query="SELECT kualifikasiVendor FROM KualifikasiVendor kualifikasiVendor WHERE kualifikasiVendor.isDelete = 0"),
	@NamedQuery(name = "KualifikasiVendor.findNama",
			query="select kualifikasiVendor from KualifikasiVendor kualifikasiVendor where kualifikasiVendor.isDelete=0 and kualifikasiVendor.nama = :nama")
})
@Table(name="T1_KUALIFIKASI_VENDOR")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_KUALIFIKASI_VENDOR", initialValue = 1, allocationSize = 1)
public class KualifikasiVendor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="KUALIFIKASI_VENDOR_ID")
	private Integer id;
	
	@Column(name="NAMA", length=100)
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
