package id.co.promise.procurement.DTO;

import java.util.List;

import id.co.promise.procurement.entity.BankVendor;
import id.co.promise.procurement.entity.BankVendorDraft;;

public class BankVendorDTO {
	
	private List<BankVendor> bankVendorList;
	
	private BankVendor bankVendor;
	
	private Integer vendorId;
	
	private List<String> namaNegara;
	
	private BankVendorDraft bankVendorDraft;
	
	private Integer status;

	public List<BankVendor> getBankVendorList() {
		return bankVendorList;
	}

	public void setBankVendorList(List<BankVendor> bankVendorList) {
		this.bankVendorList = bankVendorList;
	}

	public BankVendor getBankVendor() {
		return bankVendor;
	}

	public void setBankVendor(BankVendor bankVendor) {
		this.bankVendor = bankVendor;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public List<String> getNamaNegara() {
		return namaNegara;
	}

	public void setNamaNegara(List<String> namaNegara) {
		this.namaNegara = namaNegara;
	}

	public BankVendorDraft getBankVendorDraft() {
		return bankVendorDraft;
	}

	public void setBankVendorDraft(BankVendorDraft bankVendorDraft) {
		this.bankVendorDraft = bankVendorDraft;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	

}
