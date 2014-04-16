package ch.jtaf.control;

import ch.jtaf.data.CompetitionRankingData;
import static ch.jtaf.test.util.TestData.CATEGORY_ID;
import static ch.jtaf.test.util.TestData.COMPETITION_ID;
import static ch.jtaf.test.util.TestData.SERIES_ID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

public class ReportServiceTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static ReportService rs;

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
        byte[] report = rs.createSheets(COMPETITION_ID, null);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void testCreateCompetitionRanking() throws Exception {
        byte[] report = rs.createCompetitionRanking(COMPETITION_ID);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void testCreateSeriesRanking() throws Exception {
        byte[] report = rs.createSeriesRanking(SERIES_ID);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void testCreateEmptySheets() throws Exception {
        byte[] report = rs.createEmptySheets(CATEGORY_ID);

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
        byte[] report = rs.createEventsRanking(COMPETITION_ID);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void testGetCompetitionRanking() throws Exception {
        CompetitionRankingData competitionRanking = rs.getCompetitionRanking(COMPETITION_ID);

        assertNotNull(competitionRanking);
    }

}
