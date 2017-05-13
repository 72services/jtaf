package ch.jtaf.control;

import ch.jtaf.vo.CompetitionRankingVO;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Locale;

import static ch.jtaf.test.util.TestData.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReportServiceTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static ReportService rs;
    private static final Locale LOCALE = new Locale("DE", "ch");

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
    public void testCreateSheets() throws Exception {
        byte[] report = rs.createSheets(COMPETITION_ID, null, new Locale("de", "CH"));

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void testCreateNumbers() throws Exception {
        byte[] report = rs.createNumbers(COMPETITION_ID, null, new Locale("de", "CH"));

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void testCreateCompetitionRanking() throws Exception {
        byte[] report = rs.createCompetitionRanking(COMPETITION_ID, LOCALE);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void testCreateSeriesRanking() throws Exception {
        byte[] report = rs.createSeriesRanking(SERIES_ID, LOCALE);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void testCreateEmptySheets() throws Exception {
        byte[] report = rs.createEmptySheets(CATEGORY_ID, new Locale("de", "CH"));

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void testCreateCompetitionRankingAsCsv() throws Exception {
        String report = rs.createCompetitionRankingAsCsv(COMPETITION_ID);

        assertNotNull(report);
    }

    @Test
    public void testCreateEventsRanking() throws Exception {
        byte[] report = rs.createEventsRanking(COMPETITION_ID, LOCALE);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void testGetCompetitionRanking() throws Exception {
        CompetitionRankingVO competitionRanking = rs.getCompetitionRanking(COMPETITION_ID);

        assertNotNull(competitionRanking);
    }

}
