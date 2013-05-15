package ch.jtaf.control;

import ch.jtaf.control.report.CompetitionRanking;
import ch.jtaf.control.report.SeriesRanking;
import ch.jtaf.control.report.Sheet;
import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Competition;
import ch.jtaf.entity.CompetitionRankingData;
import ch.jtaf.entity.SeriesRankingData;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.TypedQuery;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ReportService extends AbstractService {

    @EJB
    private RankingService rankingService;

    public byte[] createSheets(Long competitionId) {
        Competition competition = em.find(Competition.class, competitionId);
        if (competition == null) {
            return null;
        } else {
            TypedQuery<Athlete> query = em.createNamedQuery("Athlete.findBySeries", Athlete.class);
            query.setParameter("seriesid", competition.getSeries().getId());
            List<Athlete> athletes = query.getResultList();

            Sheet sheet = new Sheet(competition, athletes);
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
}
