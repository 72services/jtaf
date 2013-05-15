package ch.jtaf.boundry;

import ch.jtaf.control.ReportService;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("reports")
@Stateless
public class ReportResource {

    @EJB
    private ReportService service;

    @GET
    @Path("sheet")
    @QueryParam("{competitionid}")
    @Produces({"application/pdf"})
    public byte[] getSheets(@QueryParam("competitionid") Long competitionid) {
        if (competitionid == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            byte[] report = service.createSheets(competitionid);
            if (report == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                return report;
            }

        }
    }

    @GET
    @Path("competitionranking")
    @QueryParam("{competitionid}")
    @Produces({"application/pdf"})
    public byte[] getCompetitionRanking(@QueryParam("competitionid") Long competitionid) {
        if (competitionid == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            byte[] report = service.createCompetitionRanking(competitionid);
            if (report == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                return report;
            }
        }
    }

    @GET
    @Path("seriesranking")
    @QueryParam("{seriesid}")
    @Produces({"application/pdf"})
    public byte[] getSeriesRanking(@QueryParam("seriesid") Long seriesid) {
        if (seriesid == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            byte[] report = service.createSeriesRanking(seriesid);
            if (report == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                return report;
            }
        }
    }
}
