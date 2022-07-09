/*
 * File: PerolehanPengadaanSatuan.java
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
 * Dec 9, 2016 1:03:50 PM 	Mamat 		1.0 		Created
 * method
 */
package id.co.promise.procurement.entity;

import java.util.Date;

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

import org.hibernate.annotations.ColumnDefault;

/**
 * @author Mamat
 *
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "PerolehanPengadaanSatuan.find", query = "SELECT o FROM PerolehanPengadaanSatuan o WHERE o.isDelete = 0"),
		@NamedQuery(name = "PerolehanPengadaanSatuan.findByPengadaan", query = "SELECT o FROM PerolehanPengadaanSatuan o WHERE o.isDelete=0 AND o.itemPengadaan.pengadaan.id=:pengadaanId"),
		@NamedQuery(name = "PerolehanPengadaanSatuan.findByPengadaanAndItemId", query = "SELECT o FROM PerolehanPengadaanSatuan o WHERE o.isDelete=0 AND o.itemPengadaan.pengadaan.id=:pengadaanId  AND o.itemPengadaan.item.id=:itemId"),
		@NamedQuery(name = "PerolehanPengadaanSatuan.findByPengadaanVendorAndItemId", query = "SELECT o FROM PerolehanPengadaanSatuan o WHERE o.isDelete=0 AND o.itemPengadaan.pengadaan.id=:pengadaanId AND o.vendor.id=:vendorId AND o.itemPengadaan.item.id=:itemId"),
		@NamedQuery(name = "PerolehanPengadaanSatuan.findByPengadaanAndVendor", query = "SELECT o FROM PerolehanPengadaanSatuan o WHERE o.isDelete=0 AND o.itemPengadaan.pengadaan.id=:pengadaanId AND o.vendor.id=:vendorId"),
		@NamedQuery(name = "PerolehanPengadaanSatuan.findByPengadaanVendorAndItemPengadaan", query = "SELECT o FROM PerolehanPengadaanSatuan o WHERE o.isDelete=0 AND o.itemPengadaan.pengadaan.id=:pengadaanId AND o.vendor.id=:vendorId AND o.itemPengadaan.id=:itemPengadaanId") })
@Table(name = "T5_PEROLEHAN_P_SATUAN")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_PEROLEHAN_P_SATUAN", initialValue = 1, allocationSize = 1)
public class PerolehanPengadaanSatuan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PEROLEHAN_P_SATUAN_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "ITEM_PENGADAAN_ID", referencedColumnName = "ITEM_PENGADAAN_ID")
	private ItemPengadaan itemPengadaan;

	@ManyToOne
	@JoinColumn(name = "MATA_UANG_ID", referencedColumnName = "MATA_UANG_ID")
	private MataUang mataUang;

	@ColumnDefault( value = "0" )
	@Column(name = "NILAI_SATUAN")
	private Double nilaiSatuan;

	@ColumnDefault( value = "0" )
	@Column(name = "JUMLAH")
	private Double jumlah;

	@ColumnDefault( value = "0" )
	@Column(name = "NILAI_TOTAL")
	private Double nilaiTotal;

	@ColumnDefault( value = "0" )
	@Column(name = "PAJAK_PROSENTASE")
	private Double pajakProsentase;

	@ColumnDefault( value = "0" )
	@Column(name = "PAJAK")
	private Double pajak;

	@Column(name = "KETERANGAN")
	private String keterangan;

	@ManyToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;

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
	 * @return the nilaiSatuan
	 */
	public Double getNilaiSatuan() {
		return nilaiSatuan;
	}

	/**
	 * @param nilaiSatuan
	 *            the nilaiSatuan to set
	 */
	public void setNilaiSatuan(Double nilaiSatuan) {
		this.nilaiSatuan = nilaiSatuan;
	}

	/**
	 * @return the jumlah
	 */
	public Double getJumlah() {
		return jumlah;
	}

	/**
	 * @param jumlah
	 *            the jumlah to set
	 */
	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}

	/**
	 * @return the nilaiTotal
	 */
	public Double getNilaiTotal() {
		return nilaiTotal;
	}

	/**
	 * @param nilaiTotal
	 *            the nilaiTotal to set
	 */
	public void setNilaiTotal(Double nilaiTotal) {
		this.nilaiTotal = nilaiTotal;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the itemPengadaan
	 */
	public ItemPengadaan getItemPengadaan() {
		return itemPengadaan;
	}

	/**
	 * @param itemPengadaan
	 *            the itemPengadaan to set
	 */
	public void setItemPengadaan(ItemPengadaan itemPengadaan) {
		this.itemPengadaan = itemPengadaan;
	}

	/**
	 * @return the mataUang
	 */
	public MataUang getMataUang() {
		return mataUang;
	}

	/**
	 * @param mataUang
	 *            the mataUang to set
	 */
	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
	}

	/**
	 * @return the pajakProsentase
	 */
	public Double getPajakProsentase() {
		return pajakProsentase;
	}

	/**
	 * @param pajakProsentase
	 *            the pajakProsentase to set
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
	 * @param pajak
	 *            the pajak to set
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
	 * @param keterangan
	 *            the keterangan to set
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
	 * @param vendor
	 *            the vendor to set
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
	 * @param created
	 *            the created to set
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
	 * @param updated
	 *            the updated to set
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
	 * @param deleted
	 *            the deleted to set
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
	 * @param userId
	 *            the userId to set
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
	 * @param isDelete
	 *            the isDelete to set
	 */
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

}
