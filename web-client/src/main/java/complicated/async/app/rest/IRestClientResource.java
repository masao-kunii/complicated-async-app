package complicated.async.app.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/jms")
public interface IRestClientResource {

	@GET
	@Path("/via/{via}/{queueName}/{message}")
	@Produces("application/json")
	public Response send(@PathParam("via") String via,
			@PathParam("queueName") String queueName,
			@PathParam("message") String message);

}