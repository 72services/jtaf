package ch.jtaf.control;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Category;
import ch.jtaf.entity.Competition;
import ch.jtaf.entity.Ranking;
import ch.jtaf.entity.RankingCategory;
import ch.jtaf.entity.Result;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.TypedQuery;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ReportSerivce extends AbstractService {

    public Ranking getCompetitionRanking(Long competitionid) {
        TypedQuery<Athlete> q = em.createNamedQuery("Athlete.findByCompetition", Athlete.class);
        q.setParameter("competitionid", competitionid);
        List<Athlete> list = q.getResultList();

        Competition competition = em.find(Competition.class, competitionid);
        competition.setSeries(null);

        Ranking ranking = new Ranking();
        ranking.setCompetition(competition);

        Map<Category, List<Athlete>> map = new HashMap<Category, List<Athlete>>();
        for (Athlete a : list) {
            List<Athlete> as = map.get(a.getCategory());
            if (as == null) {
                as = new ArrayList<Athlete>();
            }
            as.add(a);
            map.put(a.getCategory(), as);
        }
        for (Map.Entry<Category, List<Athlete>> entry : map.entrySet()) {
            RankingCategory rc = new RankingCategory();
            Category c = entry.getKey();
            c.setEvents(null);
            c.setSeries(null);
            rc.setCategory(c);
            rc.setAthletes(filterAndSort(competition, entry.getValue()));
            ranking.getCategories().add(rc);
        }
        return ranking;
    }

    private List<Athlete> filterAndSort(Competition competition, List<Athlete> list) {
        for (Athlete a : list) {
            a.setCategory(null);
            a.setSeries(null);
            List<Result> rs = new ArrayList<Result>();
            for (Result r : a.getResults()) {
                if (r.getCompetition().getId().compareTo(competition.getId()) == 0) {
                    r.setCompetition(null);
                    r.getEvent().setSeries(null);
                    rs.add(r);
                }
            }
            a.setResults(rs);
        }
        Collections.sort(list, new AthleteResultComparator());
        return list;
    }
}
