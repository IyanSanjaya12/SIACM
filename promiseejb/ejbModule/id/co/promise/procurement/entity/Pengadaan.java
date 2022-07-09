/*
 * File: Pengadaan.java
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
 * May 12, 2015	Agus Rochmad TR<mamat@mmi-pt.com> 		1.0 	Created
 */

/**
 * 
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

/**
 * @author Mamat
 *
 */
@Entity
@Table(name = "T4_PENGADAAN")
public class Pengadaan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PENGADAAN_ID")
	private Integer id;
	
	@Column(name = "PENGADAAN_PARENT_ID", nullable=true)
	private Integer parentId;
	
	@Column(name = "NOMOR_NOTA_DINAS ", length = 100)
	private String nomorNotaDinas;

	@Temporal(TemporalType.DATE)
	@Column(name = "TANGGAL_NOTA_DINAS")
	private Date tanggalNotaDinas;

	@Column(name = "NAMA_PENGADAAN", length = 200)
	private String namaPengadaan;

	@Column(name = "NOMOR_IZIN_PRINSIP", length = 100)
	private String nomorIzinPrinsip;

	@Column(name = "TANGGAL_IZIN_PRINSIP")
	private Date tanggalIzinPrinsip;

	@Column(name = "KETERANGAN")
	private String keterangan;

	@Column(name = "PPK")
	private String ppk;

	@Column(name = "PP")
	private String pp;

	@OneToOne
	@JoinColumn(name = "MATA_UANG_ID", referencedColumnName = "MATA_UANG_ID")
	private MataUang mataUang;

	@OneToOne
	@JoinColumn(name = "KUALIFIKASI_PENGADAAN_ID", referencedColumnName = "KUALIFIKASI_PENGADAAN_ID")
	private KualifikasiPengadaan kualifikasiPengadaan;

	@OneToOne
	@JoinColumn(name = "JENIS_PENGADAAN_ID", referencedColumnName = "JENIS_PENGADAAN_ID")
	private JenisPengadaan jenisPengadaan;

	@OneToOne
	@JoinColumn(name = "METODE_PENGADAAN_ID", referencedColumnName = "METODE_PENGADAAN_ID")
	private MetodePengadaan metodePengadaan;

	@OneToOne
	@JoinColumn(name = "JENIS_PENAWARAN_ID", referencedColumnName = "JENIS_PENAWARAN_ID")
	private JenisPenawaran jenisPenawaran;

	@OneToOne
	@JoinColumn(name = "KUALIFIKASI_VENDOR_ID", referencedColumnName = "KUALIFIKASI_VENDOR_ID")
	private KualifikasiVendor kualifikasiVendor;

	@ColumnDefault( value = "0" )
	@Column(name = "PROSENTASE_PAJAK_MATERIAL")
	private Double prosentasePajakMaterial;

	@ColumnDefault( value = "0" )
	@Column(name = "PROSENTASE_PAJAK_JASA")
	private Double prosentasePajakJasa;

	@ManyToOne
	@JoinColumn(name = "PANITIA_ID", referencedColumnName = "PANITIA_ID")
	private Panitia panitia;

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

	@Transient
	private Integer jumlahHariSla;

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getPpk() {
		return ppk;
	}

	public void setPpk(String ppk) {
		this.ppk = ppk;
	}

	public String getPp() {
		return pp;
	}

	public void setPp(String pp) {
		this.pp = pp;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomorNotaDinas() {
		return nomorNotaDinas;
	}

	public void setNomorNotaDinas(String nomorNotaDinas) {
		this.nomorNotaDinas = nomorNotaDinas;
	}

	public Date getTanggalNotaDinas() {
		return tanggalNotaDinas;
	}

	public void setTanggalNotaDinas(Date tanggalNotaDinas) {
		this.tanggalNotaDinas = tanggalNotaDinas;
	}

	public String getNamaPengadaan() {
		return namaPengadaan;
	}

	public void setNamaPengadaan(String namaPengadaan) {
		this.namaPengadaan = namaPengadaan;
	}

	public String getNomorIzinPrinsip() {
		return nomorIzinPrinsip;
	}

	public void setNomorIzinPrinsip(String nomorIzinPrinsip) {
		this.nomorIzinPrinsip = nomorIzinPrinsip;
	}

	public Date getTanggalIzinPrinsip() {
		return tanggalIzinPrinsip;
	}

	public void setTanggalIzinPrinsip(Date tanggalIzinPrinsip) {
		this.tanggalIzinPrinsip = tanggalIzinPrinsip;
	}

	public MataUang getMataUang() {
		return mataUang;
	}

	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
	}

	public KualifikasiPengadaan getKualifikasiPengadaan() {
		return kualifikasiPengadaan;
	}

	public void setKualifikasiPengadaan(
			KualifikasiPengadaan kualifikasiPengadaan) {
		this.kualifikasiPengadaan = kualifikasiPengadaan;
	}

	public JenisPengadaan getJenisPengadaan() {
		return jenisPengadaan;
	}

	public void setJenisPengadaan(JenisPengadaan jenisPengadaan) {
		this.jenisPengadaan = jenisPengadaan;
	}

	public MetodePengadaan getMetodePengadaan() {
		return metodePengadaan;
	}

	public void setMetodePengadaan(MetodePengadaan metodePengadaan) {
		this.metodePengadaan = metodePengadaan;
	}

	public JenisPenawaran getJenisPenawaran() {
		return jenisPenawaran;
	}

	public void setJenisPenawaran(JenisPenawaran jenisPenawaran) {
		this.jenisPenawaran = jenisPenawaran;
	}
	
	public KualifikasiVendor getKualifikasiVendor() {
		return kualifikasiVendor;
	}

	public void setKualifikasiVendor(KualifikasiVendor kualifikasiVendor) {
		this.kualifikasiVendor = kualifikasiVendor;
	}
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Double getProsentasePajakMaterial() {
		return prosentasePajakMaterial;
	}

	public void setProsentasePajakMaterial(Double prosentasePajakMaterial) {
		this.prosentasePajakMaterial = prosentasePajakMaterial;
	}

	public Double getProsentasePajakJasa() {
		return prosentasePajakJasa;
	}

	public void setProsentasePajakJasa(Double prosentasePajakJasa) {
		this.prosentasePajakJasa = prosentasePajakJasa;
	}

	public Panitia getPanitia() {
		return panitia;
	}

	public void setPanitia(Panitia panitia) {
		this.panitia = panitia;
	}
	
	public Integer getJumlahHariSla() {
		return jumlahHariSla;
	}

	public void setJumlahHariSla(Integer jumlahHariSla) {
		this.jumlahHariSla = jumlahHariSla;
	}
	
}
