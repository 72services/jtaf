package ch.jtaf.boundry;

import ch.jtaf.entity.SecurityUser;
import ch.jtaf.entity.UserSpace;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("userspaces")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class UserSpaceResource extends BaseResource {

    @GET
    public List<UserSpace> list(@QueryParam("space_id") Long spaceId) {
        return dataService.getUserSpacesBySpaceId(spaceId);
    }

    @GET
    @Path("current")
    public List<UserSpace> getUsersUserspaces() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return dataService.getUserSpacesOfUser(authentication.getName());
        } else {
            return new ArrayList<>();
        }
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
