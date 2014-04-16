package ch.jtaf.boundry;

import ch.jtaf.control.RankingService;
import ch.jtaf.data.CompetitionRankingData;
import ch.jtaf.interceptor.TraceInterceptor;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("rankings")
@Produces({"application/json"})
@Consumes({"application/json"})
@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class RankingResource {

    @EJB
    private RankingService service;

    @GET
    @Path("competition/{competitionid}")
    public CompetitionRankingData getCompetitionRanking(@PathParam("competitionid") Long competitionid) {
        CompetitionRankingData data = service.getCompetitionRanking(competitionid);
        if (data == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return data;
        }
    }
}
