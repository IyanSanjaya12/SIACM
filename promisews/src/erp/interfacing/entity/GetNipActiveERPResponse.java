package erp.interfacing.entity;

import com.google.gson.annotations.SerializedName;

public class GetNipActiveERPResponse {

	@SerializedName("flag")
	private Integer flag;

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
}
