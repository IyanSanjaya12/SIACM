package id.co.promise.procurement.DTO;

import java.util.List;

import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.DokumenRegistrasiVendor;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorApproval;
import id.co.promise.procurement.entity.VendorSKD;

public class ApprovalCalonVendorDTO {

private Vendor vendor;
	
	private ApprovalProcess approvalProcess;
	
	private List<DokumenRegistrasiVendor> dokumenRegistrasiVendorList;
	
	private VendorApproval vendorApproval;
	
	private List<VendorSKD> vendorSkd;

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	
	public ApprovalProcess getApprovalProcess() {
		return approvalProcess;
	}

	public void setApprovalProcess(ApprovalProcess approvalProcess) {
		this.approvalProcess = approvalProcess;
	}

	public List<DokumenRegistrasiVendor> getDokumenRegistrasiVendorList() {
		return dokumenRegistrasiVendorList;
	}

	public void setDokumenRegistrasiVendorList(
			List<DokumenRegistrasiVendor> dokumenRegistrasiVendorList) {
		this.dokumenRegistrasiVendorList = dokumenRegistrasiVendorList;
	}

	public VendorApproval getVendorApproval() {
		return vendorApproval;
	}

	public void setVendorApproval(VendorApproval vendorApproval) {
		this.vendorApproval = vendorApproval;
	}

	public List<VendorSKD> getVendorSkd() {
		return vendorSkd;
	}

	public void setVendorSkd(List<VendorSKD> vendorSkd) {
		this.vendorSkd = vendorSkd;
	}

	
}
