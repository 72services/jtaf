package ch.jtaf.boundry;

import ch.jtaf.entity.SecurityUser;
import ch.jtaf.interceptor.TraceInterceptor;
import java.security.Principal;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("users")
@Produces({"application/json"})
@Consumes({"application/json"})
@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserResource extends BaseResource {

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
            return dataService.saveUser(user);
        } catch (IllegalStateException e) {
            throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
        }
    }

    @POST
    @Path("confirm")
    @Consumes({"text/plain"})
    public void save(String confirmationId) {
        dataService.confirmUser(confirmationId);
    }
}
