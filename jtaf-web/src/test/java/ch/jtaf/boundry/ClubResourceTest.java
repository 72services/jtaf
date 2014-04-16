package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Club;
import static ch.jtaf.test.util.TestData.CLUB_ID;
import static ch.jtaf.test.util.TestData.SPACE_ID;
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
        cr.sessionContext = new TestSessionContext();
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

    @Test
    public void testGet() throws Exception {
        Club c = cr.get(CLUB_ID);

        assertNotNull(c);
    }

    @Test
    public void testDelete() throws Exception {
        cr.delete(CLUB_ID);
    }

}
