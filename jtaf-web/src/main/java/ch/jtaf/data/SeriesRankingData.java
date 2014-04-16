package ch.jtaf.data;

import ch.jtaf.entity.Series;
import java.util.ArrayList;
import java.util.List;

public class SeriesRankingData {

    private Series series;
    private List<SeriesRankingCategoryData> categories = new ArrayList<>();

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public List<SeriesRankingCategoryData> getCategories() {
        return categories;
    }

    public void setCategories(List<SeriesRankingCategoryData> categories) {
        this.categories = categories;
    }
}
