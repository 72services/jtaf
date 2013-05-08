package ch.jtaf.boundry;

import ch.jtaf.control.CompetitionService;
import ch.jtaf.model.Serie;
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

@Path("series")
@Produces({"application/json"})
@Consumes({"application/json"})
@Stateless
public class SeriesResource {

    @EJB
    private CompetitionService competitionService;

    @GET
    public List<Serie> list() {
        return competitionService.getSeries();
    }

    @POST
    public Serie save(Serie serie) {
        return competitionService.save(serie);
    }

    @GET
    @Path("{id}")
    public Serie get(@PathParam("id") Long id) throws WebApplicationException {
        Serie s = competitionService.get(Serie.class, id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return s;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Serie s = competitionService.get(Serie.class, id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            competitionService.delete(s);
        }
    }
}
