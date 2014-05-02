package ch.jtaf.report;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Category;
import ch.jtaf.i18n.I18n;
import ch.jtaf.vo.CompetitionRankingCategoryVO;
import ch.jtaf.vo.CompetitionRankingVO;
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
import java.util.Locale;
import org.jboss.logging.Logger;

public class Diplomas extends AbstractReport {

    private static final float ATHLETE_FONT_SIZE = 12f;

    private Document document;
    private PdfWriter pdfWriter;
    private final byte[] logo;
    private final CompetitionRankingVO ranking;

    public Diplomas(CompetitionRankingVO ranking, byte[] logo, Locale locale) {
        super(locale);
        this.ranking = ranking;
        this.logo = logo;
    }

    public byte[] create() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            document = new Document(PageSize.A5, cmToPixel(1.5f), cmToPixel(1.5f), cmToPixel(1.5f), cmToPixel(1.5f));
            pdfWriter = PdfWriter.getInstance(document, baos);
            document.open();
            boolean first = true;
            for (CompetitionRankingCategoryVO cat : ranking.getCategories()) {
                int rank = 1;
                for (Athlete athlete : cat.getAthletes()) {
                    if (!first) {
                        document.newPage();
                    }
                    createTitle();
                    createLogo();
                    createCompetitionInfo();
                    createAthleteInfo(rank, athlete, cat.getCategory());
                    first = false;
                    rank++;
                }
            }
            document.close();
            pdfWriter.flush();
            return baos.toByteArray();
        } catch (DocumentException | IOException e) {
            Logger.getLogger(Diplomas.class).error(e.getMessage(), e);
            return new byte[0];
        }
    }

    private void createLogo() throws BadElementException, DocumentException, MalformedURLException, IOException {
        if (logo != null) {
            Image image = Image.getInstance(logo);
            image.setAbsolutePosition(cmToPixel(1f), cmToPixel(50f));
            image.scaleToFit(cmToPixel(12f), cmToPixel(12f));
            document.add(image);
        }
    }

    private void createAthleteInfo(int rank, Athlete athlete, Category category) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{2f, 10f, 10f, 3f, 1f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(2f));

        addCell(table, rank + ".", ATHLETE_FONT_SIZE);
        addCell(table, athlete.getLastName(), ATHLETE_FONT_SIZE);
        addCell(table, athlete.getFirstName(), ATHLETE_FONT_SIZE);
        addCell(table, athlete.getYear() + "", ATHLETE_FONT_SIZE);
        addCell(table, category.getAbbreviation(), ATHLETE_FONT_SIZE);

        document.add(table);
    }

    private void createTitle() throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(
                new Phrase(I18n.getInstance().getString(locale, "Diploma"),
                        FontFactory.getFont(FontFactory.HELVETICA, 60f)));
        cell.setBorder(0);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

        table.addCell(cell);
        document.add(table);
    }

    private void createCompetitionInfo() throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(12f));

        PdfPCell cell = new PdfPCell(
                new Phrase(ranking.getCompetition().getName() + " "
                        + sdf.format(ranking.getCompetition().getCompetitionDate()),
                        FontFactory.getFont(FontFactory.HELVETICA, 25f)));
        cell.setBorder(0);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

        table.addCell(cell);
        document.add(table);
    }
}
