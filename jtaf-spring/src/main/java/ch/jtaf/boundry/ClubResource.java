package ch.jtaf.boundry;

import ch.jtaf.entity.Club;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/res/clubs", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClubResource extends BaseResource {

    @GetMapping
    public List<Club> list(@RequestParam("space_id") Long spaceId) {
        return dataService.getClubs(spaceId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Club save(@RequestBody Club club) {
        if (isUserGrantedForSpace(club.getSpace_id())) {
            return dataService.save(club);
        } else {
            throw new ForbiddenException();
        }
    }

    @GetMapping("{id}")
    public Club get(@PathVariable Long id) {
        Club c = dataService.get(Club.class, id);
        if (c == null) {
            throw new NotFoundException();
        } else {
            return c;
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        Club c = dataService.get(Club.class, id);
        if (c == null) {
            throw new NotFoundException();
        } else if (isUserGrantedForSpace(c.getSpace_id())) {
            dataService.delete(c);
        } else {
            throw new ForbiddenException();
        }
    }
}
