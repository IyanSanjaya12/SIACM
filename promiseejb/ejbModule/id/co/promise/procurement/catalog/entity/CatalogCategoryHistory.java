package id.co.promise.procurement.catalog.entity;

import java.io.Serializable;

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

@Entity
@NamedQueries({
	@NamedQuery(name="CatalogCategoryHistory.getByVersion", query="SELECT cch FROM CatalogCategoryHistory cch "
			+ "WHERE cch.isDelete = 0 AND cch.catalogCategory.id = :catalogCategoryId ORDER BY cch.perubahanVersi DESC ")
})
@Table(name = "T4_CATALOG_CATEGORIES_HISTORY")
public class CatalogCategoryHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_CATEGORIES_HISTORY_ID")
	private Integer id;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "CATALOG_CATEGORIES_ID", referencedColumnName = "CATALOG_CATEGORIES_ID")
	private CatalogCategory catalogCategory;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID")
	private Category category;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="CATALOG_ID", referencedColumnName = "CATALOG_ID")
	private Catalog catalog;
	
	@Column(name = "ISDELETE", length=1)
	private Integer isDelete;
	
	@Column(name = "PERUBAHAN_VERSI")
	private Integer perubahanVersi;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public CatalogCategory getCatalogCategory() {
		return catalogCategory;
	}

	public void setCatalogCategory(CatalogCategory catalogCategory) {
		this.catalogCategory = catalogCategory;
	}

	public Integer getPerubahanVersi() {
		return perubahanVersi;
	}

	public void setPerubahanVersi(Integer perubahanVersi) {
		this.perubahanVersi = perubahanVersi;
	}
	
}
