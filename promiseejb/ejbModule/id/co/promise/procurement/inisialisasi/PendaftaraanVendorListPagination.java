package id.co.promise.procurement.inisialisasi;

import id.co.promise.procurement.entity.PendaftaranVendor;
import id.co.promise.procurement.entity.Pengadaan;

import java.io.Serializable;
import java.util.List;

public class PendaftaraanVendorListPagination implements Serializable {

	private Integer userId;
	private Integer jmlData;
	private Integer startIndex;
	private Integer endIndex;
	private List<PendaftaranVendor> pendaftaranVendorList;
	
	public PendaftaraanVendorListPagination(Integer userId, int jmlData, Integer startIndex, Integer endIndex, List<PendaftaranVendor> pendaftaranVendorList) {
		this.userId = userId;
		this.jmlData = jmlData;
		this.pendaftaranVendorList = pendaftaranVendorList;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getJmlData() {
		return jmlData;
	}

	public void setJmlData(Integer jmlData) {
		this.jmlData = jmlData;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

	public List<PendaftaranVendor> getPendaftaranVendorList() {
		return pendaftaranVendorList;
	}

	public void setPendaftaranVendorList(
			List<PendaftaranVendor> pendaftaranVendorList) {
		this.pendaftaranVendorList = pendaftaranVendorList;
	}
	
}
