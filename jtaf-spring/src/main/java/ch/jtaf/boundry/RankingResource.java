package ch.jtaf.boundry;

import ch.jtaf.control.ReportService;
import ch.jtaf.vo.CompetitionRankingVO;
import ch.jtaf.vo.SeriesRankingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/res/rankings", produces = MediaType.APPLICATION_JSON_VALUE)
public class RankingResource {

    @Autowired
    protected ReportService reportService;

    @GetMapping("competition/{competitionid}")
    public CompetitionRankingVO getCompetitionRanking(@PathVariable Long competitionid) {
        CompetitionRankingVO data = reportService.getCompetitionRanking(competitionid);
        if (data == null) {
            throw new NotFoundException();
        } else {
            return data;
        }
    }

    @GetMapping(value = "competition/pdf/{competitionid}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getCompetitionRankingAsPdf(HttpServletRequest hsr, @PathVariable Long competitionid) {
        if (competitionid == null) {
            throw new BadRequestException();
        } else {
            byte[] report = reportService.createCompetitionRanking(competitionid, hsr.getLocale());
            if (report == null) {
                throw new NotFoundException();
            } else {
                return report;
            }
        }
    }

    @GetMapping("series/{seriesid}")
    public SeriesRankingVO getSeriesRanking(@PathVariable Long seriesid) {
        SeriesRankingVO data = reportService.getSeriesRanking(seriesid);
        if (data == null) {
            throw new NotFoundException();
        } else {
            return data;
        }
    }

    @GetMapping(value = "series/pdf/{seriesid}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getSeriesRankingAsPdf(HttpServletRequest hsr, @PathVariable Long seriesid) {
        if (seriesid == null) {
            throw new BadRequestException();
        } else {
            byte[] report = reportService.createSeriesRanking(seriesid, hsr.getLocale());
            if (report == null) {
                throw new NotFoundException();
            } else {
                return report;
            }
        }
    }

    @GetMapping(value = "diploma/{competitionid}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getDiploma(HttpServletRequest hsr, @PathVariable Long competitionid) {
        if (competitionid == null) {
            throw new BadRequestException();
        } else {
            byte[] report = reportService.createDiploma(competitionid, hsr.getLocale());
            if (report == null) {
                throw new NotFoundException();
            } else {
                return report;
            }
        }
    }

    @GetMapping(value = "events/{competitionid}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getEventsRanking(HttpServletRequest hsr, @PathVariable Long competitionid) {
        if (competitionid == null) {
            throw new BadRequestException();
        } else {
            byte[] report = reportService.createEventsRanking(competitionid, hsr.getLocale());
            if (report == null) {
                throw new NotFoundException();
            } else {
                return report;
            }
        }
    }

    @GetMapping(value = "club/{seriesid}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getClubRanking(HttpServletRequest hsr, @PathVariable Long seriesId) {
        if (seriesId == null) {
            throw new BadRequestException();
        } else {
            byte[] report = reportService.createClubRanking(seriesId, hsr.getLocale());
            if (report == null) {
                throw new NotFoundException();
            } else {
                return report;
            }
        }
    }
}
