package ch.jtaf.boundry;

import ch.jtaf.entity.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("spaces")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class SpaceResource extends BaseResource {

    @GET
    public List<Space> list() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return dataService.getMySpaces(userName);
    }

    @GET
    @Path("/series")
    public List<Series> listSeries() {
        return dataService.getAllSeries();
    }

    @POST
    public Space save(Space space) {
        if (space.getId() == null) {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            SecurityUser user = dataService.get(SecurityUser.class, userName);
            UserSpace userSpace = new UserSpace();
            userSpace.setRole(UserSpaceRole.OWNER);
            userSpace.setSpace(space);
            userSpace.setUser(user);
            dataService.save(userSpace);
        } else {
            if (!isUserGrantedForSpace(space.getId())) {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }
        }
        return dataService.save(space);
    }

    @GET
    @Path("{id}")
    public Space get(@PathParam("id") Long id) {
        Space s = dataService.getSpace(id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return s;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Space s = dataService.get(Space.class, id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else if (isUserGrantedForSpace(s.getId())) {
            dataService.deleteSpace(s);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
