package ch.jtaf.boundry;

import ch.jtaf.entity.Event;
import ch.jtaf.interceptor.TraceInterceptor;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("events")
@Produces({"application/json"})
@Consumes({"application/json"})
@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class EventResource extends BaseResource {

    @GET
    public List<Event> list(@QueryParam("series_id") Long seriesId) {
        return dataService.getEvents(seriesId);
    }

    @POST
    public Event save(Event event) {
        if (isUserGrantedForSeries(event.getSeries_id())) {
            return dataService.save(event);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    @GET
    @Path("{id}")
    public Event get(@PathParam("id") Long id) {
        Event e = dataService.get(Event.class, id);
        if (e == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return e;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Event e = dataService.get(Event.class, id);
        if (e == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else if (isUserGrantedForSeries(e.getSeries_id())) {
            dataService.delete(e);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
