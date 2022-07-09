/*
* File: ApprovalProcessPRItemCostValidationDTO.java
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
* Feb 20, 2017 10:01:03 AM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.approval;

import id.co.promise.procurement.entity.PurchaseRequestItem;

/**
 * @author Mamat
 *
 */
public class ApprovalProcessPRItemCostValidationDTO {
	private PurchaseRequestItem purchaseRequestItem;
	private boolean isCostAvailable;
	/**
	 * @return the purchaseRequestItem
	 */
	public PurchaseRequestItem getPurchaseRequestItem() {
		return purchaseRequestItem;
	}
	/**
	 * @param purchaseRequestItem the purchaseRequestItem to set
	 */
	public void setPurchaseRequestItem(PurchaseRequestItem purchaseRequestItem) {
		this.purchaseRequestItem = purchaseRequestItem;
	}
	/**
	 * @return the isCostAvailable
	 */
	public boolean isCostAvailable() {
		return isCostAvailable;
	}
	/**
	 * @param isCostAvailable the isCostAvailable to set
	 */
	public void setCostAvailable(boolean isCostAvailable) {
		this.isCostAvailable = isCostAvailable;
	}
	
	
}
