package id.co.promise.procurement.inisialisasi;

import id.co.promise.procurement.entity.Pengadaan;

import java.io.Serializable;
import java.util.List;

public class PengadaanListPagination implements Serializable {

	private Integer jmlData;
	private Integer startIndex;
	private Integer endIndex;
	private List<Pengadaan> pengadaanList;

	public PengadaanListPagination(int jmlData, Integer startIndex,
			Integer endIndex, List<Pengadaan> pengadaanList) {
		this.jmlData = jmlData;
		this.pengadaanList = pengadaanList;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
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

	public List<Pengadaan> getPengadaanList() {
		return pengadaanList;
	}

	public void setPengadaanList(List<Pengadaan> pengadaanList) {
		this.pengadaanList = pengadaanList;
	}
}
