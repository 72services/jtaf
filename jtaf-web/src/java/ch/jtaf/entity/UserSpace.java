package ch.jtaf.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "UserSpace.findAll",
            query = "select u from UserSpace u where u.space.id = :space_id"),
    @NamedQuery(name = "UserSpace.findByUserAndSpace",
            query = "select u from UserSpace u where u.user.email = :email and u.space.id = :space_id"),
    @NamedQuery(name = "UserSpace.findByUser",
            query = "select u from UserSpace u where u.user.email = :email"),
    @NamedQuery(name = "UserSpace.findByUserAndSpaceAndRole",
            query = "select u from UserSpace u where u.role = :role and u.space.id = :space_id")
})
@NamedNativeQuery(name = "UserSpace.findByUserAndSeries",
        query = "SELECT U.* FROM USERSPACE U JOIN SERIES S ON S.SPACE_ID = U.SPACE_ID "
        + "WHERE U.USER_EMAIL = ? AND S.ID = ?")
public class UserSpace {

    @Id
    @GeneratedValue
    private Long id;
    private UserSpaceRole role;
    @ManyToOne
    private SecurityUser user;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Space space;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SecurityUser getUser() {
        return user;
    }

    public void setUser(SecurityUser user) {
        this.user = user;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public UserSpaceRole getRole() {
        return role;
    }

    public void setRole(UserSpaceRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserSpace{" + "id=" + id + ", role=" + role + ", user=" + user + ", space=" + space + '}';
    }
}
