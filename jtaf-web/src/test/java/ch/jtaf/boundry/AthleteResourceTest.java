package ch.jtaf.boundry;

import ch.jtaf.test.util.TestSessionContext;
import ch.jtaf.control.DataService;
import ch.jtaf.entity.Athlete;
import static ch.jtaf.test.util.TestData.ATHLETE_ID;
import static ch.jtaf.test.util.TestData.COMPETITION_ID;
import static ch.jtaf.test.util.TestData.SERIES_ID;
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

public class AthleteResourceTest {

    private static AthleteResource ar;
    private static DataService ds;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        ds = new DataService();
        ds.em = em;
        ar = new AthleteResource();
        ar.dataService = ds;
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
        ar.sessionContext = new TestSessionContext();
    }

    @Test
    public void testList() throws Exception {
        List<AthleteTO> list = ar.list(SERIES_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testSave() throws Exception {
        Athlete athlete = ds.get(Athlete.class, ATHLETE_ID);

        assertNotNull(athlete);

        Athlete save = ar.save(null, athlete);

        assertNotNull(save);
    }

    @Test(expected = WebApplicationException.class)
    public void testSaveUnallowed() throws Exception {
        Athlete athlete = ds.get(Athlete.class, ATHLETE_ID);
        assertNotNull(athlete);

        ar.sessionContext = new UnallowedTestSessionContext();
        ar.save(null, athlete);
    }

    @Test
    public void testGet() throws Exception {
        Athlete athlete = ar.get(ATHLETE_ID, COMPETITION_ID);

        assertNotNull(athlete);
    }

    @Test(expected = WebApplicationException.class)
    public void testGetNotFound() throws Exception {
        ar.get(0l, COMPETITION_ID);
    }

    @Test
    public void testSearch() throws Exception {
        List<Athlete> list = ar.search(SERIES_ID, COMPETITION_ID, "Grimm");

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testDelete() throws Exception {
        ar.delete(ATHLETE_ID);
    }

    @Test(expected = WebApplicationException.class)
    public void testDeleteUnallowd() throws Exception {
        ar.sessionContext = new UnallowedTestSessionContext();
        ar.delete(ATHLETE_ID);
    }
}
