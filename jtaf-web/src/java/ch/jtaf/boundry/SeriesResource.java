package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Series;
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
    public List<Series> list(@QueryParam("withCompetitions") String withCompetitions) {
        if (withCompetitions == null || !Boolean.parseBoolean(withCompetitions)) {
            return service.getSeriesList();
        } else {
            return service.getSeriesWithCompetitions();
        }
    }

    @POST
    public Series save(Series series) {
        return service.save(series);
    }

    @GET
    @Path("{id}")
    public Series get(@PathParam("id") Long id, @QueryParam("function") String function)
            throws WebApplicationException {
        if (function == null) {
            Series s = service.getSeries(id);
            if (s == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                return s;
            }
        }
        else {
            return service.copySeries(id);
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Series s = service.get(Series.class, id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            service.delete(s);
        }
    }
}
