package ch.jtaf.control.report;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.CompetitionRankingCategoryData;
import ch.jtaf.entity.Result;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jboss.logging.Logger;

public class CompetitionRanking extends Ranking {

    private Document document;
    private PdfWriter pdfWriter;
    private final ch.jtaf.entity.CompetitionRankingData ranking;

    public CompetitionRanking(ch.jtaf.entity.CompetitionRankingData ranking) {
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
                        "Ranking", ranking.getCompetition().getName(),
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
        for (CompetitionRankingCategoryData category : ranking.getCategories()) {
            PdfPTable table = new PdfPTable(new float[]{2f, 10f, 10f, 2f, 5f, 5f});
            table.setWidthPercentage(100);
            table.setSpacingBefore(cmToPixel(1f));

            createCategoryTitle(table, category);

            int position = 1;
            for (Athlete athlete : category.getAthletes()) {
                createAthleteRow(table, position, athlete);
                position++;
            }
            document.add(table);
        }
    }

    private void createCategoryTitle(PdfPTable table, CompetitionRankingCategoryData category) {
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
        for (Result result : athlete.getResults()) {
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
