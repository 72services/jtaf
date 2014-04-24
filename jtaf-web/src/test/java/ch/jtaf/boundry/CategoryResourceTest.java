package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Category;
import static ch.jtaf.test.util.TestData.CATEGORY_ID;
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

public class CategoryResourceTest {

    private static CategoryResource cr;
    private static DataService ds;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        ds = new DataService();
        ds.em = em;
        cr = new CategoryResource();
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
        List<Category> list = cr.list(SERIES_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testSave() throws Exception {
        Category c = ds.get(Category.class, CATEGORY_ID);

        assertNotNull(c);

        Category save = cr.save(c);

        assertNotNull(save);
    }

    @Test(expected = WebApplicationException.class)
    public void testSaveUnallowed() throws Exception {
        Category c = ds.get(Category.class, CATEGORY_ID);
        assertNotNull(c);

        cr.sessionContext = new UnallowedTestSessionContext();
        cr.save(c);
    }

    @Test
    public void testGet() throws Exception {
        Category c = cr.get(CATEGORY_ID);

        assertNotNull(c);
    }

    @Test(expected = WebApplicationException.class)
    public void testGetNotfound() throws Exception {
        cr.sessionContext = new UnallowedTestSessionContext();
        cr.get(0l);
    }

    @Test
    public void testDelete() throws Exception {
        cr.delete(CATEGORY_ID);
    }

    @Test(expected = WebApplicationException.class)
    public void testDeleteUnallowed() throws Exception {
        cr.sessionContext = new UnallowedTestSessionContext();
        cr.delete(CATEGORY_ID);
    }
}
