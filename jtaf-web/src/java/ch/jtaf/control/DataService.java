package ch.jtaf.control;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Category;
import ch.jtaf.entity.Club;
import ch.jtaf.entity.Competition;
import ch.jtaf.entity.Event;
import ch.jtaf.entity.Result;
import ch.jtaf.entity.SecurityUser;
import ch.jtaf.entity.Series;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.TypedQuery;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class DataService extends AbstractService {
    
    public List<Series> getSeriesList() {
        TypedQuery<Series> q = em.createNamedQuery("Series.findAll", Series.class);
        return q.getResultList();
    }
    
    public List<Series> getSeriesWithCompetitions() {
        List<Series> list = getSeriesList();
        List<Series> series = new ArrayList<>();
        for (Series s : list) {
            series.add(getSeries(s.getId()));
        }
        return series;
    }
    
    public List<Competition> getCompetititions() {
        TypedQuery<Competition> q = em.createNamedQuery("Competition.findAll", Competition.class);
        return q.getResultList();
    }
    
    public List<Event> getEvents() {
        TypedQuery<Event> q = em.createNamedQuery("Event.findAll", Event.class);
        return q.getResultList();
    }
    
    public List<Category> getCategories() {
        TypedQuery<Category> q = em.createNamedQuery("Category.findAll", Category.class);
        return q.getResultList();
    }
    
    public Series getSeries(Long id) {
        Series s = em.find(Series.class, id);
        TypedQuery<Competition> q = em.createNamedQuery("Competition.findBySeries",
                Competition.class);
        q.setParameter("series", s);
        List<Competition> cs = q.getResultList();
        for (Competition c : cs) {
            c.setSeries(null);
            c.setNumberOfAthletes(getNumberOfAthletes(c).intValue());
        }
        s.setCompetitions(cs);
        return s;
    }
    
    public List<Category> getCategoryFromSeries(Long id) {
        Series series = em.find(Series.class, id);
        TypedQuery<Category> q = em.createNamedQuery("Category.findBySeries", Category.class);
        q.setParameter("series", series);
        return q.getResultList();
    }
    
    public List<Event> getEventFromSeries(Long id) {
        Series series = em.find(Series.class, id);
        TypedQuery<Event> q = em.createNamedQuery("Event.findBySeries", Event.class);
        q.setParameter("series", series);
        return q.getResultList();
    }
    
    public List<Athlete> getAthleteFromSeries(Long id) {
        TypedQuery<Athlete> q = em.createNamedQuery("Athlete.findBySeries", Athlete.class);
        q.setParameter("seriesid", id);
        return q.getResultList();
    }
    
    public List<Club> getClubs() {
        TypedQuery<Club> q = em.createNamedQuery("Club.findAll", Club.class);
        return q.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Athlete saveAthlete(Athlete a) {
        Category c = this.getCategoryWithGenderAndAge(a.getSeries(), a.getGender(), a.getYear());
        if (c == null) {
            throw new IllegalArgumentException();
        }
        if (!c.equals(a.getCategory())) {
            // Category has changed. All Results must be deleted
            for (Result r : a.getResults()) {
                delete(r);
            }
            a.setResults(new ArrayList<Result>());
            a.setCategory(c);
        }
        return this.save(a);
    }
    
    private Category getCategoryWithGenderAndAge(Series series, String gender, int year) {
        TypedQuery<Category> q = em.createNamedQuery("Category.findBySeriesAndYearAndGender", Category.class);
        q.setParameter("gender", gender);
        q.setParameter("year", year);
        q.setParameter("series", series);
        return q.getSingleResult();
    }
    
    public List<Athlete> searchAthletes(String searchterm) {
        String queryString = "select a from Athlete a "
                + "where lower(a.lastName) like :searchterm "
                + "or lower(a.firstName) like :searchterm";
        TypedQuery query = em.createQuery(queryString, Athlete.class);
        if (searchterm != null) {
            searchterm = searchterm.toLowerCase();
        }
        searchterm += "%";
        query.setParameter("searchterm", searchterm);
        return query.getResultList();
    }
    
    private Long getNumberOfAthletes(Competition c) {
        String queryString = "select count(distinct a) from Athlete a join a.results r "
                + "where r.competition = :competition";
        TypedQuery query = em.createQuery(queryString, Athlete.class);
        query.setParameter("competition", c);
        return (Long) query.getSingleResult();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Series copySeries(Long id) {
        Series series = em.find(Series.class, id);
        Series copy = new Series();
        copy.setLogo(series.getLogo());
        copy.setName("Copy of " + series.getName());
        em.persist(copy);
        List<Event> events = copyEvents(series, copy);
        copyCategories(series, copy, events);
        copyAthletes(series, copy);
        return copy;
    }
    
    private List<Event> copyEvents(Series orig, Series series) {
        TypedQuery<Event> q = em.createNamedQuery("Event.findBySeries", Event.class);
        q.setParameter("series", orig);
        List<Event> events = q.getResultList();
        List<Event> copies = new ArrayList<>();
        for (Event event : events) {
            Event copy = new Event();
            copy.setA(event.getA());
            copy.setB(event.getB());
            copy.setC(event.getC());
            copy.setGender(event.getGender());
            copy.setName(event.getName());
            copy.setSeries(series);
            copy.setType(event.getType());
            em.persist(copy);
            copies.add(copy);
        }
        return copies;
    }
    
    private List<Category> copyCategories(Series orig, Series series, List<Event> events) {
        TypedQuery<Category> q = em.createNamedQuery("Category.findBySeries", Category.class);
        q.setParameter("series", orig);
        List<Category> categories = q.getResultList();
        List<Category> copies = new ArrayList<>();
        for (Category category : categories) {
            Category copy = new Category();
            copy.setAbbreviation(category.getAbbreviation());
            copy.setGender(category.getGender());
            copy.setName(category.getName());
            copy.setSeries(series);
            copy.setYearFrom(category.getYearFrom());
            copy.setYearTo(category.getYearTo());
            for (Event eventFromCategory : category.getEvents()) {
                for (Event eventCopy : events) {
                    if (eventFromCategory.getName().equals(eventCopy.getName())
                            && eventFromCategory.getGender().equals(eventCopy.getGender())) {
                        copy.getEvents().add(eventCopy);
                    }
                }
            }
            em.persist(copy);
            copies.add(copy);
        }
        return copies;
    }
    
    private void copyAthletes(Series orig, Series series) {
        TypedQuery<Athlete> q = em.createNamedQuery("Athlete.findBySeries", Athlete.class);
        q.setParameter("seriesid", orig.getId());
        List<Athlete> athletes = q.getResultList();
        for (Athlete athlete : athletes) {
            Athlete copy = new Athlete();
            copy.setCategory(getCategoryWithGenderAndAge(series, athlete.getGender(), athlete.getYear()));
            copy.setClub(athlete.getClub());
            copy.setFirstName(athlete.getFirstName());
            copy.setGender(athlete.getGender());
            copy.setLastName(athlete.getLastName());
            copy.setSeries(series);
            copy.setYear(athlete.getYear());
            em.persist(copy);
        }
    }
    
    public SecurityUser get(Class<SecurityUser> aClass, String name) {
        return em.find(aClass, name);
    }
}
