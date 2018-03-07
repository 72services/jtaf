package ch.jtaf.control.reporting.data

import ch.jtaf.entity.AthleteWithEventDTO
import ch.jtaf.entity.Event
import java.util.*

class EventsRankingEventData(val event: Event, val athletes: List<AthleteWithEventDTO>) : Comparable<EventsRankingEventData> {


    override fun hashCode(): Int {
        var hash = 7
        hash = 73 * hash + Objects.hashCode(this.event)
        return hash
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        val otherEvent = other as EventsRankingEventData?
        return this.event == otherEvent?.event
    }

    override fun compareTo(other: EventsRankingEventData): Int {
        return this.event.id!!.compareTo(other.event.id!!)
    }
}
