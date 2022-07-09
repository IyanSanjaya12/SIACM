package id.co.promise.procurement.catalog.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.ItemType;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.ProductType;
import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorProfile;



@Entity

@NamedQueries({
	@NamedQuery(name="Catalog.getList", query="SELECT catalog FROM Catalog catalog where catalog.isDelete = 0 "),
	@NamedQuery(name="Catalog.getListCount", query="SELECT COUNT (catalog) FROM Catalog catalog JOIN CatalogCategory catalogCategory "
			+ "on catalog.id = catalogCategory.catalog and catalog.isDelete = 0 and catalog.namaIND like :search and catalog.vendor.nama =:vendorName"),
	@NamedQuery(name="Catalog.getCatalogExisting", query="SELECT catalog FROM Catalog catalog WHERE catalog.isDelete = :isDelete "
			+ "AND catalog.item = :item AND catalog.catalogKontrak = :catalogKontrak"),
})
@Table(name = "T3_CATALOG")
/*
 * @TableGenerator(name = "tableSequence", table = "PROMISE_SEQUENCE",
 * pkColumnName = "TABLE_SEQ_NAME", valueColumnName = "TABLE_SEQ_VALUE",
 * pkColumnValue = "PROMISE_T3_CATALOG", initialValue = 1, allocationSize = 1)
 */
