package ch.jtaf.boundry;

import ch.jtaf.control.ReportService;
import ch.jtaf.vo.CompetitionRankingVO;
import ch.jtaf.vo.SeriesRankingVO;
import ch.jtaf.interceptor.TraceInterceptor;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("rankings")
@Produces("application/json")
@Consumes("application/json")
@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class RankingResource {

    @EJB
    protected ReportService reportService;

    @GET
    @Path("competition/{competitionid}")
    public CompetitionRankingVO getCompetitionRanking(@PathParam("competitionid") Long competitionid) {
        CompetitionRankingVO data = reportService.getCompetitionRanking(competitionid);
        if (data == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return data;
        }
    }

    @GET
    @Path("competition/pdf/{competitionid}")
    @Produces("application/pdf")
    public byte[] getCompetitionRankingAsPdf(@Context HttpServletRequest hsr, @PathParam("competitionid") Long competitionid) {
        if (competitionid == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            byte[] report = reportService.createCompetitionRanking(competitionid, hsr.getLocale());
            if (report == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                return report;
            }
        }
    }

    @GET
    @Path("series/{seriesid}")
    public SeriesRankingVO getSeriesRanking(@PathParam("seriesid") Long seriesid) {
        SeriesRankingVO data = reportService.getSeriesRanking(seriesid);
        if (data == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return data;
        }
    }

    @GET
    @Path("series/pdf/{seriesid}")
    @Produces("application/pdf")
    public byte[] getSeriesRankingAsPdf(@Context HttpServletRequest hsr, @PathParam("seriesid") Long seriesid) {
        if (seriesid == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            byte[] report = reportService.createSeriesRanking(seriesid, hsr.getLocale());
            if (report == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                return report;
            }
        }
    }

    @GET
    @Path("diploma/{competitionid}")
    @Produces("application/pdf")
    public byte[] getDiploma(@Context HttpServletRequest hsr, @PathParam("competitionid") Long competitionid) {
        if (competitionid == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            byte[] report = reportService.createDiploma(competitionid, hsr.getLocale());
            if (report == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                return report;
            }
        }
    }

    @GET
    @Path("events/{competitionid}")
    @Produces("application/pdf")
    public byte[] getEventsRanking(@Context HttpServletRequest hsr, @PathParam("competitionid") Long competitionid) {
        if (competitionid == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            byte[] report = reportService.createEventsRanking(competitionid, hsr.getLocale());
            if (report == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                return report;
            }
        }
    }

    @GET
    @Path("club/{seriesid}")
    @Produces("application/pdf")
    public byte[] getClubRanking(@Context HttpServletRequest hsr, @PathParam("seriesid") Long seriesId) {
        if (seriesId == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            byte[] report = reportService.createClubRanking(seriesId, hsr.getLocale());
            if (report == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                return report;
            }
        }
    }
}
