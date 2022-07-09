package erp.entity;

import com.google.gson.annotations.SerializedName;

public class BookingOrderDetailInterfacingConsume {
	
	
	@SerializedName("BO_ID")
	private Integer boId;
	
	@SerializedName("BO_LINE_ID")
	private Integer  boItemId;

	@SerializedName("ITEM_CODE")
	private String itemCode;
	
	@SerializedName("ITEM_ID")
	private Integer itemId;
	
	@SerializedName("QUANTITY")
	private Double quantity;
	
	@SerializedName("VENDOR_NO")
	private String  vendorNo;
	
	@SerializedName("PRICE")
	private Double price;
	
	@SerializedName("PRICE_DISC")
	private Double priceDisc;
	
	@SerializedName("CURRENCY")
	private String kodeMataUang;
	
	@SerializedName("TOTAL_AMOUNT")
	private Double totalAmount;
	
	@SerializedName("UOM")
	private String uomCode;
	
	@SerializedName("CATALOG_ITEM_ID")
	private Integer catalogId;
	
	@SerializedName("DISCOUNT")
	private Double discount;
	
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getBoId() {
		return boId;
	}

	public void setBoId(Integer boId) {
		this.boId = boId;
	}

	public Integer getBoItemId() {
		return boItemId;
	}

	public void setBoItemId(Integer boItemId) {
		this.boItemId = boItemId;
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

	public String getVendorNo() {
		return vendorNo;
	}

	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
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

	public String getKodeMataUang() {
		return kodeMataUang;
	}

	public void setKodeMataUang(String kodeMataUang) {
		this.kodeMataUang = kodeMataUang;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getUomCode() {
		return uomCode;
	}

	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}

	public Integer getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	} 

}
