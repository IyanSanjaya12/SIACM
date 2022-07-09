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

@Entity
@Table(name = "T2_ROLE_MENU")
@NamedQueries({
		@NamedQuery(name = "RoleMenu.find", query = "SELECT m FROM RoleMenu m WHERE m.isDelete = 0"),
		@NamedQuery(name = "RoleMenuListByRole.find", query = "SELECT m FROM RoleMenu m WHERE m.isDelete = 0 AND m.role.id=:roleId"),
		@NamedQuery(name = "RoleMenuListByRole.findByRoleIdAndMenuId", query = "SELECT roleMenu FROM RoleMenu roleMenu WHERE roleMenu.isDelete = 0 AND roleMenu.role.id=:roleId AND roleMenu.menu.id=:menuId"),
		@NamedQuery(name = "RoleMenuRootListByRole.find",
			query = "SELECT m FROM Menu m where m.isDelete = 0 AND "
				+ "m.parentId is null and m.id IN ( select mmm.parentId from  Menu mmm, RoleMenu rrm where rrm.isDelete=0 AND mmm.id = rrm.menu.id and rrm.role.id  =:roleId ) "
				+ "ORDER BY m.urutan") })

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_ROLE_MENU", initialValue = 1, allocationSize = 1)
public class RoleMenu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROLE_MENU_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")
	private Role role;

	@ManyToOne
	@JoinColumn(name = "MENU_ID", referencedColumnName = "MENU_ID")
	private Menu menu;

	@Column(name = "SEQUENCE")
	private Integer sequence;

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

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
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

}
