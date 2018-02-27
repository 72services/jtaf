package ch.jtaf.entity

import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
data class OrganizationUser(
        @Id @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,

        var abbreviation: String = "",
        var name: String = "",

        var owner: String? = null,

        @ManyToOne
        var organization: Organization? = null,

        @OneToMany
        @JoinTable(name = "organizationuser_securityuser",
                joinColumns = [(JoinColumn(name = "organizationuser_id", referencedColumnName = "id"))],
                inverseJoinColumns = [(JoinColumn(name = "securityuser_id", referencedColumnName = "id", unique = true))])
        var users: MutableList<SecurityUser> = ArrayList()
)