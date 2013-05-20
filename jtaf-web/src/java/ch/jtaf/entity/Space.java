package ch.jtaf.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Space {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany
    private List<Series> series = new ArrayList<>();
    @OneToMany
    private List<SecurityUser> users = new ArrayList<>();

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

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    public List<SecurityUser> getUsers() {
        return users;
    }

    public void setUsers(List<SecurityUser> users) {
        this.users = users;
    }
}
