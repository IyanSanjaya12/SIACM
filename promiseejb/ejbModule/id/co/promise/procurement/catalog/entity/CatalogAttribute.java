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
import javax.persistence.Table;


@Entity
@Table(name = "T4_CATALOG_ATTRIBUTES")
/*
 * @TableGenerator(name = "tableSequence", table = "PROMISE_SEQUENCE",
 * pkColumnName = "TABLE_SEQ_NAME", valueColumnName = "TABLE_SEQ_VALUE",
 * pkColumnValue = "T4_CATALOG_ATTRIBUTES", initialValue = 1, allocationSize =
 * 1)
 */
public class CatalogAttribute implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_ATTRIBUTES_ID")
	private Integer id;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "ATTRIBUTE_PANEL_GROUP_ID")
	private AttributePanelGroup attributePanelGroup;
	
	@Column(name = "NILAI")
	private String nilai;
	
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

	public String getNilai() {
		return nilai;
	}

	public void setNilai(String nilai) {
		this.nilai = nilai;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public AttributePanelGroup getAttributePanelGroup() {
		return attributePanelGroup;
	}

	public void setAttributePanelGroup(AttributePanelGroup attributePanelGroup) {
		this.attributePanelGroup = attributePanelGroup;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
}
