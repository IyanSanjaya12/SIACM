package id.co.promise.procurement.entity.siacm;

import java.util.List;

public class PenjualanDTO {
	
	private Penjualan penjualan;
	
	private List<PenjualanDetail> penjualanDetailList;
	
	private PenjualanDetail penjualanDetail;
	
	public Penjualan getPenjualan() {
		return penjualan;
	}
	
	public void setPenjualan(Penjualan penjualan) {
		this.penjualan = penjualan;
	}
	
	public List<PenjualanDetail> getPenjualanDetailList() {
		return penjualanDetailList;
	}
	
	public void setPenjualanDetailList(List<PenjualanDetail> penjualanDetailList) {
		this.penjualanDetailList = penjualanDetailList;
	}
	
	public PenjualanDetail getPenjualanDetail() {
		return penjualanDetail;
	}
	
	public void setPenjualanDetail(PenjualanDetail penjualanDetail) {
		this.penjualanDetail = penjualanDetail;
	}
	
	
}
