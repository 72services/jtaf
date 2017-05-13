package ch.jtaf.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "club")
@NamedQueries({
    @NamedQuery(name = "Club.findAll", query = "select c from Club c where c.space_id = :space_id order by c.name")
})
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String abbreviation;
    private String name;
    private Long space_id;

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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

    public Long getSpace_id() {
        return space_id;
    }

    public void setSpace_id(Long space_id) {
        this.space_id = space_id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final Club other = (Club) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Club{" + "id=" + id + ", abbreviation=" + abbreviation + ", name=" + name + ", space_id=" + space_id + '}';
    }
}
