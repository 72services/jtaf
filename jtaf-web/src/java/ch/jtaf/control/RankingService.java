package ch.jtaf.control;

import ch.jtaf.control.util.AthleteCompetitionResultComparator;
import ch.jtaf.control.util.AthleteSeriesResultComparator;
import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Category;
import ch.jtaf.entity.Competition;
import ch.jtaf.entity.CompetitionRankingData;
import ch.jtaf.entity.CompetitionRankingCategoryData;
import ch.jtaf.entity.Result;
import ch.jtaf.entity.Series;
import ch.jtaf.entity.SeriesRankingCategoryData;
import ch.jtaf.entity.SeriesRankingData;
import ch.jtaf.interceptor.TraceInterceptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.TypedQuery;

@Interceptors({TraceInterceptor.class})
@Stateless
public class RankingService extends AbstractService {

    public CompetitionRankingData getCompetitionRanking(Long competitionid) {
        Competition competition = em.find(Competition.class, competitionid);
        if (competition == null) {
            return null;
        }

        TypedQuery<Athlete> q = em.createNamedQuery("Athlete.findByCompetition", Athlete.class);
        q.setParameter("competitionid", competitionid);
        List<Athlete> list = q.getResultList();

        CompetitionRankingData ranking = new CompetitionRankingData();
        ranking.setCompetition(competition);

        Map<Category, List<Athlete>> map = new HashMap<>();
        for (Athlete a : list) {
            List<Athlete> as = map.get(a.getCategory());
            if (as == null) {
                as = new ArrayList<>();
            }
            as.add(a);
            map.put(a.getCategory(), as);
        }
        for (Map.Entry<Category, List<Athlete>> entry : map.entrySet()) {
            CompetitionRankingCategoryData rc = new CompetitionRankingCategoryData();
            Category c = entry.getKey();
            c.setEvents(null);
            rc.setCategory(c);
            rc.setAthletes(filterAndSort(competition, entry.getValue()));
            ranking.getCategories().add(rc);
        }
        Collections.sort(ranking.getCategories());
        return ranking;
    }

    public SeriesRankingData getSeriesRanking(Long seriesId) {
        Series series = em.find(Series.class, seriesId);
        if (series == null) {
            return null;
        }
        TypedQuery<Athlete> q = em.createNamedQuery("Athlete.findBySeries", Athlete.class);
        q.setParameter("series_id", seriesId);
        List<Athlete> list = q.getResultList();

        TypedQuery<Competition> qc = em.createNamedQuery("Competition.findAll", Competition.class);
        qc.setParameter("series_id", seriesId);
        List<Competition> cs = qc.getResultList();
        series.setCompetitions(cs);

        SeriesRankingData ranking = new SeriesRankingData();
        ranking.setSeries(series);

        Map<Category, List<Athlete>> map = new HashMap<>();
        for (Athlete a : list) {
            List<Athlete> as = map.get(a.getCategory());
            if (as == null) {
                as = new ArrayList<>();
            }
            as.add(a);
            map.put(a.getCategory(), as);
        }
        for (Map.Entry<Category, List<Athlete>> entry : map.entrySet()) {
            SeriesRankingCategoryData rc = new SeriesRankingCategoryData();
            Category c = entry.getKey();
            rc.setCategory(c);
            rc.setAthletes(filterAndSort(series, entry.getValue()));
            ranking.getCategories().add(rc);
        }
        Collections.sort(ranking.getCategories());
        return ranking;
    }

    private List<Athlete> filterAndSort(Competition competition, List<Athlete> list) {
        for (Athlete a : list) {
            a.setCategory(null);

            List<Result> rs = new ArrayList<>();
            for (Result r : a.getResults()) {
                if (r.getCompetition().equals(competition)) {
                    rs.add(r);
                }
            }
            a.setResults(rs);
        }
        Collections.sort(list, new AthleteCompetitionResultComparator(competition));
        return list;
    }

    private List<Athlete> filterAndSort(Series series, List<Athlete> list) {
        List<Athlete> filtered = new ArrayList<>();
        for (Athlete athlete : list) {
            int soll = athlete.getCategory().getEvents().size() * series.getCompetitions().size();
            int ist = athlete.getResults().size();
            if (ist == soll) {
                athlete.setCategory(null);
                List<Result> rs = new ArrayList<>();
                for (Result r : athlete.getResults()) {
                    rs.add(r);
                }
                athlete.setResults(rs);
                filtered.add(athlete);
            }
        }
        Collections.sort(filtered, new AthleteSeriesResultComparator(series));
        return filtered;
    }
}
