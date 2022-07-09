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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "T1_ATTRIBUTE_GROUP")

@TableGenerator(name = "tableSequence", table = "SEQUENCE",pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE",pkColumnValue = "T1_ATTRIBUTE_GROUP", initialValue = 1, allocationSize = 1)
 
public class AttributeGroup implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ATTRIBUTE_GROUP_ID")
	private Integer id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "FLAG_ENABLED", length=1)
	private Boolean flagEnabled;
	
	@Column(name = "ISDELETE", length=1)
	private Integer isDelete;
	
	@OneToMany(cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name="ATTRIBUTE_GROUP_ID")
	private List<AttributePanelGroup> attributePanelGroupList;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public List<AttributePanelGroup> getAttributePanelGroupList() {
		return attributePanelGroupList;
	}

	public void setAttributePanelGroupList(
			List<AttributePanelGroup> attributePanelGroupList) {
		this.attributePanelGroupList = attributePanelGroupList;
	}
}
