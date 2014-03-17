package complicated.async.app.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Path("/jms")
public interface IJmsRestResource {

	@GET
	@Path("/send/{queueName}/{message}")
	public String send(@PathParam("queueName") String queueName,
			@PathParam("message") String message);

	@POST
	@Path("/sendlargemessage/{queueName}")
	public String sendlargemessage(@PathParam("queueName") String queueName,
			@FormParam("message") String message);

	@GET
	@Path("/receive/{queueName}/{timeout}/{retry}")
	public String receive(@PathParam("queueName") String queueName,
			@PathParam("timeout") long timeout, @PathParam("retry") int retry);

	@GET
	@Path("/receive/{queueName}/{timeout}/{retry}/forceRollback")
	public String receiveAndRollback(@PathParam("queueName") String queueName,
			@PathParam("timeout") long timeout, @PathParam("retry") int retry);
	
	@GET
	@Path("/receive/{queueName}/{timeout}/{retry}/from")
	public String receiveFrom(@PathParam("queueName") String queueName,
			@PathParam("timeout") long timeout, @PathParam("retry") int retry, @QueryParam("cf") String cf);

}