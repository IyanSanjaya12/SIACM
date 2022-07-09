package erp.interfacing.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class GetMasterItemERPResponse {
	
	@SerializedName("categoryId")
	@JsonProperty("categoryId")
	private String categoryId;
	
	@SerializedName("itemCode")
	@JsonProperty("itemCode")
	private String itemCode;
	
	@SerializedName("itemId")
	@JsonProperty("itemId")
	private Integer itemId ;
	
	@SerializedName("description")
	@JsonProperty("description")
	private String description;
	
	@SerializedName("uomCode")
	@JsonProperty("uomCode")
	private String uomCode;	
	
	@SerializedName("uomDesc")
	@JsonProperty("uomDesc")
	private String uomDesc;	
	
	@SerializedName("status")
	@JsonProperty("status")
	private String status;	
	
	@SerializedName("categoryName")
	@JsonProperty("categoryName")
	private String categoryName;
	
	@SerializedName("creationDate")
	@JsonProperty("creationDate")
	private String creationDate;
	
	@SerializedName("createdBy")
	@JsonProperty("createdBy")
	private String createdBy;
	
	@SerializedName("lastUpdateDate")
	@JsonProperty("lastUpdateDate")
	private String lastUpdateDate;
	
	@SerializedName("lastUpdatedBy")
	@JsonProperty("lastUpdatedBy")
	private String lastUpdatedBy;
	
	@SerializedName("orgList")
	@JsonProperty("orgList")
	private List <DataOrgListERPResponse> orgList;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUomCode() {
		return uomCode;
	}

	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}

	public String getUomDesc() {
		return uomDesc;
	}

	public void setUomDesc(String uomDesc) {
		this.uomDesc = uomDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public List<DataOrgListERPResponse> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<DataOrgListERPResponse> orgList) {
		this.orgList = orgList;
	}
	
}
