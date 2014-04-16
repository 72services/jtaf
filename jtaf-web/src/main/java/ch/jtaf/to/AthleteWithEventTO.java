package ch.jtaf.to;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Event;
import ch.jtaf.entity.Result;

public class AthleteWithEventTO implements Comparable<AthleteWithEventTO> {

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
    public int compareTo(AthleteWithEventTO o) {
        int res = this.getAthlete().getId().compareTo(o.getAthlete().getId());
        if (res != 0) {
            return res;
        }
        res = this.getEvent().getId().compareTo(o.getEvent().getId());
        if (res != 0) {
            return res;
        }
        return Integer.compare(this.getResult().getPoints(), o.getResult().getPoints());
    }

}
