package id.co.promise.procurement.purchaseorder;

import id.co.promise.procurement.entity.PurchaseOrderItem;

import java.io.Serializable;
import java.util.Date;

public class PurchaseOrderItemDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer purchaseOrderId;
	private String vendorName;
	private String itemName;
	private Double quantityPurchaseRequest;
	private Double quantitySend;
	private Double unitPrice;
	private Double totalUnitPrices;
	private Date deliveryTime;
	private String status;
	private String unit;
	private Integer vendorId;

	public PurchaseOrderItemDTO(PurchaseOrderItem poi) {
		this.id = poi.getId();
		this.purchaseOrderId = poi.getPurchaseOrder().getId();
		this.vendorName = poi.getVendorName();
		if(poi.getItem() != null){
			this.itemName = poi.getItem().getNama();

			this.unit = poi.getItem().getSatuanId().getNama();
		}
		this.quantityPurchaseRequest = poi.getQuantityPurchaseRequest();
		this.quantitySend = poi.getQuantitySend();
		this.unitPrice = poi.getUnitPrice();
		this.totalUnitPrices = poi.getTotalUnitPrices();
		this.deliveryTime = poi.getDeliveryTime();
		this.status = poi.getStatus();
		if(poi.getVendor()!=null){
			this.vendorId = poi.getVendor().getId();
		}

	}

	public Integer getId() {
		return id;
	}

	public String getVendorName() {
		return vendorName;
	}

	public String getItemName() {
		return itemName;
	}

	public Double getQuantityPurchaseRequest() {
		return quantityPurchaseRequest;
	}

	public Double getQuantitySend() {
		return quantitySend;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public Double getTotalUnitPrices() {
		return totalUnitPrices;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public String getStatus() {
		return status;
	}

	public Integer getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public String getUnit() {
		return unit;
	}
	
	public Integer getVendorId(){
		return vendorId;
	}

}
