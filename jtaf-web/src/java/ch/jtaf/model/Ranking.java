package ch.jtaf.model;

import java.util.ArrayList;
import java.util.List;

public class Ranking {

    private Competition competition;
    private List<RankingCategory> categories = new ArrayList<RankingCategory>();

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public List<RankingCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<RankingCategory> categories) {
        this.categories = categories;
    }
}
