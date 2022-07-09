package id.co.promise.procurement.catalog.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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

import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.MataUang;

@Entity
@NamedQueries({
	@NamedQuery(name="CatalogContractDetail.getByItem", query="SELECT ccd FROM CatalogContractDetail ccd where ccd.isDelete = :isDelete "
			+ "AND ccd.item = :item "),
	@NamedQuery(name="CatalogContractDetail.findByCatalogContractId", query="SELECT ccd FROM CatalogContractDetail ccd where ccd.isDelete = 0 "
			+ "AND ccd.catalogContract.id =:catalogContractId And ccd.id NOT IN (SELECT catalog.catalogContractDetail.id FROM Catalog catalog Where catalog.catalogKontrak.id=:catalogContractId AND catalog.vendor.id=:vendorId AND catalog.isDelete = 0 ) AND ccd.flagItemUsed = 0 "),
})
@Table(name = "T5_CATALOG_CONTRACT_DETAIL")
/*
 * @TableGenerator(name = "tableSequence", table = "PROMISE_SEQUENCE",
 * pkColumnName = "TABLE_SEQ_NAME", valueColumnName = "TABLE_SEQ_VALUE",
 * pkColumnValue = "T5_CATALOG_CONTRACT_DETAIL", initialValue = 1,
 * allocationSize = 1)
 */
public class CatalogContractDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_CONTRACT_DETAIL_ID")
	private Integer id;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "CATALOG_CONTRACT_ID")
	private CatalogKontrak catalogContract;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ITEM_ID")
	private Item item;

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

	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "MATA_UANG_ID")
	private MataUang mataUang;
	
	@Column(name = "ITEM_DESC")
	private String itemDesc;
	
	@Column(name = "UNIT_PRICE")
	private Double unitPrice;
	
	@Column(name = "FLAG_ITEM_USED")
	private Integer flagItemUsed;
	
	@Column(name = "QUANTITY")
	private Double quantity;
	
	@Column(name = "QTY_MAX_ORDER")
	private Double qtyMaxOrder;
	
	@Column(name = "ITEM_PLANT", length = 255)
	private String itemPlant;
	

	public Integer getFlagItemUsed() {
		return flagItemUsed;
	}

	public void setFlagItemUsed(Integer flagItemUsed) {
		this.flagItemUsed = flagItemUsed;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CatalogKontrak getCatalogContract() {
		return catalogContract;
	}

	public void setCatalogContract(CatalogKontrak catalogContract) {
		this.catalogContract = catalogContract;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
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

	public MataUang getMataUang() {
		return mataUang;
	}

	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getQtyMaxOrder() {
		return qtyMaxOrder;
	}

	public void setQtyMaxOrder(Double qtyMaxOrder) {
		this.qtyMaxOrder = qtyMaxOrder;
	}

	public String getItemPlant() {
		return itemPlant;
	}

	public void setItemPlant(String itemPlant) {
		this.itemPlant = itemPlant;
	}

}
