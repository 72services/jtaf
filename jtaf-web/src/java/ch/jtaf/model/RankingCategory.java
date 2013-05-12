package ch.jtaf.model;

import java.util.ArrayList;
import java.util.List;

public class RankingCategory {

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
