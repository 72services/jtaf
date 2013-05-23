package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Series;
import ch.jtaf.interceptor.TraceInterceptor;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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

@Path("series")
@Produces({"application/json"})
@Consumes({"application/json"})
@Interceptors({TraceInterceptor.class})
@Stateless
public class SeriesResource {

    @EJB
    private DataService service;

    @GET
    public List<Series> list(@QueryParam("space_id") Long spaceId, @QueryParam("withCompetitions") String withCompetitions) {
        if (withCompetitions == null || !Boolean.parseBoolean(withCompetitions)) {
            return service.getSeriesList(spaceId);
        } else {
            return service.getSeriesWithCompetitions(spaceId);
        }
    }

    @POST
    public Series save(Series series) {
        return service.save(series);
    }

    @POST
    @Path("{id}")
    public void copy(@PathParam("id") Long id, @QueryParam("function") String function) {
        service.copySeries(id);
    }

    @GET
    @Path("{id}")
    public Series get(@PathParam("id") Long id)
            throws WebApplicationException {
        Series s = service.getSeries(id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return s;
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
