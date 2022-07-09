/*
* File: ItemWSDLServices.java
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
* Nov 29, 2016 4:11:15 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.security.TokenSession;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 * @author Mamat
 *
 */
@Stateless
@WebService(serviceName="Master", 
targetNamespace = "http://promise.co.id/wsdl",
endpointInterface="id.co.promise.procurement.master.ItemWSDLImpl")
public class ItemWSDLServices implements ItemWSDLImpl {
	@EJB
	private ItemSession itemSession;
	@EJB
	private TokenSession tokenSession;
	
	public List<Item> getItemList() {
		return itemSession.getItemList();
	}
	
	public Item create(Item item, String token){
		return itemSession.insertItem(item, tokenSession.findByToken(token));
	}
	
	public Item edit(Item item, String token){
		return itemSession.updateItem(item, tokenSession.findByToken(token));
	}
	
	public Item delete(int itemId, String token){
		return itemSession.deleteItem(itemId, tokenSession.findByToken(token));
	}
}
