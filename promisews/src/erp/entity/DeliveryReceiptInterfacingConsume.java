package erp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class DeliveryReceiptInterfacingConsume {
	
	@SerializedName("CREATION_DATE")
	@JsonProperty("CREATION_DATE")
	private String creationDate ;
	
	@SerializedName("LAST_UPDATE_DATE")
	@JsonProperty("LAST_UPDATE_DATE")
	private String lastUpdateDate ;

	@SerializedName("ORG_CODE")
	@JsonProperty("ORG_CODE")
	private String orgCode;
	
	@SerializedName("PO_NUMBER")
	@JsonProperty("PO_NUMBER")
	private String poNumber;
	
	@SerializedName("PO_LINE")
	@JsonProperty("PO_LINE")
	private Integer poLine;
	
	@SerializedName("PO_QTY")
	@JsonProperty("PO_QTY")
	private Integer poQty;
	
	@SerializedName("RECEIPT_NUM")
	@JsonProperty("RECEIPT_NUM")
	private String receiptNum;
	
	@SerializedName("RECEIPT_QUANTITY")
	@JsonProperty("RECEIPT_QUANTITY")
	private String receiptQuantity;
	
	@SerializedName("RECEIPT_DATE")
	@JsonProperty("RECEIPT_DATE")
	private String receiptDate;
	
	@SerializedName("RECEIVING_STATUS")
	@JsonProperty("RECEIVING_STATUS")
	private String receivingStatus;
	
	public String getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public Integer getPoLine() {
		return poLine;
	}

	public void setPoLine(Integer poLine) {
		this.poLine = poLine;
	}

	public Integer getPoQty() {
		return poQty;
	}

	public void setPoQty(Integer poQty) {
		this.poQty = poQty;
	}

	public String getReceiptNum() {
		return receiptNum;
	}

	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}

	public String getReceiptQuantity() {
		return receiptQuantity;
	}

	public void setReceiptQuantity(String receiptQuantity) {
		this.receiptQuantity = receiptQuantity;
	}

	public String getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getReceivingStatus() {
		return receivingStatus;
	}

	public void setReceivingStatus(String receivingStatus) {
		this.receivingStatus = receivingStatus;
	}
	
	
}
