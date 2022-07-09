/*
* File: ShippingToPRServices.java
* This class is created to handle all processing involved
* in a shopping cart.
*
* Copyright (c) 2016 Mitra Mandiri Informatika
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
* Nov 7, 2016 1:04:55 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.purchaserequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.ShippingToPR;
import id.co.promise.procurement.master.AddressBookSession;
import id.co.promise.procurement.security.TokenSession;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Mamat
 *
 */

@Stateless
@Path("/procurement/ShippingToPRServices")
@Produces(MediaType.APPLICATION_JSON)
public class ShippingToPRServices {
	@EJB
	private ShippingToPRSession stpSession;
	@EJB
	private AddressBookSession addressBookSession;
	@EJB
	private PurchaseRequestItemSession priSession;
	@EJB
	private TokenSession tokenSession;
	
	private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Path("/findByPR/{prid}")
	@GET
	public List<ShippingToPR> findByPR(@PathParam("prid")int prId){
		return stpSession.findShippingByPR(prId);
	}
	
	@Path("/insert")
	@POST
	public ShippingToPR insert(@FormParam("addressBookId")int addressBookId,
			@FormParam("prItemId")int prItemId,
			@FormParam("quantity")String quantity,
			@FormParam("prNumber")String prNumber,
			@FormParam("fullName")String fullName,
			@FormParam("addressLabel")String addressLabel,
			@FormParam("streetAddress")String streetAddress,
			@FormParam("telephone1")String telephone1,
			@FormParam("shippingGroup")int shippingGroup,
			@FormParam("deliveryTime") String deliveryTime,
			@HeaderParam("Authorization") String token){
		
		ShippingToPR stp = new ShippingToPR();
		if(addressBookId>0){
			stp.setAddressBook(addressBookSession.find(addressBookId));
		}		
		if(prItemId>0){
			stp.setPurchaseRequestItem(prItemId);
		}
		
		Date dDeliveryTime = null;
		try {
			dDeliveryTime = sdf.parse(deliveryTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		stp.setQuantity(Double.parseDouble(quantity));
		stp.setPrNumber(prNumber);
		stp.setFullName(fullName);
		stp.setAddressLabel(addressLabel);
		stp.setStreetAddress(streetAddress);
		stp.setTelephone1(telephone1);
		stp.setShippingGroup(shippingGroup);
		stp.setDeliveryTime(dDeliveryTime);
		stpSession.inserShippingToPR(stp, tokenSession.findByToken(token));
		return stp;
	}
}
