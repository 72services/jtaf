package ch.jtaf.boundry;

import ch.jtaf.entity.Club;
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

@Path("clubs")
@Produces({"application/json"})
@Consumes({"application/json"})
@Interceptors({TraceInterceptor.class})
@Stateless
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
