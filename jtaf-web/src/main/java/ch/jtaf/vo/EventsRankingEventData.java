package ch.jtaf.vo;

import ch.jtaf.entity.Event;
import ch.jtaf.to.AthleteWithEventTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventsRankingEventData implements Comparable<EventsRankingEventData> {

    private Event event;
    private List<AthleteWithEventTO> athletes = new ArrayList<>();

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<AthleteWithEventTO> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<AthleteWithEventTO> athletes) {
        this.athletes = athletes;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.event);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventsRankingEventData other = (EventsRankingEventData) obj;
        if (!Objects.equals(this.event, other.event)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(EventsRankingEventData o) {
        return this.event.getId().compareTo(o.event.getId());
    }
}
