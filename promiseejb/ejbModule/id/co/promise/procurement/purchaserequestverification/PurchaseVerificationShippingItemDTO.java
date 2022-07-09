/*
* File: PurchaseVerificationShippingItemDTO.java
* This class is created to handle all processing involved
* in a shopping cart.
*
* Copyright (c) 2017 Mitra Mandiri Informatika
* Jl. Tebet Raya no. 11 B Jakarta Selatan
* All Rights Reserved.
*
* This software is the confidential and proprietary
* information of Mitra Mandiri Informatika ("Confidential
* Information").
*
* You shall not disclose such Confidential Information and
* shall use it only in accordance with the terms of the
* license agreement you entered into with MMI.
*
* Date 				Author 			Version 	Changes
* Feb 21, 2017 2:21:23 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.purchaserequestverification;

import id.co.promise.procurement.entity.Item;

/**
 * @author Mamat
 *
 */
public class PurchaseVerificationShippingItemDTO {
	private int shippingToPRId;
	private int shippingGroup;
	private int purchaseRequestItemId;
	private double quantity;
	private double quantityPRItem;
	private String ItemName;
	private double hargaSatuanPRItem;
	private double hargaTotalPRItem;
	
	/**
	 * @return the shippingToPRId
	 */
	public int getShippingToPRId() {
		return shippingToPRId;
	}
	/**
	 * @param shippingToPRId the shippingToPRId to set
	 */
	public void setShippingToPRId(int shippingToPRId) {
		this.shippingToPRId = shippingToPRId;
	}
	/**
	 * @return the shippingGroup
	 */
	public int getShippingGroup() {
		return shippingGroup;
	}
	/**
	 * @param shippingGroup the shippingGroup to set
	 */
	public void setShippingGroup(int shippingGroup) {
		this.shippingGroup = shippingGroup;
	}
	/**
	 * @return the purchaseRequestItemId
	 */
	public int getPurchaseRequestItemId() {
		return purchaseRequestItemId;
	}
	/**
	 * @param purchaseRequestItemId the purchaseRequestItemId to set
	 */
	public void setPurchaseRequestItemId(int purchaseRequestItemId) {
		this.purchaseRequestItemId = purchaseRequestItemId;
	}
	/**
	 * @return the quantity
	 */
	public double getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return ItemName;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	/**
	 * @return the quantityPRItem
	 */
	public double getQuantityPRItem() {
		return quantityPRItem;
	}
	/**
	 * @param quantityPRItem the quantityPRItem to set
	 */
	public void setQuantityPRItem(double quantityPRItem) {
		this.quantityPRItem = quantityPRItem;
	}
	/**
	 * @return the hargaSatuanPRItem
	 */
	public double getHargaSatuanPRItem() {
		return hargaSatuanPRItem;
	}
	/**
	 * @param hargaSatuanPRItem the hargaSatuanPRItem to set
	 */
	public void setHargaSatuanPRItem(double hargaSatuanPRItem) {
		this.hargaSatuanPRItem = hargaSatuanPRItem;
	}
	/**
	 * @return the hargaTotalPRItem
	 */
	public double getHargaTotalPRItem() {
		return hargaTotalPRItem;
	}
	/**
	 * @param hargaTotalPRItem the hargaTotalPRItem to set
	 */
	public void setHargaTotalPRItem(double hargaTotalPRItem) {
		this.hargaTotalPRItem = hargaTotalPRItem;
	}
	
	
}
