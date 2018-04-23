package ch.jtaf.entity

import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
data class SecurityUser(
        @Id @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,

        @Column(unique = true)
        var email: String = "",
        var secret: String = "",

        var firstName: String = "",
        var lastName: String = "",

        var confirmationId: String = "",
        var confirmed: Boolean = false
) {
    @OneToMany
    @JoinTable(name = "user_group",
            joinColumns = [(JoinColumn(name = "user_id", referencedColumnName = "id"))],
            inverseJoinColumns = [(JoinColumn(name = "group_id", referencedColumnName = "id", unique = true))])
    var groups: MutableList<SecurityGroup> = ArrayList()
}