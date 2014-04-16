package ch.jtaf.control.report;

import ch.jtaf.entity.Athlete;
import ch.jtaf.data.CompetitionRankingCategoryData;
import ch.jtaf.data.CompetitionRankingData;

public class CompetitionCsvExport {

    private final CompetitionRankingData ranking;

    public CompetitionCsvExport(CompetitionRankingData ranking) {
        this.ranking = ranking;
    }

    public String create() {
        StringBuilder sb = new StringBuilder();
        for (CompetitionRankingCategoryData category : ranking.getCategories()) {
            int position = 1;
            for (Athlete athlete : category.getAthletes()) {
                sb.append(position);
                sb.append(",");
                sb.append(athlete.getLastName());
                sb.append(",");
                sb.append(athlete.getFirstName());
                sb.append(",");
                sb.append(category.getCategory().getAbbreviation());
                sb.append("\n");
                position++;
            }
        }
        return sb.toString();
    }

}
