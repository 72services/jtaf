package ch.jtaf.control.report;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Competition;
import ch.jtaf.entity.Event;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Sheet {

    private static final float CM_PER_INCH = 2.54f;
    private static final float DPI = 72f;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private Document document;
    private PdfWriter pdfWriter;
    private final Competition competition;
    private final List<Athlete> athletes;

    public Sheet(Competition competition, Athlete athlete) {
        this.competition = competition;
        this.athletes = new ArrayList<Athlete>();
        this.athletes.add(athlete);
    }

    public Sheet(Competition competition, List<Athlete> athletes) {
        this.competition = competition;
        this.athletes = athletes;
    }

    public byte[] create() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document = new Document(PageSize.A5);
            pdfWriter = PdfWriter.getInstance(document, baos);

            document.open();

            boolean first = true;
            for (Athlete athlete : athletes) {
                if (!first) {
                    document.newPage();
                }
                //createLogo();
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
        Image image = Image.getInstance(getClass().getClassLoader().getResource(
                "logo_letter.png"));
        image.setAbsolutePosition(cmToPixel(15.2f), cmToPixel(26f));
        image.scaleAbsolute(122, 94);
        document.add(image);
    }

    private void createAthleteInfo(Athlete athlete) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(2f));

        addCell(table, "Id");
        addCell(table, athlete.getId().toString());
        addCell(table, "Category");
        addCell(table, athlete.getCategory().getAbbreviation());

        addCell(table, "Last name");
        addCell(table, athlete.getLastName());
        addCell(table, "First name");
        addCell(table, athlete.getFirstName());

        addCell(table, "Year");
        addCell(table, athlete.getYear() + "");
        addCell(table, "Club");
        addCell(table, athlete.getClub() == null ? "" : athlete.getClub().getAbbreviation());

        document.add(table);
    }

    private void createCompetitionRow() throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(1f));

        addCell(table, competition.getName());
        addCell(table, sdf.format(competition.getCompetitionDate()));

        document.add(table);
    }

    private void createEventTable(Athlete athlete) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(1f));

        for (Event event : athlete.getCategory().getEvents()) {
            addCell(table, event.getName());
            addCell(table, "");
        }

        document.add(table);
    }

    public float cmToPixel(Float cm) {
        return (cm / CM_PER_INCH) * DPI;
    }

    private void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 9f)));
        cell.setBorder(0);
        table.addCell(cell);
    }
}
