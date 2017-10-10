package ch.jtaf.boundry;

import ch.jtaf.entity.SecurityUser;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/res/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserResource extends BaseResource {

    @GetMapping("current")
    public SecurityUser getCurrentUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        SecurityUser securityUser = dataService.get(SecurityUser.class, userName);
        if (securityUser == null) {
            throw new NoContentException();
        } else {
            return securityUser;
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public SecurityUser save(HttpServletRequest hsr, @RequestBody SecurityUser user) {
        return dataService.saveUser(user, hsr.getLocale());
    }

    @PostMapping(value = "changepassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SecurityUser changePassword(@RequestBody SecurityUser user) {
        return dataService.changePassword(user);
    }

    @PostMapping(value = "confirm", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void save(String confirmationId) {
        dataService.confirmUser(confirmationId);
    }

    @GetMapping("logout")
    public void logout(HttpServletRequest req) {
        req.getSession().invalidate();
    }
}
