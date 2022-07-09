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

import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.ItemType;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.ProductType;
import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;

@Entity
@NamedQueries({
	@NamedQuery(name="CatalogHistory.findByCatalog", query="SELECT catalogHistory FROM CatalogHistory catalogHistory "
					+ "WHERE catalogHistory.isDelete = :isDelete AND catalogHistory.catalog.id = :catalogId ORDER BY catalogHistory.id DESC")
})
@Table(name="T4_CATALOG_HISTORY")
public class CatalogHistory implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_HISTORY_ID")
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
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "CATALOG_KONTRAK_ID",referencedColumnName = "CATALOG_KONTRAK_ID")
	private CatalogKontrak catalogKontrak;
	
	@ManyToOne
	@JoinColumn(name = "CATALOG_ID", referencedColumnName = "CATALOG_ID")
	private Catalog catalog;

	@Column(name = "REVISION_NUMBER")
	private String revisionNumber;
	
	@Column(name = "PERUBAHAN_VERSI")
	private Integer perubahanVersi;
	/*penambahan field KAI 01/18/2021*/
	@Column(name = "MINIMUM_QTY_ORDER")
	private Double minimumQuantityOrder;
	
	@Column(name = "MAKSIMUM_QTY_ORDER_HARI")
	private Double maksimumQuantityOrderDay;
	
	@Column(name = "MAKSIMUM_QTY_ORDER_ORDER")
	private Double maksimumQuantityPerOrder;
	
	@Transient
	private List<CatalogCategory> catalogCategoryList;
	
	@Transient
	private String todo;
	
	@Transient
	private List<CatalogAttribute> catalogAttributeList;
	
	@Transient
	private List<CatalogBulkPrice> catalogBulkPriceList;
	
	@Transient
	private List<CatalogStock> catalogStockList;
	
	@Transient
	private List<CatalogFee> catalogFeeList;
	
	@Transient
	private List<CatalogImage> catalogImageList;
	
	@Transient
	private List<CatalogImageHistory> catalogImageHistoryList;
	
	@Transient
	private List<Category> categoryList;
	
	@Transient
	private Boolean isChange;

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

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public String getNamaIND() {
		return namaIND;
	}

	public void setNamaIND(String namaIND) {
		this.namaIND = namaIND;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
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

	public Satuan getSatuan() {
		return satuan;
	}

	public void setSatuan(Satuan satuan) {
		this.satuan = satuan;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getIsVendor() {
		return isVendor;
	}

	public void setIsVendor(Boolean isVendor) {
		this.isVendor = isVendor;
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public String getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(String revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	public List<CatalogCategory> getCatalogCategoryList() {
		return catalogCategoryList;
	}

	public void setCatalogCategoryList(List<CatalogCategory> catalogCategoryList) {
		this.catalogCategoryList = catalogCategoryList;
	}

	public String getTodo() {
		return todo;
	}

	public void setTodo(String todo) {
		this.todo = todo;
	}

	public List<CatalogAttribute> getCatalogAttributeList() {
		return catalogAttributeList;
	}

	public void setCatalogAttributeList(List<CatalogAttribute> catalogAttributeList) {
		this.catalogAttributeList = catalogAttributeList;
	}

	public List<CatalogBulkPrice> getCatalogBulkPriceList() {
		return catalogBulkPriceList;
	}

	public void setCatalogBulkPriceList(List<CatalogBulkPrice> catalogBulkPriceList) {
		this.catalogBulkPriceList = catalogBulkPriceList;
	}

	public List<CatalogStock> getCatalogStockList() {
		return catalogStockList;
	}

	public void setCatalogStockList(List<CatalogStock> catalogStockList) {
		this.catalogStockList = catalogStockList;
	}

	public List<CatalogFee> getCatalogFeeList() {
		return catalogFeeList;
	}

	public void setCatalogFeeList(List<CatalogFee> catalogFeeList) {
		this.catalogFeeList = catalogFeeList;
	}

	public List<CatalogImage> getCatalogImageList() {
		return catalogImageList;
	}

	public void setCatalogImageList(List<CatalogImage> catalogImageList) {
		this.catalogImageList = catalogImageList;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public Integer getPerubahanVersi() {
		return perubahanVersi;
	}

	public void setPerubahanVersi(Integer perubahanVersi) {
		this.perubahanVersi = perubahanVersi;
	}

	public List<CatalogImageHistory> getCatalogImageHistoryList() {
		return catalogImageHistoryList;
	}

	public void setCatalogImageHistoryList(List<CatalogImageHistory> catalogImageHistoryList) {
		this.catalogImageHistoryList = catalogImageHistoryList;
	}

	public Boolean getIsChange() {
		return isChange;
	}

	public void setIsChange(Boolean isChange) {
		this.isChange = isChange;
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
