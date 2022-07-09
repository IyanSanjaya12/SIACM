package id.co.promise.procurement.menu;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.entity.Menu;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.CacheManager;

@Stateless
@Path(value = "/procurement/menu/menu")
@Produces(MediaType.APPLICATION_JSON)
public class MenuServices {

	@EJB
	private MenuSession menuSession;
	@EJB
	private RoleUserSession roleUserSession;
	@EJB
	TokenSession tokenSession;

	/*@Path("/get-menu/{id}")
	@GET
	public Menu getMenu(@PathParam("id") int id) {
		return menuSession.getMenu(id);
	}*/

	@Path("/get-list")
	@GET
	public List<Menu> getMenulist() {
		return menuSession.getMenuList();
	}

	@Path("/get-list-by-parentId/{parentId}")
	@GET
	public List<Menu> getMenuListByParentId(@PathParam("parentId") Integer parentId) {
		return menuSession.getMenuListByParentId(parentId);
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(Menu menu, @HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";

		try {
			Boolean isSaveNama = menuSession.checkNamaMenu(menu.getNama(), toDo, menu.getId());
	
			if (!isSaveNama) {
				String errorNama = "Nama Menu Tidak Boleh Sama";
				Boolean isValid = false;
				map.put("isValid", isValid);
				map.put("errorNama", errorNama);
			}
	
			if (isSaveNama) {
				menuSession.insertMenu(menu, tokenSession.findByToken(token));
			}
			
			map.put("Menu", menu);
			
			CacheManager.deleteAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/update")
	@POST
	public Map update(Menu menu, @HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";
		try {
			Boolean isSaveNama = menuSession.checkNamaMenu(menu.getNama(), toDo, menu.getId());
	
			if (!isSaveNama) {
				String errorNama = "Nama Menu Tidak Boleh Sama";
				Boolean isValid = false;
				map.put("isValid", isValid);
				map.put("errorNama", errorNama);
			}
	
			if (isSaveNama) {
				menuSession.updateMenu(menu, tokenSession.findByToken(token));
			}
	
			map.put("Menu", menu);
		
			CacheManager.deleteAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}

	@Path("/delete")
	@POST
	public Menu delete(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		
		Menu menu = new Menu();
		try {
			menu = menuSession.deleteMenu(id, tokenSession.findByToken(token));
			CacheManager.deleteAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return menu;
	}

	@Path("/deleteRow/{id}")
	@GET
	public Menu deleteRowMenu(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		Menu menu = new Menu();
		try {
			menu = menuSession.deleteRowMenu(id, tokenSession.findByToken(token));
			CacheManager.deleteAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return menu;
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	@Path("/get-tree-by-token")
	@GET
	public List<Menu> getMenuList(
			@HeaderParam("Authorization") String tokenStr) {
		Token token = tokenSession.findByToken(tokenStr);
		RoleUser roleUser = roleUserSession.getByToken(token);
		
		String key = "mn_" + roleUser.getRole().getCode().toLowerCase();
		List<Menu> menuList = (List<Menu>) CacheManager.getByKey(key, List.class);
		if (menuList == null) {
			menuList = menuSession.getMenuListTreeByRole(roleUser.getRole().getId());
			CacheManager.saveByKey(key, menuList);
		}
		return menuList;
	}
}
