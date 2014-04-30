package ch.jtaf.control;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AbstractService {

    @PersistenceContext
    public EntityManager em;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public <T> T get(Class<T> clazz, Long id) {
        return em.find(clazz, id);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T> T save(T t) {
        return em.merge(t);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T> void delete(T t) {
        em.remove(em.merge(t));
    }
}
