package ch.jtaf.boundry;

import ch.jtaf.control.ReportService;
import static ch.jtaf.test.util.TestData.CATEGORY_ID;
import static ch.jtaf.test.util.TestData.COMPETITION_ID;
import static ch.jtaf.test.util.TestData.SERIES_ID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.WebApplicationException;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

public class ReportResourceTest {

    private static ReportResource rr;
    private static ReportService rs;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        rs = new ReportService();
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
        byte[] report = rr.getSheets(COMPETITION_ID, CATEGORY_ID, null);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void testGetNumbers() throws Exception {
        byte[] report = rr.getNumbers(COMPETITION_ID, null);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test(expected = WebApplicationException.class)
    public void testGetSheetsNotFound() throws Exception {
        rr.getSheets(0l, 0l, null);
    }

    @Test
    public void testGetCompetitionRanking() throws Exception {
        byte[] report = rr.getCompetitionRanking(COMPETITION_ID);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void testGetEventsRanking() throws Exception {
        byte[] report = rr.getEventsRanking(COMPETITION_ID);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test(expected = WebApplicationException.class)
    public void testGetEventsRankingNull() throws Exception {
        rr.getEventsRanking(null);
    }

    @Test
    public void testExportAsCsv() throws Exception {
        String report = rr.exportAsCsv(COMPETITION_ID);

        assertNotNull(report);
    }

    @Test
    public void testGetSeriesRanking() throws Exception {
        byte[] report = rr.getSeriesRanking(SERIES_ID);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test(expected = WebApplicationException.class)
    public void testGetSeriesRankingNull() throws Exception {
        rr.getSeriesRanking(null);
    }
}
