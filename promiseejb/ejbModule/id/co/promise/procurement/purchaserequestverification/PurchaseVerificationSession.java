/*
* File: PurchaseVerificationServices.java
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
* Feb 20, 2017 4:58:42 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.purchaserequestverification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.promise.procurement.alokasianggaran.AlokasiAnggaranSession;
import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.entity.AlokasiAnggaran;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.ShippingToPR;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.purchaserequest.ShippingToPRSession;
import id.co.promise.procurement.vendor.VendorSession;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

/**
 * @author Mamat
 *
 */
@Stateless
@LocalBean
public class PurchaseVerificationSession {
	@EJB
	private PurchaseRequestSession prSession;
	@EJB
	private PurchaseRequestItemSession prItemSession;
	@EJB
	private ShippingToPRSession shippingToPRSession;
	@EJB
	private ItemSession itemSession;
	@EJB
	private VendorSession vendorSession;
	@EJB
	private AlokasiAnggaranSession alokasiAnggaranSession;
	@EJB
	private CatalogSession catalogSession;
		
	public List<PurchaseVerificationShippingDTO> findShippingByPR(int prid){
		List<PurchaseVerificationShippingDTO> pvShippingList = new ArrayList<PurchaseVerificationShippingDTO>();
		List<ShippingToPR> shippingList = shippingToPRSession.findShippingByPR(prid);
		int groupId = 0;
		for (ShippingToPR shippingToPR : shippingList) {
			if(groupId!=shippingToPR.getShippingGroup()){
				PurchaseVerificationShippingDTO pvShipping = new PurchaseVerificationShippingDTO();
				pvShipping.setGroup(shippingToPR.getShippingGroup());
				pvShipping.setAddress(shippingToPR.getStreetAddress());
				pvShipping.setCompanyName(shippingToPR.getAddressLabel());
				pvShipping.setFullName(shippingToPR.getFullName());
				pvShipping.setTelp(shippingToPR.getTelephone1());
				pvShipping.setDeliveryTime(shippingToPR.getDeliveryTime());
				pvShippingList.add(pvShipping);
				groupId = shippingToPR.getShippingGroup();
			}			
		}
		int index=0;
		for (PurchaseVerificationShippingDTO pvShipping : pvShippingList) {
			List<PurchaseVerificationShippingItemDTO> pvShippingItemList = new ArrayList<PurchaseVerificationShippingItemDTO>();
			for (ShippingToPR shippingToPR : shippingList) {
				if(shippingToPR.getShippingGroup()==pvShipping.getGroup()){
					PurchaseVerificationShippingItemDTO pvShippingItem = new PurchaseVerificationShippingItemDTO();
					PurchaseRequestItem prItem = prItemSession.getPurchaseRequestItem(shippingToPR.getPurchaseRequestItem());
					pvShippingItem.setItemName(prItem.getItemname());
					pvShippingItem.setQuantityPRItem(prItem.getQuantity());
					pvShippingItem.setHargaSatuanPRItem(prItem.getPrice());
					pvShippingItem.setHargaTotalPRItem(prItem.getTotal());
					pvShippingItem.setPurchaseRequestItemId(shippingToPR.getPurchaseRequestItem());
					pvShippingItem.setQuantity(shippingToPR.getQuantity());
					pvShippingItem.setShippingGroup(shippingToPR.getShippingGroup());
					pvShippingItem.setShippingToPRId(shippingToPR.getId());
					pvShippingItemList.add(pvShippingItem);					
				}
			}
			pvShipping.setPurchaseVerificationShippingItemDTO(pvShippingItemList);
			pvShippingList.set(index, pvShipping);
			index++;
		}
		
		return pvShippingList;
	}

	/**
	* Insert element at front of the sequence.
	* @param pElement the element to add 
	* @return the element is inserted as the first element of
	* the sequence
	* @exception java.lang.NullPointerException
	* if element is detected null
	*/
	public PurchaseRequestItem updatePurchaseRequestItem(int prItemId,
			int itemId, int vendorId, int isFromCatalog, Token token) {
		PurchaseRequestItem prItem = prItemSession.getPurchaseRequestItem(prItemId);
		//update data
		//no catalog
		if(isFromCatalog==0){
			Item item = itemSession.getItem(itemId);
			prItem.setItem(item);
			prItem.setItemname(item.getNama());
			if(item.getKode() != null){
				prItem.setKode(item.getKode());	
			}
			if(vendorId>0)
				prItem.setVendor(vendorSession.getVendor(vendorId));
			
			prItemSession.updatePurchaseRequestItem(prItem, token);
		} else if(isFromCatalog==1){
			//is from catalog
			Catalog catalog = catalogSession.find(itemId);
			prItem.setItem(catalog.getItem());
			prItem.setVendor(catalog.getVendor());
			prItem.setItemname(catalog.getItem().getNama());
			prItem.setKode(catalog.getItem().getKode());
			prItem.setMataUang(catalog.getMataUang());
			prItem.setPrice(catalog.getHarga());
			prItem.setSpecification(catalog.getDeskripsiIND());
			prItem.setTotal(prItem.getQuantity()*catalog.getHarga());

			prItemSession.updatePurchaseRequestItem(prItem, token);
		} else {
			//?
			return null;
		}
		return prItem;
	}

	/**
	* Insert element at front of the sequence.
	* @param pElement the element to add 
	* @return the element is inserted as the first element of
	* the sequence
	* @exception java.lang.NullPointerException
	* if element is detected null
	*/
	public PurchaseRequest updatePurchaseRequestStatus(int prId, int processId,
			Token token) {
		PurchaseRequest pr = prSession.get(prId);
		/*if(processId == 1) { //Direct PO
			pr.setStatus(7); // 7 = Verified, status ketika sudah diverifikasi & memilih direct PO
		} else if(processId == 2){ //Procurement Process
			pr.setStatus(5); //5 = On Progress / Process, siap untuk diproses pengadaan
		}*/
		
		
		if(processId == 1) { //Direct PO
			pr.setStatus(5); // 5 = On Progress / Process
		} else if(processId == 2){ //Procurement 
			pr.setStatus(7); //7 = //Procurement Process
		}
		
		pr.setVerifiedDate(new Date());
		
		prSession.update(pr, token);
		
		// Langsung Update jumlah Bugjet di T2_ALOKASI_ANGGARAN
		// Update jumlah Bugjet Berlangsung DI PURCHASE REQUEST SERVICE SAAT PR DI APPROVE
/*		List<AlokasiAnggaran> costAlList = alokasiAnggaranSession.findByNomorDraft(pr.getCostcenter());
		if(costAlList != null && costAlList.size() > 0) {
			Double jumlahBJOld = costAlList.get(0).getBookingAnggaran();
			Double jumlahBJNew = new Double(pr.getTotalcost()) + jumlahBJOld;
			costAlList.get(0).setBookingAnggaran(jumlahBJNew);
			alokasiAnggaranSession.updateAlokasiAnggaran(costAlList.get(0), token);
		}*/
		return pr;
	}
}
