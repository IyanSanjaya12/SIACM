package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.Vendor;

public class VendorDTO {
	private Integer id;
	private String nama;
	private String alamat;
	private String nomorTelpon;
	private String email;
	private String npwp;
	private String penanggungJawab;
	private int user;
	private Integer status;

	public VendorDTO(Vendor vendor) {
		this.id = vendor.getId();
		this.nama = vendor.getNama();
		this.alamat = vendor.getAlamat();
		this.nomorTelpon = vendor.getNomorTelpon();
		this.email = vendor.getEmail();
		this.npwp = vendor.getNpwp();
		this.penanggungJawab = vendor.getPenanggungJawab();
		this.user = vendor.getUser();
		this.status = vendor.getStatus();
	}

	public Integer getId() {
		return id;
	}

	public String getNama() {
		return nama;
	}

	public String getAlamat() {
		return alamat;
	}

	public String getNomorTelpon() {
		return nomorTelpon;
	}

	public String getEmail() {
		return email;
	}

	public String getNpwp() {
		return npwp;
	}

	public String getPenanggungJawab() {
		return penanggungJawab;
	}

	public int getUser() {
		return user;
	}

	public Integer getStatus() {
		return status;
	}

}
