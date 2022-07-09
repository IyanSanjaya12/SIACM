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
@Table(name = "T4_CATALOG_IMAGES")
public class CatalogImage implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_IMAGES_ID")
	private Integer id;
	
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
}
