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
@Table(name = "T4_CATALOG_BULK_PRICE_HISTORY")
@NamedQueries({
	@NamedQuery(name="CatalogBulkPriceHistory.getByVersion", query="SELECT catBulkPriceHistory FROM CatalogBulkPriceHistory catBulkPriceHistory "
			+ "WHERE catBulkPriceHistory.isDelete = 0 AND "
			+ "catBulkPriceHistory.catalogBulkPrice.id = :catalogBulkPriceId "
			+ "ORDER BY catBulkPriceHistory.perubahanVersi DESC ")
})

public class CatalogBulkPriceHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_BULK_PRICE_HISTORY_ID")
	private Integer id;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="CATALOG_ID")
	private Catalog catalog;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="CATALOG_BULK_PRICE_ID")
	private CatalogBulkPrice catalogBulkPrice;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true, name = "DELETED")
	private Date deleted;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED",nullable=true)
	private Date updated;
	
	@Column(name = "ISDELETE", length=1)
	private Integer isDelete;
	
	
	@Column(name = "MAX_QUANTITY")
	private Integer maxQuantity;
	
	@Column(name = "MIN_QUANTITY")
	private Integer minQuantity;
	
	@ColumnDefault( value = "0" )
	@Column(name = "DISKON")
	private Double diskon;
	
	@Column(name = "PERUBAHAN_VERSI")
	private Integer perubahanVersi;
	
	@Column(name = "ACTION")
	private String action;
	
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

	public CatalogBulkPrice getCatalogBulkPrice() {
		return catalogBulkPrice;
	}

	public void setCatalogBulkPrice(CatalogBulkPrice catalogBulkPrice) {
		this.catalogBulkPrice = catalogBulkPrice;
	}

	public Integer getPerubahanVersi() {
		return perubahanVersi;
	}

	public void setPerubahanVersi(Integer perubahanVersi) {
		this.perubahanVersi = perubahanVersi;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	
	
	
}
