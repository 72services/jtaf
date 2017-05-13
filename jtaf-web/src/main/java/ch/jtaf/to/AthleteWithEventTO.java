package ch.jtaf.to;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Event;
import ch.jtaf.entity.Result;
import java.util.Objects;

public class AthleteWithEventTO {

    private final Athlete athlete;
    private final Event event;
    private final Result result;

    public AthleteWithEventTO(Athlete athlete, Event event, Result result) {
        this.athlete = athlete;
        this.event = event;
        this.result = result;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public Event getEvent() {
        return event;
    }

    public Result getResult() {
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.athlete);
        hash = 97 * hash + Objects.hashCode(this.event);
        hash = 97 * hash + Objects.hashCode(this.result);
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
        final AthleteWithEventTO other = (AthleteWithEventTO) obj;
        if (!Objects.equals(this.athlete, other.athlete)) {
            return false;
        }
        return Objects.equals(this.event, other.event) && Objects.equals(this.result, other.result);
    }

}
