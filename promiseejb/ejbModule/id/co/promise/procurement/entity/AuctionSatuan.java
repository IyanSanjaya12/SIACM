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
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T5_AUCTION_SATUAN")
@NamedQueries({
	@NamedQuery(name="AuctionSatuan.find",
			query="SELECT au FROM AuctionSatuan au WHERE au.isDelete = 0"),
	@NamedQuery(name = "AuctionSatuan.findAuctionSatuanByPengadaan", 
			query = "SELECT au FROM AuctionSatuan au WHERE au.isDelete = 0 AND au.pengadaan.id=:pengadaanId"),
	@NamedQuery(name = "AuctionSatuan.findAuctionSatuanByPengadaanAndNoSesi", 
			query = "SELECT au FROM AuctionSatuan au WHERE au.isDelete = 0 AND au.noSesi=:noSesi "
					+ "AND au.pengadaan.id=:pengadaanId"),
	@NamedQuery(name = "AuctionSatuan.findAuctionSatuanByPengadaanAndItemPengadaan", 
			query = "SELECT au FROM AuctionSatuan au WHERE au.isDelete = 0 AND au.itemPengadaan.id=:itemPengadaanId "
					+ "AND au.pengadaan.id=:pengadaanId")
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_AUCTION_SATUAN", initialValue = 1, allocationSize = 1)
public class AuctionSatuan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AUCTION_ID")
	private Integer id;
	
	@Column(name = "NO_SESI", length=150)
	private String noSesi;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "WAKTU_AWAL")
	private Date waktuAwal;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "WAKTU_AKHIR")
	private Date waktuAkhir;
	
	@Column(name = "KETERANGAN", length=250)
	private String keterangan;
	
	@ColumnDefault( value = "0" )
	@Column(name = "HARGA_AWAL")
	private Double hargaAwal;
	
	@ColumnDefault( value = "0" )
	@Column(name = "SELISIH_PENAWARAN")
	private Double selisihPenawaran;
	
	@Column(name = "STATUS")
	private Integer status;
	
	@ManyToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")
	private Pengadaan pengadaan;
	
	@ManyToOne
	@JoinColumn(name = "ITEM_PENGADAAN_ID", referencedColumnName = "ITEM_PENGADAAN_ID")
	private ItemPengadaan itemPengadaan;
	
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

	public String getNoSesi() {
		return noSesi;
	}

	public Date getWaktuAwal() {
		return waktuAwal;
	}

	public Date getWaktuAkhir() {
		return waktuAkhir;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public Double getHargaAwal() {
		return hargaAwal;
	}

	public Double getSelisihPenawaran() {
		return selisihPenawaran;
	}

	public Integer getStatus() {
		return status;
	}

	public Pengadaan getPengadaan() {
		return pengadaan;
	}

	public ItemPengadaan getItemPengadaan() {
		return itemPengadaan;
	}

	public Date getCreated() {
		return created;
	}

	public Date getUpdated() {
		return updated;
	}

	public Date getDeleted() {
		return deleted;
	}

	public Integer getUserId() {
		return userId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNoSesi(String noSesi) {
		this.noSesi = noSesi;
	}

	public void setWaktuAwal(Date waktuAwal) {
		this.waktuAwal = waktuAwal;
	}

	public void setWaktuAkhir(Date waktuAkhir) {
		this.waktuAkhir = waktuAkhir;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public void setHargaAwal(Double hargaAwal) {
		this.hargaAwal = hargaAwal;
	}

	public void setSelisihPenawaran(Double selisihPenawaran) {
		this.selisihPenawaran = selisihPenawaran;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setPengadaan(Pengadaan pengadaan) {
		this.pengadaan = pengadaan;
	}

	public void setItemPengadaan(ItemPengadaan itemPengadaan) {
		this.itemPengadaan = itemPengadaan;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

}
