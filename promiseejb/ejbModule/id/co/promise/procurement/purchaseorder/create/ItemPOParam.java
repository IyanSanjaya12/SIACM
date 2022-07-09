package id.co.promise.procurement.purchaseorder.create;


public class ItemPOParam {
	private Integer pritemid;
	private String name;
	private Double quantity;
	private Double price;
	private String costcenter;
	private String unit;
	private String matauang;
	private Double shipquantity;
	private Boolean selected;

	public Integer getPritemid() {
		return pritemid;
	}

	public void setPritemid(Integer pritemid) {
		this.pritemid = pritemid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getCostcenter() {
		return costcenter;
	}

	public void setCostcenter(String costcenter) {
		this.costcenter = costcenter;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMatauang() {
		return matauang;
	}

	public void setMatauang(String matauang) {
		this.matauang = matauang;
	}

	public Double getShipquantity() {
		return shipquantity;
	}

	public void setShipquantity(Double shipquantity) {
		this.shipquantity = shipquantity;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

}
