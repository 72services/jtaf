package ch.jtaf.control;

import ch.jtaf.report.CompetitionCsvExport;
import ch.jtaf.report.CompetitionRanking;
import ch.jtaf.report.EventsRanking;
import ch.jtaf.report.SeriesRanking;
import ch.jtaf.report.Sheets;
import ch.jtaf.comperator.AthleteCompetitionResultComparator;
import ch.jtaf.comperator.AthleteSeriesResultComparator;
import ch.jtaf.vo.CompetitionRankingCategoryVO;
import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Category;
import ch.jtaf.entity.Club;
import ch.jtaf.entity.Competition;
import ch.jtaf.vo.CompetitionRankingVO;
import ch.jtaf.vo.EventsRankingEventData;
import ch.jtaf.vo.SeriesRankingCategoryVO;
import ch.jtaf.vo.EventsRankingVO;
import ch.jtaf.entity.Series;
import ch.jtaf.vo.SeriesRankingVO;
import ch.jtaf.entity.Event;
import ch.jtaf.entity.EventType;
import ch.jtaf.entity.Result;
import ch.jtaf.interceptor.TraceInterceptor;
import ch.jtaf.report.ClubRanking;
import ch.jtaf.report.Diplomas;
import ch.jtaf.report.Numbers;
import ch.jtaf.to.AthleteWithEventTO;
import ch.jtaf.vo.ClubRankingVO;
import ch.jtaf.vo.ClubResultVO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ReportService extends AbstractService {

    public byte[] createSheets(Long competitionId, String order, Locale locale) {
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

            Sheets sheet = new Sheets(competition, athletes, get(Series.class, competition.getSeries_id()).getLogo(), locale);
            return sheet.create();
        }
    }

    public byte[] createNumbers(Long competitionId, String order, Locale locale) {
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

            Numbers numbers = new Numbers(competition, athletes, null, locale);
            return numbers.create();
        }
    }

    public byte[] createCompetitionRanking(Long competitionId, Locale locale) {
        CompetitionRankingVO ranking = getCompetitionRanking(competitionId);
        if (ranking == null) {
            return null;
        }
        CompetitionRanking report = new CompetitionRanking(ranking, locale);
        return report.create();
    }

    public byte[] createSeriesRanking(Long seriesId, Locale locale) {
        SeriesRankingVO ranking = getSeriesRanking(seriesId);
        if (ranking == null) {
            return null;
        }
        SeriesRanking report = new SeriesRanking(ranking, locale);
        return report.create();
    }

    public byte[] createEmptySheets(Long categoryid, Locale locale) {
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

        Sheets sheet = new Sheets(template, series.getLogo(), locale);
        return sheet.create();
    }

    public String createCompetitionRankingAsCsv(Long competitionId) {
        CompetitionRankingVO ranking = getCompetitionRanking(competitionId);
        if (ranking == null) {
            return null;
        }
        CompetitionCsvExport export = new CompetitionCsvExport(ranking);
        return export.create();
    }

    public byte[] createEventsRanking(Long competitionId, Locale locale) {
        EventsRankingVO ranking = getEventsRanking(competitionId);
        if (ranking == null) {
            return null;
        }
        EventsRanking report = new EventsRanking(ranking, locale);
        return report.create();
    }

    public byte[] createDiploma(Long competitionId, Locale locale) {
        CompetitionRankingVO ranking = getCompetitionRanking(competitionId);
        if (ranking == null) {
            return null;
        }
        Series series = get(Series.class, ranking.getCompetition().getSeries_id());

        Diplomas diploma = new Diplomas(ranking, series.getLogo(), locale);
        return diploma.create();
    }

    public CompetitionRankingVO getCompetitionRanking(Long competitionid) {
        Competition competition = em.find(Competition.class, competitionid);
        if (competition == null) {
            return null;
        }

        List<Athlete> list = getAthletesWithCompetionResults(competitionid);

        CompetitionRankingVO ranking = new CompetitionRankingVO();
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
            CompetitionRankingCategoryVO rc = new CompetitionRankingCategoryVO();
            Category c = entry.getKey();
            c.setEvents(null);
            rc.setCategory(c);
            rc.setAthletes(filterAndSortByTotalPoints(competition, entry.getValue()));
            ranking.getCategories().add(rc);
        }
        Collections.sort(ranking.getCategories());
        return ranking;
    }

    public SeriesRankingVO getSeriesRanking(Long seriesId) {
        Series series = em.find(Series.class, seriesId);
        if (series == null) {
            return null;
        }
        TypedQuery<Athlete> q = em.createNamedQuery("Athlete.findBySeries", Athlete.class);
        q.setParameter("series_id", seriesId);
        List<Athlete> list = q.getResultList();
        for (Athlete a : list) {
            TypedQuery<Result> tq = em.createNamedQuery("Result.findByAthleteAndSeries", Result.class);
            tq.setParameter("athleteId", a.getId());
            tq.setParameter("seriesId", seriesId);
            List<Result> results = tq.getResultList();
            a.setResults(results);
        }

        TypedQuery<Competition> qc = em.createNamedQuery("Competition.findAll", Competition.class);
        qc.setParameter("series_id", seriesId);
        List<Competition> cs = qc.getResultList();
        series.setCompetitions(cs);

        SeriesRankingVO ranking = new SeriesRankingVO();
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
            SeriesRankingCategoryVO rc = new SeriesRankingCategoryVO();
            Category c = entry.getKey();
            rc.setCategory(c);
            rc.setAthletes(filterAndSort(series, entry.getValue()));
            ranking.getCategories().add(rc);
        }
        Collections.sort(ranking.getCategories());
        return ranking;
    }

    private EventsRankingVO getEventsRanking(Long competitionid) {
        Competition competition = em.find(Competition.class, competitionid);
        if (competition == null) {
            return null;
        }

        List<Athlete> list = getAthletesWithCompetionResults(competitionid);

        EventsRankingVO ranking = new EventsRankingVO();
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
                        return o2.getResult().getResultAsDouble().compareTo(o1.getResult().getResultAsDouble());
                    } else {
                        return o1.getResult().getResultAsDouble().compareTo(o2.getResult().getResultAsDouble());
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

    public byte[] createClubRanking(Long seriesId, Locale locale) {
        ClubRankingVO ranking = getClubRanking(seriesId);

        ClubRanking report = new ClubRanking(ranking, locale);
        return report.create();
    }

    private List<Athlete> getAthletesWithCompetionResults(Long competitionid) {
        Query q = em.createNativeQuery("select distinct a.* from athlete a join result r on a.id = r.athlete_id and r.competition_id = ?", Athlete.class);
        q.setParameter(1, competitionid);
        List<Athlete> as = q.getResultList();

        TypedQuery<Result> tq = em.createNamedQuery("Result.findByCompetition", Result.class);
        tq.setParameter("competitionId", competitionid);
        List<Result> results = tq.getResultList();
        Map<Long, List<Result>> map = new HashMap<>();
        for (Result r : results) {
            List<Result> entry = map.get(r.getAthlete_id());
            if (entry != null) {
                entry.add(r);
            } else {
                List<Result> list = new ArrayList<>();
                list.add(r);
                map.put(r.getAthlete_id(), list);
            }
        }

        for (Athlete a : as) {
            a.setResults(map.get(a.getId()));
        }
        return as;
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

    private ClubRankingVO getClubRanking(Long seriesId) {
        final SeriesRankingVO seriesRanking = getSeriesRanking(seriesId);
        Map<Club, ClubResultVO> pointsPerClub = new HashMap<>();
        for (SeriesRankingCategoryVO c : seriesRanking.getCategories()) {
            int points = c.getAthletes().size();
            for (Athlete a : c.getAthletes()) {
                if (pointsPerClub.containsKey(a.getClub())) {
                    ClubResultVO cr = pointsPerClub.get(a.getClub());
                    cr.setPoints(cr.getPoints() + points);
                } else {
                    ClubResultVO cr = new ClubResultVO();
                    cr.setClub(a.getClub());
                    cr.setPoints(points);
                    pointsPerClub.put(a.getClub(), cr);
                }
                points--;
            }
        }

        ClubRankingVO cr = new ClubRankingVO();
        cr.setSeries(seriesRanking.getSeries());
        cr.setClubs(new ArrayList(pointsPerClub.values()));
        Collections.sort(cr.getClubs());
        return cr;
    }
}
