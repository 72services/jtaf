package ch.jtaf.boundry;

import ch.jtaf.entity.Athlete;
import ch.jtaf.to.AthleteTO;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("athletes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
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
                return dataService.getAthlete(savedAthlete.getId(), competitionId);
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
