package ch.jtaf.boundry;

import ch.jtaf.control.ReportService;
import ch.jtaf.test.util.TestHttpServletRequest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

import static ch.jtaf.test.util.TestData.CATEGORY_ID;
import static ch.jtaf.test.util.TestData.COMPETITION_ID;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReportResourceTest {

    private static ReportResource rr;
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static final HttpServletRequest hsr = new TestHttpServletRequest();

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        ReportService rs = new ReportService();
        rs.em = em;
        rr = new ReportResource();
        rr.service = rs;
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
    public void testGetSheets() throws Exception {
        byte[] report = rr.getSheets(hsr, COMPETITION_ID, CATEGORY_ID, null);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void testGetNumbers() throws Exception {
        byte[] report = rr.getNumbers(hsr, COMPETITION_ID, null);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test(expected = WebApplicationException.class)
    public void testGetSheetsNotFound() throws Exception {
        rr.getSheets(hsr, 0l, 0l, null);
    }

    @Test
    public void testExportAsCsv() throws Exception {
        String report = rr.exportAsCsv(COMPETITION_ID);

        assertNotNull(report);
    }

}
