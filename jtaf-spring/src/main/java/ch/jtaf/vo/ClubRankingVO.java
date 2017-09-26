package ch.jtaf.vo;

import ch.jtaf.entity.Series;

import java.util.ArrayList;
import java.util.List;

public class ClubRankingVO {

    private Series series;
    private List<ClubResultVO> clubs = new ArrayList<>();

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public List<ClubResultVO> getClubs() {
        return clubs;
    }

    public void setClubs(List<ClubResultVO> clubs) {
        this.clubs = clubs;
    }

}
