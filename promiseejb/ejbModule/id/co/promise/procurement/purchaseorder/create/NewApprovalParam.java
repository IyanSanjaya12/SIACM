package id.co.promise.procurement.purchaseorder.create;

import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Role;
import id.co.promise.procurement.entity.User;

// new Approval
public class NewApprovalParam {
	private Integer id;
	private User user;
	private Role role;
	private Organisasi organisasi;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
	}

}
