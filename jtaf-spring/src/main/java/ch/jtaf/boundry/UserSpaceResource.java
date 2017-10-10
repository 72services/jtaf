package ch.jtaf.boundry;

import ch.jtaf.entity.SecurityUser;
import ch.jtaf.entity.UserSpace;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Component
@RestController
@RequestMapping(value = "/res/userspaces", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserSpaceResource extends BaseResource {

    @GetMapping
    public List<UserSpace> list(@RequestParam("space_id") Long spaceId) {
        return dataService.getUserSpacesBySpaceId(spaceId);
    }

    @GetMapping("current")
    public List<UserSpace> getUsersUserspaces() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return dataService.getUserSpacesOfUser(authentication.getName());
        } else {
            return new ArrayList<>();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserSpace save(UserSpace userSpace) {
        if (isUserGrantedForSpace(userSpace.getSpace().getId())) {
            if (userSpace.getUser().getConfirmationId() == null) {
                SecurityUser user = dataService.get(SecurityUser.class, userSpace.getUser().getEmail());
                if (user == null) {
                    throw new NotFoundException();
                }
                userSpace.setUser(user);
            }
            return dataService.save(userSpace);
        } else {
            throw new ForbiddenException();
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) {
        UserSpace s = dataService.get(UserSpace.class, id);
        if (s == null) {
            throw new NotFoundException();
        } else if (isUserGrantedForSpace(s.getSpace().getId())) {
            dataService.delete(s);
        } else {
            throw new ForbiddenException();
        }
    }
}
