package id.co.promise.procurement.utils;

import id.co.promise.procurement.catalog.entity.Attribute;
import id.co.promise.procurement.catalog.entity.AttributePanelGroup;
import id.co.promise.procurement.catalog.entity.Category;
import id.co.promise.procurement.entity.BidangUsaha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StaticUtility {
	public static String setStringMatchAnywhere(String stringInput) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StaticProperties.PERSEN);
        stringBuilder.append(stringInput.toUpperCase());
        stringBuilder.append(StaticProperties.PERSEN);
        
        return stringBuilder.toString();
    }
	
	public static void populateTreeList(Category categoryNode, List<Category> categoryTreeList, Boolean udah) {		
		if (categoryTreeList != null && categoryTreeList.size() > 0) {
			for (Category treeCategory : categoryTreeList) {
				if (udah) {
					break;
				}
				if (categoryNode.getParentCategory() != null && treeCategory.getId().equals(categoryNode.getParentCategory().getId())) {
					if (treeCategory.getCategoryChildList() == null) {
						treeCategory.setCategoryChildList(new ArrayList<Category>());
					}
					Category tempCategory = new Category();
					tempCategory.setId(categoryNode.getId());
					tempCategory.setAdminLabel(categoryNode.getAdminLabel());
					tempCategory.setDescription(categoryNode.getDescription());
					tempCategory.setFlagEnabled(categoryNode.getFlagEnabled());
					tempCategory.setIsDelete(categoryNode.getIsDelete());
					tempCategory.setTranslateEng(categoryNode.getTranslateEng());
					tempCategory.setTranslateInd(categoryNode.getTranslateInd());
					tempCategory.setValue(categoryNode.getValue());
					
					Category tempParentCategory = new Category();
					tempParentCategory.setId(treeCategory.getId());
					tempParentCategory.setAdminLabel(treeCategory.getAdminLabel());
					tempParentCategory.setDescription(treeCategory.getDescription());
					tempParentCategory.setFlagEnabled(treeCategory.getFlagEnabled());
					tempParentCategory.setIsDelete(treeCategory.getIsDelete());
					tempParentCategory.setTranslateEng(treeCategory.getTranslateEng());
					tempParentCategory.setTranslateInd(treeCategory.getTranslateInd());
					tempParentCategory.setValue(treeCategory.getValue());
					
					tempCategory.setParentCategory(tempParentCategory);
					
					List<Category> categoryListParent = new ArrayList<Category>();
					categoryListParent.add(categoryNode.getParentCategory());
					for (int i = 0; i < categoryListParent.size(); i++) {
						if(categoryListParent.get(i).getParentCategory() != null) {
							Category parentCategories = new Category();
							parentCategories.setId(categoryListParent.get(i).getParentCategory().getId());
							parentCategories.setAdminLabel(categoryListParent.get(i).getParentCategory().getAdminLabel());
							parentCategories.setDescription(categoryListParent.get(i).getParentCategory().getDescription());
							parentCategories.setFlagEnabled(categoryListParent.get(i).getParentCategory().getFlagEnabled());
							parentCategories.setIsDelete(categoryListParent.get(i).getParentCategory().getIsDelete());
							parentCategories.setTranslateEng(categoryListParent.get(i).getParentCategory().getTranslateEng());
							parentCategories.setTranslateInd(categoryListParent.get(i).getParentCategory().getTranslateInd());
							parentCategories.setValue(categoryListParent.get(i).getParentCategory().getValue());
							parentCategories.setParentCategory(categoryListParent.get(i).getParentCategory().getParentCategory());
							categoryListParent.add(parentCategories);
						}
					}
					
					Collections.reverse(categoryListParent);
					
					tempCategory.setCategoryParentList(categoryListParent);
					
					treeCategory.getCategoryChildList().add(tempCategory);
					udah = true;
					break;
				}
				populateTreeList(categoryNode, treeCategory.getCategoryChildList(), false);
			}
		}
	}
	
	public static void populateBidangUsahaTreeList(BidangUsaha bidangUsahaNode, List<BidangUsaha> bidangUsahaTreeList, Boolean isExist) {		
		if (bidangUsahaTreeList != null && bidangUsahaTreeList.size() > 0) {
			for (BidangUsaha treeBidangUsaha : bidangUsahaTreeList) {
				if (isExist) {
					break;
				}
				if (bidangUsahaNode.getParentId() != null && treeBidangUsaha.getId().equals(bidangUsahaNode.getParentId())) {
					if (treeBidangUsaha.getBidangUsahaChildList() == null) {
						treeBidangUsaha.setBidangUsahaChildList(new ArrayList<BidangUsaha>());
					}
					BidangUsaha tempBidangUsaha = new BidangUsaha();
					tempBidangUsaha.setId(bidangUsahaNode.getId());
					tempBidangUsaha.setIsDelete(bidangUsahaNode.getIsDelete());
					tempBidangUsaha.setNama(bidangUsahaNode.getNama());
					tempBidangUsaha.setParentId(bidangUsahaNode.getParentId());
					
					treeBidangUsaha.getBidangUsahaChildList().add(tempBidangUsaha);
					isExist = true;
					break;
				}
				populateBidangUsahaTreeList(bidangUsahaNode, treeBidangUsaha.getBidangUsahaChildList(), false);
			}
		}
	}
	
	public static List<AttributePanelGroup> populateAttributeGroupList(List<AttributePanelGroup> attributePanelGroupList) {
		List<AttributePanelGroup> tempAttributePanelGroupList = new ArrayList<AttributePanelGroup>();
		String groupName = "";
		if (attributePanelGroupList != null && attributePanelGroupList.size() > 0) {
			for (AttributePanelGroup attributePanelGroup : attributePanelGroupList) {
				if (attributePanelGroup.getName().equals(groupName) == false) {
					groupName = attributePanelGroup.getName();
					tempAttributePanelGroupList.add(attributePanelGroup);
				}
				for (AttributePanelGroup tempAttributePanelGroup : tempAttributePanelGroupList) {
					if (tempAttributePanelGroup.getName().equals(attributePanelGroup.getName())) {
						if (tempAttributePanelGroup.getAttributeList() == null) {
							tempAttributePanelGroup.setAttributeList(new ArrayList<Attribute>());
						}
						tempAttributePanelGroup.getAttributeList().add(attributePanelGroup.getAttribute());
					}
				}
			}
		}
		return tempAttributePanelGroupList;
	}
}
