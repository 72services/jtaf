package ch.jtaf.control.report;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.CompetitionRankingCategoryData;
import ch.jtaf.entity.Result;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;

public class CompetitionRanking extends ReportBase {

    private Document document;
    private PdfWriter pdfWriter;
    private final ch.jtaf.entity.CompetitionRankingData ranking;

    public CompetitionRanking(ch.jtaf.entity.CompetitionRankingData ranking) {
        this.ranking = ranking;
    }

    public byte[] create() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(document, baos);
            pdfWriter.setPageEvent(new HeaderFooter(
                    "Ranking", ranking.getCompetition().getName(),
                    sdf.format(ranking.getCompetition().getCompetitionDate())));
            document.open();

            createRanking();

            document.close();
            pdfWriter.flush();

            byte[] ba = baos.toByteArray();
            baos.close();

            return ba;
        } catch (Exception e) {
            e.printStackTrace();
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
        addCell(table, category.getCategory().getAbbreviation(), 12f);
        addCell(table, category.getCategory().getName() + " "
                + category.getCategory().getYearFrom() + " - " + category.getCategory().getYearTo(), 5, 12f);

        addCell(table, " ", 6, NORMAL_FONT_SIZE, true);
    }

    private void createAthleteRow(PdfPTable table, int position, Athlete athlete) throws DocumentException {
        addCell(table, position + ".");
        addCell(table, athlete.getLastName());
        addCell(table, athlete.getFirstName());
        addCell(table, athlete.getYear() + "");
        addCell(table, athlete.getClub() == null ? "" : athlete.getClub().getAbbreviation());
        addCell(table, athlete.getTotalPoints(ranking.getCompetition()) + "", false);

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

    private void addResultsCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 8f)));
        cell.setColspan(5);
        cell.setBorder(0);
        cell.setPaddingBottom(8f);
        table.addCell(cell);
    }
}
