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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "T4_CATALOG_STOCK")
@NamedQueries({
		@NamedQuery(name = "CatalogStock.updateCatalogStock", query = "update CatalogStock cs set cs.isDelete=1 where cs.catalog.id = :catalogId "),
		@NamedQuery(name = "CatalogStock.deleteByIdCatalog", query = "delete from CatalogStock cs where cs.catalog.id = :catalogId "),
		@NamedQuery(name = "CatalogStock.getByCatalog", query = "SELECT cs FROM CatalogStock cs WHERE cs.isDelete = 0 and cs.catalog = :catalog"),
		@NamedQuery(name = "CatalogStock.getCatalogStockList", query = "SELECT cs FROM CatalogStock cs WHERE cs.isDelete = 0"),
		@NamedQuery(name = "CatalogStock.getCatalogStockById", query = "SELECT cs FROM CatalogStock cs WHERE cs.isDelete = 0 and cs.id=:id") })
public class CatalogStock implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_STOCK_ID")
	private Integer id;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "CATALOG_ID", nullable = false)
	private Catalog catalog;

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

}
