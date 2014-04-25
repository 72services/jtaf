package ch.jtaf.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(name = "category")
@NamedQueries({
    @NamedQuery(name = "Category.findAll", query = "select c from Category c where c.series_id = :series_id order by c.abbreviation"),
    @NamedQuery(name = "Category.findBySeriesAndYearAndGender", query = "select c from Category c where c.series_id = :series_id and c.gender = :gender and :year between c.yearFrom and c.yearTo")
})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String abbreviation;
    private String name;
    @Column(name = "yearfrom")
    private int yearFrom;
    @Column(name = "yearto")
    private int yearTo;
    private String gender;
    private Long series_id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderColumn(name = "position")
    @JoinTable(
            name = "category_event",
            joinColumns = {
                @JoinColumn(name = "category_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "event_id", referencedColumnName = "id", unique = true)}
    )
    private List<Event> events = new ArrayList<>();

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(int yearFrom) {
        this.yearFrom = yearFrom;
    }

    public int getYearTo() {
        return yearTo;
    }

    public void setYearTo(int yearTo) {
        this.yearTo = yearTo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

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

    public Long getSeries_id() {
        return series_id;
    }

    public void setSeries_id(Long series_id) {
        this.series_id = series_id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final Category other = (Category) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", abbreviation=" + abbreviation + ", name=" + name + ", yearFrom=" + yearFrom + ", yearTo=" + yearTo + ", gender=" + gender + ", series_id=" + series_id + '}';
    }
}
