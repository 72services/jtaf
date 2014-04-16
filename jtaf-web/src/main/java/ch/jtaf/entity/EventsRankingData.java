package ch.jtaf.entity;

import ch.jtaf.data.EventsRankingEventData;
import java.util.ArrayList;
import java.util.List;

public class EventsRankingData {

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
