package id.co.promise.procurement.DTO;

import java.util.ArrayList;
import java.util.List;

import id.co.promise.procurement.entity.CostCenterSap;
import id.co.promise.procurement.entity.OrgApprovalPath;
import id.co.promise.procurement.entity.PurGroupSap;
import id.co.promise.procurement.entity.PurchOrg;
import id.co.promise.procurement.entity.StoreLocSap;
import id.co.promise.procurement.entity.Vendor;

public class BookingOrderDTO {

	private String boNumber;
	
	private String grandTotalWithQty;
	
	private ArrayList<Double> qtyList=new ArrayList<Double>();
	
	private ArrayList<Double> hargaNormalList=new ArrayList<Double>();
	
	private Double totalOngkir;
	
	private Double totalHarga;
	
	private List<BookingOrderItemDTO> bookingOrderItemDTOList;
	
	private ArrayList<Double> discountList=new ArrayList<Double>();
	
	private ArrayList<Double> asuransiList=new ArrayList<Double>();
	
	private ArrayList<Double> ongkirList=new ArrayList<Double>();
	
	private OrgApprovalPath orgApprovalPath;
	
	private String linkLampiranPr;
	
	private String linkLampiranKontrak;
	
	private Vendor vendor;
	
	private Integer slaDeliveryTime;
	
	private String puspelCode;
	
	private String gvDoctype;

	private String gvHeadnote;

	private String gvIntermsoft;
	
	private String gvAttachment;
	
	private CostCenterSap costCenter;
	
	private  PurGroupSap purGroup;
	
	private StoreLocSap storeLoc;
	
	private PurchOrg purchOrg;
	
	private String acctasscat;
	
	private String gvRequisitioner;
	
	private String gvTestRun;
	
	public String getAcctasscat() {
		return acctasscat;
	}
	public void setAcctasscat(String acctasscat) {
		this.acctasscat = acctasscat;
	}
	public Double getTotalHarga() {
		return totalHarga;
	}
	public void setTotalHarga(Double totalHarga) {
		this.totalHarga = totalHarga;
	}
	public String getBoNumber() {
		return boNumber;
	}
	public void setBoNumber(String boNumber) {
		this.boNumber = boNumber;
	}
	public String getGrandTotalWithQty() {
		return grandTotalWithQty;
	}
	public void setGrandTotalWithQty(String grandTotalWithQty) {
		this.grandTotalWithQty = grandTotalWithQty;
	}
	public List<BookingOrderItemDTO> getBookingOrderItemDTOList() {
		return bookingOrderItemDTOList;
	}
	public void setBookingOrderItemDTOList(List<BookingOrderItemDTO> bookingOrderItemDTOList) {
		this.bookingOrderItemDTOList = bookingOrderItemDTOList;
	}
		
	public ArrayList<Double> getQtyList() {
		return qtyList;
	}
	public void setQtyList(ArrayList<Double> qtyList) {
		this.qtyList = qtyList;
	}
	public ArrayList<Double> getHargaNormalList() {
		return hargaNormalList;
	}
	public void setHargaNormalList(ArrayList<Double> hargaNormalList) {
		this.hargaNormalList = hargaNormalList;
	}
	public Double getTotalOngkir() {
		return totalOngkir;
	}
	public void setTotalOngkir(Double totalOngkir) {
		this.totalOngkir = totalOngkir;
	}
	public ArrayList<Double> getDiscountList() {
		return discountList;
	}
	public void setDiscountList(ArrayList<Double> discountList) {
		this.discountList = discountList;
	}
	public ArrayList<Double> getAsuransiList() {
		return asuransiList;
	}
	public void setAsuransiList(ArrayList<Double> asuransiList) {
		this.asuransiList = asuransiList;
	}
	public ArrayList<Double> getOngkirList() {
		return ongkirList;
	}
	public void setOngkirList(ArrayList<Double> ongkirList) {
		this.ongkirList = ongkirList;
	}
	public OrgApprovalPath getOrgApprovalPath() {
		return orgApprovalPath;
	}
	public void setOrgApprovalPath(OrgApprovalPath orgApprovalPath) {
		this.orgApprovalPath = orgApprovalPath;
	}
	public String getLinkLampiranPr() {
		return linkLampiranPr;
	}
	public void setLinkLampiranPr(String linkLampiranPr) {
		this.linkLampiranPr = linkLampiranPr;
	}
	public String getLinkLampiranKontrak() {
		return linkLampiranKontrak;
	}
	public void setLinkLampiranKontrak(String linkLampiranKontrak) {
		this.linkLampiranKontrak = linkLampiranKontrak;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public Integer getSlaDeliveryTime() {
		return slaDeliveryTime;
	}
	public void setSlaDeliveryTime(Integer slaDeliveryTime) {
		this.slaDeliveryTime = slaDeliveryTime;
	}
	public String getPuspelCode() {
		return puspelCode;
	}
	public void setPuspelCode(String puspelCode) {
		this.puspelCode = puspelCode;
	}
	public String getGvDoctype() {
		return gvDoctype;
	}
	public void setGvDoctype(String gvDoctype) {
		this.gvDoctype = gvDoctype;
	}
	public String getGvHeadnote() {
		return gvHeadnote;
	}
	public void setGvHeadnote(String gvHeadnote) {
		this.gvHeadnote = gvHeadnote;
	}
	public String getGvIntermsoft() {
		return gvIntermsoft;
	}
	public void setGvIntermsoft(String gvIntermsoft) {
		this.gvIntermsoft = gvIntermsoft;
	}
	public String getGvAttachment() {
		return gvAttachment;
	}
	public void setGvAttachment(String gvAttachment) {
		this.gvAttachment = gvAttachment;
	}

	public CostCenterSap getCostCenter() {
		return costCenter;
	}
	public void setCostCenter(CostCenterSap costCenter) {
		this.costCenter = costCenter;
	}
	public PurGroupSap getPurGroup() {
		return purGroup;
	}
	public void setPurGroup(PurGroupSap purGroup) {
		this.purGroup = purGroup;
	}
	public StoreLocSap getStoreLoc() {
		return storeLoc;
	}
	public void setStoreLoc(StoreLocSap storeLoc) {
		this.storeLoc = storeLoc;
	}
	public String getGvRequisitioner() {
		return gvRequisitioner;
	}
	public void setGvRequisitioner(String gvRequisitioner) {
		this.gvRequisitioner = gvRequisitioner;
	}
	public String getGvTestRun() {
		return gvTestRun;
	}
	public void setGvTestRun(String gvTestRun) {
		this.gvTestRun = gvTestRun;
	}
	public PurchOrg getPurchOrg() {
		return purchOrg;
	}
	public void setPurchOrg(PurchOrg purchOrg) {
		this.purchOrg = purchOrg;
	}
	
	
}
