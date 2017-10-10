package ch.jtaf.boundry;

import ch.jtaf.entity.Event;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/res/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventResource extends BaseResource {

    @GetMapping
    public List<Event> list(@RequestParam("series_id") Long seriesId) {
        return dataService.getEvents(seriesId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Event save(@RequestBody Event event) {
        if (isUserGrantedForSeries(event.getSeries_id())) {
            return dataService.save(event);
        } else {
            throw new ForbiddenException();
        }
    }

    @GetMapping("{id}")
    public Event get(@PathVariable Long id) {
        Event e = dataService.get(Event.class, id);
        if (e == null) {
            throw new NotFoundException();
        } else {
            return e;
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        Event e = dataService.get(Event.class, id);
        if (e == null) {
            throw new NotFoundException();
        } else if (isUserGrantedForSeries(e.getSeries_id())) {
            dataService.delete(e);
        } else {
            throw new ForbiddenException();
        }
    }
}
