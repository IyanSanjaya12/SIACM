package id.co.promise.procurement.DTO;


public class BookingOrderItemDTO {

	Integer catalogId ;
	Double qty;
	// KAI - 2021/01/08 - [21483]
	String gLAccount;
	// KAI - 20210204 - #20867
	Integer addressBookId;
	
	Double ongkosKirim;
	
	Double hargaWithDiscount;
	
	Double discount;
	
	Integer slaDeliveryTime;
	
	public Integer getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public String getgLAccount() {
		return gLAccount;
	}
	public void setgLAccount(String gLAccount) {
		this.gLAccount = gLAccount;
	}
	public Integer getAddressBookId() {
		return addressBookId;
	}
	public void setAddressBookId(Integer addressBookId) {
		this.addressBookId = addressBookId;
	}
	public Double getOngkosKirim() {
		return ongkosKirim;
	}
	public void setOngkosKirim(Double ongkosKirim) {
		this.ongkosKirim = ongkosKirim;
	}
	public Double getHargaWithDiscount() {
		return hargaWithDiscount;
	}
	public void setHargaWithDiscount(Double hargaWithDiscount) {
		this.hargaWithDiscount = hargaWithDiscount;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Integer getSlaDeliveryTime() {
		return slaDeliveryTime;
	}
	public void setSlaDeliveryTime(Integer slaDeliveryTime) {
		this.slaDeliveryTime = slaDeliveryTime;
	}
	
	
}
