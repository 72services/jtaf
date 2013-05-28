package ch.jtaf.boundry;

import ch.jtaf.entity.Series;
import ch.jtaf.interceptor.TraceInterceptor;
import java.util.List;
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
public class SeriesResource extends BaseResource {

    @GET
    public List<Series> list(@QueryParam("space_id") Long spaceId,
            @QueryParam("withCompetitions") String withCompetitions) {
        if (withCompetitions == null || !Boolean.parseBoolean(withCompetitions)) {
            return dataService.getSeriesList(spaceId);
        } else {
            return dataService.getSeriesWithCompetitions(spaceId);
        }
    }

    @POST
    public Series save(Series series) {
        if (isUserGrantedForSpace(series.getSpace_id())) {
            return dataService.save(series);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    @POST
    @Path("{id}")
    public void copy(@PathParam("id") Long id, @QueryParam("function") String function) {
        dataService.copySeries(id);
    }

    @GET
    @Path("{id}")
    public Series get(@PathParam("id") Long id) {
        Series s = dataService.getSeries(id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return s;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Series s = dataService.get(Series.class, id);
        if (isUserGrantedForSpace(s.getSpace_id())) {
            if (s == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                dataService.delete(s);
            }
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
