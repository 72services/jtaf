package ch.jtaf.vo;

import ch.jtaf.entity.Club;

public class ClubResultVO implements Comparable<ClubResultVO>{

    private Club club;
    private Integer points;

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public int compareTo(ClubResultVO o) {
        return o.getPoints().compareTo(points);
    }

}
