package ch.jtaf.report;

import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.Locale;

public abstract class Ranking extends AbstractReport {

    protected Ranking(Locale locale) {
        super(locale);
    }

    protected void addCategoryTitleCellWithColspan(PdfPTable table, String text, int colspan) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12f)));
        cell.setBorder(0);
        cell.setColspan(colspan);
        table.addCell(cell);
    }

    protected void addResultsCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 7f)));
        cell.setColspan(5);
        cell.setBorder(0);
        cell.setPaddingBottom(8f);
        table.addCell(cell);
    }
}
