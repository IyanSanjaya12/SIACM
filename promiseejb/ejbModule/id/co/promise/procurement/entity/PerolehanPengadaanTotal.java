/*
* File: PerolehanPengadaanTotal.java
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
* Dec 9, 2016 12:57:46 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

/**
 * @author Mamat
 *
 */
@Entity //findByPengadaan
@NamedQueries({ @NamedQuery(name = "PerolehanPengadaanTotal.find", query = "SELECT o FROM PerolehanPengadaanTotal o WHERE o.isDelete = 0"),
	@NamedQuery(name = "PerolehanPengadaanTotal.findByPengadaan", query = "SELECT o FROM PerolehanPengadaanTotal o WHERE o.isDelete = 0 AND o.pengadaan.id=:pengadaanId"),
	@NamedQuery(name="PerolehanPengadaanTotal.findByPengadaanAndVendor", query="SELECT o FROM PerolehanPengadaanTotal o WHERE o.isDelete = 0 AND o.pengadaan.id=:pengadaanId AND o.vendor.id=:vendorId"),
	@NamedQuery(name="PerolehanPengadaanTotal.findTotalPerolehanByVendorId", 
		query="SELECT SUM(o.nilai) FROM PerolehanPengadaanTotal o WHERE o.isDelete = 0 AND o.vendor.id=:vendorId"),
	@NamedQuery(name="PerolehanPengadaanTotal.getPengadaanId", 
		query="SELECT o.pengadaan.id FROM PerolehanPengadaanTotal o WHERE o.isDelete = 0")
})
@Table(name = "T5_PEROLEHAN_P_TOTAL")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_PEROLEHAN_P_TOTAL", initialValue = 1, allocationSize = 1)
public class PerolehanPengadaanTotal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PEROLEHAN_P_TOTAL_ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")
	private Pengadaan pengadaan;
	
	@ManyToOne
	@JoinColumn(name = "MATA_UANG_ID", referencedColumnName = "MATA_UANG_ID")
	private MataUang mataUang;
	
	@ColumnDefault( value = "0" )
	@Column(name="NILAI")
	private Double nilai;
	
	@ColumnDefault( value = "0" )
	@Column(name="PAJAK_PROSENTASE")
	private Double pajakProsentase;
	
	@ColumnDefault( value = "0" )
	@Column(name="PAJAK")
	private Double pajak;
	
	@Column(name="KETERANGAN")
	private String keterangan;
	
	@ManyToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	@Transient
	private List<ItemPengadaan> itemPengadaanList;
	
	@Transient
	private Double totalHPS;
	
	public List<ItemPengadaan> getItemPengadaanList() {
		return itemPengadaanList;
	}

	public void setItemPengadaanList(List<ItemPengadaan> itemPengadaanList) {
		this.itemPengadaanList = itemPengadaanList;
	}

	public Double getTotalHPS() {
		return totalHPS;
	}

	public void setTotalHPS(Double totalHPS) {
		this.totalHPS = totalHPS;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED")
	private Date deleted;

	@Column(name = "USERID")
	private Integer userId;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the pengadaan
	 */
	public Pengadaan getPengadaan() {
		return pengadaan;
	}

	/**
	 * @param pengadaan the pengadaan to set
	 */
	public void setPengadaan(Pengadaan pengadaan) {
		this.pengadaan = pengadaan;
	}

	/**
	 * @return the mataUang
	 */
	public MataUang getMataUang() {
		return mataUang;
	}

	/**
	 * @param mataUang the mataUang to set
	 */
	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
	}

	/**
	 * @return the nilai
	 */
	public Double getNilai() {
		return nilai;
	}

	/**
	 * @param nilai the nilai to set
	 */
	public void setNilai(Double nilai) {
		this.nilai = nilai;
	}

	/**
	 * @return the pajakProsentase
	 */
	public Double getPajakProsentase() {
		return pajakProsentase;
	}

	/**
	 * @param pajakProsentase the pajakProsentase to set
	 */
	public void setPajakProsentase(Double pajakProsentase) {
		this.pajakProsentase = pajakProsentase;
	}

	/**
	 * @return the pajak
	 */
	public Double getPajak() {
		return pajak;
	}

	/**
	 * @param pajak the pajak to set
	 */
	public void setPajak(Double pajak) {
		this.pajak = pajak;
	}

	/**
	 * @return the keterangan
	 */
	public String getKeterangan() {
		return keterangan;
	}

	/**
	 * @param keterangan the keterangan to set
	 */
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	/**
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	/**
	 * @return the deleted
	 */
	public Date getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the isDelete
	 */
	public Integer getIsDelete() {
		return isDelete;
	}

	/**
	 * @param isDelete the isDelete to set
	 */
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
	
}
