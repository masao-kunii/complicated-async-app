package complicated.async.app.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;

import complicated.async.app.utils.JMSClient;


/**
 *
 */
@Stateless
public class MessageSenderBean2 {
    @Inject
    private Logger log;

	@Inject
	private JMSClient client;

	public boolean send(String queueName, String message) {
		log.fine(String.format("MessageSenderBean2 - message:%s queue:%s", message, queueName));
		try {
			client.sendText(queueName, message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		log.fine("MessageSenderBean2 - success");
		return true;
	}
}