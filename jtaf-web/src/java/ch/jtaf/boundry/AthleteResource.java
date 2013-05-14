package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Athlete;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AthleteResource {

    @EJB
    private DataService service;

    @GET
    @QueryParam("{series}")
    public List<Athlete> list(@QueryParam("series") Long id) {
        if (id != null) {
            return service.getAthleteFromSeries(id);
        } else {
            return new ArrayList<Athlete>();
        }
    }

    @POST
    public Athlete save(Athlete a) {
        return service.saveAthlete(a);
    }

    @GET
    @Path("{id}")
    public Athlete get(@PathParam("id") Long id) throws WebApplicationException {
        Athlete a = service.get(Athlete.class, id);
        if (a == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return a;
        }
    }

    @GET
    @Path("search")
    @QueryParam("{query}")
    public Athlete search(@QueryParam("query") String query) throws WebApplicationException {
        Athlete a = service.searchAthlete(query);
        if (a == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return a;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Athlete a = service.get(Athlete.class, id);
        if (a == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            service.delete(a);
        }
    }
}
