package ch.jtaf.report;

import ch.jtaf.i18n.I18n;
import ch.jtaf.to.AthleteWithEventTO;
import ch.jtaf.vo.EventsRankingEventData;
import ch.jtaf.vo.EventsRankingVO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

public class EventsRanking extends Ranking {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventsRanking.class);

    private Document document;
    private final EventsRankingVO ranking;

    public EventsRanking(EventsRankingVO ranking, Locale locale) {
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
                        I18n.getInstance().getString(locale, "Event Ranking"),
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
            LOGGER.error(e.getMessage(), e);
            return new byte[0];
        }
    }

    private void createRanking() throws DocumentException {
        for (EventsRankingEventData event : ranking.getEvents()) {
            PdfPTable table = new PdfPTable(new float[]{2f, 10f, 10f, 2f, 2f, 5f, 5f});
            table.setWidthPercentage(100);
            table.setSpacingBefore(cmToPixel(1f));
            table.setKeepTogether(true);

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
        addCategoryTitleCellWithColspan(table, event.getEvent().getLongName() + " / " + event.getEvent().getName() + " / " + event.getEvent().getGender(), 7);

        addCategoryTitleCellWithColspan(table, " ", 7);
    }

    private void createAthleteRow(PdfPTable table, int position, AthleteWithEventTO athlete) {
        addCell(table, position + ".");
        addCell(table, athlete.getAthlete().getLastName());
        addCell(table, athlete.getAthlete().getFirstName());
        addCell(table, athlete.getAthlete().getYear() + "");
        addCell(table, athlete.getAthlete().getCategory().getAbbreviation() + "");
        addCell(table, athlete.getAthlete().getClub() == null ? "" : athlete.getAthlete().getClub().getAbbreviation());
        addCellAlignRight(table, "" + athlete.getResult().getResult());
    }

}
