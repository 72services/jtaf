package ch.jtaf.boundry;

import ch.jtaf.model.Event;
import ch.jtaf.control.CompetitionService;
import ch.jtaf.model.Category;
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

@Path("events")
@Produces({"application/json"})
@Consumes({"application/json"})
@Stateless
public class CategoryResource {

    @EJB
    private CompetitionService competitionService;

    @GET
    public List<Category> list() {
        return competitionService.getCategories();
    }

    @POST
    public Category save(Category c) {
        return competitionService.save(c);
    }

    @GET
    @Path("{id}")
    public Category get(@PathParam("id") Long id) throws WebApplicationException {
        Category c = competitionService.get(Category.class, id);
        if (c == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return c;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Category c = competitionService.get(Category.class, id);
        if (c == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            competitionService.delete(c);
        }
    }
}
