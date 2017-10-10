package ch.jtaf.boundry;

import ch.jtaf.entity.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/res/spaces", produces = MediaType.APPLICATION_JSON_VALUE)
public class SpaceResource extends BaseResource {

    @GetMapping
    public List<Space> list() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return dataService.getMySpaces(userName);
    }

    @GetMapping("series")
    public List<Series> listSeries() {
        return dataService.getAllSeries();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Space save(@RequestBody Space space) {
        if (space.getId() == null) {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            SecurityUser user = dataService.get(SecurityUser.class, userName);
            UserSpace userSpace = new UserSpace();
            userSpace.setRole(UserSpaceRole.OWNER);
            userSpace.setSpace(space);
            userSpace.setUser(user);
            dataService.save(userSpace);
        } else {
            if (!isUserGrantedForSpace(space.getId())) {
                throw new ForbiddenException();
            }
        }
        return dataService.save(space);
    }

    @GetMapping("{id}")
    public Space get(@PathVariable Long id) {
        Space s = dataService.getSpace(id);
        if (s == null) {
            throw new NotFoundException();
        } else {
            return s;
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        Space s = dataService.get(Space.class, id);
        if (s == null) {
            throw new NotFoundException();
        } else if (isUserGrantedForSpace(s.getId())) {
            dataService.deleteSpace(s);
        } else {
            throw new ForbiddenException();
        }
    }
}
