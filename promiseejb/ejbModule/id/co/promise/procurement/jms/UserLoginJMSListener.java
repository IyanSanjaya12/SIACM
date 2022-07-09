package id.co.promise.procurement.jms;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.logging.Logger;


@MessageDriven(mappedName = "USERLOGIN", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/USERLOGIN_IN") })
public class UserLoginJMSListener implements MessageListener {
	final static Logger log = Logger.getLogger(UserLoginJMSListener.class);
	@Resource
	private MessageDrivenContext mdc;

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage msg = (TextMessage) message;
			log.info(">> Message Found : " + msg.getText());
		} catch (JMSException ex) {
			mdc.setRollbackOnly();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
