package ch.jtaf.entity;

import java.util.ArrayList;
import java.util.List;

public class CompetitionRankingTO {

    private Competition competition;
    private List<RankingCategoryTO> categories = new ArrayList<RankingCategoryTO>();

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public List<RankingCategoryTO> getCategories() {
        return categories;
    }

    public void setCategories(List<RankingCategoryTO> categories) {
        this.categories = categories;
    }
}
