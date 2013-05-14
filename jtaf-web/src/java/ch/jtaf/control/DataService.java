package ch.jtaf.control;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Category;
import ch.jtaf.entity.Club;
import ch.jtaf.entity.Competition;
import ch.jtaf.entity.Event;
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
        List<Series> series = new ArrayList<Series>();
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
        Series series = em.find(Series.class, id);
        TypedQuery<Athlete> q = em.createNamedQuery("Athlete.findBySeries", Athlete.class);
        q.setParameter("series", series);
        return q.getResultList();
    }

    public List<Club> getClubs() {
        TypedQuery<Club> q = em.createNamedQuery("Club.findAll", Club.class);
        return q.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Athlete saveAthlete(Athlete a) {
        Category c = this.getCategoryWithGenderAndAge(a.getGender(), a.getYear());
        if (c == null) {
            throw new IllegalArgumentException();
        }
        a.setCategory(c);
        return this.save(a);
    }

    private Category getCategoryWithGenderAndAge(String gender, int year) {
        TypedQuery<Category> q = em.createNamedQuery("Category.findByYearAndGender", Category.class);
        q.setParameter("gender", gender);
        q.setParameter("year", year);
        return q.getSingleResult();
    }

    public Athlete searchAthlete(String args) {
        if (args == null || args.equals("")) {
            return null;
        } else {
            String queryString = "select a from Athlete a where ";
            String[] parts = args.split(" ");
            boolean first = true;
            for (String part : parts) {
                String[] condition = part.split("=");
                if (!first) {
                    queryString += " and ";
                }
                queryString += " lower(a." + condition[0] + ") = '" + condition[1].toLowerCase() + "'";
                first = false;
            }
            TypedQuery query = em.createQuery(queryString, Athlete.class);
            List<Athlete> list = query.getResultList();
            if (list != null && list.size() > 0) {
                return list.get(0);
            } else {
                return null;
            }
        }
    }
}
