/*
* File: Vendor.java
* This class is created to handle all processing involved
* in a PT. MMI Research.
*
* Copyright (c) 2015 Mitra Mandiri Informatika
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
* Date Author Version Changes
* Apr 21, 2015	Agus Rochmad TR<mamat@mmi-pt.com> 		1.0 	Created
*/

/**
 * 
 */
package id.co.promise.procurement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@NamedQueries({
    @NamedQuery(name="VendorTipe.findAll",
                query="SELECT v FROM VendorTipe v")
}) 
@Table(name="T1_M_VENDOR_TIPE")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_M_VENDOR_TIPE", initialValue = 1, allocationSize = 1)
public class VendorTipe {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Column(name="VENDOR_TIPE_ID")
	private Integer id;
	
	@Column(name="NAMA", length=100)
	private String nama;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}
	
	
}
