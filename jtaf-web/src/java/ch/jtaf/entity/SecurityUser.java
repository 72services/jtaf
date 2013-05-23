package ch.jtaf.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
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
    @OneToMany(mappedBy = "user")
    private List<UserSpace> userSpaces = new ArrayList<>();

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

    public List<UserSpace> getUserSpaces() {
        return userSpaces;
    }

    public void setUserSpaces(List<UserSpace> userSpaces) {
        this.userSpaces = userSpaces;
    }
}