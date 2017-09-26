package ch.jtaf.control;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AbstractService {

    @PersistenceContext
    public EntityManager em;

    public <T> T get(Class<T> clazz, Long id) {
        return em.find(clazz, id);
    }

    @Transactional
    public <T> T save(T t) {
        return em.merge(t);
    }

    @Transactional
    public <T> void delete(T t) {
        em.remove(em.merge(t));
    }
}
