package erp.interfacing.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class PostLoginUserERPResponse {
	
	@SerializedName("status")
	private String status;
	
	@SerializedName("data")
	List<PostLoginUserDetailERPResponse> data;

	public String getStatus() {
		return status;
	}

	public List<PostLoginUserDetailERPResponse> getData() {
		return data;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setData(List<PostLoginUserDetailERPResponse> data) {
		this.data = data;
	}
}
