package id.co.promise.procurement.email;

import java.text.ParseException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.jboss.logging.Logger;

@LocalBean
@Singleton
public class EmailSchedulerSession {
	final static Logger log = Logger.getLogger(EmailSchedulerSession.class);
	@EJB
	private EmailNotificationSession emailNotificationSession;
	
	//@Schedule(hour = "*", minute = "*", second = "*/10", persistent = false) //10 detik
	@Schedule(hour = "*", minute = "*", second = "*/60", persistent = false) //1 menit
	public void runEveryMinutesTest() {
		//log.info("RUN Email Notification");
		try {
			emailNotificationSession.emailSender();
		} catch (AddressException e) {
			log.error("Error alamat email");
			e.printStackTrace();
		} catch (MessagingException e) {
			log.error("Error sending message email");
			e.printStackTrace();
		}
	}
	
	@Schedule(hour = "*", minute = "*/1", persistent = false) //5 menit
	public void runEveryHours() {
		//log.info("RUN Email Generator");
		try {
			emailNotificationSession.getEmailGenerator();
		} catch (ParseException e) {
			log.error("Error generate email");
			e.printStackTrace();
		}
	}

	
}
