package ch.jtaf.entity;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "series")
@NamedQueries({
        @NamedQuery(name = "Series.findAll", query = "select s from Series s where s.space_id = :space_id order by s.name")
})
public class Series implements Comparable<Series> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean locked;
    private boolean hide;
    private Long space_id;
    @Lob
    private byte[] logo;
    @Transient
    private List<Competition> competitions = new ArrayList<>();
    @Transient
    private List<Event> events = new ArrayList<>();
    @Transient
    private List<Category> categories = new ArrayList<>();
    @Transient
    private List<Athlete> athletes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Competition> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(List<Competition> competitions) {
        this.competitions = competitions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public Long getSpace_id() {
        return space_id;
    }

    public void setSpace_id(Long space_id) {
        this.space_id = space_id;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Athlete> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<Athlete> athletes) {
        this.athletes = athletes;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    @JsonIgnore
    public Date getMostRecentDate() {
        Date date = new Date();
        for (Competition c : competitions) {
            if (c.getCompetitionDate().compareTo(date) == -1) {
                date = c.getCompetitionDate();
            }
        }
        return date;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Series other = (Series) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Series{" + "id=" + id + ", name=" + name + ", space_id=" + space_id + '}';
    }

    @Override
    public int compareTo(Series o) {
        return o.getMostRecentDate().compareTo(getMostRecentDate());
    }
}
