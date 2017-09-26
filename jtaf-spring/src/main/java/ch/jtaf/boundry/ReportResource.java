package ch.jtaf.boundry;

import ch.jtaf.control.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("reports")
@Component
public class ReportResource {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReportResource.class);

    @Autowired
    protected ReportService service;

    @GET
    @Path("sheets")
    @Produces("application/pdf")
    public byte[] getSheets(@Context HttpServletRequest hsr, @QueryParam("competitionid") Long competitionid,
            @QueryParam("categoryid") Long categoryid, @QueryParam("orderby") String order) {
        try {
            byte[] report = null;
            if (competitionid != null) {
                report = service.createSheets(competitionid, order, hsr.getLocale());
            }
            if (categoryid != null) {
                report = service.createEmptySheets(categoryid, hsr.getLocale());
            }
            if (report == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                return report;
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @GET
    @Path("numbers")
    @Produces("application/pdf")
    public byte[] getNumbers(@Context HttpServletRequest hsr, @QueryParam("competitionid") Long competitionid,
            @QueryParam("orderby") String order) {
        try {
            byte[] report = null;
            if (competitionid != null) {
                report = service.createNumbers(competitionid, order, hsr.getLocale());
            }
            if (report == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                return report;
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
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
