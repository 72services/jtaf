package ch.jtaf.report;

import ch.jtaf.entity.Athlete;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Numbers extends AbstractReport {

    private final static float FONT_SIZE_INFO = 12f;
    private final static float FONT_SIZE_TEXT = 90f;
    private final List<Athlete> athletes;

    public Numbers(List<Athlete> athletes, Locale locale) {
        super(locale);
        this.athletes = athletes;
    }

    public byte[] create() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, cmToPixel(1.5f), cmToPixel(1.7f), cmToPixel(0.8f), cmToPixel(0f));
            PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);
            document.open();
            int i = 0;
            int number = 1;
            PdfPTable table = createMainTable();
            for (Athlete athlete : athletes) {
                if (i > 9) {
                    document.add(table);
                    document.newPage();
                    i = 0;
                    table = createMainTable();
                }
                addAthleteInfo(table, athlete, number);
                if (i % 2 == 0) {
                    addEmptyCell(table);
                }
                if (i == 1 || i == 3 || i == 5 || i == 7) {
                    addEmptyRow(table);
                }
                i++;
                number++;
            }
            document.add(table);
            document.close();
            pdfWriter.flush();
            return baos.toByteArray();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private PdfPTable createMainTable() {
        PdfPTable table = new PdfPTable(new float[]{10f, 1.8f, 10f});
        table.setWidthPercentage(100);
        return table;
    }

    private void addEmptyCell(PdfPTable table) {
        PdfPCell cellEmpty = new PdfPCell(new Phrase(""));
        cellEmpty.setBorder(0);
        table.addCell(cellEmpty);
    }

    private void addAthleteInfo(PdfPTable table, Athlete athlete, int number) {
        PdfPTable atable = new PdfPTable(1);
        atable.setWidthPercentage(100);

        PdfPCell cellId = new PdfPCell(
                new Phrase(number + "",
                        FontFactory.getFont(FontFactory.HELVETICA, FONT_SIZE_TEXT)));
        cellId.setBorder(0);
        cellId.setMinimumHeight(cmToPixel(2.5f));
        cellId.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        atable.addCell(cellId);

        String text = athlete.getLastName() + " " + athlete.getFirstName() + "\n";
        text += athlete.getCategory().getAbbreviation();
        if (athlete.getClub() != null) {
            text += " / " + athlete.getClub().getName();
        }

        PdfPCell cellName = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, FONT_SIZE_INFO)));
        cellName.setBorder(0);
        cellName.setMinimumHeight(cmToPixel(1.8f));
        cellName.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cellName.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        atable.addCell(cellName);

        PdfPCell cellTable = new PdfPCell(atable);
        cellTable.setBorder(0);

        table.addCell(cellTable);
    }

    private void addEmptyRow(PdfPTable table) {
        PdfPCell cellId = new PdfPCell(new Phrase(" "));
        cellId.setBorder(0);
        cellId.setMinimumHeight(cmToPixel(0.5f));
        cellId.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cellId);

        addEmptyCell(table);

        PdfPCell cellName = new PdfPCell(new Phrase(" "));
        cellName.setBorder(0);
        cellName.setMinimumHeight(cmToPixel(0.5f));
        cellName.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cellName);
    }

}
