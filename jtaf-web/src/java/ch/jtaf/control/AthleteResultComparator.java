package ch.jtaf.control;

import ch.jtaf.model.Athlete;
import java.util.Comparator;

public class AthleteResultComparator implements Comparator<Athlete> {

    @Override
    public int compare(Athlete o1, Athlete o2) {
        return Integer.valueOf(o1.getTotalPoints()).compareTo(
                Integer.valueOf(o2.getTotalPoints()));
    }
}
