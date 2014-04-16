package ch.jtaf.data;

import ch.jtaf.entity.Competition;
import java.util.ArrayList;
import java.util.List;

public class CompetitionRankingData {

    private Competition competition;
    private List<CompetitionRankingCategoryData> categories = new ArrayList<>();

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public List<CompetitionRankingCategoryData> getCategories() {
        return categories;
    }

    public void setCategories(List<CompetitionRankingCategoryData> categories) {
        this.categories = categories;
    }
}
