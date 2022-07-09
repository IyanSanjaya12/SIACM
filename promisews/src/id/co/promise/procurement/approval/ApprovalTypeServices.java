/*
* File: ApprovalTypeServices.java
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
* Sep 2, 2016 12:01:09 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.approval;

import java.util.List;

import id.co.promise.procurement.entity.ApprovalType;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Mamat
 *
 */
@Stateless
@Path("/procurement/approval/approvatype/")
@Produces(MediaType.APPLICATION_JSON)
public class ApprovalTypeServices {
	@EJB
	private ApprovalTypeSession approvalTypeSession;
	
	@Path("/all")
	@GET
	public List<ApprovalType> findAll(){
		return approvalTypeSession.findAll();
	}

	@Path("/get/{id}")
	@GET
	public ApprovalType getApprovalType(@PathParam("id")int id){
		return approvalTypeSession.find(id);
	}
	
}
