package erp.interfacing.entity;

import com.google.gson.annotations.SerializedName;

public class DataVendorProductsERPResponse {

	@SerializedName("groupId")
	private String groupId;
	
	@SerializedName("groupDesc")
	private String groupDesc;
	
	@SerializedName("commodity")
	private String commodity;
	
	@SerializedName("commDesc")
	private String commDesc;

	public String getGroupId() {
		return groupId;
	}

	public String getGroupDesc() {
		return groupDesc;
	}

	public String getCommodity() {
		return commodity;
	}

	public String getCommDesc() {
		return commDesc;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public void setCommodity(String commodity) {
		this.commodity = commodity;
	}

	public void setCommDesc(String commDesc) {
		this.commDesc = commDesc;
	}

}
