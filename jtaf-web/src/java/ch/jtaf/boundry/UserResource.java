package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.SecurityUser;
import ch.jtaf.interceptor.TraceInterceptor;
import java.security.Principal;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
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
public class UserResource {

    @Resource
    private SessionContext sc;
    @EJB
    private DataService service;

    @GET
    @Path("current")
    public SecurityUser getCompetitionRanking() {
        Principal principal = sc.getCallerPrincipal();
        SecurityUser user = service.get(SecurityUser.class, principal.getName());
        if (user != null) {
            user.setSecret(null);
        }
        return user;
    }

    @POST
    public SecurityUser save(SecurityUser user) {
        try {
            return service.saveUser(user);
        } catch (IllegalStateException e) {
            throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
        }
    }
}
