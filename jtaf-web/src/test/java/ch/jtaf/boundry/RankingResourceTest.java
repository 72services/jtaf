package ch.jtaf.boundry;

import ch.jtaf.control.ReportService;
import ch.jtaf.data.CompetitionRankingData;
import static ch.jtaf.test.util.TestData.COMPETITION_ID;
import static ch.jtaf.test.util.TestData.SERIES_ID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.WebApplicationException;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

public class RankingResourceTest {

    private static RankingResource rr;
    private static ReportService rs;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        rs = new ReportService();
        rs.em = em;
        rr = new RankingResource();
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
    public void testGetCompetitionRanking() throws Exception {
        CompetitionRankingData competitionRanking = rr.getCompetitionRanking(COMPETITION_ID);

        assertNotNull(competitionRanking);
    }

    @Test(expected = WebApplicationException.class)
    public void testGetCompetitionRankingNotFound() throws Exception {
        rr.getCompetitionRanking(0l);
    }

    @Test
    public void testGetCompetitionRankingAsPdf() throws Exception {
        byte[] report = rr.getCompetitionRankingAsPdf(COMPETITION_ID);

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
    public void testGetSeriesRankingAsPdf() throws Exception {
        byte[] report = rr.getSeriesRankingAsPdf(SERIES_ID);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSeriesRankingNull() throws Exception {
        rr.getSeriesRanking(null);
    }
}
