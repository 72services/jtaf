package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Ranking;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("rankings")
@Produces({"application/json"})
@Consumes({"application/json"})
@Stateless
public class RankingResource {

    @EJB
    private DataService service;

    @GET
    @Path("competition/{competitionid}")
    public Ranking getCompetitionRanking(@PathParam("competitionid") Long competitionid) {
        return service.getCompetitionRanking(competitionid);
    }
}
