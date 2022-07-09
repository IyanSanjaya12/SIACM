package id.co.promise.procurement.vendor;

import java.util.List;

import id.co.promise.procurement.entity.Vendor;

public class VendorListDTO {

	private List<Vendor> vendorList;
	private Integer totalRow;

	public List<Vendor> getVendorList() {

		return vendorList;
	}

	public void setVendorList(List<Vendor> vendorList) {

		this.vendorList = vendorList;
	}

	
	public Integer getTotalRow() {
	
		return totalRow;
	}

	
	public void setTotalRow(Integer totalRow) {
	
		this.totalRow = totalRow;
	}

	

}
