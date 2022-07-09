package id.co.promise.procurement.purchaseorder.create;

import java.util.Date;

public class PurchaseOrderItemCreatePOParamDTO {

	private Integer itemId;
	private Integer vendorId;
	private String itemName;
	private String vendorName;
	private Double quantitySend;
	private Double unitPrice;
	private Double totalUnitPrices;
	private Date deliveryTime;
	private Double quantityPurchaseRequest;
	private String status;
	private Integer pritemid;

	public Integer getPritemid() {
		return pritemid;
	}

	public void setPritemid(Integer pritemid) {
		this.pritemid = pritemid;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Double getQuantitySend() {
		return quantitySend;
	}

	public void setQuantitySend(Double quantitySend) {
		this.quantitySend = quantitySend;
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

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Double getQuantityPurchaseRequest() {
		return quantityPurchaseRequest;
	}

	public void setQuantityPurchaseRequest(Double quantityPurchaseRequest) {
		this.quantityPurchaseRequest = quantityPurchaseRequest;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
