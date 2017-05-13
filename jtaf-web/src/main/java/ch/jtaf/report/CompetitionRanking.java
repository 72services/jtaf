package ch.jtaf.report;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Result;
import ch.jtaf.i18n.I18n;
import ch.jtaf.vo.CompetitionRankingCategoryVO;
import ch.jtaf.vo.CompetitionRankingVO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.jboss.logging.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

public class CompetitionRanking extends Ranking {

    private Document document;
    private final CompetitionRankingVO ranking;

    public CompetitionRanking(CompetitionRankingVO ranking, Locale locale) {
        super(locale);
        this.ranking = ranking;
    }

    public byte[] create() {
        try {
            byte[] ba;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                float border = cmToPixel(1.5f);
                document = new Document(PageSize.A4, border, border, border, border);
                PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);
                pdfWriter.setPageEvent(new HeaderFooter(
                        I18n.getInstance().getString(locale, "Ranking"),
                        ranking.getCompetition().getName(),
                        sdf.format(ranking.getCompetition().getCompetitionDate())));
                document.open();
                createRanking();
                document.close();
                pdfWriter.flush();
                ba = baos.toByteArray();
            }

            return ba;
        } catch (DocumentException | IOException e) {
            Logger.getLogger(CompetitionRanking.class).error(e.getMessage(), e);
            return new byte[0];
        }
    }

    private void createRanking() throws DocumentException {
        for (CompetitionRankingCategoryVO category : ranking.getCategories()) {
            if (numberOfRows > 24) {
                document.newPage();
            }
            PdfPTable table = createAthletesTable();
            createCategoryTitle(table, category);
            numberOfRows += 2;

            int rank = 1;
            for (Athlete athlete : category.getAthletes()) {
                if (numberOfRows > 23) {
                    document.add(table);
                    table = createAthletesTable();
                    document.newPage();
                }
                createAthleteRow(table, rank, athlete, calculateNumberOfMedals(category));
                rank++;
                numberOfRows += 1;
            }
            document.add(table);
        }
    }

    private int calculateNumberOfMedals(CompetitionRankingCategoryVO category) {
        double numberOfMedals = 0;
        if (ranking.getCompetition().getMedalPercentage() != null
                && ranking.getCompetition().getMedalPercentage() > 0) {
            double percentage = ranking.getCompetition().getMedalPercentage();
            numberOfMedals = category.getAthletes().size() * (percentage / 100);
            if (numberOfMedals < 3 && ranking.getCompetition().isAlwaysThreeMedals()) {
                numberOfMedals = 3;
            }
        }
        return (int) numberOfMedals;
    }

    private PdfPTable createAthletesTable() {
        PdfPTable table = new PdfPTable(new float[]{2f, 10f, 10f, 2f, 5f, 5f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(0.6f));
        return table;
    }

    private void createCategoryTitle(PdfPTable table, CompetitionRankingCategoryVO category) {
        addCategoryTitleCellWithColspan(table, category.getCategory().getAbbreviation(), 1);
        addCategoryTitleCellWithColspan(table, category.getCategory().getName() + " "
                + category.getCategory().getYearFrom() + " - " + category.getCategory().getYearTo(), 5);

        addCategoryTitleCellWithColspan(table, " ", 6);
    }

    private void createAthleteRow(PdfPTable table, int rank, Athlete athlete, int numberOfMedals) {
        if (rank <= numberOfMedals) {
            addCell(table, "* " + rank + ".");
        } else {
            addCell(table, rank + ".");
        }
        addCell(table, athlete.getLastName());
        addCell(table, athlete.getFirstName());
        addCell(table, athlete.getYear() + "");
        addCell(table, athlete.getClub() == null ? "" : athlete.getClub().getAbbreviation());
        addCellAlignRight(table, athlete.getTotalPoints(ranking.getCompetition()) + "");

        StringBuilder sb = new StringBuilder();
        for (Result result : athlete.getResults(ranking.getCompetition())) {
            sb.append(result.getEvent().getName());
            sb.append(": ");
            sb.append(result.getResult());
            sb.append(" (");
            sb.append(result.getPoints());
            sb.append(") ");
        }
        addCell(table, "");
        addResultsCell(table, sb.toString());
    }

}
