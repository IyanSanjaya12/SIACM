package id.co.promise.procurement.purchaseorder;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import id.co.promise.procurement.entity.DeliveryReceivedFiles;
import id.co.promise.procurement.entity.PurchaseOrder;

public class DeliveryReceivedListPagination {

	private Integer poId;
	private Integer drId;
	private Integer orgId;
	private String poNumber;
	private String nama;
	private String vendorName;
	private Date updated;
	private Integer status;
	private String deliveryReceiptNum;
	private String deliveryOrderNum;
	
	private Integer poiId;
	private List<Double> dikirim;
	private Date dateReceived;
	private String description;
	private Integer slaDeliveryTime;
	private Date estimasi;
	private PurchaseOrder purchaseOrder;
	private Date deliveryOrderDate;
	
	@Transient
	private List<DeliveryReceivedDetailDTO> deliveryReceivedDetailDTO;
	
	@Transient
	private List<DeliveryReceivedFiles> deliveryReceivedFiles;
	
	public Integer getPoId() {
		return poId;
	}
	public void setPoId(Integer poId) {
		this.poId = poId;
	}
	public Integer getDrId() {
		return drId;
	}
	public void setDrId(Integer drId) {
		this.drId = drId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getPoiId() {
		return poiId;
	}
	public void setPoiId(Integer poiId) {
		this.poiId = poiId;
	}
	public Date getDateReceived() {
		return dateReceived;
	}
	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getSlaDeliveryTime() {
		return slaDeliveryTime;
	}
	public void setSlaDeliveryTime(Integer slaDeliveryTime) {
		this.slaDeliveryTime = slaDeliveryTime;
	}
	public Date getEstimasi() {
		return estimasi;
	}
	public void setEstimasi(Date estimasi) {
		this.estimasi = estimasi;
	}
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}
	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	public List<DeliveryReceivedFiles> getDeliveryReceivedFiles() {
		return deliveryReceivedFiles;
	}
	public void setDeliveryReceivedFiles(List<DeliveryReceivedFiles> deliveryReceivedFiles) {
		this.deliveryReceivedFiles = deliveryReceivedFiles;
	}
	public List<DeliveryReceivedDetailDTO> getDeliveryReceivedDetailDTO() {
		return deliveryReceivedDetailDTO;
	}
	public void setDeliveryReceivedDetailDTO(List<DeliveryReceivedDetailDTO> deliveryReceivedDetailDTO) {
		this.deliveryReceivedDetailDTO = deliveryReceivedDetailDTO;
	}
	public List<Double> getDikirim() {
		return dikirim;
	}
	public void setDikirim(List<Double> dikirim) {
		this.dikirim = dikirim;
	}
	public String getDeliveryReceiptNum() {
		return deliveryReceiptNum;
	}
	public void setDeliveryReceiptNum(String deliveryReceiptNum) {
		this.deliveryReceiptNum = deliveryReceiptNum;
	}
	
	public String getDeliveryOrderNum() {
		return deliveryOrderNum;
	}
	public void setDeliveryOrderNum(String deliveryOrderNum) {
		this.deliveryOrderNum = deliveryOrderNum;
	}
	public Date getDeliveryOrderDate() {
		return deliveryOrderDate;
	}
	public void setDeliveryOrderDate(Date deliveryOrderDate) {
		this.deliveryOrderDate = deliveryOrderDate;
	}
	
}
