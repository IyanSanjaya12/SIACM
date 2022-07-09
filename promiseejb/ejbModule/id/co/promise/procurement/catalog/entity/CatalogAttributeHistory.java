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
@Table(name = "T4_CATALOG_ATTRIBUTES_HISTORY")
@NamedQueries({
	@NamedQuery(name="CatalogAttributeHistory.getByVersion", query="SELECT cah FROM CatalogAttributeHistory cah "
			+ "WHERE cah.isDelete = 0 AND cah.catalogAttribute.id = :catalogAttributeId ORDER BY cah.perubahanVersi DESC "),
	@NamedQuery(name="CatalogAttributeHistory.getByCatalog", query="SELECT cah FROM CatalogAttributeHistory cah "
			+ "WHERE cah.isDelete = 0 AND cah.catalog.id = :catalogId ")
})
public class CatalogAttributeHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_ATTRIBUTES_HISTORY_ID")
	private Integer id;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "ATTRIBUTE_PANEL_GROUP_ID", referencedColumnName = "ATTRIBUTE_PANEL_GROUP_ID")
	private AttributePanelGroup attributePanelGroup;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "CATALOG_ATTRIBUTES_ID", referencedColumnName = "CATALOG_ATTRIBUTES_ID")
	private CatalogAttribute catalogAttribute;
	
	@Column(name = "NILAI")
	private String nilai;
	
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

	public CatalogAttribute getCatalogAttribute() {
		return catalogAttribute;
	}

	public void setCatalogAttribute(CatalogAttribute catalogAttribute) {
		this.catalogAttribute = catalogAttribute;
	}

	public Integer getPerubahanVersi() {
		return perubahanVersi;
	}

	public void setPerubahanVersi(Integer perubahanVersi) {
		this.perubahanVersi = perubahanVersi;
	}
	
	
}
