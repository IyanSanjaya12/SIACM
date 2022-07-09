package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T3_PERALATAN_VENDOR")
@NamedQueries({
	@NamedQuery(name="PeralatanVendor.findByVendor",
        	query="SELECT peralatanVendor FROM PeralatanVendor peralatanVendor WHERE peralatanVendor.vendor.id =:vendorId AND peralatanVendor.isDelete=0"),
    @NamedQuery(name ="PeralatanVendor.deletePeralatanVendor", 
    		query ="UPDATE PeralatanVendor peralatanVendor set peralatanVendor.isDelete = 1 WHERE peralatanVendor.vendor.id =:vendorId ")
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_PERALATAN_VENDOR", initialValue = 1, allocationSize = 1)
public class PeralatanVendor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PERALATAN_VENDOR_ID")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	@Column(name = "JENIS", length = 100)
	private String jenis;
	
	@ColumnDefault( value = "0" )
	@Column(name = "JUMLAH")
	private Double jumlah;
	
	@ColumnDefault( value = "0" )
	@Column(name = "KAPASITAS")
	private Double kapasitas;
	
	@Column(name = "MERK_TYPE", length = 50)
	private String merk;
	
	@Column(name = "TAHUN_PEMBUATAN", length = 4)
	private String tahunPembuatan;
	
	@ManyToOne
	@JoinColumn(name = "KONDISI_PERALATAN_VENDOR_ID", referencedColumnName = "KONDISI_PERALATAN_VENDOR_ID")
	private KondisiPeralatanVendor kondisi;
	
	@ManyToOne
	@JoinColumn(name = "BUKTI_KEPEMILIKAN_ID", referencedColumnName = "BUKTI_KEPEMILIKAN_ID")
	private BuktiKepemilikan buktiKepemilikan;
	
	@Column(name = "LOKASI", length = 100)
	private String lokasi;

	@Column(name = "FILE_NAME_BUKTI_KEPEMILIKAN", length = 450)
	private String fileNameBuktiKepemilikan;
	
	@Column(name = "FILE_SIZE_BUKTI_KEPEMILIKAN")
	private Long fileSizeBuktiKepemilikan;

	@Column(name = "PATH_FILE_BUKTI_KEPEMILIKAN", length = 450)
	private String pathFileBuktiKepemilikan;

	@Column(name = "FILE_NAME_GAMBAR_PERALATAN", length = 450)
	private String fileNameGambarPeralatan;
	
	@Column(name = "FILE_SIZE_GAMBAR_PERALATAN")
	private Long fileSizeGambarPeralatan;

	@Column(name = "PATH_FILE_GAMBAR_PERALATAN", length = 450)
	private String pathFileGambarPeralatan;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED")
	private Date deleted;

	@Column(name = "USERID")
	private Integer userId;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getJenis() {
		return jenis;
	}

	public void setJenis(String jenis) {
		this.jenis = jenis;
	}

	public Double getJumlah() {
		return jumlah;
	}

	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}

	public Double getKapasitas() {
		return kapasitas;
	}

	public void setKapasitas(Double kapasitas) {
		this.kapasitas = kapasitas;
	}

	public String getMerk() {
		return merk;
	}

	public void setMerk(String merk) {
		this.merk = merk;
	}

	public String getTahunPembuatan() {
		return tahunPembuatan;
	}

	public void setTahunPembuatan(String tahunPembuatan) {
		this.tahunPembuatan = tahunPembuatan;
	}

	public KondisiPeralatanVendor getKondisi() {
		return kondisi;
	}

	public void setKondisi(KondisiPeralatanVendor kondisi) {
		this.kondisi = kondisi;
	}

	public BuktiKepemilikan getBuktiKepemilikan() {
		return buktiKepemilikan;
	}

	public void setBuktiKepemilikan(BuktiKepemilikan buktiKepemilikan) {
		this.buktiKepemilikan = buktiKepemilikan;
	}

	public String getLokasi() {
		return lokasi;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public String getFileNameBuktiKepemilikan() {
		return fileNameBuktiKepemilikan;
	}

	public void setFileNameBuktiKepemilikan(String fileNameBuktiKepemilikan) {
		this.fileNameBuktiKepemilikan = fileNameBuktiKepemilikan;
	}

	public Long getFileSizeBuktiKepemilikan() {
		return fileSizeBuktiKepemilikan;
	}

	public void setFileSizeBuktiKepemilikan(Long fileSizeBuktiKepemilikan) {
		this.fileSizeBuktiKepemilikan = fileSizeBuktiKepemilikan;
	}

	public String getPathFileBuktiKepemilikan() {
		return pathFileBuktiKepemilikan;
	}

	public void setPathFileBuktiKepemilikan(String pathFileBuktiKepemilikan) {
		this.pathFileBuktiKepemilikan = pathFileBuktiKepemilikan;
	}

	public String getFileNameGambarPeralatan() {
		return fileNameGambarPeralatan;
	}

	public void setFileNameGambarPeralatan(String fileNameGambarPeralatan) {
		this.fileNameGambarPeralatan = fileNameGambarPeralatan;
	}

	public Long getFileSizeGambarPeralatan() {
		return fileSizeGambarPeralatan;
	}

	public void setFileSizeGambarPeralatan(Long fileSizeGambarPeralatan) {
		this.fileSizeGambarPeralatan = fileSizeGambarPeralatan;
	}

	public String getPathFileGambarPeralatan() {
		return pathFileGambarPeralatan;
	}

	public void setPathFileGambarPeralatan(String pathFileGambarPeralatan) {
		this.pathFileGambarPeralatan = pathFileGambarPeralatan;
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
}
