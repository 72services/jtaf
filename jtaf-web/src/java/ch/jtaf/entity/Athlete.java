package ch.jtaf.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

@Entity
public class Athlete {

    @Id
    @GeneratedValue
    private Long id;
    private String lastName;
    private String firstName;
    private int yearOfBirth;
    private String gender;
    @ManyToOne
    private Category category;
    @ManyToOne
    private Club club;
    @ManyToOne
    private Serie serie;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "postition")
    @JoinColumn(name = "athlete_id")
    private List<Result> results = new ArrayList<Result>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getYear() {
        return yearOfBirth;
    }

    public void setYear(int year) {
        this.yearOfBirth = year;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public int getTotalPoints() {
        int p = 0;
        for (Result r : results) {
            p += r.getPoints();
        }
        return p;
    }

    public void setTotalPoints(int p) {
        // Ignore. This method is only for JSON serialization
    }
}
