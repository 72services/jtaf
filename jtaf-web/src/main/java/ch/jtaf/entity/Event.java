package ch.jtaf.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "event")
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "select e from Event e where e.series_id = :series_id order by e.name")
})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "longname")
    private String longName;
    @Enumerated(EnumType.STRING)
    private EventType type;
    private String gender;
    private double a;
    private double b;
    private double c;
    private Long series_id;

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

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public Long getSeries_id() {
        return series_id;
    }

    public void setSeries_id(Long series_id) {
        this.series_id = series_id;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final Event other = (Event) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", name=" + name + ", type=" + type + ", gender=" + gender + ", a=" + a + ", b=" + b + ", c=" + c + ", series_id=" + series_id + '}';
    }

    public Long calculatePoints(String result) {
        Double points = 0.0d;
        if (result != null && Double.parseDouble(result) > 0) {
            if (type.equals(EventType.RUN)) {
                points = a * Math.pow((b - Double.parseDouble(result) * 100) / 100, c);
            } else if (type.equals(EventType.RUN_LONG)) {
                String[] parts = result.split("\\.");
                Double time;
                if (parts.length == 1) {
                    time = Double.parseDouble(parts[0]) * 60;
                } else {
                    time = Double.parseDouble(parts[0]) * 60 + Double.parseDouble(parts[1]);
                }
                points = a * Math.pow((b - time * 100) / 100, c);
            } else if (type.equals(EventType.JUMP_THROW)) {
                points = a * Math.pow((Double.parseDouble(result) * 100 - b) / 100, c);
            }
        }
        return Math.round(points);
    }
}
