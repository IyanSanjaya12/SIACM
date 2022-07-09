package erp.entity;

import com.google.gson.annotations.SerializedName;


public class PurchaseOrderDetailInterfacingConsume {
	
	@SerializedName("REQUISITION_NUMBER")
	private String requisitionNumber;
	
	@SerializedName("REQUISITION_LINE_ID")
	private String requisitionLineId;
	
	@SerializedName("PO_ID")
	private Integer poId;

	@SerializedName("ITEM_CODE")
	private String itemCode;
	
	@SerializedName("ITEM_ID")
	private Integer itemId;
	
	@SerializedName("QUANTITY")
	private Double quantity;
	
	@SerializedName("PRICE")
	private Double price;
	
	@SerializedName("PRICE_DISC")
	private Double priceDisc;
	
	@SerializedName("CURRENCY")
	private String currency;
	
	@SerializedName("TOTAL_AMOUNT")
	private Double totalAmount;
	
	@SerializedName("UOM")
	private String uom;
	
	@SerializedName("CATALOG_ITEM_ID")
	private Integer catalogItemId;
	
	@SerializedName("DISCOUNT")
	private Double discount;
	
	@SerializedName("PO_LINE_ID")
	private Integer poLineId;

	public Integer getPoLineId() {
		return poLineId;
	}

	public void setPoLineId(Integer poLineId) {
		this.poLineId = poLineId;
	}

	public String getRequisitionNumber() {
		return requisitionNumber;
	}

	public void setRequisitionNumber(String requisitionNumber) {
		this.requisitionNumber = requisitionNumber;
	}

	public String getRequisitionLineId() {
		return requisitionLineId;
	}

	public void setRequisitionLineId(String requisitionLineId) {
		this.requisitionLineId = requisitionLineId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getPriceDisc() {
		return priceDisc;
	}

	public void setPriceDisc(Double priceDisc) {
		this.priceDisc = priceDisc;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Integer getCatalogItemId() {
		return catalogItemId;
	}

	public void setCatalogItemId(Integer catalogItemId) {
		this.catalogItemId = catalogItemId;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}
}
