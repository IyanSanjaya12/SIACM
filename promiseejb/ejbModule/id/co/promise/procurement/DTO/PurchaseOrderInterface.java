/**
 * fdf
 */
package id.co.promise.procurement.DTO;


/**
 * @author User
 *
 */
public class PurchaseOrderInterface {

	private Integer id;

	private String poNumber;

	private Integer purchaseRequestId;

	private Integer pengadaanId;

	private Integer kontrakFk;

	private Integer terminFk;

	private Integer syncStatus;

	private Integer vendorPk;

	private Integer termMasterPk;

	private Integer afcoPk;
	
	private Double subTotal;
	
	private Double totalCost;

	private String currency;

	private Integer currencyPk;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public Integer getPurchaseRequestId() {
		return purchaseRequestId;
	}

	public void setPurchaseRequestId(Integer purchaseRequestId) {
		this.purchaseRequestId = purchaseRequestId;
	}

	public Integer getPengadaanId() {
		return pengadaanId;
	}

	public void setPengadaanId(Integer pengadaanId) {
		this.pengadaanId = pengadaanId;
	}

	public Integer getKontrakFk() {
		return kontrakFk;
	}

	public void setKontrakFk(Integer kontrakFk) {
		this.kontrakFk = kontrakFk;
	}

	public Integer getTerminFk() {
		return terminFk;
	}

	public void setTerminFk(Integer terminFk) {
		this.terminFk = terminFk;
	}

	public Integer getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
	}

	public Integer getVendorPk() {
		return vendorPk;
	}

	public void setVendorPk(Integer vendorPk) {
		this.vendorPk = vendorPk;
	}

	public Integer getTermMasterPk() {
		return termMasterPk;
	}

	public void setTermMasterPk(Integer termMasterPk) {
		this.termMasterPk = termMasterPk;
	}

	public Integer getAfcoPk() {
		return afcoPk;
	}

	public void setAfcoPk(Integer afcoPk) {
		this.afcoPk = afcoPk;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getCurrencyPk() {
		return currencyPk;
	}

	public void setCurrencyPk(Integer currencyPk) {
		this.currencyPk = currencyPk;
	}

}