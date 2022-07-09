package id.co.promise.procurement.jms;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.jboss.logging.Logger;


@LocalBean
@Stateless
public class ContractRequestJMSSessionPM {
	final static Logger log = Logger.getLogger(ContractRequestJMSSessionPM.class);
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connFactory;

	@Resource(mappedName = "java:/jms/queue/CONTRACTREQUEST_IN")
	private Queue qTarget;

	public Boolean sendMessage(String message) {
		try {
			Connection con = connFactory.createConnection();
			try {
				Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
				MessageProducer producer = session.createProducer(qTarget);
				producer.send(session.createTextMessage(message));
				log.info(">> Sending Message contract request : " + message);
				return true;
			} finally {
				if(con!=null)
					con.close();
			}
		} catch (JMSException e) {
			log.error(">> Error JMS Connection!");
			e.printStackTrace();
			return false;
		}
	}
}
