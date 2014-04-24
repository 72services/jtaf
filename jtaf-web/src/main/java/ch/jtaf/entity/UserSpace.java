package ch.jtaf.entity;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "userspace")
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
public class UserSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
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
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final UserSpace other = (UserSpace) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserSpace{" + "id=" + id + ", role=" + role + ", user=" + user + ", space=" + space + '}';
    }
}
