package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.Item;

public class ItemMasterDTO {
	
	private Item item;
	private Integer itemGroupId;
	
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Integer getItemGroupId() {
		return itemGroupId;
	}
	public void setItemGroupId(Integer itemGroupId) {
		this.itemGroupId = itemGroupId;
	}
	
}
