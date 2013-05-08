package ch.jtaf.boundry;

import ch.jtaf.model.Event;
import ch.jtaf.control.CompetitionService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("events")
@Produces({"application/json"})
@Consumes({"application/json"})
@Stateless
public class EventResource {

    @EJB
    private CompetitionService competitionService;

    @GET
    public List<Event> list() {
        return competitionService.getEvents();
    }

    @POST
    public Event save(Event event) {
        return competitionService.save(event);
    }

    @GET
    @Path("{id}")
    public Event get(@PathParam("id") Long id) throws WebApplicationException {
        Event e = competitionService.get(Event.class, id);
        if (e == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return e;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Event e = competitionService.get(Event.class, id);
        if (e == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            competitionService.delete(e);
        }
    }
}
