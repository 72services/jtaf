package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.UserSpace;
import static ch.jtaf.test.util.TestData.SPACE_ID;
import static ch.jtaf.test.util.TestData.USERSPACE_ID;
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

public class UserSpaceResourceTest {

    private static UserSpaceResource ur;
    private static DataService ds;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        ds = new DataService();
        ds.em = em;
        ur = new UserSpaceResource();
        ur.dataService = ds;
        ur.sessionContext = new TestSessionContext();
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
        List<UserSpace> list = ur.list(SPACE_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testGetUsersUserspaces() throws Exception {
        List<UserSpace> list = ur.getUsersUserspaces();

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

     @Test
    public void testSave() throws Exception {
        UserSpace u = ds.get(UserSpace.class, USERSPACE_ID);

        assertNotNull(u);

        UserSpace save = ur.save(u);

        assertNotNull(save);
    }


    @Test
    public void testDelete() throws Exception {
        ur.delete(USERSPACE_ID);
    }

}
