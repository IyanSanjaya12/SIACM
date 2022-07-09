package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@NamedQueries({
    @NamedQuery(name="ConditionalPrice.find", query="SELECT cp FROM ConditionalPrice cp WHERE cp.isDelete = 0"),
    @NamedQuery(name="ConditionalPrice.findConditionalPriceByTipe", query="SELECT cp FROM ConditionalPrice cp WHERE cp.isDelete = 0 AND cp.tipe=:tipe"),
    @NamedQuery(name="ConditionalPrice.findKode", query="SELECT conditionalPrice FROM ConditionalPrice conditionalPrice WHERE conditionalPrice.isDelete=0 and conditionalPrice.kode = :kode")
})

@Table(name = "T1_CONDITIONAL_PRICE")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_CONDITIONAL_PRICE", initialValue = 1, allocationSize = 1)
public class ConditionalPrice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CONDITIONAL_PRICE_ID")
	private Integer id;
	
	@Column(name = "KODE")
	private String kode;
	
	@Column(name = "NAMA")
	private String nama;
	
	@Column(name = "TIPE")
	private Integer tipe;
	
	@Column(name = "FUNGSI")
	private Integer fungsi;
	
	@Column(name = "IS_PERSENTAGE")
	private Integer isPersentage;
	
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
	
	@ColumnDefault( value = "0" )
	@Column(name="ISDELETE")
	private Integer isDelete;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Integer getTipe() {
		return tipe;
	}

	public void setTipe(Integer tipe) {
		this.tipe = tipe;
	}

	public Integer getFungsi() {
		return fungsi;
	}

	public void setFungsi(Integer fungsi) {
		this.fungsi = fungsi;
	}

	public Integer getIsPersentage() {
		return isPersentage;
	}

	public void setIsPersentage(Integer isPersentage) {
		this.isPersentage = isPersentage;
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
