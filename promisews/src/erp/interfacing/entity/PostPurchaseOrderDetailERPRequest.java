package erp.interfacing.entity;

import com.google.gson.annotations.SerializedName;

public class PostPurchaseOrderDetailERPRequest {
	
	@SerializedName("requisitionNumber")
	private String requisitionNumber;
	
	@SerializedName("requisitionLineID")
	private String requisitionLineID;
	
	@SerializedName("poID")
	private String poID;
	
	@SerializedName("itemCode")
	private String itemCode;
	
	@SerializedName("itemID")
	private String itemID;
	
	@SerializedName("quantity")
	private String quantity;
	
	@SerializedName("price")
	private String price;
	
	@SerializedName("priceDiscount")
	private String priceDiscount;
	
	@SerializedName("currency")
	private String currency;

	@SerializedName("totalAmount")
	private String totalAmount;
	
	@SerializedName("uom")
	private String uom;
	
	@SerializedName("catalogItemID")
	private String catalogItemID;
	
	@SerializedName("discount")
	private String discount;
	
	@SerializedName("poLineID")
	private String poLineID;

	public String getRequisitionNumber() {
		return requisitionNumber;
	}

	public String getRequisitionLineID() {
		return requisitionLineID;
	}

	public String getPoID() {
		return poID;
	}

	public String getItemCode() {
		return itemCode;
	}

	public String getItemID() {
		return itemID;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getPrice() {
		return price;
	}

	public String getPriceDiscount() {
		return priceDiscount;
	}

	public String getCurrency() {
		return currency;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public String getUom() {
		return uom;
	}

	public String getCatalogItemID() {
		return catalogItemID;
	}

	public String getDiscount() {
		return discount;
	}

	public String getPoLineID() {
		return poLineID;
	}

	public void setRequisitionNumber(String requisitionNumber) {
		this.requisitionNumber = requisitionNumber;
	}

	public void setRequisitionLineID(String requisitionLineID) {
		this.requisitionLineID = requisitionLineID;
	}

	public void setPoID(String poID) {
		this.poID = poID;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setPriceDiscount(String priceDiscount) {
		this.priceDiscount = priceDiscount;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public void setCatalogItemID(String catalogItemID) {
		this.catalogItemID = catalogItemID;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public void setPoLineID(String poLineID) {
		this.poLineID = poLineID;
	}
	
}
