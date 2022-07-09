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

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T5_ITEM_PENGADAAN")
@NamedQueries({
		@NamedQuery(name = "ItemPengadaan.find", query = "SELECT itemPengadaan FROM ItemPengadaan itemPengadaan WHERE itemPengadaan.isDelete = 0"),

		@NamedQuery(name = "ItemPengadaan.findByPengadaanId", query = "SELECT itemPengadaan FROM ItemPengadaan itemPengadaan WHERE itemPengadaan.isDelete = 0 AND itemPengadaan.pengadaan.id = :pengadaanId"),
		@NamedQuery(name = "ItemPengadaan.findByPengadaanIdAndItemId", query = "SELECT itemPengadaan FROM ItemPengadaan itemPengadaan WHERE itemPengadaan.isDelete = 0 AND itemPengadaan.pengadaan.id = :pengadaanId AND itemPengadaan.item.id=:itemId"),

		@NamedQuery(name = "ItemPengadaan.findTipeItemByPengadaanId", query = "SELECT itemPengadaan FROM ItemPengadaan itemPengadaan WHERE itemPengadaan.isDelete = 0 AND itemPengadaan.pengadaan.id = :pengadaanId "
				+ "AND itemPengadaan.item.itemType.id = :itemTypeId"),

		@NamedQuery(name = "ItemPengadaan.getByPengadaan", query = "SELECT SUM(itemPengadaan.totalHPS) FROM ItemPengadaan itemPengadaan WHERE itemPengadaan.isDelete = 0 AND itemPengadaan.pengadaan.id=:pengadaanId"),

		@NamedQuery(name = "ItemPengadaan.findTotalNilaiHPSPengadanByOrganisasiId", query = "SELECT SUM(itemPengadaan.totalHPS) FROM ItemPengadaan itemPengadaan WHERE itemPengadaan.isDelete = 0 AND itemPengadaan.pengadaan.panitia.divisi.id IN (:divisiId)") })
public class ItemPengadaan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ITEM_PENGADAAN_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")
	private Pengadaan pengadaan;

	@ManyToOne
	@JoinColumn(name = "MATA_UANG_ID", referencedColumnName = "MATA_UANG_ID")
	private MataUang mataUang;

	@ColumnDefault( value = "0" )
	@Column(name = "KUANTITAS")
	private Double kuantitas;

	@ColumnDefault( value = "0" )
	@Column(name = "NILAI_HPS")
	private Double nilaiHPS;

	@ColumnDefault( value = "0" )
	@Column(name = "TOTAL_HPS")
	private Double totalHPS;

	@ManyToOne
	@JoinColumn(name = "JENIS_PAJAK_ID", referencedColumnName = "JENIS_PAJAK_ID", nullable = true)
	private JenisPajak jenisPajak;

	@ColumnDefault( value = "0" )
	@Column(name = "NILAI_PAJAK")
	private Double nilaiPajak;

	@ColumnDefault( value = "0" )
	@Column(name = "PROSENTASE_PAJAK")
	private Double prosentasePajak;

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

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Pengadaan getPengadaan() {
		return pengadaan;
	}

	public void setPengadaan(Pengadaan pengadaan) {
		this.pengadaan = pengadaan;
	}

	public MataUang getMataUang() {
		return mataUang;
	}

	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
	}

	public Double getKuantitas() {
		return kuantitas;
	}

	public void setKuantitas(Double kuantitas) {
		this.kuantitas = kuantitas;
	}

	public Double getNilaiHPS() {
		return nilaiHPS;
	}

	public void setNilaiHPS(Double nilaiHPS) {
		this.nilaiHPS = nilaiHPS;
	}

	public Double getTotalHPS() {
		return totalHPS;
	}

	public void setTotalHPS(Double totalHPS) {
		this.totalHPS = totalHPS;
	}

	public JenisPajak getJenisPajak() {
		return jenisPajak;
	}

	public void setJenisPajak(JenisPajak jenisPajak) {
		this.jenisPajak = jenisPajak;
	}

	public Double getNilaiPajak() {
		return nilaiPajak;
	}

	public void setNilaiPajak(Double nilaiPajak) {
		this.nilaiPajak = nilaiPajak;
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

	public Double getProsentasePajak() {
		return prosentasePajak;
	}

	public void setProsentasePajak(Double prosentasePajak) {
		this.prosentasePajak = prosentasePajak;
	}

}
