package ch.jtaf.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tspace")
@NamedQueries({
    @NamedQuery(name = "Space.findAll", query = "select s from Space s order by s.name"),
    @NamedQuery(name = "Space.findByUser",
            query = "select u.space from UserSpace u where u.user.email = :email")
})
public class Space {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "space_id", insertable = false, updatable = false)
    private Set<Series> series = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "space_id", insertable = false, updatable = false)
    private Set<Club> clubs = new HashSet<>();
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

    public Set<Series> getSeries() {
        return series;
    }

    public void setSeries(Set<Series> series) {
        this.series = series;
    }

    public Set<Club> getClubs() {
        return clubs;
    }

    public void setClubs(Set<Club> clubs) {
        this.clubs = clubs;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Space{" + "id=" + id + ", name=" + name + '}';
    }
}
