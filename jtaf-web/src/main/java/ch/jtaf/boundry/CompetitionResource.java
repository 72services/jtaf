package ch.jtaf.boundry;

import ch.jtaf.entity.Competition;
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

@Path("competitions")
@Produces("application/json")
@Consumes("application/json")
@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CompetitionResource extends BaseResource {

    @GET
    public List<Competition> list(@QueryParam("series_id") Long seriesId) {
        return dataService.getCompetititions(seriesId);
    }

    @POST
    public Competition save(Competition competition) {
        if (isUserGrantedForSeries(competition.getSeries_id())) {
            return dataService.save(competition);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    @GET
    @Path("{id}")
    public Competition get(@PathParam("id") Long id) {
        Competition c = dataService.get(Competition.class, id);
        if (c == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return c;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Competition c = dataService.get(Competition.class, id);
        if (c == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else if (isUserGrantedForSeries(c.getSeries_id())) {
            dataService.delete(c);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
