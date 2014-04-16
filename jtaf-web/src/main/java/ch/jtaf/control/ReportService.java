package ch.jtaf.control;

import ch.jtaf.control.report.CompetitionCsvExport;
import ch.jtaf.control.report.CompetitionRanking;
import ch.jtaf.control.report.EventsRanking;
import ch.jtaf.control.report.SeriesRanking;
import ch.jtaf.control.report.Sheet;
import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Category;
import ch.jtaf.entity.Competition;
import ch.jtaf.entity.CompetitionRankingData;
import ch.jtaf.entity.EventsRankingData;
import ch.jtaf.entity.Series;
import ch.jtaf.entity.SeriesRankingData;
import ch.jtaf.interceptor.TraceInterceptor;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.TypedQuery;

@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ReportService extends AbstractService {

    @EJB
    private RankingService rankingService;

    public byte[] createSheets(Long competitionId, String order) {
        Competition competition = em.find(Competition.class, competitionId);
        if (competition == null) {
            return null;
        } else {
            TypedQuery<Athlete> query;
            if (order != null && order.equals("club")) {
                query = em.createNamedQuery("Athlete.findBySeriesOrderByClub", Athlete.class);
            } else {
                query = em.createNamedQuery("Athlete.findBySeries", Athlete.class);
            }
            query.setParameter("series_id", competition.getSeries_id());
            List<Athlete> athletes = query.getResultList();

            Sheet sheet = new Sheet(competition, athletes, rankingService.get(Series.class, competition.getSeries_id()).getLogo());
            return sheet.create();
        }
    }

    public byte[] createCompetitionRanking(Long competitionId) {
        CompetitionRankingData ranking = rankingService.getCompetitionRanking(competitionId);
        if (ranking == null) {
            return null;
        }
        CompetitionRanking report = new CompetitionRanking(ranking);
        return report.create();
    }

    public byte[] createSeriesRanking(Long seriesId) {
        SeriesRankingData ranking = rankingService.getSeriesRanking(seriesId);
        if (ranking == null) {
            return null;
        }
        SeriesRanking report = new SeriesRanking(ranking);
        return report.create();
    }

    public byte[] createEmptySheets(Long categoryid) {
        Category category = em.find(Category.class, categoryid);
        Series series = em.find(Series.class, category.getSeries_id());
        Athlete template = new Athlete();
        template.setCategory(category);

        Sheet sheet = new Sheet(template, series.getLogo());
        return sheet.create();
    }

    public String createCompetitionRankingAsCsv(Long competitionId) {
        CompetitionRankingData ranking = rankingService.getCompetitionRanking(competitionId);
        if (ranking == null) {
            return null;
        }
        CompetitionCsvExport export = new CompetitionCsvExport(ranking);
        return export.create();
    }

    public byte[] createEventsRanking(Long competitionId) {
        EventsRankingData ranking = rankingService.getEventsRanking(competitionId);
        if (ranking == null) {
            return null;
        }
        EventsRanking report = new EventsRanking(ranking);
        return report.create();
    }
}
