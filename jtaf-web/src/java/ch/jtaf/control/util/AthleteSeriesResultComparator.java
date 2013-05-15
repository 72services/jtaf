package ch.jtaf.control.util;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Series;
import java.util.Comparator;

public class AthleteSeriesResultComparator implements Comparator<Athlete> {

    private Series series;
    
    public AthleteSeriesResultComparator(Series series) {
        this.series = series;
    }
    
    @Override
    public int compare(Athlete o1, Athlete o2) {
        return Integer.valueOf(o2.getSeriesPoints(series)).compareTo(
                Integer.valueOf(o1.getSeriesPoints(series)));
    }
}
