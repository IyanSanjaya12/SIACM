/*
* File: PurchaseRequestPengadaan.java
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
* Jan 16, 2017 5:12:28 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.inisialisasi;

import id.co.promise.procurement.entity.Kontrak;
import id.co.promise.procurement.entity.Pengadaan;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestPengadaan;
import id.co.promise.procurement.kontrakmanajemen.KontrakSession;
import id.co.promise.procurement.security.TokenSession;

import java.util.ArrayList;
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
@Path(value = "/procurement/inisialisasi/purchaserequestpengadaan")
@Produces(MediaType.APPLICATION_JSON)
public class PurchaseRequestPengadaanServices {
	@EJB private PurchaseRequestPengadaanSession prPengadaanSession;
	@EJB private TokenSession tokenSession;
	@EJB private KontrakSession kontrakSession;
	
	@Path("/create")
	@POST
	public PurchaseRequestPengadaan create(@FormParam("pengadaan")int pengadaanId, 
			@FormParam("purchaseRequest")int purchaseRequestId,
			@HeaderParam("Authorization") String token){
		PurchaseRequestPengadaan prPengadaan = new PurchaseRequestPengadaan();
		Pengadaan p = new Pengadaan();
		p.setId(pengadaanId);
		prPengadaan.setPengadaan(p);
		PurchaseRequest pr = new PurchaseRequest();
		pr.setId(purchaseRequestId);
		prPengadaan.setPurchaseRequest(pr);
		
		prPengadaanSession.createPurchaseRequestPengadaan(prPengadaan, tokenSession.findByToken(token));
		return prPengadaan;
	}
	

	@Path("/edit")
	@POST
	public PurchaseRequestPengadaan edit(
			@FormParam("id") int id,
			@FormParam("pengadaan")int pengadaanId, 
			@FormParam("purchaseRequest")int purchaseRequestId,
			@HeaderParam("Authorization") String token){
		PurchaseRequestPengadaan prPengadaan = prPengadaanSession.find(id);
		Pengadaan p = new Pengadaan();
		p.setId(pengadaanId);
		prPengadaan.setPengadaan(p);
		PurchaseRequest pr = new PurchaseRequest();
		pr.setId(purchaseRequestId);
		prPengadaan.setPurchaseRequest(pr);		
		prPengadaanSession.editPurchaseRequestPengadaan(prPengadaan, tokenSession.findByToken(token));
		return prPengadaan;
	}

	@Path("/findByPengadaanId/{pengadaanId}")
	@GET
	public List<PurchaseRequestPengadaan> findByPengadaanId(@PathParam("pengadaanId")int pengadaanId){
		return prPengadaanSession.getListByPengadaanId(pengadaanId);
	}
	
	@Path("/findByPurchaseRequestId/{purchaseRequestId}")
	@GET
	public List<PurchaseRequestPengadaan> findByPurchaseRequestId(@PathParam("purchaseRequestId")int purchaseRequestId){
		return prPengadaanSession.getListByPerencanaanId(purchaseRequestId);
	}
	
	@Path("/findByPurchaseRequestIdContract/{purchaseRequestId}")
	@GET
	public List<PurchaseRequestPengadaan> findByPurchaseRequestIdContract(@PathParam("purchaseRequestId")int purchaseRequestId){
		List<PurchaseRequestPengadaan> list = new ArrayList<PurchaseRequestPengadaan>();
		List<Object[]> listObj	= prPengadaanSession.findByPurchaseRequestIdContract(purchaseRequestId);
		if(listObj !=null){
			for(Object[] obj : listObj){
				PurchaseRequestPengadaan pr = new PurchaseRequestPengadaan();
				pr = (PurchaseRequestPengadaan)obj[0];
				pr.setKontrak((Kontrak)obj[1]);
				list.add(pr);
			}
		}
		return list;
	}
}
