package ch.jtaf.boundry;

import ch.jtaf.entity.SecurityUser;
import ch.jtaf.entity.Space;
import ch.jtaf.entity.UserSpace;
import ch.jtaf.entity.UserSpaceRole;
import ch.jtaf.interceptor.TraceInterceptor;
import java.security.Principal;
import java.util.List;
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

@Path("spaces")
@Produces({"application/json"})
@Consumes({"application/json"})
@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SpaceResource extends BaseResource {

    @GET
    public List<Space> list(@QueryParam("my") Boolean my) {
        if (my != null && my) {
            Principal principal = sessionContext.getCallerPrincipal();
            return dataService.getMySpaces(principal.getName());
        } else {
            return dataService.getSpaces();
        }
    }

    @POST
    public Space save(Space space) {
        if (space.getId() == null) {
            Principal principal = sessionContext.getCallerPrincipal();
            SecurityUser user = dataService.get(SecurityUser.class, principal.getName());
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
