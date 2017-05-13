package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Club;
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

import static ch.jtaf.test.util.TestData.CLUB_ID;
import static ch.jtaf.test.util.TestData.SPACE_ID;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClubResourceTest {

    private static ClubResource cr;
    private static DataService ds;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        ds = new DataService();
        ds.em = em;
        cr = new ClubResource();
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
        List<Club> list = cr.list(SPACE_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testSave() throws Exception {
        Club c = ds.get(Club.class, CLUB_ID);
        assertNotNull(c);

        Club save = cr.save(c);
        assertNotNull(save);
    }

    @Test(expected = WebApplicationException.class)
    public void testSaveUnallowed() throws Exception {
        Club c = ds.get(Club.class, CLUB_ID);
        assertNotNull(c);

        cr.sessionContext = new UnallowedTestSessionContext();
        cr.save(c);
    }

    @Test
    public void testGet() throws Exception {
        Club c = cr.get(CLUB_ID);

        assertNotNull(c);
    }

    @Test(expected = WebApplicationException.class)
    public void testGetNotFound() throws Exception {
        cr.get(0L);
    }

    @Test(expected = WebApplicationException.class)
    public void testDelete() throws Exception {
        cr.sessionContext = new UnallowedTestSessionContext();
        cr.delete(CLUB_ID);
    }

}
