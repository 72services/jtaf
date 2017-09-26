package ch.jtaf.boundry;

import ch.jtaf.entity.Competition;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("competitions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
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
