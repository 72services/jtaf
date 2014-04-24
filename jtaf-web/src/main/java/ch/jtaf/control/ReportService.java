package ch.jtaf.control;

import ch.jtaf.report.CompetitionCsvExport;
import ch.jtaf.report.CompetitionRanking;
import ch.jtaf.report.EventsRanking;
import ch.jtaf.report.SeriesRanking;
import ch.jtaf.report.Sheet;
import ch.jtaf.comperator.AthleteCompetitionResultComparator;
import ch.jtaf.comperator.AthleteSeriesResultComparator;
import ch.jtaf.data.CompetitionRankingCategoryData;
import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Category;
import ch.jtaf.entity.Competition;
import ch.jtaf.data.CompetitionRankingData;
import ch.jtaf.data.EventsRankingEventData;
import ch.jtaf.data.SeriesRankingCategoryData;
import ch.jtaf.data.EventsRankingData;
import ch.jtaf.entity.Series;
import ch.jtaf.data.SeriesRankingData;
import ch.jtaf.entity.Event;
import ch.jtaf.entity.EventType;
import ch.jtaf.entity.Result;
import ch.jtaf.interceptor.TraceInterceptor;
import ch.jtaf.to.AthleteWithEventTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
public class ReportService extends AbstractService {

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

            Sheet sheet = new Sheet(competition, athletes, get(Series.class, competition.getSeries_id()).getLogo());
            return sheet.create();
        }
    }

    public byte[] createCompetitionRanking(Long competitionId) {
        CompetitionRankingData ranking = getCompetitionRanking(competitionId);
        if (ranking == null) {
            return null;
        }
        CompetitionRanking report = new CompetitionRanking(ranking);
        return report.create();
    }

    public byte[] createSeriesRanking(Long seriesId) {
        SeriesRankingData ranking = getSeriesRanking(seriesId);
        if (ranking == null) {
            return null;
        }
        SeriesRanking report = new SeriesRanking(ranking);
        return report.create();
    }

    public byte[] createEmptySheets(Long categoryid) {
        Category category = em.find(Category.class, categoryid);
        if (category == null) {
            throw new IllegalArgumentException();
        }
        Series series = em.find(Series.class, category.getSeries_id());
        if (series == null) {
            throw new IllegalArgumentException();
        }
        Athlete template = new Athlete();
        template.setCategory(category);

        Sheet sheet = new Sheet(template, series.getLogo());
        return sheet.create();
    }

    public String createCompetitionRankingAsCsv(Long competitionId) {
        CompetitionRankingData ranking = getCompetitionRanking(competitionId);
        if (ranking == null) {
            return null;
        }
        CompetitionCsvExport export = new CompetitionCsvExport(ranking);
        return export.create();
    }

    public byte[] createEventsRanking(Long competitionId) {
        EventsRankingData ranking = getEventsRanking(competitionId);
        if (ranking == null) {
            return null;
        }
        EventsRanking report = new EventsRanking(ranking);
        return report.create();
    }

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

    private EventsRankingData getEventsRanking(Long competitionid) {
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
            Collections.sort(entry.getValue(), new Comparator<AthleteWithEventTO>() {

                @Override
                public int compare(AthleteWithEventTO o1, AthleteWithEventTO o2) {
                    if (o1.getEvent().getType().equals(EventType.JUMP_THROW)) {
                        return o2.getResult().getResult().compareTo(o1.getResult().getResult());
                    } else {
                        return o1.getResult().getResult().compareTo(o2.getResult().getResult());
                    }
                }

            });
            re.setAthletes(entry.getValue());
            ranking.getEvents().add(re);
        }
        Collections.sort(ranking.getEvents(), new Comparator<EventsRankingEventData>() {

            @Override
            public int compare(EventsRankingEventData o1, EventsRankingEventData o2) {
                if (o1.getEvent().getName().equals(o2.getEvent().getName())) {
                    return o2.getEvent().getGender().compareTo(o1.getEvent().getGender());
                } else {
                    return o1.getEvent().getName().compareTo(o2.getEvent().getName());
                }
            }
        });
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
            if (athlete.getCategory().getEvents().size() * series.getCompetitions().size() == athlete.getResults().size()) {
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
