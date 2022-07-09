/*
* File: PurchaseVerificationShippingDTO.java
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
* Feb 21, 2017 2:17:26 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.purchaserequestverification;

import java.util.Date;
import java.util.List;

/**
 * @author Mamat
 *
 */
public class PurchaseVerificationShippingDTO {
	private String companyName;
	private String fullName;
	private String address;
	private String telp;
	private Date deliveryTime;
	private int group;
	private List<PurchaseVerificationShippingItemDTO> purchaseVerificationShippingItemDTO;
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the telp
	 */
	public String getTelp() {
		return telp;
	}
	/**
	 * @param telp the telp to set
	 */
	public void setTelp(String telp) {
		this.telp = telp;
	}
	/**
	 * @return the group
	 */
	public int getGroup() {
		return group;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(int group) {
		this.group = group;
	}
	/**
	 * @return the purchaseVerificationShippingItemDTO
	 */
	public List<PurchaseVerificationShippingItemDTO> getPurchaseVerificationShippingItemDTO() {
		return purchaseVerificationShippingItemDTO;
	}
	/**
	 * @param purchaseVerificationShippingItemDTO the purchaseVerificationShippingItemDTO to set
	 */
	public void setPurchaseVerificationShippingItemDTO(
			List<PurchaseVerificationShippingItemDTO> purchaseVerificationShippingItemDTO) {
		this.purchaseVerificationShippingItemDTO = purchaseVerificationShippingItemDTO;
	}
	public Date getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	
}
