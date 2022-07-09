package id.co.promise.procurement.DTO;

import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorApproval;


public class ApprovalBlacklistVendorDTO {
	
	private Vendor vendor;
		
	private VendorApproval vendorApproval;
	
	private ApprovalProcess approvalProcess;
	
	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public VendorApproval getVendorApproval() {
		return vendorApproval;
	}

	public void setVendorApproval(VendorApproval vendorApproval) {
		this.vendorApproval = vendorApproval;
	}

	public ApprovalProcess getApprovalProcess() {
		return approvalProcess;
	}

	public void setApprovalProcess(ApprovalProcess approvalProcess) {
		this.approvalProcess = approvalProcess;
	}
	
	
}
