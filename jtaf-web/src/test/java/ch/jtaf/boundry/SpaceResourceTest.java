package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Space;
import static ch.jtaf.test.util.TestData.SPACE_ID;
import ch.jtaf.test.util.TestSessionContext;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SpaceResourceTest {
    
    private static SpaceResource sr;
    private static DataService ds;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        ds = new DataService();
        ds.em = em;
        sr = new SpaceResource();
        sr.dataService = ds;
        sr.sessionContext = new TestSessionContext();
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
        List<Space> list = sr.list(false);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testSave() throws Exception {
        Space s = ds.get(Space.class, SPACE_ID);

        assertNotNull(s);

        Space save = sr.save(s);

        assertNotNull(save);
    }

    @Test
    public void testGet() throws Exception {
        Space s = sr.get(SPACE_ID);

        assertNotNull(s);
    }

    @Test
    public void testDelete() throws Exception {
        sr.delete(SPACE_ID);
    }

}