package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.SecurityUser;
import ch.jtaf.entity.Space;
import ch.jtaf.entity.UserSpace;
import ch.jtaf.interceptor.TraceInterceptor;
import java.security.Principal;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("spaces")
@Produces({"application/json"})
@Consumes({"application/json"})
@Interceptors({TraceInterceptor.class})
@Stateless
public class SpaceResource {

    @Resource
    private SessionContext sessionContext;
    @EJB
    private DataService service;

    @GET
    public List<Space> list() {
        return service.getSpaces();
    }

    @POST
    public Space save(Space space) {
        if (space.getId() == null) {
            Principal principal = sessionContext.getCallerPrincipal();
            SecurityUser user = service.get(SecurityUser.class, principal.getName());
            UserSpace userSpace = new UserSpace();
            userSpace.setSpace(space);
            userSpace.setUser(user);
            service.save(userSpace);
        }
        return service.save(space);
    }

    @GET
    @Path("{id}")
    public Space get(@PathParam("id") Long id) throws WebApplicationException {
        Space s = service.getSpace(id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return s;
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Space s = service.get(Space.class, id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            service.delete(s);
        }
    }
}
