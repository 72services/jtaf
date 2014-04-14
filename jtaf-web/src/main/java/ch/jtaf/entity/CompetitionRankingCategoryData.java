package ch.jtaf.entity;

import java.util.ArrayList;
import java.util.List;

public class CompetitionRankingCategoryData implements Comparable<CompetitionRankingCategoryData> {

    private Category category;
    private List<Athlete> athletes = new ArrayList<>();

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Athlete> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<Athlete> athletes) {
        this.athletes = athletes;
    }

    @Override
    public int compareTo(CompetitionRankingCategoryData o) {
        return this.category.getAbbreviation().compareTo(o.getCategory().getAbbreviation());
    }
}
