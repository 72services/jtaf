package ch.jtaf.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "securitygroup")
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.email);
        hash = 59 * hash + Objects.hashCode(this.name);
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
        final SecurityGroup other = (SecurityGroup) obj;
        return Objects.equals(this.email, other.email) && Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "SecurityGroup{" + "email=" + email + ", name=" + name + '}';
    }
}
