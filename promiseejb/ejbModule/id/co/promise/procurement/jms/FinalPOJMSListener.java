package id.co.promise.procurement.jms;

import id.co.promise.procurement.entity.StoreJMS;
import id.co.promise.procurement.master.StoreJMSSession;
import id.co.promise.procurement.utils.Constant;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.logging.Logger;

@MessageDriven(mappedName = "FINALPUCHASEORDER", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/FINALPUCHASEORDER_IN") })
public class FinalPOJMSListener implements MessageListener {
	final static Logger log = Logger.getLogger(FinalPOJMSListener.class);
	@Resource private MessageDrivenContext mdc;
	@EJB private StoreJMSSession storeJMSSession;

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage msg = (TextMessage) message;
			String result 	= "";
			log.info(">> Message final purchase order listener : " + msg.getText());
			result = msg.getText();
			StoreJMS storeJMS = new StoreJMS();
			storeJMS.setStoreJmsCreated(new Date());
			storeJMS.setStoreJmsData(result);
			storeJMS.setStoreJmsModule(Constant.JMS_FINAL_PO);
			storeJMS.setStoreJmsStat(Constant.ZERO_VALUE);
			storeJMSSession.insertStoreJMS(storeJMS, null);
		} catch (JMSException ex) {
			mdc.setRollbackOnly();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
