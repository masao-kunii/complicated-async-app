package complicated.async.app.mdb;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.jms.Message;

public class TestMDB extends AbstractMDB {
	@Inject
	private Logger log;

	public void onMessage(final Message message) {
		log.info("onMessage " + message);
	}
}