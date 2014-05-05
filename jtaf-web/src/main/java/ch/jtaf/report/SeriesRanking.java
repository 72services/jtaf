package ch.jtaf.report;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Competition;
import ch.jtaf.vo.SeriesRankingCategoryVO;
import ch.jtaf.vo.SeriesRankingVO;
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

public class SeriesRanking extends Ranking {

    private Document document;
    private PdfWriter pdfWriter;
    private final SeriesRankingVO ranking;

    public SeriesRanking(SeriesRankingVO ranking, Locale locale) {
        super(locale);
        this.ranking = ranking;
    }

    public byte[] create() {
        try {
            byte[] ba;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                document = new Document(PageSize.A4);
                pdfWriter = PdfWriter.getInstance(document, baos);
                pdfWriter.setPageEvent(new HeaderFooter(
                        I18n.getInstance().getString(locale, "Series Ranking"),
                        ranking.getSeries().getName(),
                        ""));
                document.open();
                createRanking();
                document.close();
                pdfWriter.flush();
                ba = baos.toByteArray();
            }
            return ba;
        } catch (DocumentException | IOException e) {
            Logger.getLogger(SeriesRanking.class).error(e.getMessage(), e);
            return new byte[0];
        }
    }

    private void createRanking() throws DocumentException {
        for (SeriesRankingCategoryVO category : ranking.getCategories()) {
            if (numberOfRows > 22) {
                document.newPage();
            }
            PdfPTable table = createAthletesTable();
            createCategoryTitle(table, category);
            numberOfRows += 2;

            int position = 1;
            for (Athlete athlete : category.getAthletes()) {
                createAthleteRow(table, position, athlete);
                position++;
                numberOfRows += 1;
                if (numberOfRows > 23) {
                    document.add(table);
                    document.newPage();
                    table = createAthletesTable();
                }
            }
            document.add(table);
        }
    }

    private PdfPTable createAthletesTable() {
        PdfPTable table = new PdfPTable(new float[]{2f, 10f, 10f, 2f, 5f, 5f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(1f));
        return table;
    }

    private void createCategoryTitle(PdfPTable table, SeriesRankingCategoryVO category) {
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
            sb.append(" ");
        }
        addCell(table, "");
        addResultsCell(table, sb.toString());
    }
}
