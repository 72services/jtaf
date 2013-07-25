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

    protected static final float DEFAULT_FONT_SIZE = 9f;
    protected static final float CM_PER_INCH = 2.54f;
    protected static final float DPI = 72f;
    protected SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd.MM.yyyy");

    protected float cmToPixel(Float cm) {
        return (cm / CM_PER_INCH) * DPI;
    }

    protected void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, DEFAULT_FONT_SIZE)));
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
            header = new PdfPTable(3);
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
            } catch (DocumentException ex) {
                Logger.getLogger(CompetitionRanking.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            addCell(table, "Created by jtaf.ch");
            addCellAlignRight(table, "Page " + document.getPageNumber());

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
