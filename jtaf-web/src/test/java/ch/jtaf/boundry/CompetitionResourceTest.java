package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Competition;
import ch.jtaf.test.util.TestSessionContext;
import ch.jtaf.test.util.UnallowedTestSessionContext;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.WebApplicationException;
import java.util.List;

import static ch.jtaf.test.util.TestData.COMPETITION_ID;
import static ch.jtaf.test.util.TestData.SERIES_ID;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CompetitionResourceTest {

    private static CompetitionResource cr;
    private static DataService ds;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        ds = new DataService();
        ds.em = em;
        cr = new CompetitionResource();
        cr.dataService = ds;
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
        cr.sessionContext = new TestSessionContext();
    }

    @Test
    public void testList() throws Exception {
        List<Competition> list = cr.list(SERIES_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testSave() throws Exception {
        Competition c = ds.get(Competition.class, COMPETITION_ID);
        assertNotNull(c);

        Competition save = cr.save(c);
        assertNotNull(save);
    }

    @Test(expected = WebApplicationException.class)
    public void testSaveUnallowed() throws Exception {
        Competition c = ds.get(Competition.class, COMPETITION_ID);
        assertNotNull(c);

        cr.sessionContext = new UnallowedTestSessionContext();
        cr.save(c);
    }

    @Test
    public void testGet() throws Exception {
        Competition c = cr.get(COMPETITION_ID);

        assertNotNull(c);
    }

    @Test(expected = WebApplicationException.class)
    public void testGetNotFound() throws Exception {
        cr.get(0L);
    }

    @Test
    public void testDelete() throws Exception {
        cr.delete(COMPETITION_ID);
    }

    @Test(expected = WebApplicationException.class)
    public void testDeleteUnallowed() throws Exception {
        cr.sessionContext = new UnallowedTestSessionContext();
        cr.delete(COMPETITION_ID);
    }
}
