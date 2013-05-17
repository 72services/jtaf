package ch.jtaf.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SecurityGroup {

    @Id
    private String email;
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
