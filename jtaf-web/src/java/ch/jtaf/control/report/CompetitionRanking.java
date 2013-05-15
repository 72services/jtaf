package ch.jtaf.control.report;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.CompetitionRankingTO;
import ch.jtaf.entity.RankingCategoryTO;
import ch.jtaf.entity.Result;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

public class CompetitionRanking {
    
    private static final float CM_PER_INCH = 2.54f;
    private static final float DPI = 72f;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private Document document;
    private PdfWriter pdfWriter;
    private final CompetitionRankingTO ranking;
    
    public CompetitionRanking(CompetitionRankingTO ranking) {
        this.ranking = ranking;
    }
    
    public byte[] create() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document = new Document(PageSize.A5);
            pdfWriter = PdfWriter.getInstance(document, baos);
            
            document.open();
            
            createHeader();
            createRanking();
            
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
    
    private void createHeader() throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(1f));
        
        addCell(table, "Ranking");
        addCell(table, ranking.getCompetition().getName());
        addCell(table, sdf.format(ranking.getCompetition().getCompetitionDate()));
        
        document.add(table);
    }
    
    private void createRanking() throws DocumentException {
        for (RankingCategoryTO category : ranking.getCategories()) {
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(cmToPixel(1f));
            
            createCategoryTitle(table, category);
            int position = 1;
            for (Athlete athlete : category.getAthletes()) {
                createRow(table, position, athlete);
                position++;
            }
            document.add(table);
        }
    }
    
    private void createCategoryTitle(PdfPTable table, RankingCategoryTO category) {
        addCell(table, category.getCategory().getAbbreviation());
        addCell(table, category.getCategory().getName());
        addCell(table, category.getCategory().getYearFrom() + " - " + category.getCategory().getYearTo(), 4);
    }
    
    private void createRow(PdfPTable table, int position, Athlete athlete) throws DocumentException {
        addCell(table, position + ".");
        addCell(table, athlete.getLastName());
        addCell(table, athlete.getFirstName());
        addCell(table, athlete.getYear() + "");
        addCell(table, athlete.getClub() == null ? "" : athlete.getClub().getAbbreviation());
        addCell(table, athlete.getTotalPoints() + "", false);
        
        addCell(table, "");
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (Result result : athlete.getResults()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(result.getEvent().getName());
            sb.append(": ");
            sb.append(result.getResult());
            sb.append(" (");
            sb.append(result.getPoints());
            sb.append(" )");
            first = false;
        }
        addResultsCell(table, sb.toString());
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
    
    private void addCell(PdfPTable table, String text, boolean left) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 9f)));
        cell.setBorder(0);
        if (!left) {
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        }
        table.addCell(cell);
    }
    
    private void addCell(PdfPTable table, String text, int colspan) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 9f)));
        cell.setColspan(colspan);
        cell.setBorder(0);
        table.addCell(cell);
    }
    
    private void addResultsCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 9f)));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);
    }
}
