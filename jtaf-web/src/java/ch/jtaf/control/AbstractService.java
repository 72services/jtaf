package ch.jtaf.control;

import ch.jtaf.interceptor.TraceInterceptor;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AbstractService {

    @PersistenceContext
    protected EntityManager em;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public <T> T get(Class<T> clazz, Long id) {
        return em.find(clazz, id);
    }

    public <T> T save(T t) {
        return em.merge(t);
    }

    public <T> void delete(T t) {
        t = em.merge(t);
        em.remove(t);
    }
}
