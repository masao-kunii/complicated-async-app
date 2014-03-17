package complicated.async.app.ejb2.utils;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Named
public class JMSClient2 {
	@Inject
	private Logger log;

	@Resource(lookup="java:/jms/mserver/pkg/QueueConnectionFactory")
	private ConnectionFactory connectionFactory;

	public void sendText(String queueName, String text) throws JMSException, NamingException {
		sendText(queueName, text,  connectionFactory);
	}

	public void sendText(String queueName, String text, String cfJndi) throws JMSException, NamingException {
		log.info(String.format("JMSClient2#sendText[%s] to %s at %s", text, queueName, cfJndi));
		if(cfJndi == null){
			log.info("JNDI name is null.");
			sendText(queueName, text);
		}else{
			InitialContext jndi = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory)jndi.lookup(cfJndi);
			sendText(queueName, text, cf);
		}
		log.info(String.format("JMSClient2#sendText end", text, queueName));
	}

	public void sendText(String queueName, String text, ConnectionFactory cf) throws JMSException {
		log.info(String.format("JMSClient2#sendText[%s] to %s", text, queueName));
		try {
			log.info(getClass().getClassLoader().toString());
			log.info(getClass().getClassLoader().loadClass("org.apache.activemq.ra.ActiveMQManagedConnection").toString());
		} catch (ClassNotFoundException e1) {
		}

		final long start = System.currentTimeMillis();
		Connection connection = null;
		Session session = null;
		try {
			connection = cf.createConnection();
			session = connection.createSession(true, -1);
			Queue queue = session.createQueue(queueName);
			TextMessage message = session.createTextMessage();
			message.setText(text);
			message.setLongProperty("timestamp", System.currentTimeMillis());
			MessageProducer producer = session.createProducer(queue);
			producer.send(message);
			session.commit();
		} catch (Exception e) {
			if(session != null){
				try {
					session.rollback();
				} catch (JMSException ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			if(connection != null){
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
		log.info("JMSClient2#sent in [" + (System.currentTimeMillis() - start) + " ms]");
	}
}
