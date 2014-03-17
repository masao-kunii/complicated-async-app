package complicated.async.app.mdb;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import complicated.async.app.service.MessageSenderBean2;

public class ForwardMDB extends AbstractMDB {
	@Inject
	private Logger log;

	@Inject
	private MessageSenderBean2 sender;
	
	public void onMessage(final Message message) {
		log.info("onMessage " + message);
		try {
			log.fine(getClass().getClassLoader().toString());
			log.fine(getClass().getClassLoader().loadClass("org.apache.activemq.ra.ActiveMQManagedConnection").toString());
		} catch (ClassNotFoundException e1) {
		}

		try {
			String queueName = message.getStringProperty("boundfor");
			if(queueName == null){
				try {
					InitialContext jndi = new InitialContext();
					jndi.lookup("java:/jms/queues/ext/1/testQueue");
				} catch (NamingException e) {
					log.fine(e.getMessage());
				}
			}
			
			if(queueName == null){
				queueName = "testQueue";
			}			
			
			if(message.getStringProperty("cf") == null){
				sender.send(queueName, message.toString());
			}else{
				sender.send(queueName, message.toString(), message.getStringProperty("cf"));
			}
		} catch (JMSException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}