/*
* File: PurchaseRequestVerificationSession.java
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
* Feb 20, 2017 4:59:37 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.purchaserequestverification;

import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.security.TokenSession;

import java.util.List;

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
@Path("/procurement/PurchaseRequestVerificationServices")
@Produces(MediaType.APPLICATION_JSON)
public class PurchaseRequestVerificationServices {
	@EJB
	private PurchaseVerificationSession pvSession;
	@EJB
	private TokenSession tokenSession;
	
	@GET
	@Path("/findByPR/{prid}")
	public List<PurchaseVerificationShippingDTO> findShippingByPR(@PathParam("prid")int prid){
		return pvSession.findShippingByPR(prid);
	}
	
	@POST
	@Path("/updatepritem")
	public PurchaseRequestItem updatePurchaseRequestItem(@FormParam("pritemid")int prItemId,
			@FormParam("itemId")int itemId,
			@FormParam("vendorId")int vendorId,
			@FormParam("isFromCatalog")int isFromCatalog,
			@HeaderParam("Authorization") String token){
		
		return pvSession.updatePurchaseRequestItem(prItemId, itemId, vendorId,isFromCatalog, tokenSession.findByToken(token));
	}
	
	@POST
	@Path("/updateprstatus")
	public PurchaseRequest updatePurchaseRequestStatus(@FormParam("prId")int prId, @FormParam("processId")int processId, @HeaderParam("Authorization") String token){
		return pvSession.updatePurchaseRequestStatus(prId, processId, tokenSession.findByToken(token));
	}
}
