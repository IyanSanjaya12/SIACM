package id.co.promise.procurement.utils;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.jboss.logging.Logger;

import id.co.promise.procurement.email.EmailNotificationSession;

public class Test {
	final static Logger log = Logger.getLogger(Test.class);
	public static void main(String [] args) throws ClassNotFoundException{
		EmailNotificationSession emailNotificationSession = new EmailNotificationSession();
		log.info(">> Sending Mail Proccesed");
		try {
			emailNotificationSession.emailSenderTest("yudi.ismiaji@mmi-pt.com", "Tes SMTP", "Tes kirim email");
			log.info(">> Sent Mail Success");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