public class Catalog implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_ID")
	private Integer id;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="ATTRIBUTE_GROUP_ID")
	private AttributeGroup attributeGroup;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "ITEM_TYPE_ID", referencedColumnName = "ITEM_TYPE_ID")
	private ItemType itemType;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "PRODUCT_TYPE_ID", referencedColumnName = "PRODUCT_TYPE_ID")
	private ProductType productType;
	
	@Column(name = "NAME_IND")
	private String namaIND;
	
	@Column(name = "DISCOUNT")
	private Integer discount;
	
	@Column(name = "NAME_ENG")
	private String namaENG;
	
	@Column(name = "DESKRIPSI_IND", columnDefinition = "varchar(2000)", length = 2000)
	private String deskripsiIND;
	
	@Column(name = "DESKRIPSI_ENG", columnDefinition = "varchar(2000)", length = 2000)
	private String deskripsiENG;
	
	@Column(name = "KODE_PRODUK")
	private String kodeProduk;
	
	@Column(name = "KODE_PRODUK_PANITIA")
	private String kodeProdukPanitia;
	
	@Column(name = "KODE_ITEM")
	private String kodeItem;
	
	@ColumnDefault( value = "0" )
	@Column(name = "HARGA")
	private Double harga;
	
	@ColumnDefault( value = "0" )
	@Column(name = "HARGA_EPROC")
	private Double harga_eproc;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "MATA_UANG_ID", referencedColumnName = "MATA_UANG_ID")
	private MataUang mataUang;	
	
	@ColumnDefault( value = "0" )
	@Column(name = "BERAT")
	private Double berat;
	
	@Column(name="SATUAN_BERAT")
	private String satuanBerat;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "SATUAN_ID", referencedColumnName = "SATUAN_ID")
	private Satuan satuan;
	
	@Column(name = "WHOLESALER",length=1)
	private Boolean wholesaler;
	
	@ColumnDefault( value = "0" )
	@Column(name = "PANJANG")
	private Double panjang;
	
	@Column(name="SATUAN_PANJANG")
	private String satuanPanjang;
		
	@ColumnDefault( value = "0" )
	@Column(name = "LEBAR")
	private Double lebar;
	
	@Column(name = "SATUAN_LEBAR")
	private String satuanLebar;
	
	@ColumnDefault( value = "0" )
	@Column(name = "TINGGI")
	private Double tinggi;
	
	@Column(name = "SATUAN_TINGGI")
	private String satuanTinggi;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="VENDOR_ID", referencedColumnName="VENDOR_ID")
	private Vendor vendor;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="ITEM_ID", referencedColumnName="ITEM_ID")
	private Item item;
	
	@Column(name = "STATUS", length=1)
	private Boolean status;
	
	@Column(name = "IS_VENDOR",  length=1)
	private Boolean isVendor;
	
	@Column(name = "APPROVAL_STATUS")
	private Integer approvalStatus;
	
	@Column(name = "RATING")
	private Integer rating;
	
	@Column(name = "RATING_COMMENT", columnDefinition = "varchar(1000)", length = 1000)
	private String ratingComment;
		
	@Column(name = "ISDELETE",  length=1)
	private Integer isDelete;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	private User user;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;
	
	@Column(name = "MINIMAL_QUANTITY")
	private Integer minimalQuantity;
	
	@Column(name = "CURRENT_STOCK")
	private Integer currentStock;
	
	@Column(name = "IS_APPROVAL")
	private Integer isApproval;
	
	@ManyToOne
	@JoinColumn(name = "CATALOG_KONTRAK_ID", referencedColumnName = "CATALOG_KONTRAK_ID")
	private CatalogKontrak catalogKontrak;
	
	@ManyToOne
	@JoinColumn(name = "CATALOG_CONTRACT_DETAIL_ID", referencedColumnName = "CATALOG_CONTRACT_DETAIL_ID")
	private CatalogContractDetail catalogContractDetail;
	/*penambahan field KAI 01/18/2021*/
	@Column(name = "MINIMUM_QTY_ORDER")
	private Double minimumQuantityOrder;
	
	@Column(name = "MAKSIMUM_QTY_ORDER_HARI")
	private Double maksimumQuantityOrderDay;
	
	@Column(name = "MAKSIMUM_QTY_ORDER_ORDER")
	private Double maksimumQuantityPerOrder;
	
	@Transient
	private Boolean isAvailable;
	
	@Transient
	private List<Category> categoryList;
	
	@Transient
	private List<CatalogCategory> catalogCategoryList;
	
	@Transient
	private List<CatalogLocation> catalogLocationList;
	
	@Transient
	private List<CatalogPriceRange> catalogPriceRangeList;
	
	@Transient
	private List<CatalogImage> catalogImageList;
	
	@Transient
	private VendorProfile vendorProfile;
	
	@Transient
	private List<CatalogAttribute> catalogAttributeList;
	
	@Transient
	private List<CatalogBulkPrice> catalogBulkPriceList;
	
	@Transient
	private List<CatalogStock> catalogStockList;
	
	@Transient
	private List<CatalogFee> catalogFeeList;
	
	@Transient
	private Integer quantity;
	
	//untuk menangkap post get katalog by category
	@Transient
	private Integer categoryId;
	
	@Transient
	private ApprovalProcess approvalProcess;
	
	@Transient
	private Integer transaksi;
	
	@Transient
	private Integer isApprovalTahapan;
	
	@Transient
	private String todo;

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AttributeGroup getAttributeGroup() {
		return attributeGroup;
	}

	public void setAttributeGroup(AttributeGroup attributeGroup) {
		this.attributeGroup = attributeGroup;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	public String getNamaIND() {
		return namaIND;
	}

	public void setNamaIND(String namaIND) {
		this.namaIND = namaIND;
	}

	public String getNamaENG() {
		return namaENG;
	}

	public void setNamaENG(String namaENG) {
		this.namaENG = namaENG;
	}

	public String getDeskripsiIND() {
		return deskripsiIND;
	}

	public void setDeskripsiIND(String deskripsiIND) {
		this.deskripsiIND = deskripsiIND;
	}

	public String getDeskripsiENG() {
		return deskripsiENG;
	}

	public void setDeskripsiENG(String deskripsiENG) {
		this.deskripsiENG = deskripsiENG;
	}

	public String getKodeProduk() {
		return kodeProduk;
	}

	public void setKodeProduk(String kodeProduk) {
		this.kodeProduk = kodeProduk;
	}

	public Double getHarga() {
		return harga;
	}

	public void setHarga(Double harga) {
		this.harga = harga;
	}

	public Double getHarga_eproc() {
		return harga_eproc;
	}

	public void setHarga_eproc(Double harga_eproc) {
		this.harga_eproc = harga_eproc;
	}

	public MataUang getMataUang() {
		return mataUang;
	}

	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
	}

	public Double getBerat() {
		return berat;
	}

	public void setBerat(Double berat) {
		this.berat = berat;
	}

	public String getSatuanBerat() {
		return satuanBerat;
	}

	public void setSatuanBerat(String satuanBerat) {
		this.satuanBerat = satuanBerat;
	}

	public Boolean getWholesaler() {
		return wholesaler;
	}

	public void setWholesaler(Boolean wholesaler) {
		this.wholesaler = wholesaler;
	}

	public Double getPanjang() {
		return panjang;
	}

	public void setPanjang(Double panjang) {
		this.panjang = panjang;
	}

	public String getSatuanPanjang() {
		return satuanPanjang;
	}

	public void setSatuanPanjang(String satuanPanjang) {
		this.satuanPanjang = satuanPanjang;
	}

	public Double getLebar() {
		return lebar;
	}

	public void setLebar(Double lebar) {
		this.lebar = lebar;
	}

	public String getSatuanLebar() {
		return satuanLebar;
	}

	public void setSatuanLebar(String satuanLebar) {
		this.satuanLebar = satuanLebar;
	}

	public Double getTinggi() {
		return tinggi;
	}

	public void setTinggi(Double tinggi) {
		this.tinggi = tinggi;
	}

	public String getSatuanTinggi() {
		return satuanTinggi;
	}

	public void setSatuanTinggi(String satuanTinggi) {
		this.satuanTinggi = satuanTinggi;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Satuan getSatuan() {
		return satuan;
	}

	public void setSatuan(Satuan satuan) {
		this.satuan = satuan;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getRatingComment() {
		return ratingComment;
	}

	public void setRatingComment(String ratingComment) {
		this.ratingComment = ratingComment;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public List<CatalogLocation> getCatalogLocationList() {
		return catalogLocationList;
	}

	public void setCatalogLocationList(List<CatalogLocation> catalogLocationList) {
		this.catalogLocationList = catalogLocationList;
	}

	public List<CatalogPriceRange> getCatalogPriceRangeList() {
		return catalogPriceRangeList;
	}

	public void setCatalogPriceRangeList(
			List<CatalogPriceRange> catalogPriceRangeList) {
		this.catalogPriceRangeList = catalogPriceRangeList;
	}

	public List<CatalogImage> getCatalogImageList() {
		return catalogImageList;
	}

	public void setCatalogImageList(List<CatalogImage> catalogImageList) {
		this.catalogImageList = catalogImageList;
	}

	public List<CatalogCategory> getCatalogCategoryList() {
		return catalogCategoryList;
	}

	public void setCatalogCategoryList(List<CatalogCategory> catalogCategoryList) {
		this.catalogCategoryList = catalogCategoryList;
	}

	public VendorProfile getVendorProfile() {
		return vendorProfile;
	}

	public void setVendorProfile(VendorProfile vendorProfile) {
		this.vendorProfile = vendorProfile;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public List<CatalogAttribute> getCatalogAttributeList() {
		return catalogAttributeList;
	}

	public void setCatalogAttributeList(List<CatalogAttribute> catalogAttributeList) {
		this.catalogAttributeList = catalogAttributeList;
	}

	public String getKodeProdukPanitia() {
		return kodeProdukPanitia;
	}

	public void setKodeProdukPanitia(String kodeProdukPanitia) {
		this.kodeProdukPanitia = kodeProdukPanitia;
	}


	public String getKodeItem() {
		return kodeItem;
	}

	public void setKodeItem(String kodeItem) {
		this.kodeItem = kodeItem;
	}

	public Boolean getIsVendor() {
		return isVendor;
	}

	public void setIsVendor(Boolean isVendor) {
		this.isVendor = isVendor;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public List<CatalogBulkPrice> getCatalogBulkPriceList() {
		return catalogBulkPriceList;
	}

	public void setCatalogBulkPriceList(List<CatalogBulkPrice> catalogBulkPriceList) {
		this.catalogBulkPriceList = catalogBulkPriceList;
	}

	public Integer getMinimalQuantity() {
		return minimalQuantity;
	}

	public void setMinimalQuantity(Integer minimalQuantity) {
		this.minimalQuantity = minimalQuantity;
	}

	public Integer getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(Integer currentStock) {
		this.currentStock = currentStock;
	}
	
	public List<CatalogStock> getCatalogStockList() {
		return catalogStockList;
	}

	public void setCatalogStockList(List<CatalogStock> catalogStockList) {
		this.catalogStockList = catalogStockList;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public List<CatalogFee> getCatalogFeeList() {
		return catalogFeeList;
	}

	public void setCatalogFeeList(List<CatalogFee> catalogFeeList) {
		this.catalogFeeList = catalogFeeList;
	}
	
	public Integer getIsApproval() {
		return isApproval;
	}

	public void setIsApproval(Integer isApproval) {
		this.isApproval = isApproval;
	}

	public CatalogKontrak getCatalogKontrak() {
		return catalogKontrak;
	}

	public void setCatalogKontrak(CatalogKontrak catalogKontrak) {
		this.catalogKontrak = catalogKontrak;
	}

	public ApprovalProcess getApprovalProcess() {
		return approvalProcess;
	}

	public void setApprovalProcess(ApprovalProcess approvalProcess) {
		this.approvalProcess = approvalProcess;
	}

	public CatalogContractDetail getCatalogContractDetail() {
		return catalogContractDetail;
	}

	public void setCatalogContractDetail(CatalogContractDetail catalogContractDetail) {
		this.catalogContractDetail = catalogContractDetail;
	}

	public Integer getTransaksi() {
		return transaksi;
	}

	public void setTransaksi(Integer transaksi) {
		this.transaksi = transaksi;
	}

	public Integer getIsApprovalTahapan() {
		return isApprovalTahapan;
	}

	public void setIsApprovalTahapan(Integer isApprovalTahapan) {
		this.isApprovalTahapan = isApprovalTahapan;
	}

	public String getTodo() {
		return todo;
	}

	public void setTodo(String todo) {
		this.todo = todo;
	}

	public Double getMinimumQuantityOrder() {
		return minimumQuantityOrder;
	}

	public void setMinimumQuantityOrder(Double minimumQuantityOrder) {
		this.minimumQuantityOrder = minimumQuantityOrder;
	}

	public Double getMaksimumQuantityOrderDay() {
		return maksimumQuantityOrderDay;
	}

	public void setMaksimumQuantityOrderDay(Double maksimumQuantityOrderDay) {
		this.maksimumQuantityOrderDay = maksimumQuantityOrderDay;
	}

	public Double getMaksimumQuantityPerOrder() {
		return maksimumQuantityPerOrder;
	}

	public void setMaksimumQuantityPerOrder(Double maksimumQuantityPerOrder) {
		this.maksimumQuantityPerOrder = maksimumQuantityPerOrder;
	}
	
}
