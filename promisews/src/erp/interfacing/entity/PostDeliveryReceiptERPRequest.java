package erp.interfacing.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class PostDeliveryReceiptERPRequest {
	
	@SerializedName("poNumber")
	@JsonProperty("poNumber")
	private String poNumber;
	
	@SerializedName("boId")
	@JsonProperty("boId")
	private String boId;
	
	@SerializedName("receivedDate")
	@JsonProperty("receivedDate")
	private String receivedDate;
	
	@SerializedName("expectedReceivedDate")
	@JsonProperty("expectedReceivedDate")
	private String expectedReceivedDate;
	
	@SerializedName("orgCode")
	@JsonProperty("orgCode")
	private String orgCode;
	
	@SerializedName("preparerNum")
	@JsonProperty("preparerNum")
	private String preparerNum;

	@SerializedName("shipmentNumber")
	@JsonProperty("shipmentNumber")
	private String shipmentNumber;
	
	@SerializedName("shipmentDate")
	@JsonProperty("shipmentDate")
	private String shipmentDate;
	
	@SerializedName("receiptList")
	@JsonProperty("receiptList")
	private List<DataReceiptListERPRequest> receiptList;

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getBoId() {
		return boId;
	}

	public void setBoId(String boId) {
		this.boId = boId;
	}

	public String getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getExpectedReceivedDate() {
		return expectedReceivedDate;
	}

	public void setExpectedReceivedDate(String expectedReceivedDate) {
		this.expectedReceivedDate = expectedReceivedDate;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getPreparerNum() {
		return preparerNum;
	}

	public void setPreparerNum(String preparerNum) {
		this.preparerNum = preparerNum;
	}

	public List<DataReceiptListERPRequest> getReceiptList() {
		return receiptList;
	}

	public void setReceiptList(List<DataReceiptListERPRequest> receiptList) {
		this.receiptList = receiptList;
	}

	public String getShipmentNumber() {
		return shipmentNumber;
	}

	public void setShipmentNumber(String shipmentNumber) {
		this.shipmentNumber = shipmentNumber;
	}

	public String getShipmentDate() {
		return shipmentDate;
	}

	public void setShipmentDate(String shipmentDate) {
		this.shipmentDate = shipmentDate;
	}
	
	
}
