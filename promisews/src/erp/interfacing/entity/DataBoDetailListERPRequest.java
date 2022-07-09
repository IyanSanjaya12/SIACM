package erp.interfacing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class DataBoDetailListERPRequest {

	@SerializedName("itemCode")
	@JsonProperty("itemCode")
	private String itemCode;
	
	@SerializedName("itemID")
	@JsonProperty("itemID")
	private String itemID;
	
	@SerializedName("discount")
	@JsonProperty("discount")
	private String discount;
	
	@SerializedName("boID")
	@JsonProperty("boID")
	private String boID;
	
	@SerializedName("quantity")
	@JsonProperty("quantity")
	private String quantity;
	
	@SerializedName("uom")
	@JsonProperty("uom")
	private String uom;
	
	@SerializedName("currency")
	@JsonProperty("currency")
	private String currency;
	
	@SerializedName("boLineID")
	@JsonProperty("boLineID")
	private String boLineID;
	
	@SerializedName("price")
	@JsonProperty("price")
	private String price;
	
	@SerializedName("priceDiscount")
	@JsonProperty("priceDiscount")
	private String priceDiscount;
	
	@SerializedName("catalogItemID")
	@JsonProperty("catalogItemID")
	private String catalogItemID;
	
	@SerializedName("totalAmount")
	@JsonProperty("totalAmount")
	private String totalAmount;
	
	@SerializedName("creationDate")
	@JsonProperty("creationDate")
	private String creationDate;
	
	@SerializedName("lastUpdateDate")
	@JsonProperty("lastUpdateDate")
	private String lastUpdateDate;
	
	@SerializedName("itemDescription")
	@JsonProperty("itemDescription")
	private String itemDescription;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getBoID() {
		return boID;
	}

	public void setBoID(String boID) {
		this.boID = boID;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBoLineID() {
		return boLineID;
	}

	public void setBoLineID(String boLineID) {
		this.boLineID = boLineID;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPriceDiscount() {
		return priceDiscount;
	}

	public void setPriceDiscount(String priceDiscount) {
		this.priceDiscount = priceDiscount;
	}

	public String getCatalogItemID() {
		return catalogItemID;
	}

	public void setCatalogItemID(String catalogItemID) {
		this.catalogItemID = catalogItemID;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	
	
	
}
