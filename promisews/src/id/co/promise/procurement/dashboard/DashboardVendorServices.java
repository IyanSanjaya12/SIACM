/*
* File: DashboardVendorServices.java
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
* Feb 1, 2017 3:07:28 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.dashboard;

import id.co.promise.procurement.security.TokenSession;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Mamat
 *
 */

@Stateless
@Path(value = "/procurement/DashboardVendorServices")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardVendorServices {
	@EJB
	private DashboadVendorSession dashboardVendorSession;
	@EJB
	private TokenSession tokenSession;
}
