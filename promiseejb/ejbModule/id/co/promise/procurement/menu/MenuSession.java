package id.co.promise.procurement.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Menu;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class MenuSession extends AbstractFacadeWithAudit<Menu> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public MenuSession() {
		super(Menu.class);
	}

	public Menu getMenu(int id) {
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Menu> getMenuListByRole(Integer roleId) {
		Query q = em.createNamedQuery("Menu.findByRole").setParameter("roleId",
				roleId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Menu> getMenuList() {
		Query q = em.createNamedQuery("Menu.find");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Menu> getMenuListByParentId(Integer parentId) {
		Query q = em.createNamedQuery("Menu.findByParentId").setParameter(
				"parentId", parentId);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Boolean checkNamaMenu(String nama, String toDo, Integer MenuId) {
		Query q = em.createNamedQuery("Menu.findNama");
		q.setParameter("nama", nama);
		List<Menu> menuList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (menuList != null && menuList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (menuList != null && menuList.size() > 0) {
				if (MenuId.equals(menuList.get(0).getId())) {
					isSave = true;
				} else {
					isSave = false;
				}
			} else {
				isSave = true;
			}
		}

		return isSave;

	}

	public Menu insertMenu(Menu menu, Token token) {
		menu.setCreated(new Date());
		menu.setIsDelete(0);
		super.create(menu, AuditHelper.OPERATION_CREATE, token);
		return menu;
	}

	public Menu updateMenu(Menu menu, Token token) {
		menu.setUpdated(new Date());
		super.edit(menu, AuditHelper.OPERATION_UPDATE, token);
		return menu;
	}

	public Menu deleteMenu(int menuId, Token token) {
		Menu menu = super.find(menuId);
		menu.setIsDelete(1);
		menu.setDeleted(new Date());
		super.edit(menu, AuditHelper.OPERATION_DELETE, token);
		return menu;
	}

	public Menu deleteRowMenu(int menuId, Token token) {
		Menu menu = super.find(menuId);
		super.remove(menu, AuditHelper.OPERATION_ROW_DELETE, token);
		return menu;
	}
	
	public List<Menu> getMenuListTreeByRole(Integer roleId) {

		// get leaf

		List<Menu> menuList = getMenuListByRole(roleId);

		// find parent
		List<Menu> parentMenuList = new ArrayList<Menu>();
		for (Menu menu : menuList) {
			while (menu.getParentId() != null) {
				menu = getMenu(menu.getParentId());
			}
			if (!parentMenuList.contains(menu)) {
				parentMenuList.add(menu);
			}
		}

		// Order by sequence
		if (parentMenuList.size() > 0) {
			Collections.sort(parentMenuList, new Comparator<Menu>() {
				@Override
				public int compare(final Menu object1, final Menu object2) {
					return object1.getUrutan().compareTo(
							object2.getUrutan());
				}
			});
		}

		List<Menu> treeMenuList = new ArrayList<Menu>();
		for (Menu menu : parentMenuList) {
			treeMenuList.add(buildMenu(menu, roleId));
		}

		List<Menu> cleanMenuList = new ArrayList<Menu>();

		for (Menu menu : treeMenuList) {
			cleanMenuList.add(clearEmptyParentMenu(menu));
		}

		return cleanMenuList;
	}

	public Menu buildMenu(Menu menu, Integer roleId) {
		List<Menu> childList = getMenuListByParentId(menu.getId());
		List<Menu> menuListByRole = getMenuListByRole(roleId);

		if (childList.size() > 0) {
			List<Menu> newChildList = new ArrayList<Menu>();
			for (Menu child : childList) {
				List<Menu> numChild = getMenuListByParentId(child.getId());

				// if menu is folder
				if (numChild.size() > 0) {
					buildMenu(child, roleId);
					newChildList.add(child);

				} else
				// if menu is Leaf
				{
					if (menuListByRole.contains(child)) {
						newChildList.add(child);

					}
				}

			}
			menu.setChildMenuList(newChildList);
		}

		return menu;

	}

	public Menu clearEmptyParentMenu(Menu menu) {
		List<Menu> childList = menu.getChildMenuList();
		if (childList != null) {
			List<Menu> newChildList = new ArrayList<Menu>();
			for (Menu child : childList) {
				if (child.getChildMenuList() != null
						&& child.getChildMenuList().size() > 0) {
					clearEmptyParentMenu(child);
					newChildList.add(child);
				} else {
					if (child.getPath() != null) {

						newChildList.add(child);
					}
				}
			}
			menu.setChildMenuList(newChildList);
		}
		return menu;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
}
