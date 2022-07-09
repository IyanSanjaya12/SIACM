/*
* File: AlokasiAnggaranPaginationDTO.java
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
* Jan 30, 2017 1:45:30 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.alokasianggaran;

import id.co.promise.procurement.entity.AlokasiAnggaran;

import java.util.List;

/**
 * @author Mamat
 *
 */
public class AlokasiAnggaranPaginationDTO {
	private Integer jmlData;
	private Integer startIndex;
	private Integer endIndex;
	private List<AlokasiAnggaran> alokasiAnggaranList;
	
	
	/**
	 * @param jmlData
	 * @param startIndex
	 * @param endIndex
	 * @param alokasiAnggaranList
	 */
	public AlokasiAnggaranPaginationDTO(Integer jmlData, Integer startIndex,
			Integer endIndex, List<AlokasiAnggaran> alokasiAnggaranList) {
		super();
		this.jmlData = jmlData;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.alokasiAnggaranList = alokasiAnggaranList;
	}
	/**
	 * @return the jmlData
	 */
	public Integer getJmlData() {
		return jmlData;
	}
	/**
	 * @param jmlData the jmlData to set
	 */
	public void setJmlData(Integer jmlData) {
		this.jmlData = jmlData;
	}
	/**
	 * @return the startIndex
	 */
	public Integer getStartIndex() {
		return startIndex;
	}
	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	/**
	 * @return the endIndex
	 */
	public Integer getEndIndex() {
		return endIndex;
	}
	/**
	 * @param endIndex the endIndex to set
	 */
	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}
	/**
	 * @return the alokasiAnggaranList
	 */
	public List<AlokasiAnggaran> getAlokasiAnggaranList() {
		return alokasiAnggaranList;
	}
	/**
	 * @param alokasiAnggaranList the alokasiAnggaranList to set
	 */
	public void setAlokasiAnggaranList(List<AlokasiAnggaran> alokasiAnggaranList) {
		this.alokasiAnggaranList = alokasiAnggaranList;
	}
	
	
}
