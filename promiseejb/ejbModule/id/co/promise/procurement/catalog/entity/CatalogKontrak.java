package id.co.promise.procurement.catalog.entity;

import java.io.Serializable;
import java.util.Date;

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

import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

import id.co.promise.procurement.entity.Vendor;

@Entity
@Table(name = "T4_CATALOG_KONTRAK")
@NamedQueries({
		@NamedQuery(name = "CatalogKontrak.find", query = "SELECT catalogKontrak FROM CatalogKontrak catalogKontrak WHERE catalogKontrak.isDelete = 0 "),
		@NamedQuery(name = "CatalogKontrak.getNonActive", query = "SELECT COUNT(catalogKontrak) FROM CatalogKontrak catalogKontrak WHERE catalogKontrak.isDelete = 0 and "
				+ "catalogKontrak.tglAkhirKontrak <= :endDate AND catalogKontrak.tglAkhirKontrak <= :endDatePlus21 "),
		@NamedQuery(name = "CatalogKontrak.getNonActiveByOrganizationId", query = "SELECT COUNT(catalogKontrak) FROM CatalogKontrak catalogKontrak WHERE catalogKontrak.isDelete = 0 and "
				+ "catalogKontrak.tglAkhirKontrak <= :endDate AND catalogKontrak.tglAkhirKontrak <= :endDatePlus21 And catalogKontrak.id IN ( SELECT catalogContractDetail.catalogContract.id FROM CatalogContractDetail catalogContractDetail " 
				+ "Where catalogContractDetail.item.id IN(select io.item.id from ItemOrganisasi io where io.organisasi.id =:organizationId ) AND catalogContractDetail.isDelete = 0 ) "),
		
		@NamedQuery(name = "CatalogKontrak.getAlmostExpired", query = "SELECT COUNT(catalogKontrak) FROM CatalogKontrak catalogKontrak WHERE catalogKontrak.isDelete = 0 and "
				+ " catalogKontrak.tglAkhirKontrak >= :endDate and catalogKontrak.tglAkhirKontrak <= :endDatePlus21"),
		@NamedQuery(name = "CatalogKontrak.getAlmostExpiredByOrganizationId", query = "SELECT COUNT(catalogKontrak) FROM CatalogKontrak catalogKontrak WHERE catalogKontrak.isDelete = 0 and "
				+ " catalogKontrak.tglAkhirKontrak >= :endDate and catalogKontrak.tglAkhirKontrak <= :endDatePlus21 And catalogKontrak.id IN ( SELECT catalogContractDetail.catalogContract.id FROM CatalogContractDetail catalogContractDetail " 
				+ "Where catalogContractDetail.item.id IN(select io.item.id from ItemOrganisasi io where io.organisasi.id =:organizationId ) AND catalogContractDetail.isDelete = 0 )"),
		
		@NamedQuery(name = "CatalogKontrak.getOver21Days", query = "SELECT COUNT(catalogKontrak) FROM CatalogKontrak catalogKontrak WHERE catalogKontrak.isDelete = 0  and "
				+ "catalogKontrak.tglAkhirKontrak >= :endDate  and catalogKontrak.tglAkhirKontrak >= :endDatePlus21 "),
		@NamedQuery(name = "CatalogKontrak.getOver21DaysByOrganizationId", query = "SELECT COUNT(catalogKontrak) FROM CatalogKontrak catalogKontrak WHERE catalogKontrak.isDelete = 0  and "
				+ "catalogKontrak.tglAkhirKontrak >= :endDate  and catalogKontrak.tglAkhirKontrak >= :endDatePlus21 And catalogKontrak.id IN ( SELECT catalogContractDetail.catalogContract.id FROM CatalogContractDetail catalogContractDetail " 
				+ "Where catalogContractDetail.item.id IN(select io.item.id from ItemOrganisasi io where io.organisasi.id =:organizationId ) AND catalogContractDetail.isDelete = 0 )"),
		
		@NamedQuery(name = "CatalogKontrak.findByVendor", query = "SELECT catalogKontrak FROM CatalogKontrak catalogKontrak WHERE catalogKontrak.isDelete = 0 "
				+ "AND catalogKontrak.vendor.id = :vendorId") ,
		@NamedQuery(name = "CatalogKontrak.findByVendorAndNotRelease", query = "SELECT catalogKontrak FROM CatalogKontrak catalogKontrak "
				+ "WHERE catalogKontrak.id IN (SELECT catalogContractDetail.catalogContract.id FROM CatalogContractDetail catalogContractDetail "
					+ "Where catalogContractDetail.flagItemUsed = 0 AND catalogContractDetail.id NOT IN( SELECT catalog.catalogContractDetail.id From Catalog catalog "
					+ "Where catalog.catalogContractDetail.id IS NOT NULL AND catalog.isDelete = 0) AND catalogContractDetail.isDelete = 0) "
				+ "AND catalogKontrak.vendor.id =:vendorId AND catalogKontrak.isDelete = 0 AND catalogKontrak.statusKontrak = 1 AND catalogKontrak.tglAkhirKontrak IS NOT NULL "
				+ "ORDER BY catalogKontrak.namaPekerjaan ASC") ,
		@NamedQuery(name = "CatalogKontrak.findByVendorAndNumberAndNotRelease", query = "SELECT catalogKontrak FROM CatalogKontrak catalogKontrak "
				+ "WHERE catalogKontrak.id IN (SELECT catalogContractDetail.catalogContract.id FROM CatalogContractDetail catalogContractDetail "
				+ "Where catalogContractDetail.id NOT IN( SELECT catalog.catalogContractDetail.id From Catalog catalog "
				+ "Where catalog.catalogContractDetail.id IS NOT NULL AND catalog.isDelete = 0) AND catalogContractDetail.isDelete = 0) " 
				+ "AND catalogKontrak.vendor.id =:vendorId AND catalogKontrak.isDelete = 0 AND lower(catalogKontrak.nomorKontrak) LIKE :number") })
