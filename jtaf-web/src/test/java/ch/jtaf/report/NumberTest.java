package ch.jtaf.report;

import ch.jtaf.control.ReportService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NumberTest {

    private static ReportService rs;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        rs = new ReportService();
        rs.em = em;
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
    public void testGetNumbers() throws Exception {
        byte[] report = rs.createNumbers(249l, null, Locale.GERMAN);
        assertNotNull(report);
        assertTrue(report.length > 0);
        
        FileOutputStream fos = new FileOutputStream(new File("c:/Users/SimonMartinelli/Downloads/numbers.pdf"));
        fos.write(report);
        fos.close();
    }

}
