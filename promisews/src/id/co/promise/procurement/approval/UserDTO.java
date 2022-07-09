package id.co.promise.procurement.approval;

import id.co.promise.procurement.entity.RoleUser;

public class UserDTO {
	private Integer userId;
	private String namaPengguna;
	private String userName;
	private Integer organisasiId;
	private String organisasiName;

	public UserDTO(RoleUser roleUser) {
		this.userId = roleUser.getUser().getId();
		this.userName = roleUser.getUser().getUsername();
		this.namaPengguna = roleUser.getUser().getNamaPengguna();
		if (roleUser.getOrganisasi() != null) {
			this.organisasiId = roleUser.getOrganisasi().getId();
			this.organisasiName = roleUser.getOrganisasi().getNama();
		} else {
			this.organisasiId = 0;
			this.organisasiName = "n/a";
		}
	}

	public Integer getUserId() {
		return userId;
	}

	public String getNamaPengguna() {
		return namaPengguna;
	}

	public String getUserName() {
		return userName;
	}

	public Integer getOrganisasiId() {
		return organisasiId;
	}

	public String getOrganisasiName() {
		return organisasiName;
	}

	public String getNamaPenggunaConcateOrganisasiName() {
		return this.namaPengguna + " - " + this.organisasiName;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setNamaPengguna(String namaPengguna) {
		this.namaPengguna = namaPengguna;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setOrganisasiId(Integer organisasiId) {
		this.organisasiId = organisasiId;
	}

	public void setOrganisasiName(String organisasiName) {
		this.organisasiName = organisasiName;
	}
	

}
