package id.co.promise.procurement.catalog.entity;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.Transient;

@Entity
@Table(name = "T2_CATEGORY")

@NamedQueries({
    @NamedQuery(name="category.findTranslateId", query="select category from Category category where category.isDelete=0 and category.translateInd = :translateInd"),
    @NamedQuery(name="category.getActiveCategory", query="select category from Category category where category.isDelete=0 "
    		+ " and category.id in (select catalogCategory.category.id from CatalogCategory catalogCategory where catalogCategory.isDelete = 0"
    		+ " and catalogCategory.catalog.isDelete = 0 and (current_date between catalogCategory.catalog.catalogKontrak.tglMulailKontrak  "
    		+ " and catalogCategory.catalog.catalogKontrak.tglAkhirKontrak ) and catalogCategory.catalog.catalogKontrak.isDelete = 0)")
})


@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_CATEGORY", initialValue = 1, allocationSize = 1)

public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATEGORY_ID")
	private Integer id;
	
	@Column(name = "DESCRIPTION" )
	private String description;
	
	@Column(name = "TRANSLATE_IND" )
	private String translateInd;
	
	@Column(name = "TRANSLATE_ENG" )
	private String translateEng;
	
	@Column(name = "ADMIN_LABEL")
	private String adminLabel;
	
	@Column(name = "FLAG_ENABLED", length=1)
	private Boolean flagEnabled;
	
	@Column(name = "ISDELETE", length=1)
	private Integer isDelete;
	
	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	private Category parentCategory;
	
	@Transient
	private List<Category> categoryChildList;
	
	@Transient
	private List<Category> categoryParentList;
	
	@Transient
	private Boolean value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTranslateInd() {
		return translateInd;
	}

	public void setTranslateInd(String translateInd) {
		this.translateInd = translateInd;
	}

	public String getTranslateEng() {
		return translateEng;
	}

	public void setTranslateEng(String translateEng) {
		this.translateEng = translateEng;
	}

	public String getAdminLabel() {
		return adminLabel;
	}

	public void setAdminLabel(String adminLabel) {
		this.adminLabel = adminLabel;
	}

	public Boolean getFlagEnabled() {
		return flagEnabled;
	}

	public void setFlagEnabled(Boolean flagEnabled) {
		this.flagEnabled = flagEnabled;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Category getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	public List<Category> getCategoryChildList() {
		return categoryChildList;
	}

	public void setCategoryChildList(List<Category> categoryChildList) {
		this.categoryChildList = categoryChildList;
	}
	
	public List<Category> getCategoryParentList() {
		return categoryParentList;
	}

	public void setCategoryParentList(List<Category> categoryParentList) {
		this.categoryParentList = categoryParentList;
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}
}
