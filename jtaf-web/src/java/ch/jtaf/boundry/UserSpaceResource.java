package ch.jtaf.boundry;

import ch.jtaf.entity.SecurityUser;
import ch.jtaf.entity.UserSpace;
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

@Path("userspaces")
@Produces({"application/json"})
@Consumes({"application/json"})
@Interceptors({TraceInterceptor.class})
@Stateless
public class UserSpaceResource extends BaseResource {

    @GET
    public List<UserSpace> list(@QueryParam("space_id") Long spaceId) {
        return dataService.getUserSpaces(spaceId);
    }

    @POST
    public UserSpace save(UserSpace userSpace) {
        if (isUserGrantedForSpace(userSpace.getSpace().getId())) {
            if (userSpace.getUser().getConfirmationId() == null) {
                SecurityUser user = dataService.get(SecurityUser.class, userSpace.getUser().getEmail());
                if (user == null) {
                    throw new WebApplicationException(Response.Status.NOT_FOUND);
                }
                userSpace.setUser(user);
            }
            return dataService.save(userSpace);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        UserSpace s = dataService.get(UserSpace.class, id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else if (isUserGrantedForSpace(s.getSpace().getId())) {
            dataService.delete(s);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
