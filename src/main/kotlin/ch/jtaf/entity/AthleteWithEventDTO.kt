package ch.jtaf.entity

import java.util.Objects

data class AthleteWithEventDTO(val athlete: Athlete, val category: Category, val event: Event, val result: Result)