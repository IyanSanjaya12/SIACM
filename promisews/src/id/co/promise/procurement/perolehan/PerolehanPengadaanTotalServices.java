/*
* File: PerolehanPengadaanTotalServices.java
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
* Dec 9, 2016 1:11:44 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.perolehan;

import java.util.Date;

import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.Pengadaan;
import id.co.promise.procurement.entity.PerolehanPengadaanTotal;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.audit.AuditHelper;

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
@Path(value = "/procurement/perolehan/PerolehanPengadaanTotalServices")
@Produces(MediaType.APPLICATION_JSON)
public class PerolehanPengadaanTotalServices {
	@EJB
	private TokenSession tokenSession;
	@EJB
	private PerolehanPengadaanTotalSession perolehanPengadaanTotalSesssion;
	
	@Path("/create")
	@POST
	public PerolehanPengadaanTotal create(@FormParam("pengadaan")int pengadaan, 
			@FormParam("matauang")int matauang,
			@FormParam("nilai")double nilai,
			@FormParam("pajakProsentase")double pajakProsentase,
			@FormParam("pajak")double pajak,
			@FormParam("keterangan")String keterangan,
			@FormParam("vendor")int vendor,
			@HeaderParam("Authorization")String token){
		PerolehanPengadaanTotal ppt = new PerolehanPengadaanTotal();
		Pengadaan pengadaanObj = new Pengadaan();
		pengadaanObj.setId(pengadaan);
		ppt.setPengadaan(pengadaanObj);
		MataUang mataUangObj = new MataUang();
		ppt.setMataUang(mataUangObj);		
		ppt.setNilai(nilai);
		ppt.setPajakProsentase(pajakProsentase);
		ppt.setPajak(pajak);
		ppt.setKeterangan(keterangan);
		Vendor vendorObj = new Vendor();
		vendorObj.setId(vendor);
		ppt.setVendor(vendorObj);
		
		ppt.setCreated(new Date());
		ppt.setUserId(tokenSession.findByToken(token).getUser().getId());
		ppt.setIsDelete(0);
		
		perolehanPengadaanTotalSesssion.create(ppt, AuditHelper.OPERATION_CREATE, tokenSession.findByToken(token));
		return ppt;
	}

	@Path("/findByPengadaan/{pengadaanId}")
	@GET
	public PerolehanPengadaanTotal findByPengadaan(@PathParam("pengadaanId")int pengadaan){
		return perolehanPengadaanTotalSesssion.findByPengadaan(pengadaan);
	}
	
	@Path("/findByPengadaanAndVendor/{pengadaanId}/{vendorId}")
	@GET
	public PerolehanPengadaanTotal findByPengadaanAndVendor(@PathParam("pengadaanId")int pengadaan, @PathParam("vendorId")int vendor){
		return perolehanPengadaanTotalSesssion.findByPengadaanAndVendor(pengadaan, vendor);
	}
}
