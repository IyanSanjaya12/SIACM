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
@NamedQueries({
		@NamedQuery(name = "PenawaranItem.find", query = "SELECT penawaranItem FROM PenawaranItem penawaranItem WHERE penawaranItem.isDelete = 0"),
		@NamedQuery(name = "PenawaranItem.findByVendorIdAndPengadaanId", query = "SELECT dpa FROM PenawaranItem dpa WHERE dpa.suratPenawaran.vendor.id=:vendorId AND dpa.suratPenawaran.pengadaan.id=:pengadaanId"),
		@NamedQuery(name = "PenawaranItem.findByItemIdAndPengadaanId", 
			query = "SELECT dpa FROM PenawaranItem dpa WHERE dpa.item.id=:itemId AND dpa.suratPenawaran.pengadaan.id=:pengadaanId")})
@Table(name = "T6_PENAWARAN_ITEM")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T6_PENAWARAN_ITEM", initialValue = 1, allocationSize = 1)
public class PenawaranItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PENAWARAN_ITEM_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "SURAT_PENAWARAN_ID", referencedColumnName = "SURAT_PENAWARAN_ID")
	private SuratPenawaran suratPenawaran;

	@OneToOne
	@JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID")
	private Item item;

	@ColumnDefault( value = "0" )
	@Column(name = "JUMLAH")
	private Double jumlah;

	@ColumnDefault( value = "0" )
	@Column(name = "HARGA_SATUAN")
	private Double hargaSatuan;

	@ColumnDefault( value = "0" )
	@Column(name = "HARGA_TOTAL")
	private Double hargaTotal;

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

	public SuratPenawaran getSuratPenawaran() {
		return suratPenawaran;
	}

	public void setSuratPenawaran(SuratPenawaran suratPenawaran) {
		this.suratPenawaran = suratPenawaran;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Double getJumlah() {
		return jumlah;
	}

	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}

	public Double getHargaSatuan() {
		return hargaSatuan;
	}

	public void setHargaSatuan(Double hargaSatuan) {
		this.hargaSatuan = hargaSatuan;
	}

	public Double getHargaTotal() {
		return hargaTotal;
	}

	public void setHargaTotal(Double hargaTotal) {
		this.hargaTotal = hargaTotal;
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
