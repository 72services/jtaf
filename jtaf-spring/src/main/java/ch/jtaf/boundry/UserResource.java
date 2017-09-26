package ch.jtaf.boundry;

import ch.jtaf.entity.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class UserResource extends BaseResource {

    @GET
    @Path("current")
    public SecurityUser getCurrentUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return dataService.get(SecurityUser.class, userName);
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
