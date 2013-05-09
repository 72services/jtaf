package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.model.Competition;
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

@Path("competitions")
@Produces({"application/json"})
@Consumes({"application/json"})
@Stateless
public class CompetitionResource {

    @EJB
    private DataService competitionService;

    @GET
    public List<Competition> list() {
        return competitionService.getCompetititions();
    }

    @POST
    public Competition save(Competition competition) {
        return competitionService.save(competition);
    }

    @GET
    @Path("{id}")
    public Competition get(@PathParam("id") Long id) throws WebApplicationException {
        Competition c = competitionService.get(Competition.class, id);
        if (c == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return c;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Competition c = competitionService.get(Competition.class, id);
        if (c == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            competitionService.delete(c);
        }
    }
}
