package id.co.promise.procurement.DTO;

import java.util.List;

public class FormBillingRequest {
	
	private BillingMemoRequest billingMemoRequest;
	
	private List<FinalContractItem> finalContractItemList;
	
	private List<FinalContractJasa> finalContractJasaList;

	public BillingMemoRequest getBillingMemoRequest() {
		return billingMemoRequest;
	}

	public void setBillingMemoRequest(BillingMemoRequest billingMemoRequest) {
		this.billingMemoRequest = billingMemoRequest;
	}

	public List<FinalContractItem> getFinalContractItemList() {
		return finalContractItemList;
	}

	public void setFinalContractItemList(List<FinalContractItem> finalContractItemList) {
		this.finalContractItemList = finalContractItemList;
	}

	public List<FinalContractJasa> getFinalContractJasaList() {
		return finalContractJasaList;
	}

	public void setFinalContractJasaList(List<FinalContractJasa> finalContractJasaList) {
		this.finalContractJasaList = finalContractJasaList;
	}
	
	
}
