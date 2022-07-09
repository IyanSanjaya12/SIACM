package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.Organisasi;

import java.util.List;


public class ReportVendor {
	
	List<ReportVendorDTO> reportVendorDTOList;
	Long totalData;
	List<Organisasi> organisasiList;
	Organisasi afco;
	
	public List<ReportVendorDTO> getReportVendorDTOList() {
		return reportVendorDTOList;
	}
	public void setReportVendorDTOList(List<ReportVendorDTO> reportVendorDTOList) {
		this.reportVendorDTOList = reportVendorDTOList;
	}
	public Long getTotalData() {
		return totalData;
	}
	public void setTotalData(Long totalData) {
		this.totalData = totalData;
	}
	public List<Organisasi> getOrganisasiList() {
		return organisasiList;
	}
	public void setOrganisasiList(List<Organisasi> organisasiList) {
		this.organisasiList = organisasiList;
	}
	public Organisasi getAfco() {
		return afco;
	}
	public void setAfco(Organisasi afco) {
		this.afco = afco;
	}
}
