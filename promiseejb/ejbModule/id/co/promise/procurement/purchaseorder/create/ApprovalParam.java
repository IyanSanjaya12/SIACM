package id.co.promise.procurement.purchaseorder.create;

import java.util.List;

import id.co.promise.procurement.entity.ApprovalLevel;

public class ApprovalParam {
	private Integer selected;
	private List<ApprovalLevel> levelList;
	private NewApprovalParam newSelected;

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}

	public List<ApprovalLevel> getLevelList() {
		return levelList;
	}

	public void setLevelList(List<ApprovalLevel> levelList) {
		this.levelList = levelList;
	}

	public NewApprovalParam getNewSelected() {
		return newSelected;
	}

	public void setNewSelected(NewApprovalParam newSelected) {
		this.newSelected = newSelected;
	}

}
