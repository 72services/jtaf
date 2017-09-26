package ch.jtaf.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "securityuser")
@NamedQueries({
        @NamedQuery(name = "SecurityUser.findByConfirmationId",
                query = "select u from SecurityUser u where u.confirmationId = :confirmationId"),
        @NamedQuery(name = "SecurityUser.findByUsernameAndPassword",
                query = "select u from SecurityUser u where u.email = :email and u.secret = :secret")

})
public class SecurityUser {

    @Id
    private String email;
    private String secret;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    private String confirmationId;
    private boolean confirmed;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getConfirmationId() {
        return confirmationId;
    }

    public void setConfirmationId(String confirmationId) {
        this.confirmationId = confirmationId;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.email);
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
        final SecurityUser other = (SecurityUser) obj;
        return Objects.equals(this.email, other.email);
    }

    @Override
    public String toString() {
        return "SecurityUser{" + "email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", confirmed=" + confirmed + '}';
    }
}
