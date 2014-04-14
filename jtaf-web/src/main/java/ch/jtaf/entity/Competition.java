package ch.jtaf.entity;

import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "competition")
@NamedQueries({
    @NamedQuery(name = "Competition.findAll", query = "select c from Competition c where c.series_id = :series_id order by c.competitionDate")
})
public class Competition {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Date competitionDate;
    private Long series_id;
    @Transient
    private int numberOfAthletesWithResults;
    @Transient
    private int numberOfAthletes;

    public Date getCompetitionDate() {
        return competitionDate;
    }

    public void setCompetitionDate(Date competitionDate) {
        this.competitionDate = competitionDate;
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

    public int getNumberOfAthletes() {
        return numberOfAthletes;
    }

    public void setNumberOfAthletes(int numberOfAthletes) {
        this.numberOfAthletes = numberOfAthletes;
    }

    public int getNumberOfAthletesWithResults() {
        return numberOfAthletesWithResults;
    }

    public void setNumberOfAthletesWithResults(int numberOfAthletesWithResults) {
        this.numberOfAthletesWithResults = numberOfAthletesWithResults;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final Competition other = (Competition) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Competition{" + "id=" + id + ", name=" + name + ", competitionDate=" + competitionDate + ", series_id=" + series_id + ", numberOfAthletes=" + numberOfAthletes + '}';
    }
}
