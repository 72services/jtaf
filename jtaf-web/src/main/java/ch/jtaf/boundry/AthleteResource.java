package ch.jtaf.boundry;

import ch.jtaf.entity.Athlete;
import ch.jtaf.to.AthleteTO;
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

@Path("athletes")
@Produces({"application/json"})
@Consumes({"application/json"})
@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AthleteResource extends BaseResource {

    @GET
    public List<AthleteTO> list(@QueryParam("series_id") Long seriesId) {
        return dataService.getAthleteTOs(seriesId);
    }

    @POST
    public Athlete save(@QueryParam("competition_id") Long competitionId, Athlete a) {
        if (isUserGrantedForSeries(a.getSeries_id())) {
            Athlete savedAthlete = dataService.saveAthlete(a);
            if (competitionId != null) {
                dataService.saveResults(competitionId, a.getId(), a.getResults());
                return dataService.getAthlete(a.getId(), competitionId);
            } else {
                return savedAthlete;
            }
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    @GET
    @Path("{id}")
    public Athlete get(@PathParam("id") Long id, @QueryParam("competition_id") Long competitionId) {
        Athlete a = dataService.getAthlete(id, competitionId);
        if (a == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return a;
        }
    }

    @GET
    @Path("search")
    public List<Athlete> search(@QueryParam("series_id") Long seriesId,
            @QueryParam("competition_id") Long competitionId,
            @QueryParam("query") String query) {
        return dataService.searchAthletes(seriesId, competitionId, query);
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Athlete a = dataService.get(Athlete.class, id);
        if (a == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else if (isUserGrantedForSeries(a.getSeries_id())) {
            dataService.delete(a);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
