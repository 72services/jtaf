package ch.jtaf.control;

import ch.jtaf.model.Athlete;
import ch.jtaf.model.Category;
import ch.jtaf.model.Club;
import ch.jtaf.model.Competition;
import ch.jtaf.model.Event;
import ch.jtaf.model.Serie;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.TypedQuery;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class DataService extends AbstractService {

    public List<Serie> getSeries() {
        TypedQuery<Serie> q = em.createQuery("select s from Serie s order by s.name",
                Serie.class);
        return q.getResultList();
    }

    public List<Competition> getCompetititions() {
        TypedQuery<Competition> q = em.createQuery("select c from Competition c order by c.competitionDate",
                Competition.class);
        return q.getResultList();
    }

    public List<Event> getEvents() {
        TypedQuery<Event> q = em.createQuery("select e from Event e order by e.name",
                Event.class);
        return q.getResultList();
    }

    public List<Category> getCategories() {
        TypedQuery<Category> q = em.createQuery("select c from Category c order by c.abbrevation",
                Category.class);
        return q.getResultList();
    }

    public Serie getSerie(Long id) {
        Serie s = em.find(Serie.class, id);
        TypedQuery<Competition> q = em.createQuery("select c from Competition c where c.serie = :serie order by c.name",
                Competition.class);
        q.setParameter("serie", s);
        List<Competition> cs = q.getResultList();
        for (Competition c : cs) {
            c.setSerie(null);
        }
        s.setCompetitions(cs);
        return s;
    }

    public List<Category> getCategoryFromSerie(Long id) {
        Serie serie = em.find(Serie.class, id);
        TypedQuery<Category> q = em.createQuery("select c from Category c where c.serie = :serie order by c.abbrevation",
                Category.class);
        q.setParameter("serie", serie);
        return q.getResultList();
    }

    public List<Event> getEventFromSerie(Long id) {
        Serie serie = em.find(Serie.class, id);
        TypedQuery<Event> q = em.createQuery("select e from Event e where e.serie = :serie order by e.name",
                Event.class);
        q.setParameter("serie", serie);
        return q.getResultList();
    }

    public List<Athlete> getAthleteFromSerie(Long id) {
        Serie serie = em.find(Serie.class, id);
        TypedQuery<Athlete> q = em.createQuery("select a from Athlete a where a.serie = :serie order by a.id",
                Athlete.class);
        q.setParameter("serie", serie);
        return q.getResultList();
    }

    public List<Club> getClubs() {
        TypedQuery<Club> q = em.createQuery("select c from Club c order by c.name",
                Club.class);
        return q.getResultList();
    }
}
