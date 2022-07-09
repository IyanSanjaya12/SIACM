/*
* File: ItemWSDLImpl.java
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
* Nov 29, 2016 4:18:17 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.Item;

import java.util.List;

import javax.jws.WebService;

/**
 * @author Mamat
 *
 */
@WebService(targetNamespace = "http://promise.co.id/wsdl")
public interface ItemWSDLImpl {
	public List<Item> getItemList();
	public Item create(Item item, String token);
	public Item edit(Item item, String token);
	public Item delete(int itemId, String token);
}
