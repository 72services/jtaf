package ch.jtaf.control;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Category;
import ch.jtaf.entity.Club;
import ch.jtaf.entity.Competition;
import ch.jtaf.entity.Event;
import ch.jtaf.entity.SecurityUser;
import ch.jtaf.entity.Series;
import ch.jtaf.entity.Space;
import ch.jtaf.entity.UserSpace;
import ch.jtaf.exception.ConfigurationException;
import static ch.jtaf.test.util.TestData.COMPETITION_ID;
import static ch.jtaf.test.util.TestData.SERIES_ID;
import static ch.jtaf.test.util.TestData.SPACE_ID;
import ch.jtaf.to.AthleteTO;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

public class DataServiceTest {

    private static final String EMAIL = "simon@martinelli.ch";
    private static Locale locale = new Locale("DE", "ch");

    private static DataService ds;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        ds = new DataService();
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        ds.em = em;
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
    public void testGetSeriesList() throws Exception {
        List<Series> list = ds.getSeriesList(SPACE_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testGetSeriesWithCompetitions() throws Exception {
        List<Series> list = ds.getSeriesWithCompetitions(SPACE_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testGetCompetititions() throws Exception {
        List<Competition> list = ds.getCompetititions(SERIES_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testGetEvents() throws Exception {
        List<Event> list = ds.getEvents(SERIES_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testGetCategories() throws Exception {
        List<Category> list = ds.getCategories(SERIES_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testGetSeries() throws Exception {
        Series series = ds.getSeries(SERIES_ID);

        assertNotNull(series);
    }

    @Test
    public void testGetClubs() throws Exception {
        List<Club> list = ds.getClubs(SPACE_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testSaveAthlete() throws Exception {
        List<Athlete> list = ds.getAthletes(SERIES_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);

        ds.saveAthlete(list.get(0));
    }

    @Test
    public void testSearchAthletes() throws Exception {
        List<Athlete> list = ds.searchAthletes(SERIES_ID, COMPETITION_ID, "Grimm");

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testGetSpaces() throws Exception {
        List<Space> list = ds.getSpaces();

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testGetSpace() throws Exception {
        Space space = ds.getSpace(SPACE_ID);

        assertNotNull(space);
    }

    @Test
    public void testGetAthletes() throws Exception {
        List<Athlete> list = ds.getAthletes(SERIES_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testGetAthleteTOs() throws Exception {
        List<AthleteTO> list = ds.getAthleteTOs(SERIES_ID);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testGetUserSpacesBySpaceId() throws Exception {
        List<UserSpace> userSpace = ds.getUserSpacesBySpaceId(SPACE_ID);

        assertNotNull(userSpace);
    }

    @Test
    public void testGetUserSpaceByUserAndSeries() throws Exception {
        Object userSpace = ds.getUserSpaceByUserAndSeries(EMAIL, SERIES_ID);

        assertNotNull(userSpace);
    }

    @Test
    public void testGetUserSpaceByUserAndSpace() throws Exception {
        Object userSpace = ds.getUserSpaceByUserAndSpace(EMAIL, SPACE_ID);

        assertNotNull(userSpace);

    }

    @Test
    public void testGetMySpaces() throws Exception {
        List<Space> list = ds.getMySpaces(EMAIL);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testGetUserSpacesOfUser() throws Exception {
        List<UserSpace> list = ds.getUserSpacesOfUser(EMAIL);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void copySeries() throws Exception {
        Series copy = ds.copySeries(SERIES_ID);

        assertNotNull(copy);
    }

    @Test
    public void exportSeries() throws Exception {
        Series series = ds.getSeries(SERIES_ID);

        assertNotNull(series);

        Series export = ds.exportSeries(series);

        assertNotNull(export);
    }

    @Test(expected = ConfigurationException.class)
    public void saveUser() throws Exception {
        SecurityUser user = ds.get(SecurityUser.class, EMAIL);

        assertNotNull(user);

        SecurityUser savedUser = ds.saveUser(user, locale);

        assertNotNull(savedUser);
    }

}
