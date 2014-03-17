package complicated.async.app.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import complicated.async.app.service.MessageReceiverBean;
import complicated.async.app.service.MessageSenderBean;

public class JmsRestResource implements IJmsRestResource{
	@Inject
	private Logger log;
	
	@Inject
	MessageSenderBean senderBean;

	@Inject
	MessageReceiverBean receiverBean;

	@Override
	public String send(@PathParam("queueName") String queueName,
			@PathParam("message") String message) {
		log.fine(String.format("send %s to %s", queueName, message));
		boolean ret = senderBean.send(queueName, message);
		return ret ? "OK" : "ERROR";
	}

	public String sendBoundFor(@PathParam("queueName") String queueName,
			@PathParam("message") String message, @PathParam("finalDestination") String finalDestination){
		log.fine(String.format("send %s to %s", queueName, message));
		Map<String, Object> property = new HashMap<>();
		property.put("boundfor", finalDestination);
		boolean ret = senderBean.send(queueName, message, property);
		return ret ? "OK" : "ERROR";
	}

	public String sendBoundForAt(@PathParam("queueName") String queueName,
			@PathParam("message") String message, @PathParam("finalDestination") String finalDestination, @QueryParam("cf") String cf){
		log.fine(String.format("send %s to %s", queueName, message));
		Map<String, Object> property = new HashMap<>();
		property.put("boundfor", finalDestination);
		property.put("cf", cf);
		boolean ret = senderBean.send(queueName, message, property);
		return ret ? "OK" : "ERROR";
	}

	@Override
	public String sendlargemessage(@PathParam("queueName") String queueName,
			@FormParam("message") String message){
		log.fine(String.format("sendlargemessage %s to %s", queueName, message));
		boolean ret = senderBean.send(queueName, message);
		return ret ? "OK" : "ERROR";
	}

	@Override
	public String receive(@PathParam("queueName") String queueName,
			@PathParam("timeout") long timeout, @PathParam("retry") int retry){
		log.fine(String.format("receive message from %s", queueName));
		String response = receiverBean.receive(queueName, timeout, retry);
		return response;
	}

	@Override
	public String receiveAndRollback(@PathParam("queueName") String queueName,
			@PathParam("timeout") long timeout, @PathParam("retry") int retry){
		log.fine(String.format("receive message from %s", queueName));
		String response = receiverBean.receive(queueName, timeout, retry);
		return response;
	}

	@Override
	public String receiveFrom(@PathParam("queueName") String queueName,
			@PathParam("timeout") long timeout, @PathParam("retry") int retry, @QueryParam("cf") String cf){
		log.fine(String.format("receive message from %s", queueName));
		String response = receiverBean.receive(queueName, timeout, retry, cf);
		return response;
	}

}