package ch.jtaf.boundry;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/res/result", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResultResource extends BaseResource {

    @GetMapping
    public Long calculatePoints(@RequestParam("event_id") Long eventId, @RequestParam("result") String result) {
        return dataService.calculatePoints(eventId, result);
    }

}
