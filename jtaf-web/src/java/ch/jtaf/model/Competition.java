package ch.jtaf.model;

import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Competition {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Date competitionDate;

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
