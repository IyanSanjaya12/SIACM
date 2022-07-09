package id.co.promise.procurement.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T1_MENU")
@NamedQueries({
		@NamedQuery(name = "Menu.find", query = "SELECT m FROM Menu m WHERE m.isDelete = 0 AND m.parentId is not null "),
		@NamedQuery(name = "Menu.findByParentId", query = "SELECT  m FROM Menu m WHERE m.isDelete = 0 AND m.parentId =:parentId ORDER BY m.urutan"),
		@NamedQuery(name = "Menu.findNama", query = "select menu FROM Menu menu where menu.isDelete=0 and menu.nama = :nama"),
		@NamedQuery(name="Menu.findByRole",
		query="SELECT  m FROM Menu m, RoleMenu rm WHERE m.isDelete = 0 AND rm.isDelete = 0 AND rm.menu.id = m.id  AND rm.role.id=:roleId order by urutan")})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_MENU", initialValue = 1, allocationSize = 1)
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MENU_ID")
	private Integer id;

	@Column(name = "PARENT_ID")
	private Integer parentId;

	@Column(name = "NAMA")
	private String nama;

	@Column(name = "ICON")
	private String icon;

	@Column(name = "PATH")
	private String path;

	@ColumnDefault( value = "0" )
	@Column(name = "URUTAN")
	private Integer urutan;

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

	@Column(name = "ISDELETE")
	private Integer isDelete;

	@Column(name = "MENU_TRANSLATE")
	private String menuTranslate;

	@Transient
	private List<Menu> childMenuList;

	public List<Menu> getChildMenuList() {
		return childMenuList;
	}

	public void setChildMenuList(List<Menu> childMenuList) {
		this.childMenuList = childMenuList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getUrutan() {
		return urutan;
	}

	public void setUrutan(Integer urutan) {
		this.urutan = urutan;
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

	public String getMenuTranslate() {
		return menuTranslate;
	}

	public void setMenuTranslate(String menuTranslate) {
		this.menuTranslate = menuTranslate;
	}

}
