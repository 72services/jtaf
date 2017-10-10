package ch.jtaf.boundry;

import ch.jtaf.entity.Athlete;
import ch.jtaf.to.AthleteTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/res/athletes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AthleteResource extends BaseResource {

    @GetMapping
    public List<AthleteTO> list(@RequestParam("series_id") Long seriesId) {
        return dataService.getAthleteTOs(seriesId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Athlete save(@RequestParam("competition_id") Long competitionId, @RequestBody Athlete a) {
        if (isUserGrantedForSeries(a.getSeries_id())) {
            Athlete savedAthlete = dataService.saveAthlete(a);
            if (competitionId != null) {
                dataService.saveResults(competitionId, a.getId(), a.getResults());
                return dataService.getAthlete(savedAthlete.getId(), competitionId);
            } else {
                return savedAthlete;
            }
        } else {
            throw new ForbiddenException();
        }
    }

    @GetMapping("{id}")
    public Athlete get(@PathVariable Long id, @RequestParam("competition_id") Long competitionId) {
        Athlete a = dataService.getAthlete(id, competitionId);
        if (a == null) {
            throw new NotFoundException();
        } else {
            return a;
        }
    }

    @GetMapping("search")
    public List<Athlete> search(@RequestParam("series_id") Long seriesId,
                                @RequestParam("competition_id") Long competitionId,
                                @RequestParam("query") String query) {
        return dataService.searchAthletes(seriesId, competitionId, query);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        Athlete a = dataService.get(Athlete.class, id);
        if (a == null) {
            throw new NotFoundException();
        } else if (isUserGrantedForSeries(a.getSeries_id())) {
            dataService.delete(a);
        } else {
            throw new ForbiddenException();
        }
    }
}
