package complicated.async.app.service;

import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;

import complicated.async.app.utils.JMSClient;


/**
 *
 */
@Stateless
public class MessageSenderBean {
    @Inject
    @Named("produceLog")
    private Logger log;

	@Inject
	private JMSClient client;

	public boolean send(String queueName, String message) {
		log.info(String.format("MessageSenderBean - message:%s queue:%s", message, queueName));
		try {
			client.sendText(queueName, message, null);
		} catch (JMSException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		log.info("MessageSenderBean - success");
		return true;
	}

	public boolean send(String queueName, String message, Map<String, Object> header) {
		log.info(String.format("MessageSenderBean - message:%s queue:%s", message, queueName));
		try {
			client.sendText(queueName, message, header);
		} catch (JMSException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		log.info("MessageSenderBean - success");
		return true;
	}
}