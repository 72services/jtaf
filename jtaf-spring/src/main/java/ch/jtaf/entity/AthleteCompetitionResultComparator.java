package ch.jtaf.entity;

import java.util.Comparator;

public class AthleteCompetitionResultComparator implements Comparator<Athlete> {

    private final Competition competition;

    public AthleteCompetitionResultComparator(Competition competition) {
        this.competition = competition;
    }

    @Override
    public int compare(Athlete o1, Athlete o2) {
        return Integer.valueOf(o2.getTotalPoints(competition)).compareTo(o1.getTotalPoints(competition));
    }
}
