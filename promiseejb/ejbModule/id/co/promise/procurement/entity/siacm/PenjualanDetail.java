package id.co.promise.procurement.entity.siacm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "TB_PENJUALAN_DETAIL")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_PENJUALAN_DETAIL", initialValue = 1, allocationSize = 1)
public class PenjualanDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PENJUALAN_DETAIL_ID")
	private Integer id;

	@Column(name="QUANTITY")
	private Double quantity;
	
	@Column(name = "TOTAL")
	private Double total;
	
	@ManyToOne
	@JoinColumn(name = "BARANG_ID", referencedColumnName = "BARANG_ID")
	private Barang barang;
	
	@ManyToOne
	@JoinColumn(name = "PENJUALAN_ID", referencedColumnName = "PENJUALAN_ID")
	private Penjualan penjualan;
	
	@Column(name = "HARGA_BELI")
	private Double hargaBeli;

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
	
	@Transient
	private Boolean isOpenForm;

	public Double getHargaBeli() {
		return hargaBeli;
	}

	public void setHargaBeli(Double hargaBeli) {
		this.hargaBeli = hargaBeli;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Barang getBarang() {
		return barang;
	}

	public void setBarang(Barang barang) {
		this.barang = barang;
	}

	public Penjualan getPenjualan() {
		return penjualan;
	}

	public void setPenjualan(Penjualan penjualan) {
		this.penjualan = penjualan;
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

	public Boolean getIsOpenForm() {
		return isOpenForm;
	}

	public void setIsOpenForm(Boolean isOpenForm) {
		this.isOpenForm = isOpenForm;
	}
	
	
}
