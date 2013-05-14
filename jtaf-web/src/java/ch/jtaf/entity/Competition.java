package ch.jtaf.entity;

import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "Competition.findAll", query = "select c from Competition c order by c.competitionDate"),
    @NamedQuery(name = "Competition.findBySeries", query = "select c from Competition c where c.series = :series order by c.name")
})
public class Competition {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Date competitionDate;
    @ManyToOne
    private Series series;

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

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
}
