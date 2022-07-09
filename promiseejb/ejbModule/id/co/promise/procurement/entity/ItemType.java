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

import id.co.promise.procurement.catalog.entity.CatalogKontrak;

@Entity
@Table(name = "T1_ITEM_TYPE")
@NamedQueries({ 
	@NamedQuery(name = "ItemType.find", query = "SELECT itemType FROM ItemType itemType WHERE itemType.isDelete = 0"),
	@NamedQuery(name="ItemType.findNama", query= "select itemType from ItemType itemType where itemType.isDelete=0 and itemType.nama = :nama")
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_ITEM_TYPE", initialValue = 1, allocationSize = 1)
public class ItemType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ITEM_TYPE_ID")
	private Integer id;

	@Column(name = "NAMA", length = 100)
	private String nama;

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
	
	@Column(name = "ITEM_TYPE_CODE_EBS")
	private String itemTypeCodeEbs;
	
	@Column(name = "ITEM_TYPE_NAME_EBS")
	private String itemTypeNameEbs;
	
	@Column(name = "SOURCE")
	private String source;

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

	public String getItemTypeCodeEbs() {
		return itemTypeCodeEbs;
	}

	public void setItemTypeCodeEbs(String itemTypeCodeEbs) {
		this.itemTypeCodeEbs = itemTypeCodeEbs;
	}

	public String getItemTypeNameEbs() {
		return itemTypeNameEbs;
	}

	public void setItemTypeNameEbs(String itemTypeNameEbs) {
		this.itemTypeNameEbs = itemTypeNameEbs;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
