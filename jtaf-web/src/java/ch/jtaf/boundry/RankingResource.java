package ch.jtaf.boundry;

import ch.jtaf.control.RankingService;
import ch.jtaf.entity.CompetitionRankingTO;
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
    private RankingService service;

    @GET
    @Path("competition/{competitionid}")
    public CompetitionRankingTO getCompetitionRanking(@PathParam("competitionid") Long competitionid) {
        return service.getCompetitionRanking(competitionid);
    }
}
