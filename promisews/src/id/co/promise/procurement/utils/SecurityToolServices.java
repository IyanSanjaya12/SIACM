package id.co.promise.procurement.utils;

import id.co.promise.procurement.entity.Aksi;
import id.co.promise.procurement.entity.MenuAksi;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.menu.AksiSession;
import id.co.promise.procurement.menu.MenuAksiSession;
import id.co.promise.procurement.menu.MenuSession;
import id.co.promise.procurement.menu.RoleMenuSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

@Stateless
@Path(value = "/procurement/menu/securityToolServices")
@Produces(MediaType.APPLICATION_JSON)
public class SecurityToolServices {
	final static Logger logger = Logger.getLogger(SecurityToolServices.class);

	@EJB
	private AksiSession aksiSession;

	@EJB
	private MenuSession menuSession;

	@EJB
	private RoleMenuSession roleMenuSession;

	@EJB
	private MenuAksiSession menuAksiSession;

	@EJB
	TokenSession tokenSession;
	@EJB
	RoleUserSession roleUserSession;

	@Path("/insertSecurity")
	@POST
	public void insertSecurity(@FormParam("token") String token,
			@FormParam("activeMenuId") Integer activeMenuId,
			@FormParam("requestPath") String requestPath) {
		
		logger.info("==> IS_INSERT_SECURITY_SERVICES?" + ParamContext.getParameterBooleanByName("IS_INSERT_SECURITY_SERVICES"));
		logger.info("token?" + token);
		logger.info("activeMenuId?" + activeMenuId);

		if ((ParamContext.getParameterBooleanByName("IS_INSERT_SECURITY_SERVICES") && (!token.equalsIgnoreCase("a") ) && ((activeMenuId != 0) || (activeMenuId == 0)) )) {

			Token t = tokenSession.findByToken(token);

			User user = t == null ? null : t.getUser();
			logger.info("User = " + user + "  activeMenuId = " + activeMenuId+ "  requestPath = " + requestPath);

			if ((user != null) && (activeMenuId != 0)) {

				//for (Aksi service : ParamContext.getAllServices()) {
				List<Aksi> aksiList =  aksiSession.getAksiListByPath(requestPath);
				
					if (aksiList.size() != 0) {
						Aksi service = aksiList.get(0);
						logger.info("PATH FOUND, T1_AKSI:ID = " + service.getId() + " , T1_AKSI:PATH = "+ service.getPath());
						List<MenuAksi> menuAksiList = menuAksiSession.getMenuAksiListByMenuAksi(activeMenuId,service.getId());
						
						if (menuAksiList.size() == 0) {
							MenuAksi menuAksi = new MenuAksi();
							menuAksi.setAksi(service);
							menuAksi.setMenu(menuSession.find(activeMenuId));
							menuAksiSession.insertMenuAksi(menuAksi, null);
							logger.info("NEW SERVICE INSERTED!");
							
						}
						else
						{
							logger.info("SERVICE IS ALREADY EXIST!");
						}
						
					} else {
						Aksi aksi = new Aksi();
						aksi.setPath(requestPath);
						aksiSession.insertAksi(aksi, t);
						
						MenuAksi menuAksi = new MenuAksi();
						menuAksi.setAksi(aksi);
						menuAksi.setMenu(menuSession.find(activeMenuId));
						menuAksiSession.insertMenuAksi(menuAksi, null);
						logger.info("NEW SERVICE INSERTED!");
						
					}
				//}

			} else {
				// jika service yang tidak memiliki menu
				List<Aksi> aksiList =  aksiSession.getAksiListByPath(requestPath);
				
				if (aksiList.size() == 0 ) {
					Aksi aksi = new Aksi();
					aksi.setPath(requestPath);
					aksi.setIsPublic(false);
					aksiSession.insertAksi(aksi, null);
				}
				
			}
		} else if ((ParamContext.getParameterBooleanByName("IS_INSERT_SECURITY_SERVICES") && (token.equalsIgnoreCase("a")) && (activeMenuId == 0) )) {
			
			List<Aksi> aksiList =  aksiSession.getAksiListByPath(requestPath);
			
			if (aksiList.size() == 0 ) {
				Aksi aksi = new Aksi();
				aksi.setPath(requestPath);
				aksi.setIsPublic(true);
				aksiSession.insertAksi(aksi, null);
			}
			
		}
	}
}
