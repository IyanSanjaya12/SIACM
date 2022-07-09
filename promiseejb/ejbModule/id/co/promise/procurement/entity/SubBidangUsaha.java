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
@Table(name = "T2_SUB_BIDANG_USAHA")
@NamedQueries({
	@NamedQuery(name="SubBidangUsaha.find",
			query="SELECT subBidangUsaha FROM SubBidangUsaha subBidangUsaha WHERE subBidangUsaha.isDelete = 0"),
	@NamedQuery(name="SubBidangUsaha.findByBidangUsahaId",
    		query="SELECT subBidangUsaha FROM SubBidangUsaha subBidangUsaha WHERE subBidangUsaha.isDelete = 0 AND subBidangUsaha.bidangUsaha.id =:id"),
	@NamedQuery(name="SubBidangUsaha.findNama",
    		query="SELECT subBidangUsaha FROM SubBidangUsaha subBidangUsaha WHERE subBidangUsaha.isDelete = 0 AND subBidangUsaha.nama =:nama"),
	@NamedQuery(name="SubBidangUsaha.getActiveSubBidangUsaha", query="select subBidangUsaha from SubBidangUsaha subBidangUsaha where subBidangUsaha.isDelete=0 "
    		+ " and subBidangUsaha.id in (select segmentasiVendor.subBidangUsaha.id from SegmentasiVendor segmentasiVendor where segmentasiVendor.isDelete = 0"
    		+ " and segmentasiVendor.vendor.isDelete = 0 and segmentasiVendor.vendor.id in (select catalogKontrak.vendor.id from CatalogKontrak catalogKontrak where "
    		+ " catalogKontrak.isDelete = 0 and (current_date between catalogKontrak.tglMulailKontrak and catalogKontrak.tglAkhirKontrak)))")
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_SUB_BIDANG_USAHA", initialValue = 1, allocationSize = 1)
public class SubBidangUsaha {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SUB_BIDANG_USAHA_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "BIDANG_USAHA_ID", referencedColumnName = "BIDANG_USAHA_ID")
	private BidangUsaha bidangUsaha;
	
	@Column(name = "NAMA", length = 200)
	private String nama;
	
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
	
	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	@Column(name = "SUB_BIDANG_USAHA_CODE")
	private String subBidangUsahaCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public BidangUsaha getBidangUsaha() {
		return bidangUsaha;
	}

	public void setBidangUsaha(BidangUsaha bidangUsaha) {
		this.bidangUsaha = bidangUsaha;
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

	public String getSubBidangUsahaCode() {
		return subBidangUsahaCode;
	}

	public void setSubBidangUsahaCode(String subBidangUsahaCode) {
		this.subBidangUsahaCode = subBidangUsahaCode;
	}
}
