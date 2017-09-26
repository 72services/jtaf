package ch.jtaf.vo;

import ch.jtaf.entity.Competition;

import java.util.ArrayList;
import java.util.List;

public class EventsRankingVO {

    private Competition competition;
    private List<EventsRankingEventData> events = new ArrayList<>();

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public List<EventsRankingEventData> getEvents() {
        return events;
    }

    public void setEvents(List<EventsRankingEventData> events) {
        this.events = events;
    }

}
