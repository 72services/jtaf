package ch.jtaf.boundry;

import ch.jtaf.interceptor.TraceInterceptor;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("result")
@Produces({"application/json"})
@Consumes({"application/json"})
@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ResultResource extends BaseResource {

    @GET
    public Long calculatePoints(@QueryParam("event_id") Long eventId, @QueryParam("result") String result) {
        return dataService.calculatePoints(eventId, result);
    }

}
