package erp.entity;

import com.google.gson.annotations.SerializedName;

public class UomInterfacingConsume {
	
	@SerializedName("UOM_CODE")
	private String uomCode;
	
	@SerializedName("UOM_DESCRIPTION")
	private String uomDescription;

	public String getUomCode() {
		return uomCode;
	}

	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}

	public String getUomDescription() {
		return uomDescription;
	}

	public void setUomDescription(String uomDescription) {
		this.uomDescription = uomDescription;
	}
}
