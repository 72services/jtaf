package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.model.Club;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("clubs")
@Produces({"application/json"})
@Consumes({"application/json"})
@Stateless
public class ClubResource {

    @EJB
    private DataService service;

    @GET
    public List<Club> list() {
        return service.getClubs();
    }

    @POST
    public Club save(Club club) {
        return service.save(club);
    }

    @GET
    @Path("{id}")
    public Club get(@PathParam("id") Long id) throws WebApplicationException {
        Club c = service.get(Club.class, id);
        if (c == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return c;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Club c = service.get(Club.class, id);
        if (c == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            service.delete(c);
        }
    }
}
