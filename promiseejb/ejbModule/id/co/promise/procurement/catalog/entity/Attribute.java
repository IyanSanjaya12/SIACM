package id.co.promise.procurement.catalog.entity;

/**
 * @author F.H.K
 *
 */

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "T1_ATTRIBUTE")

@TableGenerator(name = "tableSequence", table = "SEQUENCE",pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE",pkColumnValue = "T1_ATTRIBUTE", initialValue = 1, allocationSize = 1)
public class Attribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ATTRIBUTE_ID")
	private Integer id;
	
	@Column(name = "NAME")
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "INPUT_TYPE_ID", referencedColumnName = "INPUT_TYPE_ID")
	private InputType inputType;	
	
	@Column(name = "REQUIRED_FIELD", length=1)
	private Boolean required;
	
	@Column(name = "UNIQUE_FIELD", length=1)
	private Boolean unique;
	
	@Column(name = "SEARCHABLE_FIELD", length=1)
	private Boolean searchable;
	
	@Column(name = "SORTABLE_FIELD", length=1)
	private Boolean sortable;	
	
	@Column(name = "CONFIGURABLE_FIELD", length=1)
	private Boolean configurable;
	
	@Column(name = "TRANSLATE_IND" )
	private String translateInd;
	
	@Column(name = "TRANSLATE_ENG" )
	private String translateEng;
	
	@Column(name = "FLAG_ENABLED", length=1)
	private Boolean flagEnabled;
	
	@Column(name = "ISDELETE", length=1)
	private Integer isDelete;
	
	//cascade=CascadeType.ALL, fetch=FetchType.EAGER
	@OneToMany(cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name="ATTRIBUTE_ID")
	private List<AttributeOption> attributeOptionList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InputType getInputType() {
		return inputType;
	}

	public void setInputType(InputType inputType) {
		this.inputType = inputType;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean getUnique() {
		return unique;
	}

	public void setUnique(Boolean unique) {
		this.unique = unique;
	}

	public Boolean getSearchable() {
		return searchable;
	}

	public void setSearchable(Boolean searchable) {
		this.searchable = searchable;
	}

	public Boolean getSortable() {
		return sortable;
	}

	public void setSortable(Boolean sortable) {
		this.sortable = sortable;
	}

	public Boolean getConfigurable() {
		return configurable;
	}

	public void setConfigurable(Boolean configurable) {
		this.configurable = configurable;
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

	public List<AttributeOption> getAttributeOptionList() {
		return attributeOptionList;
	}

	public void setAttributeOptionList(List<AttributeOption> attributeOptionList) {
		this.attributeOptionList = attributeOptionList;
	}
}
