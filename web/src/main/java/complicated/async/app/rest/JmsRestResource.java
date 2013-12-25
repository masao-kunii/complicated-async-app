package complicated.async.app.rest;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import complicated.async.app.service.MessageSenderBean;

public class JmsRestResource implements IJmsRestResource{
	@Inject
	private Logger log;
	
	@Inject
	MessageSenderBean senderBean;

	@Override
	public Response send(@PathParam("queueName") String queueName,
			@PathParam("message") String message) {
		log.fine(String.format("send %s to %s", queueName, message));
		senderBean.send(queueName, message);
		return Response.ok().build();
	}

}