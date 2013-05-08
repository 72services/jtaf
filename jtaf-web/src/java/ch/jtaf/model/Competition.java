package ch.jtaf.model;

import java.sql.Date;
import javax.persistence.Column;
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
    @Column(insertable = false, updatable = false)
    private Long serie_id;

    public Long getSerie_id() {
        return serie_id;
    }

    public void setSerie_id(Long serie_id) {
        this.serie_id = serie_id;
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
