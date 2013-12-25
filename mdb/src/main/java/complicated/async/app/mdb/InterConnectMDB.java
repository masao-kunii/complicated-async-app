package complicated.async.app.mdb;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;

import org.jboss.ejb3.annotation.ResourceAdapter;

import complicated.async.app.service.LogBean;
import complicated.async.app.service.MessageSenderBean2;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
@ActivationConfigProperty(propertyName = "destination", propertyValue = "interConnectQueue") })
@ResourceAdapter("activemq-rar-5.8.0.redhat-60024.rar")
public class InterConnectMDB extends AbstractMDB {
	@Inject
	@Named("produceLog")
	private Logger log;

	@Inject
	private LogBean logBean;

	@Inject
	private MessageSenderBean2 sender;

	public void onMessage(final Message message) {
		log.info("onMessage " + message);
		try {
			log.fine(getClass().getClassLoader().toString());
			log.fine(getClass().getClassLoader().loadClass("org.apache.activemq.ra.ActiveMQManagedConnection").toString());
		} catch (ClassNotFoundException e1) {
		}
		logBean.insert("onMessage " + message);

		try {
			sender.send(message.getStringProperty("boundfor"), message.toString());
		} catch (JMSException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}