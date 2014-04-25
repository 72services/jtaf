package ch.jtaf.control;

import ch.jtaf.exception.ConfigurationException;
import ch.jtaf.entity.Athlete;
import ch.jtaf.to.AthleteTO;
import ch.jtaf.entity.Category;
import ch.jtaf.entity.Club;
import ch.jtaf.entity.Competition;
import ch.jtaf.entity.Event;
import ch.jtaf.entity.Result;
import ch.jtaf.entity.SecurityGroup;
import ch.jtaf.entity.SecurityUser;
import ch.jtaf.entity.Series;
import ch.jtaf.entity.Space;
import ch.jtaf.entity.UserSpace;
import ch.jtaf.entity.UserSpaceRole;
import ch.jtaf.interceptor.TraceInterceptor;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.jboss.crypto.CryptoUtil;
import org.jboss.logging.Logger;

@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class DataService extends AbstractService {
    
    @Resource(mappedName = "java:jboss/mail/Default")
    private Session mailSession;
    
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
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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
            a.setResults(new ArrayList<Result>());
            a.setCategory(c);
        }
        return this.save(a);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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
    
    public List<Athlete> searchAthletes(Long seriesId, String searchterm) {
        String queryString = "select a from Athlete a "
                + "where a.series_id = :series_id and "
                + "(lower(a.lastName) like :searchterm "
                + "or lower(a.firstName) like :searchterm)";
        TypedQuery<Athlete> query = em.createQuery(queryString, Athlete.class);
        query.setParameter("series_id", seriesId);
        if (searchterm != null) {
            searchterm = searchterm.toLowerCase();
        }
        searchterm += "%";
        query.setParameter("searchterm", searchterm);
        return query.getResultList();
    }
    
    private Long getNumberOfAthletesWithResults(Competition c) {
        String queryString = "select count(distinct a) from Athlete a join a.results r "
                + "where r.competition = :competition";
        TypedQuery query = em.createQuery(queryString, Long.class);
        query.setParameter("competition", c);
        return (Long) query.getSingleResult();
    }
    
    private Long getNumberOfAthletes(Long seriesId) {
        String queryString = "select count(distinct a) from Athlete a where a.series_id = :series_id";
        TypedQuery query = em.createQuery(queryString, Long.class);
        query.setParameter("series_id", seriesId);
        return (Long) query.getSingleResult();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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
            copy.setSeries_id(series.getId());
            copy.setType(event.getType());
            em.persist(copy);
            copies.add(copy);
        }
        return copies;
    }
    
    private List<Category> copyCategories(Series orig, Series series, List<Event> events) {
        TypedQuery<Category> q = em.createNamedQuery("Category.findAll", Category.class);
        q.setParameter("series_id", orig.getId());
        List<Category> categories = q.getResultList();
        List<Category> copies = new ArrayList<>();
        for (Category category : categories) {
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
            copies.add(copy);
        }
        return copies;
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
    
    public List<Space> getSpaces() {
        TypedQuery<Space> q = em.createNamedQuery("Space.findAll", Space.class);
        List<Space> spaces = q.getResultList();
        for (Space space : spaces) {
            space.setSeries(getSeriesWithCompetitions(space.getId()));
            space.setClubs(getClubs(space.getId()));
        }
        return spaces;
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
        TypedQuery<AthleteTO> q = em.createQuery("SELECT NEW "
                + "ch.jtaf.to.AthleteTO(a.id, a.lastName, a.firstName, a.yearOfBirth, a.gender, a.category.abbreviation, a.club.abbreviation) "
                + "FROM Athlete a WHERE a.series_id = :series_id", AthleteTO.class);
        q.setParameter("series_id", seriesId);
        return q.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public SecurityUser saveUser(SecurityUser user) throws NoSuchAlgorithmException {
        if (user.getConfirmationId() == null) {
            if (em.find(SecurityUser.class, user.getEmail()) == null) {
                String passwordHash = CryptoUtil.createPasswordHash("MD5", "BASE64", null, null, user.getSecret());
                user.setSecret(passwordHash);
                int hashCode = user.getEmail().hashCode() + user.getLastName().hashCode() + user.getFirstName().hashCode();
                String string = Integer.toString(hashCode * -1);
                Logger.getLogger(DataService.class).debug(string);
                user.setConfirmationId(string);
                
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
        sendMail(user);
        return user;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public SecurityUser changePassword(SecurityUser user) throws NoSuchAlgorithmException {
        String passwordHash = CryptoUtil.createPasswordHash("MD5", "BASE64", null, null, user.getSecret());
        user.setSecret(passwordHash);
        user = em.merge(user);
        return user;
    }
    
    private void sendMail(SecurityUser user) {
        try {
            String confirmationUrl = System.getProperty("jtaf.confirmation.url");
            if (confirmationUrl == null) {
                throw new ConfigurationException("jtaf.confirmation.url");
            }
            
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress("noreply@jtaf.ch", "JTAF - Track and Field"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(user.getEmail(), user.getFirstName() + " " + user.getLastName()));
            msg.setSubject("JTAF Registration");
            msg.setText("Please confirm your registration: "
                    + confirmationUrl
                    + "/confirm.html?confirmation_id="
                    + user.getConfirmationId());
            msg.saveChanges();
            Transport.send(msg);
        } catch (UnsupportedEncodingException | MessagingException ex) {
            Logger.getLogger(DataService.class).error(ex.getMessage(), ex);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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
    
    public Object getUserSpaceByUserAndSeries(String email, Long series_id) {
        Query q = em.createNativeQuery("SELECT u.* FROM userspace u JOIN series s ON s.space_id = u.space_id WHERE u.user_email = ? AND s.id = ?");
        q.setParameter(1, email);
        q.setParameter(2, series_id);
        return q.getSingleResult();
    }
    
    public UserSpace getUserSpaceByUserAndSpace(String email, Long space_id) {
        TypedQuery<UserSpace> q = em.createNamedQuery("UserSpace.findByUserAndSpace", UserSpace.class);
        q.setParameter("email", email);
        q.setParameter("space_id", space_id);
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
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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
}
