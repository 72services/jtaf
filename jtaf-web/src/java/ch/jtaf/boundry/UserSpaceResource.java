package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.SecurityUser;
import ch.jtaf.entity.UserSpace;
import ch.jtaf.interceptor.TraceInterceptor;
import java.util.List;
import javax.ejb.EJB;
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
public class UserSpaceResource {

    @EJB
    private DataService service;

    @GET
    public List<UserSpace> list(@QueryParam("space_id") Long spaceId) {
        return service.getUserSpaces(spaceId);
    }

    @POST
    public UserSpace save(UserSpace userSpace) {
        if (userSpace.getUser().getConfirmationId() == null) {
            SecurityUser user = service.get(SecurityUser.class, userSpace.getUser().getEmail());
            if (user == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            userSpace.setUser(user);
        }
        return service.save(userSpace);
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        UserSpace s = service.get(UserSpace.class, id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            service.delete(s);
        }
    }
}
