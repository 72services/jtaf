package ch.jtaf.entity;

import java.util.ArrayList;
import java.util.List;

public class RankingCategoryTO {

    private Category category;
    private List<Athlete> athletes = new ArrayList<Athlete>();

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
}
