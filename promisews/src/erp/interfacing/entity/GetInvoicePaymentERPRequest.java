package erp.interfacing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class GetInvoicePaymentERPRequest {

	@SerializedName("creationDate")
	@JsonProperty("creationDate")
	private String creationDate;
	
	@SerializedName("lastUpdateDate")
	@JsonProperty("lastUpdateDate")
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
