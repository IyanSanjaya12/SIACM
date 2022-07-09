package id.co.promise.procurement.catalog.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "T4_CATALOG_STOCK_HISTORY")
public class CatalogStockHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_STOCK_HISTORY_ID")
	private Integer id;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "CATALOG_ID", nullable = false)
	private Catalog catalog;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "CATALOG_STOCK_ID", nullable = false)
	private CatalogStock catalogStock;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", nullable = false)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, name = "DELETED")
	private Date deleted;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED", nullable = true)
	private Date updated;

	@Column(name = "ISDELETE", length = 1, nullable = false)
	private Integer isDelete;

	@Column(name = "QUANTITY", nullable = false)
	private Integer quantity;

	@Column(name = "TOTAL_STOCK", nullable = false)
	private Integer totalStock;

	@Column(name = "NOTES")
	private String notes;
	
	@Column(name = "PERUBAHAN_VERSI")
	private Integer perubahanVersi;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(Integer totalStock) {
		this.totalStock = totalStock;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public CatalogStock getCatalogStock() {
		return catalogStock;
	}

	public void setCatalogStock(CatalogStock catalogStock) {
		this.catalogStock = catalogStock;
	}

	public Integer getPerubahanVersi() {
		return perubahanVersi;
	}

	public void setPerubahanVersi(Integer perubahanVersi) {
		this.perubahanVersi = perubahanVersi;
	}
	
	

}
