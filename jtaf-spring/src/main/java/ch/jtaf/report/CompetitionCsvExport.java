package ch.jtaf.report;

import ch.jtaf.entity.Athlete;
import ch.jtaf.vo.CompetitionRankingCategoryVO;
import ch.jtaf.vo.CompetitionRankingVO;

public class CompetitionCsvExport {

    private final CompetitionRankingVO ranking;

    public CompetitionCsvExport(CompetitionRankingVO ranking) {
        this.ranking = ranking;
    }

    public String create() {
        StringBuilder sb = new StringBuilder();
        for (CompetitionRankingCategoryVO category : ranking.getCategories()) {
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
