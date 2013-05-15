package ch.jtaf.control;

import ch.jtaf.control.report.CompetitionRanking;
import ch.jtaf.control.report.Sheet;
import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Competition;
import ch.jtaf.entity.CompetitionRankingTO;
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
        if (competition != null) {
            TypedQuery<Athlete> query = em.createNamedQuery("Athlete.findBySeries", Athlete.class);
            query.setParameter("series", competition.getSeries());
            List<Athlete> athletes = query.getResultList();

            Sheet sheet = new Sheet(competition, athletes);
            return sheet.create();
        } else {
            return new byte[0];
        }
    }

    public byte[] createCompetitionRanking(Long competitionId) {
        CompetitionRankingTO ranking = rankingService.getCompetitionRanking(competitionId);
        CompetitionRanking report = new CompetitionRanking(ranking);
        return report.create();
    }
}
