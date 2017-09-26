package ch.jtaf.vo;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompetitionRankingCategoryVO implements Comparable<CompetitionRankingCategoryVO> {

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
        final CompetitionRankingCategoryVO other = (CompetitionRankingCategoryVO) obj;
        return Objects.equals(this.category, other.category);
    }

    @Override
    public int compareTo(CompetitionRankingCategoryVO o) {
        return this.category.getAbbreviation().compareTo(o.getCategory().getAbbreviation());
    }
}
