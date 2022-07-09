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
	@NamedQuery(name="CatalogCategory.getDashboardData", query="SELECT COUNT (catalogCategory) , catalogCategory.category.translateInd  FROM CatalogCategory catalogCategory "
			+ " where (current_date between catalogCategory.catalog.catalogKontrak.tglMulailKontrak and catalogCategory.catalog.catalogKontrak.tglAkhirKontrak) "
			+ " and catalogCategory.catalog.catalogKontrak.isDelete = 0 and catalogCategory.isDelete = 0 "
			+ " and catalogCategory.category.id in (:categoryIdList) group by catalogCategory.category.translateInd ")
})
@Table(name = "T4_CATALOG_CATEGORIES")
/*
 * @TableGenerator(name = "tableSequence", table = "PROMISE_SEQUENCE",
 * pkColumnName = "TABLE_SEQ_NAME", valueColumnName = "TABLE_SEQ_VALUE",
 * pkColumnValue = "T4_CATALOG_CATEGORIES", initialValue = 1, allocationSize =
 * 1)
 */
public class CatalogCategory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_CATEGORIES_ID")
	private Integer id;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID")
	private Category category;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="CATALOG_ID")
	private Catalog catalog;
	
	@Column(name = "ISDELETE", length=1)
	private Integer isDelete;

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
}
