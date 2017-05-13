package ch.jtaf.boundry;

import ch.jtaf.entity.SecurityUser;
import ch.jtaf.interceptor.TraceInterceptor;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@Path("users")
@Produces("application/json")
@Consumes("application/json")
@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class UserResource extends BaseResource {

    private static final Logger LOGGER = Logger.getLogger(UserResource.class);

    @GET
    @Path("current")
    public SecurityUser getCurrentUser() {
        return dataService.get(SecurityUser.class, sessionContext.getCallerPrincipal().getName());
    }

    @POST
    public SecurityUser save(@Context HttpServletRequest hsr, SecurityUser user) {
        return dataService.saveUser(user, hsr.getLocale());
    }

    @POST
    @Path("changepassword")
    public SecurityUser changePassword(SecurityUser user) {
        return dataService.changePassword(user);
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
