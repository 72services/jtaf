package ch.jtaf.control.util;

import ch.jtaf.entity.Athlete;
import java.util.Comparator;

public class AthleteResultComparator implements Comparator<Athlete> {

    @Override
    public int compare(Athlete o1, Athlete o2) {
        return Integer.valueOf(o2.getTotalPoints()).compareTo(
                Integer.valueOf(o1.getTotalPoints()));
    }
}
