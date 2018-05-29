package ch.jtaf.entity

import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
data class OrganizationUser(
        @Id @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,
        @ManyToOne
        var organization: Organization,
        @ManyToOne
        var user: SecurityUser
)