package ch.jtaf.boundry;

import ch.jtaf.entity.Event;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
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
