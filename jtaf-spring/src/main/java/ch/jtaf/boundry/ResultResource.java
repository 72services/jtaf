package ch.jtaf.boundry;

import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("result")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class ResultResource extends BaseResource {

    @GET
    public Long calculatePoints(@QueryParam("event_id") Long eventId, @QueryParam("result") String result) {
        return dataService.calculatePoints(eventId, result);
    }

}
