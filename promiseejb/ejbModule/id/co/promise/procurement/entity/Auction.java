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
@Table(name = "T5_AUCTION")
@NamedQueries({
	@NamedQuery(name="Auction.find",
			query="SELECT au FROM Auction au WHERE au.isDelete = 0"),
	@NamedQuery(name = "Auction.findAuctionByPengadaan", 
			query = "SELECT au FROM Auction au WHERE au.isDelete = 0 AND au.pengadaan.id=:pengadaanId ORDER BY au.id"),
	@NamedQuery(name = "Auction.findAuctionByPengadaanAndNoSesi", 
			query = "SELECT au FROM Auction au WHERE au.isDelete = 0 AND au.noSesi=:noSesi "
					+ "AND au.pengadaan.id=:pengadaanId"),
	@NamedQuery(name = "Auction.findAuctionByPengadaanAndItemPengadaan", 
			query = "SELECT au FROM Auction au WHERE au.isDelete = 0 AND au.itemPengadaan.id=:itemPengadaanId "
					+ "AND au.pengadaan.id=:pengadaanId"),
	@NamedQuery(name="Auction.findLastAuctionByPengadaan", query="SELECT au FROM Auction au WHERE au.isDelete = 0 AND au.pengadaan.id=:pengadaanId ORDER BY au.id DESC")
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_AUCTION", initialValue = 1, allocationSize = 1)
public class Auction {

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
	@JoinColumn(name = "ITEM_PENGADAAN_ID", referencedColumnName = "ITEM_PENGADAAN_ID", nullable=true)
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

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNoSesi() {
		return noSesi;
	}

	public void setNoSesi(String noSesi) {
		this.noSesi = noSesi;
	}

	public Date getWaktuAwal() {
		return waktuAwal;
	}

	public void setWaktuAwal(Date waktuAwal) {
		this.waktuAwal = waktuAwal;
	}

	public Date getWaktuAkhir() {
		return waktuAkhir;
	}

	public void setWaktuAkhir(Date waktuAkhir) {
		this.waktuAkhir = waktuAkhir;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Double getHargaAwal() {
		return hargaAwal;
	}

	public void setHargaAwal(Double hargaAwal) {
		this.hargaAwal = hargaAwal;
	}

	public Double getSelisihPenawaran() {
		return selisihPenawaran;
	}

	public void setSelisihPenawaran(Double selisihPenawaran) {
		this.selisihPenawaran = selisihPenawaran;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Pengadaan getPengadaan() {
		return pengadaan;
	}

	public void setPengadaan(Pengadaan pengadaan) {
		this.pengadaan = pengadaan;
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

	public ItemPengadaan getItemPengadaan() {
		return itemPengadaan;
	}

	public void setItemPengadaan(ItemPengadaan itemPengadaan) {
		this.itemPengadaan = itemPengadaan;
	}
}
