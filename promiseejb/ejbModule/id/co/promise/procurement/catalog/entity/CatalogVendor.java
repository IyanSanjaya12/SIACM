package id.co.promise.procurement.catalog.entity;

import id.co.promise.procurement.entity.BidangUsaha;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;

import java.util.List;

public class CatalogVendor {
	private Category category;
	private BidangUsaha bidangUsaha;
	private String name;
	private String location;
	private String stringOrder;
	private Integer pageSize;
	private Integer currentPage;
	private Integer userId;
	private Boolean contract;
	private Boolean eCatalog;
	private List<Category> categoryList;
	private List<User> userList;
	private AttributeGroup attributeGroup;
//	private List<AttributePanelGroup> attributePanelGroupList;
	private Vendor vendor;
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public BidangUsaha getBidangUsaha() {
		return bidangUsaha;
	}
	public void setBidangUsaha(BidangUsaha bidangUsaha) {
		this.bidangUsaha = bidangUsaha;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStringOrder() {
		return stringOrder;
	}
	public void setStringOrder(String stringOrder) {
		this.stringOrder = stringOrder;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Boolean getContract() {
		return contract;
	}
	public void setContract(Boolean contract) {
		this.contract = contract;
	}
//	public List<AttributePanelGroup> getAttributePanelGroupList() {
//		return attributePanelGroupList;
//	}
//	public void setAttributePanelGroupList(
//			List<AttributePanelGroup> attributePanelGroupList) {
//		this.attributePanelGroupList = attributePanelGroupList;
//	}
	public List<Category> getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public AttributeGroup getAttributeGroup() {
		return attributeGroup;
	}
	public void setAttributeGroup(AttributeGroup attributeGroup) {
		this.attributeGroup = attributeGroup;
	}
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	public Boolean geteCatalog() {
		return eCatalog;
	}
	public void seteCatalog(Boolean eCatalog) {
		this.eCatalog = eCatalog;
	}
}
