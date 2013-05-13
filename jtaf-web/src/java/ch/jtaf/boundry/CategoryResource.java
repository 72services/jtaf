package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Category;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("categories")
@Produces({"application/json"})
@Consumes({"application/json"})
@Stateless
public class CategoryResource {

    @EJB
    private DataService service;

    @GET
    @QueryParam("{serie}")
    public List<Category> list(@QueryParam("serie") Long id) {
        if (id != null) {
            return service.getCategoryFromSerie(id);
        } else {
            return service.getCategories();
        }
    }

    @POST
    public Category save(Category c) {
        return service.save(c);
    }

    @GET
    @Path("{id}")
    public Category get(@PathParam("id") Long id) throws WebApplicationException {
        Category c = service.get(Category.class, id);
        if (c == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return c;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Category c = service.get(Category.class, id);
        if (c == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            service.delete(c);
        }
    }
}
