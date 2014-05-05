package ch.jtaf.report;

import ch.jtaf.i18n.I18n;
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
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractReport {

    protected static final float DEFAULT_FONT_SIZE = 9f;
    protected static final float CM_PER_INCH = 2.54f;
    protected static final float DPI = 72f;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    protected int numberOfRows;
    protected Locale locale;

    protected AbstractReport(Locale locale) {
        this.locale = locale;
    }

    protected float cmToPixel(Float cm) {
        return (cm / CM_PER_INCH) * DPI;
    }

    protected void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, DEFAULT_FONT_SIZE)));
        cell.setBorder(0);
        table.addCell(cell);
    }

    protected void addCell(PdfPTable table, String text, float fontSize) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSize)));
        cell.setBorder(0);
        table.addCell(cell);
    }

    protected void addCellAlignRight(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, DEFAULT_FONT_SIZE)));
        cell.setBorder(0);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table.addCell(cell);
    }

    class HeaderFooter extends PdfPageEventHelper {

        public static final float HEADER_FONT = 16f;
        public static final float HEADER_FONT_SMALL = 12f;
        private final PdfPTable header;

        public HeaderFooter(String left, String middle, String right) {
            header = new PdfPTable(new float[]{1, 3, 1});
            header.setWidthPercentage(100);
            header.setSpacingBefore(cmToPixel(1f));

            addHeaderCellAlignLeft(header, left);
            addHeaderCellAlignCenter(header, middle);
            addHeaderCellAlignRight(header, right);
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                document.add(header);
                numberOfRows = 0;
            } catch (DocumentException ex) {
                Logger.getLogger(CompetitionRanking.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);

            PdfPCell cellLeft = new PdfPCell(
                    new Phrase(sdf.format(new Date()),
                            FontFactory.getFont(FontFactory.HELVETICA, DEFAULT_FONT_SIZE)));
            cellLeft.setBorder(0);
            cellLeft.setBorderWidthTop(1f);
            table.addCell(cellLeft);

            PdfPCell cellCenter = new PdfPCell(
                    new Phrase("JTAF - Track and Field | www.jtaf.ch",
                            FontFactory.getFont(FontFactory.HELVETICA, DEFAULT_FONT_SIZE)));
            cellCenter.setBorder(0);
            cellCenter.setBorderWidthTop(1f);
            cellCenter.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cellCenter);

            PdfPCell cellRight = new PdfPCell(
                    new Phrase(I18n.getInstance().getString(locale, "Page")
                            + " " + document.getPageNumber(),
                            FontFactory.getFont(FontFactory.HELVETICA, DEFAULT_FONT_SIZE)));
            cellRight.setBorder(0);
            cellRight.setBorderWidthTop(1f);
            cellRight.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            table.addCell(cellRight);

            Rectangle page = document.getPageSize();
            table.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
            table.writeSelectedRows(0, 1, document.leftMargin(), document.bottomMargin(),
                    writer.getDirectContent());
        }

        private void addHeaderCellAlignLeft(PdfPTable table, String text) {
            PdfPCell cell = new PdfPCell(
                    new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, HEADER_FONT)));
            cell.setBorder(0);
            cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
            table.addCell(cell);
        }

        private void addHeaderCellAlignCenter(PdfPTable table, String text) {
            PdfPCell cell = new PdfPCell(
                    new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, HEADER_FONT)));
            cell.setBorder(0);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
            table.addCell(cell);
        }

        private void addHeaderCellAlignRight(PdfPTable table, String text) {
            PdfPCell cell = new PdfPCell(
                    new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, HEADER_FONT_SMALL)));
            cell.setBorder(0);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
            table.addCell(cell);
        }
    }
}
