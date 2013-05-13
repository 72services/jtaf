package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Serie;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("series")
@Produces({"application/json"})
@Consumes({"application/json"})
@Stateless
public class SeriesResource {

    @EJB
    private DataService service;

    @GET
    @QueryParam("{withCompetitions}")
    public List<Serie> list(@QueryParam("withCompetitions") String withCompetitions) {
        if (withCompetitions == null || !Boolean.parseBoolean(withCompetitions)) {
            return service.getSeries();
        } else {
            return service.getSeriesWithCompetitions();
        }
    }

    @POST
    public Serie save(Serie serie) {
        return service.save(serie);
    }

    @GET
    @Path("{id}")
    public Serie get(@PathParam("id") Long id) throws WebApplicationException {
        Serie s = service.getSerie(id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return s;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Serie s = service.get(Serie.class, id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            service.delete(s);
        }
    }
}
