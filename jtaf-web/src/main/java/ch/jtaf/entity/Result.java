package ch.jtaf.entity;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "result")
@NamedQueries({
    @NamedQuery(name = "Result.findByCompetition", query = "select r from Result r where r.competition.id = :competitionId order by r.athlete_id, r.position"),
    @NamedQuery(name = "Result.findByAthleteAndCompetition", query = "select r from Result r where r.athlete_id = :athleteId and r.competition.id = :competitionId order by r.position"),
    @NamedQuery(name = "Result.findByAthleteAndSeries", query = "select r from Result r where r.athlete_id = :athleteId and r.competition.series_id = :seriesId order by r.competition.id, r.position"),})
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String result;
    private int points;
    private Long athlete_id;
    @ManyToOne
    private Event event;
    @ManyToOne
    private Competition competition;
    private int position;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Long getAthlete_id() {
        return athlete_id;
    }

    public void setAthlete_id(Long athlete_id) {
        this.athlete_id = athlete_id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Double getResultAsDouble() {
        if (result != null && !result.equals("")) {
            return Double.parseDouble(result);
        } else {
            return 0.0d;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final Result other = (Result) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Result{" + "id=" + id + ", result=" + result + ", points=" + points + ", event=" + event + '}';
    }
}
