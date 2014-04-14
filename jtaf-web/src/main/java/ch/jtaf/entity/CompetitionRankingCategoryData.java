package ch.jtaf.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.category);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompetitionRankingCategoryData other = (CompetitionRankingCategoryData) obj;
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(CompetitionRankingCategoryData o) {
        return this.category.getAbbreviation().compareTo(o.getCategory().getAbbreviation());
    }
}
