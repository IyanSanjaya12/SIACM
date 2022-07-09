package id.co.promise.procurement.DTO;

import id.co.promise.procurement.entity.BlacklistVendor;
import id.co.promise.procurement.entity.UnblacklistVendor;
import id.co.promise.procurement.entity.UnblacklistVendorFile;

import java.util.List;

public class UnblacklistVendorDtl {
	
	private BlacklistVendor blacklistVendor;
	
	private UnblacklistVendor unblacklistVendor;
	
	private List<UnblacklistVendorFile> unblacklistVendorFileList;

	public BlacklistVendor getBlacklistVendor() {
		return blacklistVendor;
	}

	public void setBlacklistVendor(BlacklistVendor blacklistVendor) {
		this.blacklistVendor = blacklistVendor;
	}

	public UnblacklistVendor getUnblacklistVendor() {
		return unblacklistVendor;
	}

	public void setUnblacklistVendor(UnblacklistVendor unblacklistVendor) {
		this.unblacklistVendor = unblacklistVendor;
	}

	public List<UnblacklistVendorFile> getUnblacklistVendorFileList() {
		return unblacklistVendorFileList;
	}

	public void setUnblacklistVendorFileList(List<UnblacklistVendorFile> unblacklistVendorFileList) {
		this.unblacklistVendorFileList = unblacklistVendorFileList;
	}
	
	

}
