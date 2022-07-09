/*
* File: PerolehanPengadaanSatuanServices.java
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
* Dec 21, 2016 3:21:49 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.perolehan;

import java.util.List;

import id.co.promise.procurement.entity.PerolehanPengadaanSatuan;
import id.co.promise.procurement.security.TokenSession;

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
@Path(value = "/procurement/perolehan/PerolehanPengadaanSatuanServices")
@Produces(MediaType.APPLICATION_JSON)
public class PerolehanPengadaanSatuanServices {
	@EJB
	private TokenSession tokenSession;
	@EJB
	private PerolehanPengadaanSatuanSession perolehanPengadaanSatuanSession;
	
	@Path("/findByPengadaan/{pengadaanId}")
	@GET
	public List<PerolehanPengadaanSatuan> findByPengadaan(@PathParam("pengadaanId")int pengadaanId){
		return perolehanPengadaanSatuanSession.findByPengadaan(pengadaanId);
	}
	
}
