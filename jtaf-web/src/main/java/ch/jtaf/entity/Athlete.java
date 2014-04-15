package ch.jtaf.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(name = "athlete")
@NamedQueries({
    @NamedQuery(name = "Athlete.findBySeriesOrderByClub",
            query = "select distinct a from Athlete a where a.series_id = :series_id order by a.club.abbreviation, a.id"),
    @NamedQuery(name = "Athlete.findBySeries",
            query = "select distinct a from Athlete a where a.series_id = :series_id order by a.category.abbreviation, a.lastName, a.firstName"),
    @NamedQuery(name = "Athlete.findByCompetition",
            query = "select distinct a from Athlete a join a.results r where r.competition.id = :competitionid order by a.category.abbreviation, a.lastName, a.firstName")
})
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
    private Long series_id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderColumn(name = "postition")
    @JoinColumn(name = "athlete_id")
    private List<Result> results = new ArrayList<>();

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

    public Long getSeries_id() {
        return series_id;
    }

    public void setSeries_id(Long series_id) {
        this.series_id = series_id;
    }

    public int getTotalPoints(Competition competition) {
        int p = 0;
        for (Result r : results) {
            if (r.getCompetition().equals(competition)) {
                p += r.getPoints();
            }
        }
        return p;
    }

    public void setTotalPoints(int p) {
        // Ignore. This method is only for JSON serialization
    }

    public int getSeriesPoints(Series series) {
        int p = 0;
        for (Result r : results) {
            for (Competition competition : series.getCompetitions()) {
                if (r.getCompetition().equals(competition)) {
                    p += r.getPoints();
                }
            }
        }
        return p;
    }

    public void setSeriesPoints(int p) {
        // Ignore. This method is only for JSON serialization
    }

    @Override
    public String toString() {
        return "Athlete{" + "id=" + id + ", lastName=" + lastName + ", firstName=" + firstName + ", yearOfBirth=" + yearOfBirth + ", gender=" + gender + ", series_id=" + series_id + '}';
    }
}