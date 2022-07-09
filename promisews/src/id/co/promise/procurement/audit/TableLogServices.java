package id.co.promise.procurement.audit;

import id.co.promise.procurement.audit.TableLogSession;
import id.co.promise.procurement.entity.TableLog;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author : Reinhard MT
 */

@Stateless
@Path(value = "/procurement/log/tableLogServices")
@Produces(MediaType.APPLICATION_JSON)
public class TableLogServices {
	@EJB
	TableLogSession tableLogSession;

	@Path("/getList")
	@GET
	public List<TableLog> getList() {

		return tableLogSession.getList();
	}


}
