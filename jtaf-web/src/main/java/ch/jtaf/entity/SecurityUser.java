package ch.jtaf.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "securityuser")
@NamedQueries({
    @NamedQuery(name = "SecurityUser.findByConfirmationId",
            query = "select u from SecurityUser u where u.confirmationId = :confirmationId")
})
public class SecurityUser {

    @Id
    private String email;
    private String secret;
    private String firstName;
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
    public String toString() {
        return "SecurityUser{" + "email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", confirmed=" + confirmed + '}';
    }
}