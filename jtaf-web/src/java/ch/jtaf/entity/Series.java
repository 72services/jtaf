package ch.jtaf.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@Entity
@NamedQueries({
    @NamedQuery(name = "Series.findAll", query = "select s from Series s where s.space_id = :space_id order by s.name")
})
public class Series {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Long space_id;
    @Lob
    private byte[] logo;
    @Transient
    private List<Competition> competitions = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Competition> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(List<Competition> competitions) {
        this.competitions = competitions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public Long getSpace_id() {
        return space_id;
    }

    public void setSpace_id(Long space_id) {
        this.space_id = space_id;
    }
}
