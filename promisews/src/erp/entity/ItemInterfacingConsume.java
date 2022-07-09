package erp.entity;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemInterfacingConsume {
//sesuaikan dengan filed yang di butuhkan
	@SerializedName("CREATION_DATE")
	@JsonProperty("CREATION_DATE")
	private String creationDate ;
	
	@SerializedName("LAST_UPDATE_DATE")
	@JsonProperty("LAST_UPDATE_DATE")
	private String lastUpdateDate ;
	
	@SerializedName("ITEM_ID")
	@JsonProperty("ITEM_ID")
	private Integer itemId ;
	
	@SerializedName("ITEM_CODE")
	@JsonProperty("ITEM_CODE")
	private String itemCode;
	
	@SerializedName("DESCRIPTION")
	@JsonProperty("DESCRIPTION")
	private String description;
	
	@SerializedName("UOM")
	@JsonProperty("UOM")
	private UomInterfacingConsume uom;	
	
	@SerializedName("ORG_LIST")
	@JsonProperty("ORG_LIST")
	private List <OrganisasiInterfacingConsume> organisasiList;

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

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public UomInterfacingConsume getUom() {
		return uom;
	}

	public void setUom(UomInterfacingConsume uom) {
		this.uom = uom;
	}

	public List<OrganisasiInterfacingConsume> getOrganisasiList() {
		return organisasiList;
	}

	public void setOrganisasiList(List<OrganisasiInterfacingConsume> organisasiList) {
		this.organisasiList = organisasiList;
	}
}
