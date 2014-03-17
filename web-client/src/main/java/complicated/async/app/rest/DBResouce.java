package complicated.async.app.rest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

@Path("/ds")
public class DBResouce {
	static {
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
	}
	@Inject
	private Logger log;

	@GET
	@Path("/{dsname}/{crud}")
	@Produces("application/json")
	public String send(@PathParam("dsname") String dsname,
			@PathParam("crud") String crud) {

		try {
			InitialContext jndi = new InitialContext();
			DataSource ds = (DataSource) jndi.lookup("java:/jdbc/" + dsname);
			try (Connection conn = ds.getConnection()) {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select 1");
				log.info(String.format("Access to %s success?%s", dsname,
						rs.next()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return "done.";
	}

}