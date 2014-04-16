package ch.jtaf.control;

import ch.jtaf.control.util.AthleteCompetitionResultComparator;
import ch.jtaf.control.util.AthleteSeriesResultComparator;
import ch.jtaf.entity.Athlete;
import ch.jtaf.to.AthleteWithEventTO;
import ch.jtaf.entity.Category;
import ch.jtaf.entity.Competition;
import ch.jtaf.data.CompetitionRankingData;
import ch.jtaf.data.CompetitionRankingCategoryData;
import ch.jtaf.entity.Event;
import ch.jtaf.entity.EventsRankingData;
import ch.jtaf.data.EventsRankingEventData;
import ch.jtaf.entity.Result;
import ch.jtaf.entity.Series;
import ch.jtaf.data.SeriesRankingCategoryData;
import ch.jtaf.data.SeriesRankingData;
import ch.jtaf.interceptor.TraceInterceptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.TypedQuery;

@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
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
            rc.setAthletes(filterAndSortByTotalPoints(competition, entry.getValue()));
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

    public EventsRankingData getEventsRanking(Long competitionid) {
        Competition competition = em.find(Competition.class, competitionid);
        if (competition == null) {
            return null;
        }

        TypedQuery<Athlete> q = em.createNamedQuery("Athlete.findByCompetition", Athlete.class);
        q.setParameter("competitionid", competitionid);
        List<Athlete> list = q.getResultList();

        EventsRankingData ranking = new EventsRankingData();
        ranking.setCompetition(competition);

        Map<Event, List<AthleteWithEventTO>> map = new HashMap<>();
        for (Athlete a : list) {
            for (Result r : a.getResults()) {
                if (r.getCompetition().getId().equals(competitionid)) {
                    List<AthleteWithEventTO> as = map.get(r.getEvent());
                    if (as == null) {
                        as = new ArrayList<>();
                    }
                    as.add(new AthleteWithEventTO(a, r.getEvent(), r));
                    map.put(r.getEvent(), as);
                }
            }
        }
        for (Map.Entry<Event, List<AthleteWithEventTO>> entry : map.entrySet()) {
            EventsRankingEventData re = new EventsRankingEventData();
            Event e = entry.getKey();
            re.setEvent(e);
            Collections.sort(entry.getValue());
            re.setAthletes(entry.getValue());
            ranking.getEvents().add(re);
        }
        Collections.sort(ranking.getEvents());
        return ranking;
    }

    private List<Athlete> filterAndSortByTotalPoints(Competition competition, List<Athlete> list) {
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
