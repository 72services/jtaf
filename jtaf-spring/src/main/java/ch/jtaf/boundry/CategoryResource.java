package ch.jtaf.boundry;

import ch.jtaf.entity.Category;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class CategoryResource extends BaseResource {

    @GET
    public List<Category> list(@QueryParam("series_id") Long seriesId) {
        return dataService.getCategories(seriesId);
    }

    @POST
    public Category save(Category c) {
        if (isUserGrantedForSeries(c.getSeries_id())) {
            return dataService.save(c);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    @GET
    @Path("{id}")
    public Category get(@PathParam("id") Long id) {
        Category c = dataService.get(Category.class, id);
        if (c == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return c;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Category c = dataService.get(Category.class, id);
        if (c == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else if (isUserGrantedForSeries(c.getSeries_id())) {
            dataService.delete(c);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
