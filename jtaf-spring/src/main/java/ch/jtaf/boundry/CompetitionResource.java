package ch.jtaf.boundry;

import ch.jtaf.entity.Competition;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/res/competitions", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompetitionResource extends BaseResource {

    @GetMapping
    public List<Competition> list(@RequestParam("series_id") Long seriesId) {
        return dataService.getCompetititions(seriesId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Competition save(@RequestBody Competition competition) {
        if (isUserGrantedForSeries(competition.getSeries_id())) {
            return dataService.save(competition);
        } else {
            throw new ForbiddenException();
        }
    }

    @GetMapping("{id}")
    public Competition get(@PathVariable Long id) {
        Competition c = dataService.get(Competition.class, id);
        if (c == null) {
            throw new NotFoundException();
        } else {
            return c;
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        Competition c = dataService.get(Competition.class, id);
        if (c == null) {
            throw new NotFoundException();
        } else if (isUserGrantedForSeries(c.getSeries_id())) {
            dataService.delete(c);
        } else {
            throw new ForbiddenException();
        }
    }
}
