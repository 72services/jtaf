package ch.jtaf.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserSpace {

    @Id
    private Long id;
    private UserSpaceRole role;
    @ManyToOne
    private SecurityUser user;
    @ManyToOne
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
}
