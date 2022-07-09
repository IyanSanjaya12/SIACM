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
@Table(name = "T5_INVOICE_PAYMENT")
@NamedQueries({
		@NamedQuery(name = "InvoicePayment.getCountByDate", query = " SELECT Count(o) FROM InvoicePayment o WHERE o.isDelete = 0 AND "
		+ " (o.created between :startDate and :endDate)"),
		@NamedQuery(name = "InvoicePayment.getCountByDateAndOrganizationId", query = " SELECT Count(o) FROM InvoicePayment o WHERE o.isDelete = 0 AND "
				+ " (o.created between :startDate and :endDate) And o.organizationCode IN (Select organisasi.code From Organisasi organisasi Where organisasi.id =:organizationId)"),
		@NamedQuery(name = "InvoicePayment.findByNumber", query = "SELECT invoicePayment FROM InvoicePayment invoicePayment WHERE invoicePayment.invoiceNumber = :number")})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_INVOICE_PAYMENT", initialValue = 1, allocationSize = 1)
public class InvoicePayment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "INVOICE_PK")
	private Integer id;
	
	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	@Column(name = "ORGANIZATION_CODE")
	private String organizationCode;

	@Column(name = "INVOICE_ID_EBS")
	private String invoiceIdEbs;

	@Column(name = "INVOICE_NUMBER")
	private String invoiceNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INVOICE_DATE")
	private Date invoiceDate;
	
	@Column(name = "INVOICE_AMOUNT")
	private Double invoiceAmount;

	@Column(name = "AMOUNT_PAID")
	private Double amountPaid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CANCELED_DATE")
	private Date canceledDate;

	@Column(name = "INVOICE_STATUS")	
	private String invoiceStatus;

	@Column(name = "PO_NUMBER")
	private String poNumber;

	@Column(name = "NOMOR_KONTRAK")
	private String nomorKontrak;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getInvoiceIdEbs() {
		return invoiceIdEbs;
	}

	public void setInvoiceIdEbs(String invoiceIdEbs) {
		this.invoiceIdEbs = invoiceIdEbs;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Date getCanceledDate() {
		return canceledDate;
	}

	public void setCanceledDate(Date canceledDate) {
		this.canceledDate = canceledDate;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getNomorKontrak() {
		return nomorKontrak;
	}

	public void setNomorKontrak(String nomorKontrak) {
		this.nomorKontrak = nomorKontrak;
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

}
