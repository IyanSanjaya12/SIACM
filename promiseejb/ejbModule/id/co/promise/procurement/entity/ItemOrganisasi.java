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
@Table(name = "T2_ITEM_ORGANISASI")
@NamedQueries({
	@NamedQuery(name="ItemOrganisasi.find",
			query="SELECT itemOrganisasi FROM ItemOrganisasi itemOrganisasi WHERE itemOrganisasi.isDelete = 0"),
	@NamedQuery(name="ItemOrganisasi.findByItemId",
    		query="SELECT itemOrganisasi FROM ItemOrganisasi itemOrganisasi WHERE itemOrganisasi.isDelete = 0 AND itemOrganisasi.item.id =:itemId and itemOrganisasi.organisasi.id =:organsiasiId "),
	@NamedQuery(name="ItemOrganisasi.findByItemCodeAndOrgCode",
			query="SELECT itemOrganisasi FROM ItemOrganisasi itemOrganisasi WHERE itemOrganisasi.isDelete = 0 AND itemOrganisasi.item.kode =:itemCode and itemOrganisasi.organisasi.code =:orgCode "),
	@NamedQuery(name="ItemOrganisasi.findByItemKode",
			query="SELECT itemOrganisasi FROM ItemOrganisasi itemOrganisasi WHERE itemOrganisasi.isDelete = 0 AND itemOrganisasi.item.kode =:itemKode"),
	@NamedQuery(name ="ItemOrganisasi.deleteItemOrganisasi", query ="UPDATE ItemOrganisasi itemOrganisasi set itemOrganisasi.isDelete = 1 WHERE itemOrganisasi.item.id =:itemId ")
	
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_ITEM_ORGANISASI", initialValue = 1, allocationSize = 1)
public class ItemOrganisasi {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ITEM_ORGANISASI_ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID")
	private Item item;
	
	@ManyToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName = "ORGANISASI_ID")
	private Organisasi organisasi;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
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
	
	@Column(name = "EXPENSE_ACCOUNT")
	private String expenseAccount;
	
	@Column(name = "EXPENSE_ACCOUNT_DESCRIPTION")
	private String expenseAccountDescription;

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

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
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

	public String getExpenseAccount() {
		return expenseAccount;
	}

	public void setExpenseAccount(String expenseAccount) {
		this.expenseAccount = expenseAccount;
	}

	public String getExpenseAccountDescription() {
		return expenseAccountDescription;
	}

	public void setExpenseAccountDescription(String expenseAccountDescription) {
		this.expenseAccountDescription = expenseAccountDescription;
	}
	
}
