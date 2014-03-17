package complicated.async.app.rest;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

public class RestClientResource implements IRestClientResource {
	static {
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
	}
	@Inject
	private Logger log;
	
	private static final String URL_FORMAT = "http://%s/S2CPSA";

	public String send(String via, String queueName, String message) {
		String proxyUrl = String.format(URL_FORMAT, via);
		log.info(String.format("send %s to %s via %s", queueName, message, proxyUrl));
		
		IJmsRestResource client = ProxyFactory.create(IJmsRestResource.class,
				proxyUrl);
		
		log.info("calling..");
		String response = (String)client.send(queueName, String.format("%s to %s", message, via));
		log.info("OK");
		return response;
	}

}