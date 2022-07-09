/**
 * fdf
 */
package id.co.promise.procurement.DTO;

public class PurchaseOrderItemInterface {

	private Integer id;

	private Integer purchaseRequestItemId;

	private Double termQuantity;
	
	private Double unitPrice;
	
	private Double totalUnitPrices;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPurchaseRequestItemId() {
		return purchaseRequestItemId;
	}

	public void setPurchaseRequestItemId(Integer purchaseRequestItemId) {
		this.purchaseRequestItemId = purchaseRequestItemId;
	}

	public Double getTermQuantity() {
		return termQuantity;
	}

	public void setTermQuantity(Double termQuantity) {
		this.termQuantity = termQuantity;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getTotalUnitPrices() {
		return totalUnitPrices;
	}

	public void setTotalUnitPrices(Double totalUnitPrices) {
		this.totalUnitPrices = totalUnitPrices;
	}

}
