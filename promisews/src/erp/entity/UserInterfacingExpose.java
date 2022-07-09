package erp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInterfacingExpose {
	
	//@JsonProperty("ID_USER")
	@JsonProperty("id_user")
	private String id_user;
	
	//@JsonProperty("NIPP")
	@JsonProperty("nipp")
	private String nipp;
	
	//@JsonProperty("ID_JABATAN")
	@JsonProperty("id_jabatan")
	private String id_jabatan;
	
	//@JsonProperty("NM_JABATAN")
	@JsonProperty("nm_jabatan")
	private String nm_jabatan;
	
	//@JsonProperty("ID_CABANG")
	@JsonProperty("id_cabang")
	private String id_cabang;
	
	//@JsonProperty("NAMA_CABANG")
	@JsonProperty("nama_cabang")
	private String nama_cabang;
	
	
	//@JsonProperty("NAMA")
	@JsonProperty("nama")
	private String nama;
	
	//@JsonProperty("NM_GROUP")
	@JsonProperty("nm_group")
	private String nm_group;
	
	//@JsonProperty("ID_UNIT_KERJA")
	@JsonProperty("id_unit_kerja")
	private String id_unit_kerja;
	
	//@JsonProperty("ID_PERUSAHAAN")
	@JsonProperty("id_perusahaan")
	private String id_perusahaan;
	
	//@JsonProperty("NM_UNIT")
	@JsonProperty("nm_unit")
	private String nm_unit;
	
	//@JsonProperty("TIPE")
	@JsonProperty("tipe")
	private String tipe;
	
	@JsonProperty("EMP_NUMBER")
	private String EMP_NUMBER;

	public String getId_user() {
		return id_user;
	}

	public void setId_user(String id_user) {
		this.id_user = id_user;
	}

	public String getNipp() {
		return nipp;
	}

	public void setNipp(String nipp) {
		this.nipp = nipp;
	}

	public String getId_jabatan() {
		return id_jabatan;
	}

	public void setId_jabatan(String id_jabatan) {
		this.id_jabatan = id_jabatan;
	}

	public String getNm_jabatan() {
		return nm_jabatan;
	}

	public void setNm_jabatan(String nm_jabatan) {
		this.nm_jabatan = nm_jabatan;
	}

	public String getId_cabang() {
		return id_cabang;
	}

	public void setId_cabang(String id_cabang) {
		this.id_cabang = id_cabang;
	}

	public String getNama_cabang() {
		return nama_cabang;
	}

	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getNm_group() {
		return nm_group;
	}

	public void setNm_group(String nm_group) {
		this.nm_group = nm_group;
	}

	public String getId_unit_kerja() {
		return id_unit_kerja;
	}

	public void setId_unit_kerja(String id_unit_kerja) {
		this.id_unit_kerja = id_unit_kerja;
	}

	public String getId_perusahaan() {
		return id_perusahaan;
	}

	public void setId_perusahaan(String id_perusahaan) {
		this.id_perusahaan = id_perusahaan;
	}

	public String getNm_unit() {
		return nm_unit;
	}

	public void setNm_unit(String nm_unit) {
		this.nm_unit = nm_unit;
	}

	public String getTipe() {
		return tipe;
	}

	public void setTipe(String tipe) {
		this.tipe = tipe;
	}

	public String getEmpNumber() {
		return EMP_NUMBER;
	}

	public void setEmpNumber(String empNumber) {
		this.EMP_NUMBER = empNumber;
	}

	
	
}
