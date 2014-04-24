package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Event;
import static ch.jtaf.test.util.TestData.EVENT_ID;
import static ch.jtaf.test.util.TestData.SERIES_ID;
import ch.jtaf.test.util.TestSessionContext;
import ch.jtaf.test.util.UnallowedTestSessionContext;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.WebApplicationException;
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
        er.sessionContext = new TestSessionContext();
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

    @Test(expected = WebApplicationException.class)
    public void testSaveUnallowed() throws Exception {
        Event c = ds.get(Event.class, EVENT_ID);
        assertNotNull(c);

        er.sessionContext = new UnallowedTestSessionContext();
        er.save(c);
    }

    @Test
    public void testGet() throws Exception {
        Event c = er.get(EVENT_ID);
        assertNotNull(c);
    }

    @Test(expected = WebApplicationException.class)
    public void testGetNotFound() throws Exception {
        er.get(0l);
    }

    @Test
    public void testDelete() throws Exception {
        er.delete(EVENT_ID);
    }

    @Test(expected = WebApplicationException.class)
    public void testDeleteUnallowed() throws Exception {
        er.sessionContext = new UnallowedTestSessionContext();
        er.delete(EVENT_ID);
    }
}
