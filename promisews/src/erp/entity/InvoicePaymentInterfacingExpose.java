package erp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class InvoicePaymentInterfacingExpose {
	
	@SerializedName("CREATION_DATE")
	@JsonProperty("CREATION_DATE")
	private String creationDate;
	
	@SerializedName("LAST_UPDATE_DATE")
	@JsonProperty("LAST_UPDATE_DATE")
	private String lastUpdateDate;

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
	
	
}
