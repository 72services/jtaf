package ch.jtaf.boundry;

import ch.jtaf.entity.Club;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("clubs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class ClubResource extends BaseResource {

    @GET
    public List<Club> list(@QueryParam("space_id") Long spaceId) {
        return dataService.getClubs(spaceId);
    }

    @POST
    public Club save(Club club) {
        if (isUserGrantedForSpace(club.getSpace_id())) {
            return dataService.save(club);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    @GET
    @Path("{id}")
    public Club get(@PathParam("id") Long id) {
        Club c = dataService.get(Club.class, id);
        if (c == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return c;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Club c = dataService.get(Club.class, id);
        if (c == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else if (isUserGrantedForSpace(c.getSpace_id())) {
            dataService.delete(c);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
