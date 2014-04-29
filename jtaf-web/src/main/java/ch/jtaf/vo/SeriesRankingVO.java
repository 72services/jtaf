package ch.jtaf.vo;

import ch.jtaf.entity.Series;
import java.util.ArrayList;
import java.util.List;

public class SeriesRankingVO {

    private Series series;
    private List<SeriesRankingCategoryVO> categories = new ArrayList<>();

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public List<SeriesRankingCategoryVO> getCategories() {
        return categories;
    }

    public void setCategories(List<SeriesRankingCategoryVO> categories) {
        this.categories = categories;
    }
}
