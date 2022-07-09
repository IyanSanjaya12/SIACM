/*
* File: PerencanaanPengadaanServices.java
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
* Aug 31, 2016 9:51:35 AM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.inisialisasi;

import id.co.promise.procurement.entity.Pengadaan;
import id.co.promise.procurement.entity.Perencanaan;
import id.co.promise.procurement.entity.PerencanaanPengadaan;
import id.co.promise.procurement.security.TokenSession;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Mamat
 *
 */

@Stateless
@Path(value = "/procurement/inisialisasi/perencanaanpengadaan")
@Produces(MediaType.APPLICATION_JSON)
public class PerencanaanPengadaanServices {
	@EJB
	private PerencanaanPengadaanSession ppSession;
	@EJB
	private TokenSession tokenSession;
	
	@Path("/create")
	@POST
	public PerencanaanPengadaan create(@FormParam("pengadaan")int pengadaan, @FormParam("perencanaan")int perencanaan,
			@HeaderParam("Authorization") String token){
		PerencanaanPengadaan pp = new PerencanaanPengadaan();
		Pengadaan p = new Pengadaan();
		p.setId(pengadaan);
		Perencanaan pr = new Perencanaan();
		pr.setId(perencanaan);
		pp.setPengadaan(p);
		pp.setPerencanaan(pr);
		ppSession.createPerencanaanPengadaan(pp, tokenSession.findByToken(token));
		return pp;
	}
	
}
