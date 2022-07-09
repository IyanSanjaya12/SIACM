package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.AbstractFacade;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class RegistrasiSession {
	@EJB
	UserSession userSession;

	public Boolean getEmailIsValid(String email) {
		Boolean result = false;
		try {
			User user = userSession.getUserByEmail(email);
			return true;
		} catch (Exception e) {
		}
		return result;
	}

	public User registrasiUserVendor(String email, String password, Token token) {
		return userSession.registrasiUserVendor(email, password, token);
	}
}
