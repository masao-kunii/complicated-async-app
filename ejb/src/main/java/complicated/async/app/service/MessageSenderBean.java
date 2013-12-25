package complicated.async.app.service;

import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;

import complicated.async.app.utils.JMSClient;


/**
 *
 */
@Stateless
public class MessageSenderBean {
    @Inject
    private Logger log;

	@Inject
	private JMSClient client;

	public boolean send(String queueName, String message) {
		log.fine(String.format("MessageSenderBean - message:%s queue:%s", message, queueName));
		try {
			client.sendText(queueName, message, null);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		log.fine("MessageSenderBean - success");
		return true;
	}

	public boolean send(String queueName, String message, Map<String, Object> header) {
		log.fine(String.format("MessageSenderBean - message:%s queue:%s", message, queueName));
		try {
			client.sendText(queueName, message, header);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		log.fine("MessageSenderBean - success");
		return true;
	}
}