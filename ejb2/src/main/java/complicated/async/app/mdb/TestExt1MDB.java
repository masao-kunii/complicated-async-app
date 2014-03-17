package complicated.async.app.mdb;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;

import org.jboss.ejb3.annotation.ResourceAdapter;

public class TestExt1MDB extends AbstractMDB {
	@Inject 
	private Logger log;

	public void onMessage(final Message message) {
		log.info("onMessage " + message);
	}
}