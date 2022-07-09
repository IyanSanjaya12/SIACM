package erp.interfacing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetUserActiveERPResponse {

	private String idUser;
	
	private String nipp;
	
	private String idJabatan;
	
	private String nmJabatan;
	
	private String idCabang;
	
	private String namaCabang;
	
	private String nama;
	
	private String nmGroup;
	
	private String idUnitKerja;
	
	private String idPerusahaan;
	
	private String nmUnit;
	
	private String tipe;
	
	private String email;

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getNipp() {
		return nipp;
	}

	public void setNipp(String nipp) {
		this.nipp = nipp;
	}

	public String getIdJabatan() {
		return idJabatan;
	}

	public void setIdJabatan(String idJabatan) {
		this.idJabatan = idJabatan;
	}

	public String getNmJabatan() {
		return nmJabatan;
	}

	public void setNmJabatan(String nmJabatan) {
		this.nmJabatan = nmJabatan;
	}

	public String getIdCabang() {
		return idCabang;
	}

	public void setIdCabang(String idCabang) {
		this.idCabang = idCabang;
	}

	public String getNamaCabang() {
		return namaCabang;
	}

	public void setNamaCabang(String namaCabang) {
		this.namaCabang = namaCabang;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getnmGroup() {
		return nmGroup;
	}

	public void setnGroup(String nmGroup) {
		this.nmGroup = nmGroup;
	}

	public String getIdUnitKerja() {
		return idUnitKerja;
	}

	public void setIdUnitKerja(String idUnitKerja) {
		this.idUnitKerja = idUnitKerja;
	}

	public String getIdPerusahaan() {
		return idPerusahaan;
	}

	public void setIdPerusahaan(String idPerusahaan) {
		this.idPerusahaan = idPerusahaan;
	}

	public String getNmUnit() {
		return nmUnit;
	}

	public void setNmUnit(String nmUnit) {
		this.nmUnit = nmUnit;
	}

	public String getTipe() {
		return tipe;
	}

	public void setTipe(String tipe) {
		this.tipe = tipe;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
