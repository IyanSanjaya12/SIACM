package erp.interfacing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class PostLoginUserDetailERPResponse {
	
	@SerializedName("idUser")
	private String idUser;
	
	@SerializedName("nipp")
	private String nipp;
	
	@SerializedName("idJabatan")
	private String idJabatan;
	
	@SerializedName("nmJabatan")
	private String nmJabatan;
	
	@SerializedName("idCabang")
	private String idCabang;
	
	@SerializedName("namaCabang")
	private String namaCabang;
	
	@SerializedName("nama")
	private String nama;
	
	@SerializedName("nmGroup")
	private String nmGroup;
	
	@SerializedName("idUnitKerja")
	private String idUnitKerja;
	
	@SerializedName("idPerusahaan")
	private String idPerusahaan;
	
	@SerializedName("nmUnit")
	private String nmUnit;
	
	@SerializedName("tipe")
	private String tipe;

	public String getIdUser() {
		return idUser;
	}

	public String getNipp() {
		return nipp;
	}

	public String getIdJabatan() {
		return idJabatan;
	}

	public String getNmJabatan() {
		return nmJabatan;
	}

	public String getIdCabang() {
		return idCabang;
	}

	public String getNamaCabang() {
		return namaCabang;
	}

	public String getNama() {
		return nama;
	}

	public String getNmGroup() {
		return nmGroup;
	}

	public String getIdUnitKerja() {
		return idUnitKerja;
	}

	public String getIdPerusahaan() {
		return idPerusahaan;
	}

	public String getNmUnit() {
		return nmUnit;
	}

	public String getTipe() {
		return tipe;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public void setNipp(String nipp) {
		this.nipp = nipp;
	}

	public void setIdJabatan(String idJabatan) {
		this.idJabatan = idJabatan;
	}

	public void setNmJabatan(String nmJabatan) {
		this.nmJabatan = nmJabatan;
	}

	public void setIdCabang(String idCabang) {
		this.idCabang = idCabang;
	}

	public void setNamaCabang(String namaCabang) {
		this.namaCabang = namaCabang;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public void setNmGroup(String nmGroup) {
		this.nmGroup = nmGroup;
	}

	public void setIdUnitKerja(String idUnitKerja) {
		this.idUnitKerja = idUnitKerja;
	}

	public void setIdPerusahaan(String idPerusahaan) {
		this.idPerusahaan = idPerusahaan;
	}

	public void setNmUnit(String nmUnit) {
		this.nmUnit = nmUnit;
	}

	public void setTipe(String tipe) {
		this.tipe = tipe;
	}

}
