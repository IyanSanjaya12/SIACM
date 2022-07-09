package id.co.promise.procurement.purchaserequest.create;

import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.Item;

public class DetilCreateParam {
	private String itemname;
	private Double quantity;
	private String unit;
	private Double price;
	private String specification;
	private String pathfile;
	private Integer itemId;
	private Integer mataUangId;
	private Integer vendorId;
	private String kode;
	private String status;
	private Double total;
	private ShippingToPRParam[] shippingTo;

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getPathfile() {
		return pathfile;
	}

	public void setPathfile(String pathfile) {
		this.pathfile = pathfile;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getMataUangId() {
		return mataUangId;
	}

	public void setMataUangId(Integer mataUangId) {
		this.mataUangId = mataUangId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public ShippingToPRParam[] getShippingTo() {
		return shippingTo;
	}

	public void setShippingTo(ShippingToPRParam[] shippingTo) {
		this.shippingTo = shippingTo;
	}

}
