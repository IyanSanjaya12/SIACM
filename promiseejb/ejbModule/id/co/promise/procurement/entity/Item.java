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

@Entity
@NamedQueries({
    @NamedQuery(name="Item.find", query="SELECT item FROM Item item WHERE item.isDelete = 0"),
    @NamedQuery(name="Item.getItemByType", query="SELECT item FROM Item item WHERE item.isDelete = 0 AND item.itemType.id =:itemTypeId"),
    @NamedQuery(name="Item.findByName", query="SELECT item FROM Item item WHERE item.isDelete = 0 AND UPPER(item.nama) like :byName"),
    @NamedQuery(name="Item.findByCode", query="SELECT item FROM Item item WHERE item.isDelete = 0 AND item.kode = :code"),
    @NamedQuery(name="Item.findByKode", query="SELECT item FROM Item item WHERE item.isDelete=0 AND item.id IN "
			+ "(SELECT ccd.item.id FROM CatalogContractDetail ccd WHERE ccd.catalogContract.vendor.id =:vendor "
			+ "AND ccd.catalogContract.tglAkhirKontrak > current_date) "
			+ "AND (UPPER(item.kode) like :kode OR UPPER(item.nama) like :kode)"),
    @NamedQuery(name="Item.findByKontrak", query="SELECT ccd.item FROM CatalogContractDetail ccd WHERE ccd.isDelete=0 "
			+ "AND ccd.catalogContract.vendor.id =:vendor "
			+ "AND ccd.catalogContract.id = :contractId "
			+ "AND ccd.catalogContract.tglAkhirKontrak > current_date"),
    @NamedQuery(name = "Item.findByItemKodeEbs", query = "SELECT item FROM Item item WHERE item.kode =:kode AND item.isDelete = 0")
})
@Table(name = "T2_ITEM")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_ITEM", initialValue = 1, allocationSize = 1)
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ITEM_ID")
	private Integer id;

	@Column(name = "NAMA", length = 255)
	private String nama;
	
	@Column(name = "ITEM_KODE_EBS", length = 200)
	private String kode;
	
	@Column(name = "DESKRIPSI", length = 512)
	private String deskripsi;
	
	@OneToOne
	@JoinColumn(name = "SATUAN_ID", referencedColumnName = "SATUAN_ID")
	private Satuan satuanId;

	@OneToOne
	@JoinColumn(name = "ITEM_TYPE_ID", referencedColumnName = "ITEM_TYPE_ID")
	private ItemType itemType;
	
	@OneToOne
	@JoinColumn(name = "ITEM_GROUP_ID", referencedColumnName = "ITEM_GROUP_ID")
	private ItemGroup itemGroupId;

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
	
	@Column(name="SOURCE", columnDefinition="varchar(255) default 'PROMISE'")
	private String source;
		
	@Column(name = "ITEM_ID_EBS", length = 255)
	private String itemIdEbs;
	
	public String getItemIdEbs() {
		return itemIdEbs;
	}

	public void setItemIdEbs(String itemIdEbs) {
		this.itemIdEbs = itemIdEbs;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
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

	public ItemGroup getItemGroupId() {
		return itemGroupId;
	}

	public void setItemGroupId(ItemGroup itemGroupId) {
		this.itemGroupId = itemGroupId;
	}

	public Satuan getSatuanId() {
		return satuanId;
	}

	public void setSatuanId(Satuan satuanId) {
		this.satuanId = satuanId;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	
}
