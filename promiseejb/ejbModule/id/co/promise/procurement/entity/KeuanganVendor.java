package id.co.promise.procurement.entity;

import java.util.Date;

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

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T3_KEUANGAN_VENDOR")
@NamedQueries({
	@NamedQuery(name="KeuanganVendor.findByVendor",
        	query="SELECT keuanganVendor FROM KeuanganVendor keuanganVendor WHERE keuanganVendor.vendor.id =:vendorId AND keuanganVendor.isDelete=0"),
    @NamedQuery(name ="KeuanganVendor.deleteKeuanganVendor", 
    		query ="UPDATE KeuanganVendor keuanganVendor set keuanganVendor.isDelete = 1 WHERE keuanganVendor.vendor.id =:vendorId ")
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_KEUANGAN_VENDOR", initialValue = 1, allocationSize = 1)
public class KeuanganVendor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "KEUANGAN_VENDOR_ID")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	@Column(name = "NOMOR_AUDIT", length = 50)
	private String nomorAudit;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TANGGAL_AUDIT")
	private Date tanggalAudit;
	
	@Column(name = "NAMA_AUDIT", length = 50)
	private String namaAudit;
	
	@Column(name = "TAHUN_KEUANGAN", length = 4)
	private String tahunKeuangan;
	
	// aktiva / pasiva
	@ColumnDefault( value = "0" )
	@Column(name = "KAS")
	private Double kas;
	
	@ColumnDefault( value = "0" )
	@Column(name = "BANK")
	private Double bank;
	
	@ColumnDefault( value = "0" )
	@Column(name = "TOTAL_PIUTANG")
	private Double totalPiutang;
	
	@ColumnDefault( value = "0" )
	@Column(name = "PERSEDIAAN_BARANG")
	private Double persediaanBarang;
	
	@ColumnDefault( value = "0" )
	@Column(name = "PEKERJAAN_DALAM_PROSES")
	private Double pekerjaanDalamProses;
	
	@ColumnDefault( value = "0" )
	@Column(name = "TOTAL_AKTIVA_LANCAR")
	private Double totalAktivaLancar;
	
	@ColumnDefault( value = "0" )
	@Column(name = "PERALATAN_DAN_MESIN")
	private Double peralatanDanMesin;
	
	@ColumnDefault( value = "0" )
	@Column(name = "INVENTARIS")
	private Double inventaris;
	
	@ColumnDefault( value = "0" )
	@Column(name = "GEDUNG_GEDUNG")
	private Double gedungGedung;
	
	@ColumnDefault( value = "0" )
	@Column(name = "TOTAL_AKTIVA_TETAP")
	private Double totalAktivaTetap;
	
	@ColumnDefault( value = "0" )
	@Column(name = "AKTIVA_LAINNYA")
	private Double aktivaLainnya;
	
	@ColumnDefault( value = "0" )
	@Column(name = "TOTAL_AKTIVA")
	private Double totalAktiva;
	
	@ColumnDefault( value = "0" )
	@Column(name = "HUTANG_DAGANG")
	private Double hutangDagang;
	
	@ColumnDefault( value = "0" )
	@Column(name = "HUTANG_PAJAK")
	private Double hutangPajak;
	
	@ColumnDefault( value = "0" )
	@Column(name = "HUTANG_LAINNYA")
	private Double hutangLainnya;
	
	@ColumnDefault( value = "0" )
	@Column(name = "TOTAL_HUTANG_JANGKA_PENDEK")
	private Double totalHutangJangkaPendek;
	
	@ColumnDefault( value = "0" )
	@Column(name = "HUTANG_JANGKA_PANJANG")
	private Double hutangJangkaPanjang;
	
	@ColumnDefault( value = "0" )
	@Column(name = "KEKAYAAN_BERSIH")
	private Double kekayaanBersih;
	
	@ColumnDefault( value = "0" )
	@Column(name = "TOTAL_PASIVA")
	private Double totalPasiva;
	
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

	public String getNomorAudit() {
		return nomorAudit;
	}

	public void setNomorAudit(String nomorAudit) {
		this.nomorAudit = nomorAudit;
	}

	public Date getTanggalAudit() {
		return tanggalAudit;
	}

	public void setTanggalAudit(Date tanggalAudit) {
		this.tanggalAudit = tanggalAudit;
	}

	public String getNamaAudit() {
		return namaAudit;
	}

	public void setNamaAudit(String namaAudit) {
		this.namaAudit = namaAudit;
	}

	public String getTahunKeuangan() {
		return tahunKeuangan;
	}

	public void setTahunKeuangan(String tahunKeuangan) {
		this.tahunKeuangan = tahunKeuangan;
	}

	public Double getKas() {
		return kas;
	}

	public void setKas(Double kas) {
		this.kas = kas;
	}

	public Double getBank() {
		return bank;
	}

	public void setBank(Double bank) {
		this.bank = bank;
	}

	public Double getTotalPiutang() {
		return totalPiutang;
	}

	public void setTotalPiutang(Double totalPiutang) {
		this.totalPiutang = totalPiutang;
	}

	public Double getPersediaanBarang() {
		return persediaanBarang;
	}

	public void setPersediaanBarang(Double persediaanBarang) {
		this.persediaanBarang = persediaanBarang;
	}

	public Double getPekerjaanDalamProses() {
		return pekerjaanDalamProses;
	}

	public void setPekerjaanDalamProses(Double pekerjaanDalamProses) {
		this.pekerjaanDalamProses = pekerjaanDalamProses;
	}

	public Double getTotalAktivaLancar() {
		return totalAktivaLancar;
	}

	public void setTotalAktivaLancar(Double totalAktivaLancar) {
		this.totalAktivaLancar = totalAktivaLancar;
	}

	public Double getPeralatanDanMesin() {
		return peralatanDanMesin;
	}

	public void setPeralatanDanMesin(Double peralatanDanMesin) {
		this.peralatanDanMesin = peralatanDanMesin;
	}

	public Double getInventaris() {
		return inventaris;
	}

	public void setInventaris(Double inventaris) {
		this.inventaris = inventaris;
	}

	public Double getGedungGedung() {
		return gedungGedung;
	}

	public void setGedungGedung(Double gedungGedung) {
		this.gedungGedung = gedungGedung;
	}

	public Double getTotalAktivaTetap() {
		return totalAktivaTetap;
	}

	public void setTotalAktivaTetap(Double totalAktivaTetap) {
		this.totalAktivaTetap = totalAktivaTetap;
	}

	public Double getAktivaLainnya() {
		return aktivaLainnya;
	}

	public void setAktivaLainnya(Double aktivaLainnya) {
		this.aktivaLainnya = aktivaLainnya;
	}

	public Double getTotalAktiva() {
		return totalAktiva;
	}

	public void setTotalAktiva(Double totalAktiva) {
		this.totalAktiva = totalAktiva;
	}

	public Double getHutangDagang() {
		return hutangDagang;
	}

	public void setHutangDagang(Double hutangDagang) {
		this.hutangDagang = hutangDagang;
	}

	public Double getHutangPajak() {
		return hutangPajak;
	}

	public void setHutangPajak(Double hutangPajak) {
		this.hutangPajak = hutangPajak;
	}

	public Double getHutangLainnya() {
		return hutangLainnya;
	}

	public void setHutangLainnya(Double hutangLainnya) {
		this.hutangLainnya = hutangLainnya;
	}

	public Double getTotalHutangJangkaPendek() {
		return totalHutangJangkaPendek;
	}

	public void setTotalHutangJangkaPendek(Double totalHutangJangkaPendek) {
		this.totalHutangJangkaPendek = totalHutangJangkaPendek;
	}

	public Double getHutangJangkaPanjang() {
		return hutangJangkaPanjang;
	}

	public void setHutangJangkaPanjang(Double hutangJangkaPanjang) {
		this.hutangJangkaPanjang = hutangJangkaPanjang;
	}

	public Double getKekayaanBersih() {
		return kekayaanBersih;
	}

	public void setKekayaanBersih(Double kekayaanBersih) {
		this.kekayaanBersih = kekayaanBersih;
	}

	public Double getTotalPasiva() {
		return totalPasiva;
	}

	public void setTotalPasiva(Double totalPasiva) {
		this.totalPasiva = totalPasiva;
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
