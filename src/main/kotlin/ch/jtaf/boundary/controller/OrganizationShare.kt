package ch.jtaf.boundary.controller

import ch.jtaf.entity.Organization

data class OrganizationShare(var organization: Organization, var email: String? = null)