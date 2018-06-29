package ch.jtaf.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class OrganizationUser(
        @Id @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,
        @ManyToOne
        var organization: Organization,
        @ManyToOne
        var user: SecurityUser
)