package ch.jtaf.control.report;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportBase {

    protected static final float CM_PER_INCH = 2.54f;
    protected static final float DPI = 72f;
    protected static final float NORMAL_FONT_SIZE = 9f;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    protected float cmToPixel(Float cm) {
        return (cm / CM_PER_INCH) * DPI;
    }

    protected void addCell(PdfPTable table, String text) {
        addCell(table, text, 1, NORMAL_FONT_SIZE, true);
    }

    protected void addCell(PdfPTable table, String text, boolean left) {
        addCell(table, text, 1, NORMAL_FONT_SIZE, left);
    }

    protected void addCell(PdfPTable table, String text, int colspan, float fontSize) {
        addCell(table, text, colspan, fontSize, true);
    }

    protected void addCell(PdfPTable table, String text, float fontSize) {
        addCell(table, text, 1, fontSize, true);
    }

    protected void addCell(PdfPTable table, String text, float fontSize, boolean left) {
        addCell(table, text, 1, fontSize, left);
    }

    protected void addCell(PdfPTable table, String text, int colspan, float fontSize, boolean left) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSize)));
        cell.setBorder(0);
        cell.setColspan(colspan);
        if (!left) {
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        }
        table.addCell(cell);
    }

    protected void addCellCenter(PdfPTable table, String text, float fontSize) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSize)));
        cell.setBorder(0);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);
    }

    protected void addCellBottom(PdfPTable table, String text, float fontSize, boolean left) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSize)));
        cell.setBorder(0);
        if (!left) {
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        }
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        table.addCell(cell);
    }

    protected void addCellBottom(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, NORMAL_FONT_SIZE)));
        cell.setBorder(0);
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        table.addCell(cell);
    }

    protected void addCellBottom(PdfPTable table, String text, float fontSize) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSize)));
        cell.setBorder(0);
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        table.addCell(cell);
    }

    protected void addCellBold(PdfPTable table, String text, float fontSize) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, fontSize)));
        cell.setBorder(0);
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        table.addCell(cell);
    }

    protected void addCellWithHeight(PdfPTable table, String text, float fontSize, float height) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSize)));
        cell.setMinimumHeight(height);
        cell.setBorder(0);
        table.addCell(cell);
    }

    protected void addCellWithHeightAndBorder(PdfPTable table, String text, float fontSize, float height) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSize)));
        cell.setBorderWidth(1f);
        cell.setMinimumHeight(height);
        table.addCell(cell);
    }

    protected void addCellWithHeightAndBorderAndColspan(PdfPTable table, String text, float fontSize, float height, int colspan) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSize)));
        cell.setBorderWidth(1f);
        cell.setMinimumHeight(height);
        cell.setColspan(colspan);
        table.addCell(cell);
    }

    class HeaderFooter extends PdfPageEventHelper {

        public static final float HEADER_FONT = 16f;
        private final PdfPTable header;

        public HeaderFooter(String left, String middle, String right) {
            header = new PdfPTable(3);
            header.setWidthPercentage(100);
            header.setSpacingBefore(cmToPixel(1f));

            addCell(header, left, HEADER_FONT);
            addCellCenter(header, middle, HEADER_FONT);
            addCell(header, right, HEADER_FONT, false);
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                document.add(header);
            } catch (DocumentException ex) {
                Logger.getLogger(CompetitionRanking.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            addCell(table, "Created by jtaf.ch");
            addCell(table, "Page " + document.getPageNumber(), false);

            Rectangle page = document.getPageSize();
            table.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
            table.writeSelectedRows(0, 1, document.leftMargin(), document.bottomMargin(),
                    writer.getDirectContent());
        }
    }
}
