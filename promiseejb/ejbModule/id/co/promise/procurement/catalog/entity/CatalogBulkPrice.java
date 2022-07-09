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
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T4_CATALOG_BULK_PRICE")
@NamedQueries({
	
	@NamedQuery(name="CatalogBulkPrice.deleteByIdCatalog", query="delete from CatalogBulkPrice cbk where cbk.catalog.id = :catalogId "),
	@NamedQuery(name="CatalogBulkPrice.getByCatalog", query="SELECT cbk FROM CatalogBulkPrice cbk WHERE cbk.isDelete = 0 and cbk.catalog = :catalog"),
	@NamedQuery(name="CatalogBulkPrice.getByCatalogId", query="SELECT cbk FROM CatalogBulkPrice cbk WHERE cbk.isDelete = 0 and cbk.catalog.id = :catalogId"),
	@NamedQuery(name="CatalogBulkPrice.getCatalogBulkPriceList", query="SELECT cbk FROM CatalogBulkPrice cbk WHERE cbk.isDelete = 0"),
	@NamedQuery(name="CatalogBulkPrice.getCatalogBulkPriceListById", query="SELECT cbk FROM CatalogBulkPrice cbk WHERE cbk.isDelete = 0 and cbk.id=:id")
})

/*
 * @TableGenerator(name = "tableSequence", table = "PROMISE_SEQUENCE",
 * pkColumnName = "TABLE_SEQ_NAME", valueColumnName = "TABLE_SEQ_VALUE",
 * pkColumnValue = "T4_CATALOG_BULK_PRICE", initialValue = 1, allocationSize =
 * 1)
 */
public class CatalogBulkPrice implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_BULK_PRICE_ID")
	private Integer id;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="CATALOG_ID",nullable = false)
	private Catalog catalog;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", nullable = false)
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true, name = "DELETED")
	private Date deleted;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED",nullable=true)
	private Date updated;
	
	@Column(name = "ISDELETE", length=1,nullable = false)
	private Integer isDelete;
	
	
	@Column(name = "MAX_QUANTITY",nullable = false)
	private Integer maxQuantity;
	
	@Column(name = "MIN_QUANTITY",nullable = false)
	private Integer minQuantity;
	
	@ColumnDefault( value = "0" )
	@Column(name = "DISKON",nullable = false)
	private Double diskon;
	
	@Transient
	private CatalogBulkPriceHistory catalogBulkPriceHistory;
	
	@Transient
	private Boolean isOpenForm;
	
	
	
	public Boolean getIsOpenForm() {
		return isOpenForm;
	}

	public void setIsOpenForm(Boolean isOpenForm) {
		this.isOpenForm = isOpenForm;
	}

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

	public Integer getMaxQuantity() {
		return maxQuantity;
	}

	public void setMaxQuantity(Integer maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

	public Integer getMinQuantity() {
		return minQuantity;
	}

	public void setMinQuantity(Integer minQuantity) {
		this.minQuantity = minQuantity;
	}

	public Double getDiskon() {
		return diskon;
	}

	public void setDiskon(Double diskon) {
		this.diskon = diskon;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public CatalogBulkPriceHistory getCatalogBulkPriceHistory() {
		return catalogBulkPriceHistory;
	}

	public void setCatalogBulkPriceHistory(CatalogBulkPriceHistory catalogBulkPriceHistory) {
		this.catalogBulkPriceHistory = catalogBulkPriceHistory;
	}
	
	
}
