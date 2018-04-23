package ch.jtaf.entity

import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
data class Organization(
        @Id @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,

        @Column(name = "organization_key", unique = true)
        var key: String = "",
        var name: String = "",

        var owner: String? = null
) {
    @OneToMany(mappedBy = "organization")
    var users: MutableList<OrganizationUser> = ArrayList()
}