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
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

@Entity
@Table(name = "T2_ATTR_PANEL_GROUP")

@TableGenerator(name = "tableSequence", table = "SEQUENCE",pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE",pkColumnValue = "T2_ATTR_PANEL_GROUP", initialValue = 1,allocationSize = 1)
 
public class AttributePanelGroup implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ATTRIBUTE_PANEL_GROUP_ID")
	private Integer id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "URUTAN")
	private Integer urutan;
	
	@Column(name = "ISDELETE", length=1)
	private Integer isDelete;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="ATTRIBUTE_ID")
	private Attribute attribute;
	
	@Transient
	private List<Attribute> attributeList;
	
	@Transient
	private String value;
	
	@Transient
	private Boolean tglStatOpen;

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

	public Integer getUrutan() {
		return urutan;
	}

	public void setUrutan(Integer urutan) {
		this.urutan = urutan;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Attribute> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<Attribute> attributeList) {
		this.attributeList = attributeList;
	}

	public Boolean getTglStatOpen() {
		return tglStatOpen;
	}

	public void setTglStatOpen(Boolean tglStatOpen) {
		this.tglStatOpen = tglStatOpen;
	}
}
