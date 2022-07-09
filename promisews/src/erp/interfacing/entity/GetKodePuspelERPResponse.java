package erp.interfacing.entity;

import com.google.gson.annotations.SerializedName;

public class GetKodePuspelERPResponse {
	
	@SerializedName("kodePuspel")
	private String kodePuspel;

	public String getKodePuspel() {
		return kodePuspel;
	}

	public void setKodePuspel(String kodePuspel) {
		this.kodePuspel = kodePuspel;
	}
	
}
