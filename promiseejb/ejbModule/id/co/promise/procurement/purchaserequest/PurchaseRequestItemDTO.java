package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.master.ItemDTO;
import id.co.promise.procurement.master.MataUangDTO;
import id.co.promise.procurement.vendor.VendorDTO;

public class PurchaseRequestItemDTO {
	private Integer id;
	private PurchaseRequestDTO purchaserequestDTO;
	private VendorDTO vendorDTO;
	private ItemDTO itemDTO;
	private MataUangDTO mataUangDTO;
	private String itemname;
	private String vendorname;
	private Double quantity;
	private Double price;
	private String costcenter;
	private String status;
	private Double total;
	private String unit;
	private String specification;

	public PurchaseRequestItemDTO(PurchaseRequestItem pri) {
		this.id = pri.getId();
		this.purchaserequestDTO = new PurchaseRequestDTO(pri.getPurchaserequest());
		this.vendorDTO = new VendorDTO(pri.getVendor());
		if(pri.getItem() != null){
			this.itemDTO = new ItemDTO(pri.getItem());
		}
		this.mataUangDTO = new MataUangDTO(pri.getMataUang());
		this.itemname = pri.getItemname();
		this.vendorname = pri.getVendorname();
		this.quantity = pri.getQuantity();
		this.price = pri.getPrice();
		this.costcenter = pri.getCostcenter();
		this.status = pri.getStatus();
		this.total = pri.getTotal();
		this.unit = pri.getUnit();
		this.specification = pri.getSpecification();
	}

	public Integer getId() {
		return id;
	}

	public PurchaseRequestDTO getPurchaserequest() {
		return purchaserequestDTO;
	}

	public VendorDTO getVendor() {
		return vendorDTO;
	}

	public ItemDTO getItem() {
		return itemDTO;
	}

	public MataUangDTO getMataUang() {
		return mataUangDTO;
	}

	public String getItemname() {
		return itemname;
	}

	public String getVendorname() {
		return vendorname;
	}

	public Double getQuantity() {
		return quantity;
	}

	public Double getPrice() {
		return price;
	}

	public String getCostcenter() {
		return costcenter;
	}

	public String getStatus() {
		return status;
	}

	public Double getTotal() {
		return total;
	}

	public String getUnit() {
		return unit;
	}

	public String getSpecification() {
		return specification;
	}

}
