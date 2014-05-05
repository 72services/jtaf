package ch.jtaf.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tspace")
@NamedQueries({
    @NamedQuery(name = "Space.findAll", query = "select s from Space s order by s.name desc"),
    @NamedQuery(name = "Space.findByUser",
            query = "select u.space from UserSpace u where u.user.email = :email order by u.space.name desc")
})
public class Space implements Comparable<Space> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Transient
    private List<Series> series = new ArrayList<>();
    @Transient
    private List<Club> clubs = new ArrayList<>();
    @Transient
    private String owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    public List<Club> getClubs() {
        return clubs;
    }

    public void setClubs(List<Club> clubs) {
        this.clubs = clubs;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getMostRecentDate() {
        Date date = new Date();
        for (Series s : series) {
            if (s.getMostRecentDate().compareTo(date) == -1) {
                date = s.getMostRecentDate();
            }
        }
        return date;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final Space other = (Space) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Space{" + "id=" + id + ", name=" + name + '}';
    }

    @Override
    public int compareTo(Space o) {
        return o.getMostRecentDate().compareTo(getMostRecentDate());
    }
}
