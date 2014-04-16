package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Event;
import static ch.jtaf.test.util.TestData.EVENT_ID;
import static ch.jtaf.test.util.TestData.SERIES_ID;
import ch.jtaf.test.util.TestSessionContext;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

public class EventResourceTest {
    
    private static EventResource er;
    private static DataService ds;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        ds = new DataService();
        ds.em = em;
        er = new EventResource();
        er.dataService = ds;
        er.sessionContext = new TestSessionContext();
    }

    @AfterClass
    public static void afterClass() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Before
    public void before() {
        em.clear();
    }

    @Test
    public void testList() throws Exception {
        List<Event> list = er.list(SERIES_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testSave() throws Exception {
        Event c = ds.get(Event.class, EVENT_ID);

        assertNotNull(c);

        Event save = er.save(c);

        assertNotNull(save);
    }

    @Test
    public void testGet() throws Exception {
        Event c = er.get(EVENT_ID);

        assertNotNull(c);
    }

    @Test
    public void testDelete() throws Exception {
        er.delete(EVENT_ID);
    }

}