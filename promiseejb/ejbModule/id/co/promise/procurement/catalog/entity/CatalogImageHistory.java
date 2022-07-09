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
	@NamedQuery(name="CatalogImageHistory.getByVersion", query="SELECT cih FROM CatalogImageHistory cih "
			+ "WHERE cih.isDelete = 0 AND cih.catalogImage.id = :catalogImageId ORDER BY cih.perubahanVersi DESC "),
	@NamedQuery(name="CatalogImageHistory.getByCatalog", query="SELECT cih FROM CatalogImageHistory cih "
			+ "WHERE cih.isDelete = 0 AND cih.catalog.id = :catalogId")
})
@Table(name = "T4_CATALOG_IMAGES_HISTORY")
public class CatalogImageHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_IMAGES_HISTORY_ID")
	private Integer id;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="CATALOG_IMAGES_ID", referencedColumnName = "CATALOG_IMAGES_ID")
	private CatalogImage catalogImage;
	
	@Column(name = "IMAGES_REAL_NAME")
	private String imagesRealName; // nama file di server setelah encrypt

	@Column(name = "IMAGES_FILE_NAME")
	private String imagesFileName; // nama file di server sebelum encrypt
	
	@Column(name = "IMAGES_FILE_SIZE")
	private Long imagesFileSize;
	
	@Column(name = "STATUS")
	private Boolean status;
	
	@Column(name = "SHORT_ORDER")
	private Integer urutanPesanan;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "CATALOG_ID", referencedColumnName = "CATALOG_ID")
	private Catalog catalog;
	
	@Column(name = "ISDELETE", length=1)
	private Integer isDelete;
	
	@Column(name = "PERUBAHAN_VERSI")
	private Integer perubahanVersi;
	
	@Column(name = "ACTION")
	private String action;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImagesRealName() {
		return imagesRealName;
	}

	public void setImagesRealName(String imagesRealName) {
		this.imagesRealName = imagesRealName;
	}

	public String getImagesFileName() {
		return imagesFileName;
	}

	public void setImagesFileName(String imagesFileName) {
		this.imagesFileName = imagesFileName;
	}

	public Long getImagesFileSize() {
		return imagesFileSize;
	}

	public void setImagesFileSize(Long imagesFileSize) {
		this.imagesFileSize = imagesFileSize;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getUrutanPesanan() {
		return urutanPesanan;
	}

	public void setUrutanPesanan(Integer urutanPesanan) {
		this.urutanPesanan = urutanPesanan;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public CatalogImage getCatalogImage() {
		return catalogImage;
	}

	public void setCatalogImage(CatalogImage catalogImage) {
		this.catalogImage = catalogImage;
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
