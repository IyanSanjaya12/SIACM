package id.co.promise.procurement.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SmtpAuthenticator extends Authenticator {
	private String username;
	private String password;
	
	public SmtpAuthenticator() {
		super();
	}
	
	public SmtpAuthenticator(String username, String password){
		super();
		this.password = password;
		this.username = username;
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		String username = this.username;
		String password = this.password;
		if ((username != null) && (username.length() > 0) && (password != null)
				&& (password.length() > 0)) {
			return new PasswordAuthentication(username, password);
		}
		return null;
	}
}