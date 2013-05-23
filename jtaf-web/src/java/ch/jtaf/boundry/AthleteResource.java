package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Athlete;
import ch.jtaf.interceptor.TraceInterceptor;
import java.util.List;
import javax.ejb.EJB;
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
public class AthleteResource {

    @EJB
    private DataService service;

    @GET
    public List<Athlete> list(@QueryParam("series_id") Long seriesId) {
        return service.getAthletes(seriesId);
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
    public List<Athlete> search(@QueryParam("series_id") Long seriesId, @QueryParam("query") String query) throws WebApplicationException {
        return service.searchAthletes(seriesId, query);
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
