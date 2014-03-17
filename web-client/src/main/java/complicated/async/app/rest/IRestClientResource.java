package complicated.async.app.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/jms")
public interface IRestClientResource {

	@GET
	@Path("/via/{via}/{queueName}/{message}")
	public String send(@PathParam("via") String via,
			@PathParam("queueName") String queueName,
			@PathParam("message") String message);

}