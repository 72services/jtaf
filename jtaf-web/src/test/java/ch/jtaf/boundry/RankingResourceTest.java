package ch.jtaf.boundry;

import ch.jtaf.control.ReportService;
import ch.jtaf.test.util.TestHttpServletRequest;
import ch.jtaf.vo.CompetitionRankingVO;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.WebApplicationException;

import static ch.jtaf.test.util.TestData.COMPETITION_ID;
import static ch.jtaf.test.util.TestData.SERIES_ID;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RankingResourceTest {

    private static RankingResource rr;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        ReportService rs = new ReportService();
        rs.em = em;
        rr = new RankingResource();
        rr.reportService = rs;
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
        CompetitionRankingVO competitionRanking = rr.getCompetitionRanking(COMPETITION_ID);

        assertNotNull(competitionRanking);
    }

    @Test(expected = WebApplicationException.class)
    public void testGetCompetitionRankingNotFound() throws Exception {
        rr.getCompetitionRanking(0L);
    }

    @Test
    public void testGetCompetitionRankingAsPdf() throws Exception {
        byte[] report = rr.getCompetitionRankingAsPdf(new TestHttpServletRequest(), COMPETITION_ID);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void testGetEventsRanking() throws Exception {
        byte[] report = rr.getEventsRanking(new TestHttpServletRequest(), COMPETITION_ID);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test(expected = WebApplicationException.class)
    public void testGetEventsRankingNull() throws Exception {
        rr.getEventsRanking(new TestHttpServletRequest(), null);
    }

    @Test
    public void testGetSeriesRankingAsPdf() throws Exception {
        byte[] report = rr.getSeriesRankingAsPdf(new TestHttpServletRequest(), SERIES_ID);

        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSeriesRankingNull() throws Exception {
        rr.getSeriesRanking(null);
    }
}
