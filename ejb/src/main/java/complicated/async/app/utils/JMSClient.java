package complicated.async.app.utils;

import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Named
public class JMSClient {
	@Inject
	private Logger log;

	@Resource(lookup = "java:/jms/cp/QueueConnectionFactory")
	private ConnectionFactory connectionFactory;

	public void sendText(String queueName, String text,
			Map<String, Object> header) throws JMSException {
		log.info("JMSClient#send " + text);
		try {
			log.info(getClass().getClassLoader().toString());
			log.info(getClass().getClassLoader().loadClass("org.apache.activemq.ra.ActiveMQManagedConnection").toString());
		} catch (ClassNotFoundException e1) {
		}

		final long start = System.currentTimeMillis();
		Connection connection = null;
		Session session = null;
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(true, -1);
			Queue queue = session.createQueue(queueName);
			TextMessage message = session.createTextMessage();
			message.setText(text);
			message.setLongProperty("timestamp", System.currentTimeMillis());
			if (header != null) {
				for (Map.Entry<String, Object> property : header.entrySet()) {
					message.setObjectProperty(property.getKey(),
							property.getValue());
				}
			}
			MessageProducer producer = session.createProducer(queue);
			producer.send(message);
			session.commit();
		} catch (Exception e) {
			if (session != null) {
				try {
					session.rollback();
				} catch (JMSException ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
		log.info("JMSClient#sent in [" + (System.currentTimeMillis() - start)
				+ " ms]");
	}

	public Message receive(String queueName, long timeout) throws JMSException {
		return receive(queueName, timeout, false);
	}

	public Message receive(String queueName, long timeout, String cfJndi) throws JMSException, NamingException {
		InitialContext jndi = new InitialContext();
		ConnectionFactory cf = (ConnectionFactory)jndi.lookup(cfJndi);
		return receive(queueName, timeout, false, cf);
	}
	
	public Message receive(String queueName, long timeout, boolean forceRollback)
			throws JMSException {
		return receive(queueName, timeout, forceRollback, connectionFactory);
	}

	public Message receive(String queueName, long timeout, boolean forceRollback, ConnectionFactory cf)
			throws JMSException {
		log.info("JMSClient#receive from " + queueName);
		final long start = System.currentTimeMillis();
		Connection connection = null;
		Session session = null;
		Message message = null;
		try {
			connection = cf.createConnection();
			connection.start();
			session = connection.createSession(true, -1);
			Queue queue = session.createQueue(queueName);
			MessageConsumer consumer = session.createConsumer(queue);
			message = consumer.receive(timeout);

			if (forceRollback) {
				log.info("force rollback");
				session.rollback();
			} else {
				log.info("commit");
				session.commit();
			}
			connection.stop();
		} catch (Exception e) {
			if (session != null) {
				try {
					session.rollback();
				} catch (JMSException ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
		log.info("JMSClient#received in ["
				+ (System.currentTimeMillis() - start) + "ms]");
		return message;
	}
	
}
