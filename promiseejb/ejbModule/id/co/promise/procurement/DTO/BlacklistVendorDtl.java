package id.co.promise.procurement.DTO;

import java.util.List;

import id.co.promise.procurement.entity.BlacklistVendor;
import id.co.promise.procurement.entity.BlacklistVendorDetail;
import id.co.promise.procurement.entity.BlacklistVendorFile;

public class BlacklistVendorDtl {

	private BlacklistVendor blacklistVendor;
	
	private List<BlacklistVendorDetail> blacklistVendorDetailList;
	
	private List<BlacklistVendorFile> blacklistVendorFileList;

	public BlacklistVendor getBlacklistVendor() {
		return blacklistVendor;
	}

	public void setBlacklistVendor(BlacklistVendor blacklistVendor) {
		this.blacklistVendor = blacklistVendor;
	}

	public List<BlacklistVendorDetail> getBlacklistVendorDetailList() {
		return blacklistVendorDetailList;
	}

	public void setBlacklistVendorDetailList(List<BlacklistVendorDetail> blacklistVendorDetailList) {
		this.blacklistVendorDetailList = blacklistVendorDetailList;
	}

	public List<BlacklistVendorFile> getBlacklistVendorFileList() {
		return blacklistVendorFileList;
	}

	public void setBlacklistVendorFileList(List<BlacklistVendorFile> blacklistVendorFileList) {
		this.blacklistVendorFileList = blacklistVendorFileList;
	}
	
	
	
}
