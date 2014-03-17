package complicated.async.app.service;

import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;

import complicated.async.app.ejb2.utils.JMSClient2;

@Stateless
public class MessageSenderBean2 {
    @Inject
    private Logger log;

	@Inject
	private JMSClient2 client;

	public boolean send(String queueName, String message) {
		log.fine(String.format("MessageSenderBean2 - message:%s queue:%s", message, queueName));
		try {
			client.sendText(queueName, message);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		log.fine("MessageSenderBean2 - success");
		return true;
	}

	public boolean send(String queueName, String message, String cfName) {
		log.info(String.format("MessageSenderBean - message:%s queue:%s cfName:%s", message, queueName, cfName));
		try {
			client.sendText(queueName, message, cfName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		log.info("MessageSenderBean - success");
		return true;
	}

}