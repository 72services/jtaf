package ch.jtaf.control.report;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Competition;
import ch.jtaf.entity.Event;
import ch.jtaf.entity.Series;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class Sheet extends ReportBase {

    private final static float FONT_SIZE_INFO = 8f;
    private final static float FONT_SIZE_TEXT = 16f;
    private final static float FONT_SIZE_EVENT = 16f;
    public static final float INFO_LINE_HEIGHT = 40f;
    private Document document;
    private PdfWriter pdfWriter;
    private final Competition competition;
    private final Series series;
    private final List<Athlete> athletes;

    public Sheet(Athlete athlete) {
        this.competition = null;
        this.athletes = new ArrayList<Athlete>();
        this.athletes.add(athlete);
        this.series = athlete.getCategory().getSeries();
    }

    public Sheet(Competition competition, Athlete athlete) {
        this.competition = competition;
        this.series = competition.getSeries();
        this.athletes = new ArrayList<Athlete>();
        this.athletes.add(athlete);
    }

    public Sheet(Competition competition, List<Athlete> athletes) {
        this.competition = competition;
        this.series = competition.getSeries();
        this.athletes = athletes;
    }

    public byte[] create() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            float oneCm = cmToPixel(1f);
            document = new Document(PageSize.A5, oneCm, oneCm, cmToPixel(3.5f), oneCm);
            pdfWriter = PdfWriter.getInstance(document, baos);

            document.open();

            boolean first = true;
            for (Athlete athlete : athletes) {
                if (!first) {
                    document.newPage();
                }
                createLogo();
                createCategory(athlete);
                createAthleteInfo(athlete);
                createCompetitionRow();
                createEventTable(athlete);
                first = false;
            }

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

    private void createLogo() throws BadElementException, DocumentException, MalformedURLException, IOException {
        if (series.getLogo() != null) {
            Image image = Image.getInstance(series.getLogo());
            image.setAbsolutePosition(cmToPixel(1f), cmToPixel(19f));
            image.scaleAbsolute(120, 100);
            document.add(image);
        }
    }

    private void createCategory(Athlete athlete) throws BadElementException, DocumentException, MalformedURLException, IOException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        addCell(table, athlete.getCategory().getAbbreviation(), 80f, false);

        Rectangle page = document.getPageSize();
        table.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        table.writeSelectedRows(0, 1, document.leftMargin(), cmToPixel(21f), pdfWriter.getDirectContent());
    }

    private void createAthleteInfo(Athlete athlete) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        if (athlete.getId() != null) {
            addCellWithHeight(table, athlete.getId() == null ? "Id" : athlete.getId().toString(), FONT_SIZE_TEXT, INFO_LINE_HEIGHT);
            addCell(table, "");
        }
        if (athlete.getLastName() == null) {
            addCellWithHeightAndBorder(table, "Last name", FONT_SIZE_INFO, INFO_LINE_HEIGHT);
        } else {
            addCellWithHeight(table, athlete.getLastName(), FONT_SIZE_TEXT, INFO_LINE_HEIGHT);
        }
        if (athlete.getFirstName() == null) {
            addCellWithHeightAndBorder(table, "First name", FONT_SIZE_INFO, INFO_LINE_HEIGHT);
        } else {
            addCellWithHeight(table, athlete.getFirstName(), FONT_SIZE_TEXT, INFO_LINE_HEIGHT);
        }
        if (athlete.getYear() == 0) {
            addCellWithHeightAndBorder(table, "Year", FONT_SIZE_INFO, INFO_LINE_HEIGHT);
        } else {
            addCellWithHeight(table, athlete.getYear() + "", FONT_SIZE_TEXT, INFO_LINE_HEIGHT);
        }
        if (athlete.getClub() == null) {
            if (athlete.getId() == null) {
                addCellWithHeightAndBorder(table, "Club", FONT_SIZE_INFO, INFO_LINE_HEIGHT);
            } else {
                addCellWithHeight(table, "", FONT_SIZE_INFO, INFO_LINE_HEIGHT);
            }
        } else {
            addCellWithHeight(table, athlete.getClub().getAbbreviation(), FONT_SIZE_TEXT, INFO_LINE_HEIGHT);
        }

        document.add(table);
    }

    private void createCompetitionRow() throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(0.5f));
        table.setSpacingAfter(cmToPixel(0.5f));

        addCellBold(table, competition == null ? "" : competition.getName(), FONT_SIZE_TEXT);
        addCellBold(table, competition == null ? "" : sdf.format(competition.getCompetitionDate()), FONT_SIZE_TEXT);

        document.add(table);
    }

    private void createEventTable(Athlete athlete) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(1f));

        for (Event event : athlete.getCategory().getEvents()) {
            if (event.getType().equals(Event.JUMP_THROW)) {
                addCell(table, event.getName(), FONT_SIZE_EVENT);
                addCellWithHeightAndBorder(table, "", FONT_SIZE_EVENT, 40f);
                addCellWithHeightAndBorder(table, "", FONT_SIZE_EVENT, 40f);
                addCellWithHeightAndBorder(table, "", FONT_SIZE_EVENT, 40f);
            } else {
                addCell(table, event.getName(), 3, FONT_SIZE_EVENT);
                addCellWithHeightAndBorder(table, "", FONT_SIZE_EVENT, 40f);
            }
        }

        document.add(table);
    }
}
