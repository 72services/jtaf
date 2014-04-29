package ch.jtaf.report;

import ch.jtaf.vo.CompetitionRankingCategoryVO;
import ch.jtaf.vo.CompetitionRankingVO;
import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Result;
import ch.jtaf.i18n.I18n;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import org.jboss.logging.Logger;

public class CompetitionRanking extends Ranking {

    private Document document;
    private PdfWriter pdfWriter;
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
                pdfWriter = PdfWriter.getInstance(document, baos);
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
            PdfPTable table = createAthletesTable();
            createCategoryTitle(table, category);

            int position = 1;
            for (Athlete athlete : category.getAthletes()) {
                createAthleteRow(table, position, athlete);
                position++;
                numberOfRows += 1;
                if (numberOfRows > 24) {
                    document.add(table);
                    table = createAthletesTable();
                    document.newPage();
                }
            }
            document.add(table);
            numberOfRows += 3;
        }
    }

    private PdfPTable createAthletesTable() {
        PdfPTable table = new PdfPTable(new float[]{2f, 10f, 10f, 2f, 5f, 5f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(1f));
        return table;
    }

    private void createCategoryTitle(PdfPTable table, CompetitionRankingCategoryVO category) {
        addCategoryTitleCellWithColspan(table, category.getCategory().getAbbreviation(), 1);
        addCategoryTitleCellWithColspan(table, category.getCategory().getName() + " "
                + category.getCategory().getYearFrom() + " - " + category.getCategory().getYearTo(), 5);

        addCategoryTitleCellWithColspan(table, " ", 6);
    }

    private void createAthleteRow(PdfPTable table, int position, Athlete athlete) throws DocumentException {
        addCell(table, position + ".");
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
