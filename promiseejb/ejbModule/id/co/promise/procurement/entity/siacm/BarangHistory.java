package id.co.promise.procurement.entity.siacm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TB_BARANG_HISTORY")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "TB_BARANG_HISTORY", initialValue = 1, allocationSize = 1)
public class BarangHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BARANG_HISTORY_ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "BARANG_ID", referencedColumnName = "BARANG_ID")
	private Barang barang;

	@Column(name = "NAMA")
	private String nama;
	
	@Column(name = "KODE")
	private String kode;
	
	@Column(name = "HARGA_BELI")
	private Double hargaBeli;
	
	@Column(name = "HARGA")
	private Double harga;
	
	@Column(name = "JUMLAH")
	private Double jumlah;

	@Column(name = "STOK_MINIMAL")
	private Double stokMinimal;
	
	@ManyToOne
	@JoinColumn(name = "MOBIL_ID", referencedColumnName = "MOBIL_ID")
	private Mobil mobil;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED")
	private Date updated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED")
	private Date deleted;
	
	@Column(name="USERID")
	private Integer userId;
	
	@Column(name="ISDELETE")
	private Integer isDelete;
	
	@Column(name="STATUS")
	private Integer status;
	
	@Column(name = "KETERANGAN")
	private String keterangan;
	
	@Column(name="STATUS_BARANG")
	private Integer statusBarang;
	
	public Barang getBarang() {
		return barang;
	}

	public void setBarang(Barang barang) {
		this.barang = barang;
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

	public Double getHarga() {
		return harga;
	}

	public void setHarga(Double harga) {
		this.harga = harga;
	}

	public Double getJumlah() {
		return jumlah;
	}

	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}

	public Double getStokMinimal() {
		return stokMinimal;
	}

	public void setStokMinimal(Double stokMinimal) {
		this.stokMinimal = stokMinimal;
	}

	public Mobil getMobil() {
		return mobil;
	}

	public void setMobil(Mobil mobil) {
		this.mobil = mobil;
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

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public Double getHargaBeli() {
		return hargaBeli;
	}

	public void setHargaBeli(Double hargaBeli) {
		this.hargaBeli = hargaBeli;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Integer getStatusBarang() {
		return statusBarang;
	}

	public void setStatusBarang(Integer statusBarang) {
		this.statusBarang = statusBarang;
	}
	
}
