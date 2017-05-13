package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.SecurityUser;
import ch.jtaf.test.util.TestSessionContext;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertNotNull;

public class UserResourceTest {

    private static UserResource ur;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        DataService ds = new DataService();
        ds.em = em;
        ur = new UserResource();
        ur.dataService = ds;
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
        ur.sessionContext = new TestSessionContext();
    }

    @Test
    public void testGetCurrentUser() throws Exception {
        SecurityUser currentUser = ur.getCurrentUser();

        assertNotNull(currentUser);
    }

}
