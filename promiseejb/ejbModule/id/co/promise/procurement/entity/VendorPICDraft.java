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
@Table(name = "T3_VENDOR_PIC_DRAFT")
@NamedQueries({
	@NamedQuery(name="VendorPICDraft.findByVendor",
        	query="SELECT vendorPICDraft FROM VendorPICDraft vendorPICDraft WHERE vendorPICDraft.vendor.id =:vendorId AND vendorPICDraft.isDelete = 0  ORDER BY vendorPICDraft.jabatan.id"),
    @NamedQuery(name="VendorPICDraft.findByVendorList",
        	query="SELECT vendorPICDraft FROM VendorPICDraft vendorPICDraft WHERE vendorPICDraft.vendor.id =:vendorId "),
    @NamedQuery(name="VendorPICDraft.deleteByVendorId",
         	query="UPDATE VendorPICDraft vendorPICDraft set vendorPICDraft.isDelete = 1 WHERE vendorPICDraft.vendor.id =:vendorId "),
    @NamedQuery(name="VendorPICDraft.findByVendorAndVendorPIC",
        	query="SELECT vendorPICDraft FROM VendorPICDraft vendorPICDraft WHERE vendorPICDraft.vendor.id =:vendorId "
        			+ "AND vendorPICDraft.vendorPIC.id=:vendorPICId AND vendorPICDraft.isDelete = 0")
        	
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_VENDOR_PIC_DRAFT", initialValue = 1, allocationSize = 1)
public class VendorPICDraft {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "VENDOR_PIC_DRAFT_ID")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	@OneToOne
	@JoinColumn(name = "VENDOR_PIC_ID", referencedColumnName = "VENDOR_PIC_ID")
	private VendorPIC vendorPIC;
	
	@Column(name = "NAMA")
	private String nama;

	@OneToOne
	@JoinColumn(name = "JABATAN_ID", referencedColumnName = "JABATAN_ID")
	private Jabatan jabatan;
	
	@Column(name = "EMAIL")
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
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Jabatan getJabatan() {
		return jabatan;
	}

	public void setJabatan(Jabatan jabatan) {
		this.jabatan = jabatan;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public VendorPIC getVendorPIC() {
		return vendorPIC;
	}

	public void setVendorPIC(VendorPIC vendorPIC) {
		this.vendorPIC = vendorPIC;
	}
	
}
