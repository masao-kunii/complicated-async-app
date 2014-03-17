package complicated.async.app.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;

import complicated.async.app.utils.JMSClient;


/**
 * 
 */
@Stateless
public class MessageReceiverBean {
    @Inject
    private Logger log;
    
	@Inject
	private JMSClient client;

	public String receive(String queueName, long timeout, int retry) {
		log.info(String.format("MessageSenderBean - queue:%s timeout:%s", queueName, timeout));
		Message message = null;
		try {
			for (int i = 0; i < 1 + retry; i++) {
				message = client.receive(queueName, timeout);
				log.info("message:" + message);
			}
		} catch (JMSException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		log.info("MessageSenderBean - success");
		return message + "";
	}
	
	public String receive(String queueName, long timeout, int retry, String cf) {
		log.info(String.format("MessageSenderBean - queue:%s timeout:%s", queueName, timeout));
		Message message = null;
		try {
			for (int i = 0; i < 1 + retry; i++) {
				message = client.receive(queueName, timeout);
				log.info("message:" + message);
			}
		} catch (JMSException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		log.info("MessageSenderBean - success");
		return message + "";
	}

}