package id.co.promise.procurement.entity;

import java.util.Date;
import java.util.List;

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
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

@Entity
@NamedQueries({
		@NamedQuery(name = "BidangUsaha.find", 
				query = "SELECT bidangUsaha FROM BidangUsaha bidangUsaha WHERE bidangUsaha.isDelete = 0"),
		@NamedQuery(name = "BidangUsaha.findByParentId", 
				query = "SELECT bidangUsaha FROM BidangUsaha bidangUsaha WHERE bidangUsaha.isDelete = 0 AND bidangUsaha.parentId = :parentId"),
		@NamedQuery(name = "BidangUsaha.findByNama", 
				query = "SELECT bidangUsaha FROM BidangUsaha bidangUsaha WHERE bidangUsaha.isDelete = 0 AND bidangUsaha.nama = :nama") 
})
@Table(name = "T1_BIDANG_USAHA")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_BIDANG_USAHA", initialValue = 1, allocationSize = 1)
public class BidangUsaha {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BIDANG_USAHA_ID")
	private Integer id;

	@Column(name = "PARENT_ID")
	private Integer parentId;

	@Column(name = "NAMA", length = 100)
	private String nama;

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

	@Transient
	private List<BidangUsaha> bidangUsahaChildList;

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

	public List<BidangUsaha> getBidangUsahaChildList() {
		return bidangUsahaChildList;
	}

	public void setBidangUsahaChildList(List<BidangUsaha> bidangUsahaChildList) {
		this.bidangUsahaChildList = bidangUsahaChildList;
	}
}
