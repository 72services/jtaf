package ch.jtaf.control;

import ch.jtaf.entity.*;
import ch.jtaf.i18n.I18n;
import ch.jtaf.to.AthleteTO;
import org.jboss.crypto.CryptoUtil;
import org.qlrm.mapper.JpaResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Component
public class DataService extends AbstractService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public List<Series> getSeriesList(Long spaceId) {
        TypedQuery<Series> q = em.createNamedQuery("Series.findAll", Series.class);
        q.setParameter("space_id", spaceId);
        return q.getResultList();
    }

    public List<Series> getSeriesWithCompetitions(Long spaceId) {
        List<Series> list = getSeriesList(spaceId);
        List<Series> series = new ArrayList<>();
        for (Series s : list) {
            series.add(getSeries(s.getId()));
        }
        return series;
    }

    public List<Competition> getCompetititions(Long seriesId) {
        TypedQuery<Competition> q = em.createNamedQuery("Competition.findAll", Competition.class);
        q.setParameter("series_id", seriesId);
        return q.getResultList();
    }

    public List<Event> getEvents(Long seriesId) {
        TypedQuery<Event> q = em.createNamedQuery("Event.findAll", Event.class);
        q.setParameter("series_id", seriesId);
        return q.getResultList();
    }

    public List<Category> getCategories(Long seriesId) {
        TypedQuery<Category> q = em.createNamedQuery("Category.findAll", Category.class);
        q.setParameter("series_id", seriesId);
        return q.getResultList();
    }

    public Series getSeries(Long seriesId) {
        Series s = em.find(Series.class, seriesId);
        if (s == null) {
            return null;
        }
        TypedQuery<Competition> q = em.createNamedQuery("Competition.findAll", Competition.class);
        q.setParameter("series_id", seriesId);
        List<Competition> cs = q.getResultList();
        for (Competition c : cs) {
            c.setNumberOfAthletes(getNumberOfAthletes(s.getId()).intValue());
            c.setNumberOfAthletesWithResults(getNumberOfAthletesWithResults(c).intValue());
        }
        s.setCompetitions(cs);
        return s;
    }

    public List<Club> getClubs(Long spaceId) {
        TypedQuery<Club> q = em.createNamedQuery("Club.findAll", Club.class);
        q.setParameter("space_id", spaceId);
        return q.getResultList();
    }

    @Transactional
    public Athlete saveAthlete(Athlete a) {
        Category c = this.getCategoryWithGenderAndAge(a.getSeries_id(), a.getGender(), a.getYear());
        if (c == null) {
            throw new IllegalArgumentException();
        }
        if (!c.equals(a.getCategory())) {
            // Category has changed. All Results must be deleted
            for (Result r : a.getResults()) {
                delete(r);
            }
            a.setResults(new ArrayList<>());
            a.setCategory(c);
        }
        return this.save(a);
    }

    @Transactional
    public void recalculateCategories(Long seriesId) {
        TypedQuery<Athlete> q = em.createNamedQuery("Athlete.findBySeries", Athlete.class);
        q.setParameter("series_id", seriesId);
        List<Athlete> athletes = q.getResultList();
        for (Athlete athlete : athletes) {
            athlete.setCategory(getCategoryWithGenderAndAge(seriesId, athlete.getGender(), athlete.getYear()));
        }
    }

    private Category getCategoryWithGenderAndAge(Long seriesId, String gender, int year) {
        TypedQuery<Category> q = em.createNamedQuery("Category.findBySeriesAndYearAndGender", Category.class);
        q.setParameter("series_id", seriesId);
        q.setParameter("gender", gender);
        q.setParameter("year", year);
        return q.getSingleResult();
    }

    public List<Athlete> searchAthletes(Long seriesId, Long competitionId, String searchterm) {
        String queryString = "select a from Athlete a "
                + "where a.series_id = :series_id and "
                + "(lower(a.lastName) like :searchterm "
                + "or lower(a.firstName) like :searchterm)";
        TypedQuery<Athlete> query = em.createQuery(queryString, Athlete.class);
        query.setParameter("series_id", seriesId);
        String searchString = searchterm;
        if (searchString != null) {
            searchString = searchString.toLowerCase();
        }
        searchString += "%";
        query.setParameter("searchterm", searchString);
        List<Athlete> list = query.getResultList();
        for (Athlete a : list) {
            TypedQuery<Result> tq = em.createNamedQuery("Result.findByAthleteAndCompetition", Result.class);
            tq.setParameter("athleteId", a.getId());
            tq.setParameter("competitionId", competitionId);
            List<Result> results = tq.getResultList();
            a.setResults(results);
        }
        return list;
    }

    private BigInteger getNumberOfAthletesWithResults(Competition c) {
        Query q = em.createNativeQuery("SELECT count(DISTINCT a.id) FROM athlete a "
                + "JOIN result r ON a.id = r.athlete_id AND r.competition_id = ?");
        q.setParameter(1, c.getId());
        return (BigInteger) q.getSingleResult();
    }

    private Long getNumberOfAthletes(Long seriesId) {
        TypedQuery query = em.createQuery("select count(distinct a) from Athlete a "
                + "where a.series_id = :series_id", Long.class);
        query.setParameter("series_id", seriesId);
        return (Long) query.getSingleResult();
    }

    @Transactional
    public Series copySeries(Long id) {
        Series series = em.find(Series.class, id);
        Series copy = new Series();
        copy.setLogo(series.getLogo());
        copy.setName("Copy of " + series.getName());
        copy.setSpace_id(series.getSpace_id());
        em.persist(copy);
        List<Event> events = copyEvents(series, copy);
        copyCategories(series, copy, events);
        copyAthletes(series, copy);
        return copy;
    }

    private List<Event> copyEvents(Series orig, Series series) {
        TypedQuery<Event> q = em.createNamedQuery("Event.findAll", Event.class);
        q.setParameter("series_id", orig.getId());
        List<Event> events = q.getResultList();
        List<Event> copies = new ArrayList<>();
        for (Event event : events) {
            Event copy = new Event();
            copy.setA(event.getA());
            copy.setB(event.getB());
            copy.setC(event.getC());
            copy.setGender(event.getGender());
            copy.setName(event.getName());
            copy.setLongName(event.getLongName());
            copy.setSeries_id(series.getId());
            copy.setType(event.getType());
            em.persist(copy);
            copies.add(copy);
        }
        return copies;
    }

    private void copyCategories(Series orig, Series series, List<Event> events) {
        TypedQuery<Category> q = em.createNamedQuery("Category.findAll", Category.class);
        q.setParameter("series_id", orig.getId());

        for (Category category : q.getResultList()) {
            Category copy = new Category();
            copy.setAbbreviation(category.getAbbreviation());
            copy.setGender(category.getGender());
            copy.setName(category.getName());
            copy.setSeries_id(series.getId());
            copy.setYearFrom(category.getYearFrom());
            copy.setYearTo(category.getYearTo());
            for (Event eventFromCategory : category.getEvents()) {
                for (Event eventCopy : events) {
                    if (eventFromCategory.getName().equals(eventCopy.getName())
                            && eventFromCategory.getGender().equals(eventCopy.getGender())) {
                        copy.getEvents().add(eventCopy);
                    }
                }
            }
            em.persist(copy);
        }
    }

    private void copyAthletes(Series orig, Series series) {
        TypedQuery<Athlete> q = em.createNamedQuery("Athlete.findBySeries", Athlete.class);
        q.setParameter("series_id", orig.getId());
        List<Athlete> athletes = q.getResultList();
        for (Athlete athlete : athletes) {
            Athlete copy = new Athlete();
            copy.setCategory(getCategoryWithGenderAndAge(orig.getId(), athlete.getGender(), athlete.getYear()));
            copy.setClub(athlete.getClub());
            copy.setFirstName(athlete.getFirstName());
            copy.setGender(athlete.getGender());
            copy.setLastName(athlete.getLastName());
            copy.setSeries_id(series.getId());
            copy.setYear(athlete.getYear());
            em.persist(copy);
        }
    }

    public SecurityUser get(Class<SecurityUser> aClass, String name) {
        return em.find(aClass, name);
    }

    public List<Series> getAllSeries() {
        TypedQuery<Space> q = em.createNamedQuery("Space.findAll", Space.class);
        List<Space> spaces = q.getResultList();
        for (Space space : spaces) {
            List<Series> series = getSeriesWithCompetitions(space.getId());
            space.setSeries(series);
            space.setClubs(getClubs(space.getId()));
        }
        return sortByCompetitionDate(spaces);
    }

    public Space getSpace(Long id) {
        Space space = em.find(Space.class, id);
        if (space == null) {
            return null;
        }
        space.setSeries(getSeriesList(space.getId()));
        space.setClubs(getClubs(space.getId()));
        return space;
    }

    public List<Athlete> getAthletes(Long seriesId) {
        TypedQuery<Athlete> q = em.createNamedQuery("Athlete.findBySeries", Athlete.class);
        q.setParameter("series_id", seriesId);
        return q.getResultList();
    }

    public List<AthleteTO> getAthleteTOs(Long seriesId) {
        Query q = em.createNativeQuery("SELECT a.id AS id, a.lastname AS lastname, a.firstname AS firstname, "
                + "a.yearofbirth AS yearofbirth , a.gender AS gender, cat.abbreviation AS catabr, club.abbreviation AS clubabr, "
                + "(SELECT count(*) FROM result r JOIN athlete ath ON r.athlete_id = ath.id "
                + "JOIN competition comp ON r.competition_id = comp.id AND comp.series_id = ath.series_id "
                + "WHERE r.athlete_id = a.id) AS numberofresults "
                + "FROM athlete a JOIN category cat ON a.category_id = cat.id "
                + "LEFT OUTER JOIN club club ON a.club_id = club.id WHERE a.series_id = ? "
                + "ORDER BY a.lastname, a.firstname");
        q.setParameter(1, seriesId);
        JpaResultMapper jrm = new JpaResultMapper();
        return jrm.list(q, AthleteTO.class);
    }

    @Transactional
    public SecurityUser saveUser(SecurityUser user, Locale locale) {
        if (user.getConfirmationId() == null) {
            if (em.find(SecurityUser.class, user.getEmail()) == null) {
                String passwordHash = CryptoUtil.createPasswordHash("MD5", "BASE64", null, null, user.getSecret());
                user.setSecret(passwordHash);
                int hashCode = user.getEmail().hashCode() + user.getLastName().hashCode() + user.getFirstName().hashCode();
                String string = Integer.toString(hashCode * -1);
                user.setConfirmationId(string);
                LOGGER.debug(string);

                SecurityGroup group = new SecurityGroup();
                group.setEmail(user.getEmail());
                group.setName("user");
                em.persist(group);
            } else {
                // user already exisits!
                throw new IllegalStateException();
            }
        }
        user = em.merge(user);
        sendMail(user, locale);
        return user;
    }

    @Transactional
    public SecurityUser changePassword(SecurityUser user) {
        String passwordHash = CryptoUtil.createPasswordHash("MD5", "BASE64", null, null, user.getSecret());
        user.setSecret(passwordHash);
        user = em.merge(user);
        return user;
    }

    private void sendMail(SecurityUser user, Locale locale) {
        try {
            String confirmationUrl = System.getenv("CONFIRMATION_URL");
            if (confirmationUrl == null) {
                throw new ConfigurationException("CONFIRMATION_URL");
            }

            Message msg = javaMailSender.createMimeMessage();
            msg.setFrom(new InternetAddress("noreply@jtaf.ch", "JTAF - Track and Field"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(user.getEmail(), user.getFirstName() + " " + user.getLastName()));
            msg.setSubject("JTAF - Track and Field | Registration");
            msg.setText(createMessageBody(locale, confirmationUrl, user));
            msg.saveChanges();
            Transport.send(msg);
        } catch (UnsupportedEncodingException | MessagingException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private static String createMessageBody(Locale locale, String confirmationUrl, SecurityUser user) {
        StringBuilder sb = new StringBuilder();
        MessageFormat mf = new MessageFormat(I18n.getInstance().getString(locale, "Dear {0},"));
        sb.append(mf.format(new String[]{user.getFirstName() + " " + user.getLastName()}));
        sb.append("\n\n");
        sb.append(I18n.getInstance().getString(locale, "Please confirm your registration."));
        sb.append("\n");
        sb.append(confirmationUrl);
        sb.append("confirm.html?confirmation_id=");
        sb.append(user.getConfirmationId());
        sb.append("\n\n");
        sb.append("JTAF - Track And Field");
        sb.append("\n");
        sb.append("www.jtaf.ch");

        return sb.toString();
    }

    @Transactional
    public void confirmUser(String confirmationId) {
        TypedQuery<SecurityUser> q = em.createNamedQuery("SecurityUser.findByConfirmationId", SecurityUser.class);
        q.setParameter("confirmationId", confirmationId);
        SecurityUser u = q.getSingleResult();
        u.setConfirmed(true);
        em.merge(u);
    }

    public List<UserSpace> getUserSpacesBySpaceId(Long spaceId) {
        TypedQuery<UserSpace> q = em.createNamedQuery("UserSpace.findAll", UserSpace.class);
        q.setParameter("space_id", spaceId);
        return q.getResultList();
    }

    @Transactional
    public void deleteSpace(Space s) {
        List<UserSpace> userSpaces = getUserSpacesBySpaceId(s.getId());
        for (UserSpace userSpace : userSpaces) {
            userSpace = em.merge(userSpace);
            em.remove(userSpace);
        }
        s = em.merge(s);
        for (Series series : getSeriesList(s.getId())) {
            em.remove(series);
        }
        for (Club club : getClubs(s.getId())) {
            em.remove(club);
        }
        em.remove(s);
    }

    public Object getUserSpaceByUserAndSeries(String email, Long seriesId) {
        Query q = em.createNativeQuery("SELECT u.* FROM userspace u JOIN series s "
                + "ON s.space_id = u.space_id WHERE u.user_email = ? AND s.id = ?");
        q.setParameter(1, email);
        q.setParameter(2, seriesId);
        return q.getSingleResult();
    }

    public UserSpace getUserSpaceByUserAndSpace(String email, Long spaceId) {
        TypedQuery<UserSpace> q = em.createNamedQuery("UserSpace.findByUserAndSpace", UserSpace.class);
        q.setParameter("email", email);
        q.setParameter("space_id", spaceId);
        return q.getSingleResult();
    }

    public List<Space> getMySpaces(String name) {
        TypedQuery<Space> q = em.createNamedQuery("Space.findByUser", Space.class);
        q.setParameter("email", name);
        List<Space> list = q.getResultList();
        for (Space space : list) {
            UserSpace userSpace = getUserSpacesOwnerBySpaceId(space.getId());
            space.setOwner(userSpace.getUser().getEmail());
            space.setSeries(getSeriesList(space.getId()));
            space.setClubs(getClubs(space.getId()));
        }
        return list;
    }

    private UserSpace getUserSpacesOwnerBySpaceId(Long spaceId) {
        TypedQuery<UserSpace> q = em.createNamedQuery("UserSpace.findByUserAndSpaceAndRole", UserSpace.class);
        q.setParameter("role", UserSpaceRole.OWNER);
        q.setParameter("space_id", spaceId);
        return q.getSingleResult();
    }

    public List<UserSpace> getUserSpacesOfUser(String email) {
        TypedQuery<UserSpace> q = em.createNamedQuery("UserSpace.findByUser", UserSpace.class);
        q.setParameter("email", email);
        return q.getResultList();
    }

    public Series exportSeries(Series s) {
        s.setAthletes(getAthletes(s.getId()));
        s.setCategories(getCategories(s.getId()));
        s.setCompetitions(getCompetititions(s.getId()));
        s.setEvents(getEvents(s.getId()));
        return s;
    }

    @Transactional
    public void deleteSeries(Series s) {
        for (Athlete a : getAthletes(s.getId())) {
            em.remove(a);
        }
        for (Category c : getCategories(s.getId())) {
            em.remove(c);
        }
        for (Competition comp : getCompetititions(s.getId())) {
            em.remove(comp);
        }
        for (Event e : getEvents(s.getId())) {
            em.remove(e);
        }
        em.remove(em.merge(s));
    }

    @Transactional
    public void saveResults(Long competitionId, Long athleteId, List<Result> results) {
        TypedQuery<Result> q = em.createNamedQuery("Result.findByAthleteAndCompetition", Result.class);
        q.setParameter("competitionId", competitionId);
        q.setParameter("athleteId", athleteId);
        List<Result> list = q.getResultList();
        for (Result r : list) {
            em.remove(r);
        }
        for (Result r : results) {
            em.merge(r);
        }
    }

    public Athlete getAthlete(Long id, Long competitionId) {
        Athlete a = em.find(Athlete.class, id);
        if (a == null) {
            return null;
        }
        TypedQuery<Result> tq = em.createNamedQuery("Result.findByAthleteAndCompetition", Result.class);
        tq.setParameter("athleteId", id);
        tq.setParameter("competitionId", competitionId);
        List<Result> results = tq.getResultList();
        a.setResults(results);
        return a;
    }

    public Long calculatePoints(Long eventId, String result) {
        Event event = em.find(Event.class, eventId);
        return event.calculatePoints(result);
    }

    private List<Series> sortByCompetitionDate(List<Space> spaces) {
        List<Series> allSeries = new ArrayList<>();
        for (Space space : spaces) {
            for (Series series : space.getSeries()) {
                Collections.sort(series.getCompetitions());
            }
            allSeries.addAll(space.getSeries());
        }
        Collections.sort(allSeries);
        return allSeries;
    }

    public SecurityUser getUserByUsernameAndPassword(String name, String password) {
        TypedQuery<SecurityUser> tq = em.createNamedQuery("SecurityUser.findByUsernameAndPassword", SecurityUser.class);
        tq.setParameter("email", name);
        tq.setParameter("secret", password);
        List<SecurityUser> list = tq.getResultList();
        if (list.isEmpty()) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new IllegalStateException("More than one user with the same email found");
        }
    }

    public List<SecurityGroup> getGroupsByUsername(String name) {
        TypedQuery<SecurityGroup> tq = em.createNamedQuery("SecurityGroup.findByUsername", SecurityGroup.class);
        tq.setParameter("email", name);
        return tq.getResultList();
    }
}