public class CatalogKontrak implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_KONTRAK_ID")
	private Integer id;

	@Column(name = "NOMOR_KONTRAK")
	private String nomorKontrak;

	@Column(name = "TGL_MULAI_KONTRAK")
	private Date tglMulailKontrak;

	@Column(name = "MASA_BERLAKU_KONTRAK")
	private Integer masaBerlakuKontrak;

	@Column(name = "TGL_AKHIR_KONTRAK")
	private Date tglAkhirKontrak;

	@ColumnDefault( value = "0" )
	@Column(name = "STOCK")
	private Double stock;

	@Column(name = "ISDELETE", length = 1)
	private Integer isDelete;

	@Column(name = "NAMA_PEKERJAAN")
	private String namaPekerjaan;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "VENDOR_ID")
	private Vendor vendor;

	@Column(name = "NILAI_KONTRAK")
	private Double nilaiKontrak;
	
	@Column(name = "STATUS_KONTRAK", length = 1)
	private Integer statusKontrak;
	
	@Column(name = "REF_CATALOG_KONTRAK_ID")
	private Integer refCatalogKontrakId;
	
	@Column(name = "IS_PPN")
	private Integer isPpn;

	@Transient
	private Boolean tglStatOpen;

	@Transient
	private Boolean isOpenForm;

	@Transient
	private Integer reamingTime;
	
	@Transient//KAI 28/01/2021
	private Integer statusCatalog;

	public Date getTglMulailKontrak() {
		return tglMulailKontrak;
	}

	public void setTglMulailKontrak(Date tglMulailKontrak) {
		this.tglMulailKontrak = tglMulailKontrak;
	}

	public Integer getMasaBerlakuKontrak() {
		return masaBerlakuKontrak;
	}

	public void setMasaBerlakuKontrak(Integer masaBerlakuKontrak) {
		this.masaBerlakuKontrak = masaBerlakuKontrak;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomorKontrak() {
		return nomorKontrak;
	}

	public void setNomorKontrak(String nomorKontrak) {
		this.nomorKontrak = nomorKontrak;
	}

	public Date getTglAkhirKontrak() {
		return tglAkhirKontrak;
	}

	public void setTglAkhirKontrak(Date tglAkhirKontrak) {
		this.tglAkhirKontrak = tglAkhirKontrak;
	}

	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Boolean getTglStatOpen() {
		return tglStatOpen;
	}

	public void setTglStatOpen(Boolean tglStatOpen) {
		this.tglStatOpen = tglStatOpen;
	}

	public Boolean getIsOpenForm() {
		return isOpenForm;
	}

	public void setIsOpenForm(Boolean isOpenForm) {
		this.isOpenForm = isOpenForm;
	}

	public String getNamaPekerjaan() {
		return namaPekerjaan;
	}

	public void setNamaPekerjaan(String namaPekerjaan) {
		this.namaPekerjaan = namaPekerjaan;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Double getNilaiKontrak() {
		return nilaiKontrak;
	}

	public void setNilaiKontrak(Double nilaiKontrak) {
		this.nilaiKontrak = nilaiKontrak;
	}

	public Integer getReamingTime() {
		return reamingTime;
	}

	public void setReamingTime(Integer reamingTime) {
		this.reamingTime = reamingTime;
	}

	public Integer getStatusKontrak() {
		return statusKontrak;
	}

	public void setStatusKontrak(Integer statusKontrak) {
		this.statusKontrak = statusKontrak;
	}

	public Integer getRefCatalogKontrakId() {
		return refCatalogKontrakId;
	}

	public void setRefCatalogKontrakId(Integer refCatalogKontrakId) {
		this.refCatalogKontrakId = refCatalogKontrakId;
	}

	public Integer getStatusCatalog() {
		return statusCatalog;
	}

	public void setStatusCatalog(Integer statusCatalog) {
		this.statusCatalog = statusCatalog;
	}

	public Integer getIsPpn() {
		return isPpn;
	}

	public void setIsPpn(Integer isPpn) {
		this.isPpn = isPpn;
	}
	
	
	
	
}
