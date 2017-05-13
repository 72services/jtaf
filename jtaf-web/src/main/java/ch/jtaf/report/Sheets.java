package ch.jtaf.report;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Competition;
import ch.jtaf.entity.Event;
import ch.jtaf.entity.EventType;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.jboss.logging.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Sheets extends AbstractReport {

    private final static float FONT_SIZE_INFO = 8f;
    private final static float FONT_SIZE_TEXT = 16f;
    public static final float INFO_LINE_HEIGHT = 40f;
    private Document document;
    private PdfWriter pdfWriter;
    private final Competition competition;
    private final List<Athlete> athletes;
    private final byte[] logo;

    public Sheets(Athlete athlete, byte[] logo, Locale locale) {
        super(locale);
        this.competition = null;
        this.athletes = new ArrayList<>();
        this.athletes.add(athlete);
        this.logo = logo;
    }

    public Sheets(Competition competition, Athlete athlete, byte[] logo, Locale locale) {
        super(locale);
        this.competition = competition;
        this.athletes = new ArrayList<>();
        this.athletes.add(athlete);
        this.logo = logo;
    }

    public Sheets(Competition competition, List<Athlete> athletes, byte[] logo, Locale locale) {
        super(locale);
        this.competition = competition;
        this.athletes = athletes;
        this.logo = logo;
    }

    public byte[] create() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            float oneCm = cmToPixel(1f);
            document = new Document(PageSize.A5, oneCm, oneCm, cmToPixel(4.5f), oneCm);
            pdfWriter = PdfWriter.getInstance(document, baos);
            document.open();
            boolean first = true;
            int number = 1;
            for (Athlete athlete : athletes) {
                if (!first) {
                    document.newPage();
                }
                createLogo();
                createCategory(athlete);
                createAthleteInfo(athlete, number);
                createCompetitionRow();
                createEventTable(athlete);
                first = false;
                number++;
            }
            document.close();
            pdfWriter.flush();
            return baos.toByteArray();
        } catch (DocumentException | IOException e) {
            Logger.getLogger(Sheets.class).error(e.getMessage(), e);
            return new byte[0];
        }
    }

    private void createLogo() throws DocumentException, IOException {
        if (logo != null) {
            Image image = Image.getInstance(logo);
            image.setAbsolutePosition(cmToPixel(1f), cmToPixel(17.5f));
            image.scaleToFit(120, 60);
            document.add(image);
        }
    }

    private void createCategory(Athlete athlete) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        addCategoryCell(table, athlete.getCategory().getAbbreviation());

        Rectangle page = document.getPageSize();
        table.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        table.writeSelectedRows(0, 1, document.leftMargin(), cmToPixel(20.5f), pdfWriter.getDirectContent());
    }

    private void createAthleteInfo(Athlete athlete, int number) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(1f));

        if (athlete.getId() != null) {
            addInfoCell(table, "" + number);
            addCell(table, athlete.getId().toString());
        } else {
            addCell(table, " ");
            addCell(table, " ");
        }
        if (athlete.getLastName() == null) {
            addInfoCellWithBorder(table, "Last name");
        } else {
            addInfoCell(table, athlete.getLastName());
        }
        if (athlete.getFirstName() == null) {
            addInfoCellWithBorder(table, "First name");
        } else {
            addInfoCell(table, athlete.getFirstName());
        }
        if (athlete.getYear() == 0) {
            addInfoCellWithBorder(table, "Year");
        } else {
            addInfoCell(table, athlete.getYear() + "");
        }
        if (athlete.getClub() == null) {
            if (athlete.getId() == null) {
                addInfoCellWithBorder(table, "Club");
            } else {
                addInfoCell(table, "");
            }
        } else {
            addInfoCell(table, athlete.getClub().getAbbreviation());
        }

        document.add(table);
    }

    private void createCompetitionRow() throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(0.5f));
        table.setSpacingAfter(cmToPixel(0.5f));

        addCompetitionCell(table, competition == null ? ""
                : competition.getName() + " " + sdf.format(competition.getCompetitionDate()));

        document.add(table);
    }

    private void createEventTable(Athlete athlete) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(1f));

        for (Event event : athlete.getCategory().getEvents()) {
            if (event.getType().equals(EventType.JUMP_THROW)) {
                addInfoCell(table, event.getLongName());
                addInfoCellWithBorder(table, "");
                addInfoCellWithBorder(table, "");
                addInfoCellWithBorder(table, "");
            } else {
                addInfoCellWithColspan(table, event.getLongName(), 3);
                addInfoCellWithBorder(table, "");
            }
        }

        document.add(table);
    }

    protected void addCategoryCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 80f)));
        cell.setBorder(0);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table.addCell(cell);
    }

    private void addCompetitionCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, FONT_SIZE_TEXT)));
        cell.setBorder(0);
        table.addCell(cell);
    }

    private void addInfoCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, FONT_SIZE_TEXT)));
        cell.setBorder(0);
        cell.setMinimumHeight(INFO_LINE_HEIGHT);
        table.addCell(cell);
    }

    private void addInfoCellWithColspan(PdfPTable table, String text, int colspan) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, FONT_SIZE_TEXT)));
        cell.setBorder(0);
        cell.setColspan(colspan);
        cell.setMinimumHeight(INFO_LINE_HEIGHT);
        table.addCell(cell);
    }

    private void addInfoCellWithBorder(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, FONT_SIZE_INFO)));
        cell.setMinimumHeight(INFO_LINE_HEIGHT);
        cell.setBorderWidth(1);
        table.addCell(cell);
    }
}
