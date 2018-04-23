package ch.jtaf.control.repository

import ch.jtaf.entity.Competition
import org.springframework.data.jpa.repository.JpaRepository

interface CompetitionRepository : JpaRepository<Competition, Long>
