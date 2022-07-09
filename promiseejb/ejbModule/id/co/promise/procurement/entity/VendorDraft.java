package id.co.promise.procurement.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.Category;

@Entity
@Table(name = "T2_VENDOR_DRAFT")
@NamedQueries({ @NamedQuery(name = "VendorDraft.find", query = "SELECT vendorDraft FROM VendorDraft vendorDraft WHERE vendorDraft = vendorDraft"),
		@NamedQuery(name = "VendorDraft.findByNama", query = "SELECT vendorDraft FROM VendorDraft vendorDraft WHERE vendorDraft.nama LIKE :nama"),
		@NamedQuery(name = "VendorDraft.findByUserId", query = "SELECT vendorDraft FROM VendorDraft vendorDraft WHERE vendorDraft.user =:userId"),
		@NamedQuery(name = "VendorDraft.findByStatus", query = "SELECT vendorDraft FROM VendorDraft vendorDraft WHERE vendorDraft.status =:status"),
		@NamedQuery(name = "VendorDraft.countByStatus", query = "SELECT count(vendorDraft) FROM VendorDraft vendorDraft WHERE vendorDraft.status =:status"),
		@NamedQuery(name = "VendorDraft.findById", query = "SELECT vendorDraft FROM VendorDraft vendorDraft WHERE vendorDraft.id =:id"),
		@NamedQuery(name = "VendorDraft.findByVendorId", query = "SELECT vendorDraft FROM VendorDraft vendorDraft WHERE vendorDraft.vendor.id =:id AND vendorDraft.isDelete = 0")

})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_VENDOR_DRAFT", initialValue = 1, allocationSize = 1)
public class VendorDraft {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "VENDOR_DRAFT_ID")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;

	@Column(name = "NAMA")
	private String nama;

	@Column(name = "ALAMAT")
	private String Alamat;

	@Column(name = "NOMOR_TELEPON")
	private String nomorTelpon;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "NPWP")
	private String npwp;

	@Column(name = "PENANGGUNG_JAWAB")
	private String penanggungJawab;

	@Column(name = "RATING")
	private Integer rating;

	@Column(name = "VENDOR_COMMENT")
	private String vendorComment;

	@Column(name = "BACKGROUND_IMAGE")
	private String backgroundImage;

	@Column(name = "BACKGROUND_IMAGE_SIZE")
	private String backgroundImageSize;

	@Column(name = "LOGO_IMAGE")
	private String logoImage;

	@Column(name = "LOGO_IMAGE_SIZE")
	private String logoImageSize;

	// user login
	@Column(name = "USER_ID")
	private int user;

	@Column(name = "STATUS")
	private Integer status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED")
	private Date deleted;

	// user created
	@Column(name = "USERID")
	private Integer userId;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;

	@Column(name = "PERFORMANCE_DATE")
	private Date performanceDate;

	@ColumnDefault( value = "0" )
	@Column(name = "PERFORMANCE_AVG")
	private Double performanceAvg;

	@Column(name = "DESKRIPSI", columnDefinition = "varchar(1000)", length = 1000)
	private String deskripsi;

	@Column(name = "KOTA")
	private String kota;

	@Column(name = "PROVINSI")
	private String provinsi;

	@Column(name = "TGL_REGISTRASI")
	private Date tglRegistrasi;

	@Transient
	private List<Category> mainProductCategoryList;
	
	@Transient
	private List<SegmentasiVendor> segmentasiVendor;

	@Transient
	private Integer winnerCount;

	@Transient
	private Integer pengadaanCount;

	@Transient
	private Integer pengadaanRunningCount;

	@Transient
	private List<Catalog> catalogList;
	
	@Transient
	private List<String> paramDataTable;

	@Column(name = "STATUS_PERPANJANGAN")
	private Integer statusPerpanjangan;

	/**
	 * 0 = aktif 1 = req blacklist 2 = blacklist
	 */

	@Column(name = "STATUS_BLACKLIST")
	private Integer statusBlacklist;

	/**
	 * 0 = aktif 1 = req unblacklist
	 */

	@Column(name = "STATUS_UNBLACKLIST")
	private Integer statusUnblacklist;
	
	@OneToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName = "ORGANISASI_ID")
	private Organisasi afco;
	
	@Column(name = "GUID_NO")
	private Integer guidNo;
	
	@Column(name = "IS_PKS")
	private Integer isPKS;
	
	@Column(name = "EMAIL_REQUESTOR")
	private String emailRequestor;
	
	public Integer getIsPKS() {
		return isPKS;
	}

	public void setIsPKS(Integer isPKS) {
		this.isPKS = isPKS;
	}

	public String getEmailRequestor() {
		return emailRequestor;
	}

	public void setEmailRequestor(String emailRequestor) {
		this.emailRequestor = emailRequestor;
	}
	
	//organisasi yg dikirim ke SAP
	public Organisasi getAfco() {
		return afco;
	}

	public void setAfco(Organisasi afco) {
		this.afco = afco;
	}

	public Integer getGuidNo() {
		return guidNo;
	}

	public void setGuidNo(Integer guidNo) {
		this.guidNo = guidNo;
	}

	public Integer getId() {
		return id;
	}
	
	

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getAlamat() {
		return Alamat;
	}

	public void setAlamat(String alamat) {
		Alamat = alamat;
	}

	public String getNomorTelpon() {
		return nomorTelpon;
	}

	public void setNomorTelpon(String nomorTelpon) {
		this.nomorTelpon = nomorTelpon;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNpwp() {
		return npwp;
	}

	public void setNpwp(String npwp) {
		this.npwp = npwp;
	}

	public String getPenanggungJawab() {
		return penanggungJawab;
	}

	public void setPenanggungJawab(String penanggungJawab) {
		this.penanggungJawab = penanggungJawab;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getVendorComment() {
		return vendorComment;
	}

	public void setVendorComment(String vendorComment) {
		this.vendorComment = vendorComment;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public String getBackgroundImageSize() {
		return backgroundImageSize;
	}

	public void setBackgroundImageSize(String backgroundImageSize) {
		this.backgroundImageSize = backgroundImageSize;
	}

	public String getLogoImage() {
		return logoImage;
	}

	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}

	public String getLogoImageSize() {
		return logoImageSize;
	}

	public void setLogoImageSize(String logoImageSize) {
		this.logoImageSize = logoImageSize;
	}

	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getPerformanceDate() {
		return performanceDate;
	}

	public void setPerformanceDate(Date performanceDate) {
		this.performanceDate = performanceDate;
	}

	public Double getPerformanceAvg() {
		return performanceAvg;
	}

	public void setPerformanceAvg(Double performanceAvg) {
		this.performanceAvg = performanceAvg;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public String getKota() {
		return kota;
	}

	public void setKota(String kota) {
		this.kota = kota;
	}

	public String getProvinsi() {
		return provinsi;
	}

	public void setProvinsi(String provinsi) {
		this.provinsi = provinsi;
	}

	public Date getTglRegistrasi() {
		return tglRegistrasi;
	}

	public void setTglRegistrasi(Date tglRegistrasi) {
		this.tglRegistrasi = tglRegistrasi;
	}

	public List<Category> getMainProductCategoryList() {
		return mainProductCategoryList;
	}

	public void setMainProductCategoryList(List<Category> mainProductCategoryList) {
		this.mainProductCategoryList = mainProductCategoryList;
	}

	public Integer getWinnerCount() {
		return winnerCount;
	}

	public void setWinnerCount(Integer winnerCount) {
		this.winnerCount = winnerCount;
	}

	public Integer getPengadaanCount() {
		return pengadaanCount;
	}

	public void setPengadaanCount(Integer pengadaanCount) {
		this.pengadaanCount = pengadaanCount;
	}

	public Integer getPengadaanRunningCount() {
		return pengadaanRunningCount;
	}

	public void setPengadaanRunningCount(Integer pengadaanRunningCount) {
		this.pengadaanRunningCount = pengadaanRunningCount;
	}

	public List<Catalog> getCatalogList() {
		return catalogList;
	}

	public void setCatalogList(List<Catalog> catalogList) {
		this.catalogList = catalogList;
	}

	public Integer getStatusPerpanjangan() {
		return statusPerpanjangan;
	}

	public void setStatusPerpanjangan(Integer statusPerpanjangan) {
		this.statusPerpanjangan = statusPerpanjangan;
	}

	public Integer getStatusBlacklist() {
		return statusBlacklist;
	}

	public void setStatusBlacklist(Integer statusBlacklist) {
		this.statusBlacklist = statusBlacklist;
	}

	public Integer getStatusUnblacklist() {
		return statusUnblacklist;
	}

	public void setStatusUnblacklist(Integer statusUnblacklist) {
		this.statusUnblacklist = statusUnblacklist;
	}

	public List<SegmentasiVendor> getSegmentasiVendor() {
		return segmentasiVendor;
	}

	public void setSegmentasiVendor(List<SegmentasiVendor> segmentasiVendor) {
		this.segmentasiVendor = segmentasiVendor;
	}

	public List<String> getParamDataTable() {
		return paramDataTable;
	}

	public void setParamDataTable(List<String> paramDataTable) {
		this.paramDataTable = paramDataTable;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	
	

}
