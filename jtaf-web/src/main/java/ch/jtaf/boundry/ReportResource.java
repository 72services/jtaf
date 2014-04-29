package ch.jtaf.boundry;

import ch.jtaf.control.ReportService;
import ch.jtaf.interceptor.TraceInterceptor;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("reports")
@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ReportResource {

    @EJB
    protected ReportService service;

    @GET
    @Path("sheets")
    @Produces({"application/pdf"})
    public byte[] getSheets(@QueryParam("competitionid") Long competitionid,
            @QueryParam("categoryid") Long categoryid, @QueryParam("orderby") String order) {
        try {
            byte[] report = null;
            if (competitionid != null) {
                report = service.createSheets(competitionid, order);
            }
            if (categoryid != null) {
                report = service.createEmptySheets(categoryid);
            }
            if (report == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                return report;
            }
        } catch (IllegalArgumentException e) {
            Logger.getLogger(ReportResource.class).error(e.getMessage(), e);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @GET
    @Path("numbers")
    @Produces({"application/pdf"})
    public byte[] getNumbers(@QueryParam("competitionid") Long competitionid,
            @QueryParam("orderby") String order) {
        try {
            byte[] report = null;
            if (competitionid != null) {
                report = service.createNumbers(competitionid, order);
            }
            if (report == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                return report;
            }
        } catch (IllegalArgumentException e) {
            Logger.getLogger(ReportResource.class).error(e.getMessage(), e);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @GET
    @Path("export.csv")
    @Produces({"text/comma-separated-values"})
    public String exportAsCsv(@QueryParam("competitionid") Long competitionid) {
        if (competitionid == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            String csv = service.createCompetitionRankingAsCsv(competitionid);
            if (csv == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                return csv;
            }
        }
    }

}
