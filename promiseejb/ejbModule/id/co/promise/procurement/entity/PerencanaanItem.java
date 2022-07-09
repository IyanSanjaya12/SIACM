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
@Table(name = "T3_PERENCANAAN_ITEM")
@NamedQueries({
		@NamedQuery(name = "PerencanaanItem.find", query = "SELECT x FROM PerencanaanItem x WHERE x.isDelete = 0"),
		@NamedQuery(name = "PerencanaanItem.findByPerencanaan", query = "SELECT x FROM PerencanaanItem x WHERE x.isDelete = 0 and x.perencanaan.id=:perencanaanId and x.item.itemType.id=:itemTypeId"),
		@NamedQuery(name = "PerencanaanItem.findByPerencanaanAndItem", query = "SELECT x FROM PerencanaanItem x WHERE x.isDelete = 0 and x.perencanaan.id=:perencanaanId and x.item.id=:itemId")})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_PERENCANAAN_ITEM", initialValue = 1, allocationSize = 1)
public class PerencanaanItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PERENCANAAN_ITEM_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "PERENCANAAN_ID", referencedColumnName = "PERENCANAAN_ID")
	private Perencanaan perencanaan;

	@OneToOne
	@JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID")
	private Item item;

	@Column(name = "JUMLAH")
	private Integer jumlah;

	@ColumnDefault( value = "0" )
	@Column(name = "NILAI")
	private Double nilai;
	
	@ColumnDefault( value = "0" )
	@Column(name = "SISA")
	private Double sisa;

	@OneToOne
	@JoinColumn(name = "MATA_UANG_ID", referencedColumnName = "MATA_UANG_ID")
	private MataUang mataUang;

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

	public Perencanaan getPerencanaan() {
		return perencanaan;
	}

	public void setPerencanaan(Perencanaan perencanaan) {
		this.perencanaan = perencanaan;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Integer getJumlah() {
		return jumlah;
	}

	public void setJumlah(Integer jumlah) {
		this.jumlah = jumlah;
	}

	public Double getNilai() {
		return nilai;
	}

	public void setNilai(Double nilai) {
		this.nilai = nilai;
	}

	public Double getSisa() {
		return sisa;
	}

	public void setSisa(Double sisa) {
		this.sisa = sisa;
	}

	public MataUang getMataUang() {
		return mataUang;
	}

	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
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
