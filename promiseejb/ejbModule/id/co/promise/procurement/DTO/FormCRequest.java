package id.co.promise.procurement.DTO;

import java.util.List;

public class FormCRequest {
	
	private ContractRequest contractRequest;
	
	private ContractRequestDetail contractRequestDetail;
	
	private List<ContractRequestDetailMaterial> contractRequestDetailMaterial;
	
	//tambahan untuk dokumen pengadaan
	private List<ContractRequestDetailDok> contractRequestDetailDok;
	
	private ContractRequestDetailPenawaranVendor contractRequestDetailPenawaranVendor;

	public ContractRequest getContractRequest() {
		return contractRequest;
	}

	public void setContractRequest(ContractRequest contractRequest) {
		this.contractRequest = contractRequest;
	}

	public ContractRequestDetail getContractRequestDetail() {
		return contractRequestDetail;
	}

	public void setContractRequestDetail(ContractRequestDetail contractRequestDetail) {
		this.contractRequestDetail = contractRequestDetail;
	}

	public List<ContractRequestDetailMaterial> getContractRequestDetailMaterial() {
		return contractRequestDetailMaterial;
	}

	public void setContractRequestDetailMaterial(
			List<ContractRequestDetailMaterial> contractRequestDetailMaterial) {
		this.contractRequestDetailMaterial = contractRequestDetailMaterial;
	}

	public ContractRequestDetailPenawaranVendor getContractRequestDetailPenawaranVendor() {
		return contractRequestDetailPenawaranVendor;
	}

	public void setContractRequestDetailPenawaranVendor(
			ContractRequestDetailPenawaranVendor contractRequestDetailPenawaranVendor) {
		this.contractRequestDetailPenawaranVendor = contractRequestDetailPenawaranVendor;
	}

	public List<ContractRequestDetailDok> getContractRequestDetailDok() {
		return contractRequestDetailDok;
	}

	public void setContractRequestDetailDok(
			List<ContractRequestDetailDok> contractRequestDetailDok) {
		this.contractRequestDetailDok = contractRequestDetailDok;
	}	

}
