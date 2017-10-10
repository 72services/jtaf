package ch.jtaf.boundry;

import ch.jtaf.control.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/res/reports", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportResource {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReportResource.class);

    @Autowired
    protected ReportService service;

    @GetMapping(value = "sheets", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getSheets(HttpServletRequest hsr, @RequestParam("competitionid") Long competitionid,
                            @RequestParam("categoryid") Long categoryid, @RequestParam("orderby") String order) {
        try {
            byte[] report = null;
            if (competitionid != null) {
                report = service.createSheets(competitionid, order, hsr.getLocale());
            }
            if (categoryid != null) {
                report = service.createEmptySheets(categoryid, hsr.getLocale());
            }
            if (report == null) {
                throw new NotFoundException();
            } else {
                return report;
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new NotFoundException();
        }
    }

    @GetMapping(value = "numbers", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getNumbers(HttpServletRequest hsr, @RequestParam("competitionid") Long competitionid,
                             @RequestParam("orderby") String order) {
        try {
            byte[] report = null;
            if (competitionid != null) {
                report = service.createNumbers(competitionid, order, hsr.getLocale());
            }
            if (report == null) {
                throw new NotFoundException();
            } else {
                return report;
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new NotFoundException();
        }
    }

    @GetMapping(value = "export.csv", produces = "text/comma-separated-values")
    public String exportAsCsv(@RequestParam("competitionid") Long competitionid) {
        if (competitionid == null) {
            throw new BadRequestException();
        } else {
            String csv = service.createCompetitionRankingAsCsv(competitionid);
            if (csv == null) {
                throw new NotFoundException();
            } else {
                return csv;
            }
        }
    }

}
