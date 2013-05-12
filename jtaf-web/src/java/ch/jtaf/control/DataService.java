package ch.jtaf.control;

import ch.jtaf.model.Athlete;
import ch.jtaf.model.Category;
import ch.jtaf.model.Club;
import ch.jtaf.model.Competition;
import ch.jtaf.model.Event;
import ch.jtaf.model.Ranking;
import ch.jtaf.model.RankingCategory;
import ch.jtaf.model.Result;
import ch.jtaf.model.Serie;
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
        TypedQuery<Category> q = em.createQuery("select c from Category c "
                + "where c.gender = :gender and :year between c.yearFrom and c.yearTo",
                Category.class);
        q.setParameter("gender", gender);
        q.setParameter("year", year);
        return q.getSingleResult();
    }
    
    public Ranking getCompetitionRanking(Long competitionid) {
        TypedQuery<Athlete> q = em.createQuery("select distinct a from Athlete a "
                + "join a.results r where r.competition.id = :competitionid",
                Athlete.class);
        q.setParameter("competitionid", competitionid);
        List<Athlete> list = q.getResultList();
        
        Competition competition = em.find(Competition.class, competitionid);
        competition.setSerie(null);
        
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
            c.setSerie(null);
            rc.setCategory(c);
            rc.setAthletes(filterAndSort(competition, entry.getValue()));
            ranking.getCategories().add(rc);
        }
        return ranking;
    }
    
    private List<Athlete> filterAndSort(Competition competition, List<Athlete> list) {
        for (Athlete a : list) {
            a.setCategory(null);
            a.setSerie(null);
            List<Result> rs = new ArrayList<Result>();
            for (Result r : a.getResults()) {
                if (r.getCompetition().getId().compareTo(competition.getId()) == 0) {
                    r.setCompetition(null);
                    r.getEvent().setSerie(null);
                    rs.add(r);
                }
            }
            a.setResults(rs);
        }
        Collections.sort(list, new AthleteResultComparator());
        return list;
    }
}
