package ch.jtaf.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

@Entity
@NamedQueries({
    @NamedQuery(name = "Category.findAll", query = "select c from Category c order by c.abbrevation"),
    @NamedQuery(name = "Category.findBySerie", query = "select c from Category c where c.serie = :serie order by c.abbrevation"),
    @NamedQuery(name = "Category.findByYearAndGender", query = "select c from Category c where c.gender = :gender and :year between c.yearFrom and c.yearTo")
})
public class Category {

    @Id
    @GeneratedValue
    private Long id;
    private String abbrevation;
    private String name;
    private int yearFrom;
    private int yearTo;
    private String gender;
    @ManyToOne
    private Serie serie;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "position")
    private List<Event> events = new ArrayList<Event>();

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public String getAbbrevation() {
        return abbrevation;
    }

    public void setAbbrevation(String abbrevation) {
        this.abbrevation = abbrevation;
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
}
