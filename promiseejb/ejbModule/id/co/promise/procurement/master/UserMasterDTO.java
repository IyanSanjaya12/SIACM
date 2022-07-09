/**
 * 
 */
package id.co.promise.procurement.master;

import java.util.List;

import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Role;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;

/**
 * @author PRG-AMD1
 *
 */
public class UserMasterDTO {
	private User user;
	
	//data dto untuk modul user
	private Integer roleId;
	private String roleUserString;
	private List<Role> rolePmList;
	private List<Role> roleCmList;
	private List<Role> roleList;
	private String organisasiString;
	private Integer organisasiId;
	private List<RoleUser> roleUser;
	
	//data dto untuk modul registrasi datauser
	private Vendor vendor;
	private String newPassword;
	
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	
	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public List<Role> getRolePmList() {
		return rolePmList;
	}

	public void setRolePmList(List<Role> rolePmList) {
		this.rolePmList = rolePmList;
	}

	public List<Role> getRoleCmList() {
		return roleCmList;
	}

	public void setRoleCmList(List<Role> roleCmList) {
		this.roleCmList = roleCmList;
	}

	public String getOrganisasiString() {
		return organisasiString;
	}

	public void setOrganisasiString(String organisasiString) {
		this.organisasiString = organisasiString;
	}

	public Integer getOrganisasiId() {
		return organisasiId;
	}

	public void setOrganisasiId(Integer organisasiId) {
		this.organisasiId = organisasiId;
	}

	public List<RoleUser> getRoleUser() {
		return roleUser;
	}

	public void setRoleUser(List<RoleUser> roleUser) {
		this.roleUser = roleUser;
	}

	public String getRoleUserString() {
		return roleUserString;
	}

	public void setRoleUserString(String roleUserString) {
		this.roleUserString = roleUserString;
	}
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	
}
