package ch.jtaf.control.report;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Competition;
import ch.jtaf.entity.SeriesRankingCategoryData;
import ch.jtaf.entity.SeriesRankingData;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import org.jboss.logging.Logger;

public class SeriesRanking extends Ranking {

    private Document document;
    private PdfWriter pdfWriter;
    private final SeriesRankingData ranking;

    public SeriesRanking(SeriesRankingData ranking) {
        this.ranking = ranking;
    }

    public byte[] create() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(document, baos);
            pdfWriter.setPageEvent(new HeaderFooter(
                    "Series ranking", ranking.getSeries().getName(),
                    sdf.format(new Date())));
            document.open();

            createRanking();

            document.close();
            pdfWriter.flush();

            byte[] ba = baos.toByteArray();
            baos.close();

            return ba;
        } catch (DocumentException | IOException e) {
            Logger.getLogger(SeriesRanking.class).error(e.getMessage(), e);
            return new byte[0];
        }
    }

    private void createRanking() throws DocumentException {
        for (SeriesRankingCategoryData category : ranking.getCategories()) {
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

    private void createCategoryTitle(PdfPTable table, SeriesRankingCategoryData category) {
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
        addCellAlignRight(table, athlete.getSeriesPoints(ranking.getSeries()) + "");

        StringBuilder sb = new StringBuilder();
        for (Competition competition : ranking.getSeries().getCompetitions()) {
            sb.append(competition.getName());
            sb.append(": ");
            sb.append(athlete.getTotalPoints(competition));
        }
        addCell(table, "");
        addResultsCell(table, sb.toString());
    }
}
