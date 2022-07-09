package erp.entity;

import com.google.gson.annotations.SerializedName;

public class BidangUsahaInterfacingExpose {
	
	@SerializedName("GROUP_ID")
	private String groupId;
	
	@SerializedName("GROUP_DESCR")
	private String groupDescr;
	
	@SerializedName("COMMODITY")
	private String commodity;
	
	@SerializedName("DESCR")
	private String descr;
	
	@SerializedName("COMM_DESCR")
	private String commDescr;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupDescr() {
		return groupDescr;
	}

	public void setGroupDescr(String groupDescr) {
		this.groupDescr = groupDescr;
	}

	public String getCommodity() {
		return commodity;
	}

	public void setCommodity(String commodity) {
		this.commodity = commodity;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getCommDescr() {
		return commDescr;
	}

	public void setCommDescr(String commDescr) {
		this.commDescr = commDescr;
	}
		
}
