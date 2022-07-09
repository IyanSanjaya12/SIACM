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
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

import id.co.promise.procurement.entity.Organisasi;

@Entity
@Table(name="T4_CATALOG_FEE_HISTORY")

@NamedQueries({
	@NamedQuery(name="CatalogFeeHistory.getByVersion", query="SELECT catFeeHistory FROM CatalogFeeHistory catFeeHistory "
			+ "WHERE catFeeHistory.isDelete = 0 AND "
			+ "catFeeHistory.catalogFee.id = :catalogFeeId "
			+ "ORDER BY catFeeHistory.perubahanVersi DESC ")
})
public class CatalogFeeHistory implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_FEE_HISTORY_ID")
	private Integer id;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="CATALOG_ID")
	private Catalog catalog;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="CATALOG_FEE_ID",nullable = true)
	private CatalogFee catalogFee;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name="ORGANISASI_ID")
	private Organisasi organisasi;
	
	@Column(name="ONGKOS_KIRIM")
	private Integer ongkosKirim;
	
	@Column(name="QUANTITY_BATCH")
	private Integer quantityBatch;
	
	@ColumnDefault( value = "0" )
	@Column(name="ASURANSI", nullable = true)
	private Double asuransi;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true, name = "DELETED")
	private Date deleted;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED",nullable=true)
	private Date updated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@Column(name="ISDELETE")
	private Integer isDelete;
	
	@Column(name="USERID")
	private Integer userId;
	
	@Column(name="SLA_DELIVERY_TIME")
	private Integer slaDeliveryTime;
	
	@Column(name = "PERUBAHAN_VERSI")
	private Integer perubahanVersi;
	
	@Column(name = "ACTION")
	private String action;
	
	@Transient
	private Boolean isOpenForm;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
	}

	public Integer getOngkosKirim() {
		return ongkosKirim;
	}

	public void setOngkosKirim(Integer ongkosKirim) {
		this.ongkosKirim = ongkosKirim;
	}

	public Double getAsuransi() {
		return asuransi;
	}

	public void setAsuransi(Double asuransi) {
		this.asuransi = asuransi;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getQuantityBatch() {
		return quantityBatch;
	}

	public void setQuantityBatch(Integer quantityBatch) {
		this.quantityBatch = quantityBatch;
	}

	public Boolean getIsOpenForm() {
		return isOpenForm;
	}

	public void setIsOpenForm(Boolean isOpenForm) {
		this.isOpenForm = isOpenForm;
	}

	public Integer getSlaDeliveryTime() {
		return slaDeliveryTime;
	}

	public void setSlaDeliveryTime(Integer slaDeliveryTime) {
		this.slaDeliveryTime = slaDeliveryTime;
	}

	public CatalogFee getCatalogFee() {
		return catalogFee;
	}

	public void setCatalogFee(CatalogFee catalogFee) {
		this.catalogFee = catalogFee;
	}

	public Integer getPerubahanVersi() {
		return perubahanVersi;
	}

	public void setPerubahanVersi(Integer perubahanVersi) {
		this.perubahanVersi = perubahanVersi;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
	
	
}
