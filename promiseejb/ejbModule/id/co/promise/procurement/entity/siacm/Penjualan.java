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
@Table(name = "TB_PENJUALAN")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_PENJUALAN", initialValue = 1, allocationSize = 1)
public class Penjualan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PENJUALAN_ID")
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TANGGAL")
	private Date tanggal;
	
	@Column(name = "NO_FAKTUR")
	private String noFaktur;
	
	@Column(name = "TOTAL")
	private Double total;
	
	@ManyToOne
	@JoinColumn(name = "KARYAWAN_ID", referencedColumnName = "KARYAWAN_ID")
	private Karyawan karyawan;
	
	@ManyToOne
	@JoinColumn(name = "KEPALA_MEKANIK", referencedColumnName = "KARYAWAN_ID")
	private Karyawan kepalaMekanik;

	@ManyToOne
	@JoinColumn(name = "PERBANTUAN_ID", referencedColumnName = "KARYAWAN_ID")
	private Karyawan perbantuan;
	
	@ManyToOne
	@JoinColumn(name = "PELANGGAN_ID", referencedColumnName = "PELANGGAN_ID")
	private Pelanggan pelanggan;
	
	@ManyToOne
	@JoinColumn(name = "MOBIL_ID", referencedColumnName = "MOBIL_ID")
	private Mobil mobil;
	
	@Column(name = "PEMBAYARAN")
	private Double pembayaran;
	
	@Column(name = "TOTAL_PEMBAYARAN")
	private Double totalPembayaran;
	
	@Column(name = "DISKON")
	private Double diskon;
	
	@Column(name = "HARGA_SETELAH_DISKON")
	private Double hargaSetelahDiskon;
	
	@Column(name = "TOTAL_DISKON")
	private Double totalDiskon;
	
	@Column(name = "KEMBALIAN")
	private Double kembalian;
	
	@Column(name = "GARANSI")
	private Double garansi;
	
	@Column(name = "JENIS_GARANSI")
	private String jenisGaransi;
	
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
	
	
	public Double getTotalPembayaran() {
		return totalPembayaran;
	}

	public void setTotalPembayaran(Double totalPembayaran) {
		this.totalPembayaran = totalPembayaran;
	}

	public Double getPembayaran() {
		return pembayaran;
	}

	public void setPembayaran(Double pembayaran) {
		this.pembayaran = pembayaran;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getTanggal() {
		return tanggal;
	}

	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}

	public String getNoFaktur() {
		return noFaktur;
	}

	public void setNoFaktur(String noFaktur) {
		this.noFaktur = noFaktur;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Karyawan getKaryawan() {
		return karyawan;
	}

	public void setKaryawan(Karyawan karyawan) {
		this.karyawan = karyawan;
	}

	public Karyawan getPerbantuan() {
		return perbantuan;
	}

	public void setPerbantuan(Karyawan perbantuan) {
		this.perbantuan = perbantuan;
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

	public Pelanggan getPelanggan() {
		return pelanggan;
	}

	public void setPelanggan(Pelanggan pelanggan) {
		this.pelanggan = pelanggan;
	}

	public Mobil getMobil() {
		return mobil;
	}

	public void setMobil(Mobil mobil) {
		this.mobil = mobil;
	}

	public Double getDiskon() {
		return diskon;
	}

	public void setDiskon(Double diskon) {
		this.diskon = diskon;
	}

	public Karyawan getKepalaMekanik() {
		return kepalaMekanik;
	}

	public void setKepalaMekanik(Karyawan kepalaMekanik) {
		this.kepalaMekanik = kepalaMekanik;
	}

	public Double getHargaSetelahDiskon() {
		return hargaSetelahDiskon;
	}

	public void setHargaSetelahDiskon(Double hargaSetelahDiskon) {
		this.hargaSetelahDiskon = hargaSetelahDiskon;
	}

	public Double getTotalDiskon() {
		return totalDiskon;
	}

	public void setTotalDiskon(Double totalDiskon) {
		this.totalDiskon = totalDiskon;
	}

	public Double getKembalian() {
		return kembalian;
	}

	public void setKembalian(Double kembalian) {
		this.kembalian = kembalian;
	}

	public Double getGaransi() {
		return garansi;
	}

	public void setGaransi(Double garansi) {
		this.garansi = garansi;
	}

	public String getJenisGaransi() {
		return jenisGaransi;
	}

	public void setJenisGaransi(String jenisGaransi) {
		this.jenisGaransi = jenisGaransi;
	}
	
}
