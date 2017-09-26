package ch.jtaf.vo;

import ch.jtaf.entity.Competition;

import java.util.ArrayList;
import java.util.List;

public class CompetitionRankingVO {

    private Competition competition;
    private List<CompetitionRankingCategoryVO> categories = new ArrayList<>();

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public List<CompetitionRankingCategoryVO> getCategories() {
        return categories;
    }

    public void setCategories(List<CompetitionRankingCategoryVO> categories) {
        this.categories = categories;
    }
}
