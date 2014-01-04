package complicated.async.app.rest;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.BaseClientResponse;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

public class RestClientResource implements IRestClientResource{
	  static {RegisterBuiltin.register(ResteasyProviderFactory.getInstance());}
	@Inject
	private Logger log;

	@Override
	public Response send(String via, String queueName, String message) {
		log.fine(String.format("send %s to %s via %s", queueName, message, via));
		IJmsRestResource client = ProxyFactory.create(IJmsRestResource.class, via);
		BaseClientResponse response = (BaseClientResponse)client.send(queueName, String.format("%s to %s", message, via));

		log.fine("sent");
		return Response.ok().entity("done.").build();
	}


}