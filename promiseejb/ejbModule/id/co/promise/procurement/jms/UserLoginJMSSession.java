package id.co.promise.procurement.jms;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.jboss.logging.Logger;

@LocalBean
@Stateless
public class UserLoginJMSSession {
	final static Logger log = Logger.getLogger(UserLoginJMSSession.class);
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connFactory;

	@Resource(mappedName = "java:/jms/queue/USERLOGIN_IN")
	private Queue qTarget;

	public void sendMessage(String message) {
		try {
			Connection con = connFactory.createConnection();
			try {
				Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
				MessageProducer producer = session.createProducer(qTarget);
				producer.send(session.createTextMessage(message));
				log.info(">> Sending Message : " + message);
			} finally {
				if(con!=null)
					con.close();
			}
		} catch (JMSException e) {
			log.error("Error JMS Connection!");
			e.printStackTrace();
		}
	}

	public String getMessage() {
		String rs = "no message";
		try {
			Connection con = connFactory.createConnection();
			try {
				Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
				MessageConsumer consumer = session.createConsumer(qTarget);
				con.start();
				TextMessage message = (TextMessage) consumer.receiveNoWait();
				if (message!=null) {
					rs = message.getText();
					log.info(">> Received message :" + rs);
					return rs;
				}
			} finally {
				if(con!=null)
					con.close();
			}
		} catch (JMSException e) {
			log.error("Error JMS Connection!");
			e.printStackTrace();
		}
		return rs;
	}
}
