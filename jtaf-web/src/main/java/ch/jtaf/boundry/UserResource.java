package ch.jtaf.boundry;

import ch.jtaf.entity.SecurityUser;
import ch.jtaf.interceptor.TraceInterceptor;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("users")
@Produces({"application/json"})
@Consumes({"application/json"})
@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class UserResource extends BaseResource {

    private static final Logger LOGGER = Logger.getLogger(UserResource.class);
    
    @Context
    private HttpServletRequest request;

    @GET
    @Path("current")
    public SecurityUser getCurrentUser() {
        Principal principal = sessionContext.getCallerPrincipal();
        SecurityUser user = dataService.get(SecurityUser.class, principal.getName());
        return user;
    }

    @POST
    public SecurityUser save(SecurityUser user) {
        try {
            return dataService.saveUser(user, request);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
            throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
        }
    }

    @POST
    @Path("changepassword")
    public SecurityUser changePassword(SecurityUser user) {
        try {
            return dataService.changePassword(user);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
            throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
        }
    }

    @POST
    @Path("confirm")
    @Consumes({"text/plain"})
    public void save(String confirmationId) {
        dataService.confirmUser(confirmationId);
    }

    @GET
    @Path("logout")
    public void logout(@Context HttpServletRequest req) {
        req.getSession().invalidate();
    }
}
