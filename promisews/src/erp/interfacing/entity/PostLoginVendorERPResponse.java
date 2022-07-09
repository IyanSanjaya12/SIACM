package erp.interfacing.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PostLoginVendorERPResponse {
	
	@SerializedName("status")
	private String status;
	
	@SerializedName("data")
	List<PostLoginVendorDetailERPResponse> data;

	public String getStatus() {
		return status;
	}

	public List<PostLoginVendorDetailERPResponse> getData() {
		return data;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setData(List<PostLoginVendorDetailERPResponse> data) {
		this.data = data;
	}
	
	

}
