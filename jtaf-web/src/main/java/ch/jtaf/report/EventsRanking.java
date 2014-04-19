package ch.jtaf.report;

import ch.jtaf.to.AthleteWithEventTO;
import ch.jtaf.data.EventsRankingData;
import ch.jtaf.data.EventsRankingEventData;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jboss.logging.Logger;

public class EventsRanking extends Ranking {

    private Document document;
    private PdfWriter pdfWriter;
    private final EventsRankingData ranking;

    public EventsRanking(EventsRankingData ranking) {
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
                        "Event Ranking", ranking.getCompetition().getName(),
                        sdf.format(ranking.getCompetition().getCompetitionDate())));
                document.open();
                createRanking();
                document.close();
                pdfWriter.flush();
                ba = baos.toByteArray();
            }

            return ba;
        } catch (DocumentException | IOException e) {
            Logger.getLogger(EventsRanking.class).error(e.getMessage(), e);
            return new byte[0];
        }
    }

    private void createRanking() throws DocumentException {
        for (EventsRankingEventData event : ranking.getEvents()) {
            PdfPTable table = new PdfPTable(new float[]{2f, 10f, 10f, 2f, 2f, 5f, 5f});
            table.setWidthPercentage(100);
            table.setSpacingBefore(cmToPixel(1f));

            createEventTitle(table, event);

            int position = 1;
            for (AthleteWithEventTO athlete : event.getAthletes()) {
                createAthleteRow(table, position, athlete);
                position++;
            }
            document.add(table);
        }
    }

    private void createEventTitle(PdfPTable table, EventsRankingEventData event) {
        addCategoryTitleCellWithColspan(table, event.getEvent().getName(), 2);
        addCategoryTitleCellWithColspan(table, event.getEvent().getLongName(), 3);
        addCategoryTitleCellWithColspan(table, event.getEvent().getGender(), 2);


        addCategoryTitleCellWithColspan(table, " ", 7);
    }

    private void createAthleteRow(PdfPTable table, int position, AthleteWithEventTO athlete) throws DocumentException {
        addCell(table, position + ".");
        addCell(table, athlete.getAthlete().getLastName());
        addCell(table, athlete.getAthlete().getFirstName());
        addCell(table, athlete.getAthlete().getYear() + "");
        addCell(table, athlete.getAthlete().getCategory().getAbbreviation() + "");
        addCell(table, athlete.getAthlete().getClub() == null ? "" : athlete.getAthlete().getClub().getAbbreviation());
        addCellAlignRight(table, "" + athlete.getResult().getResult());
    }

}
