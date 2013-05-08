package ch.jtaf.control;

import ch.jtaf.model.Competition;
import ch.jtaf.model.Serie;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

@Stateless
public class CompetitionService extends AbstractService {

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
}
