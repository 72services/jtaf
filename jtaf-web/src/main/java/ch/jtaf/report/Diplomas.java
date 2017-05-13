package ch.jtaf.report;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Category;
import ch.jtaf.i18n.I18n;
import ch.jtaf.vo.CompetitionRankingCategoryVO;
import ch.jtaf.vo.CompetitionRankingVO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.jboss.logging.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Diplomas extends AbstractReport {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("d. MMMMM yyyy");
    private static final float ATHLETE_FONT_SIZE = 12f;

    private Document document;
    private final byte[] logo;
    private final CompetitionRankingVO ranking;

    public Diplomas(CompetitionRankingVO ranking, byte[] logo, Locale locale) {
        super(locale);
        this.ranking = ranking;
        this.logo = logo;
    }

    public byte[] create() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            document = new Document(PageSize.A5, cmToPixel(1.5f), cmToPixel(1.5f), cmToPixel(1f), cmToPixel(1.5f));
            PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);
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

    private void createLogo() throws DocumentException, IOException {
        if (logo != null) {
            Image image = Image.getInstance(logo);
            image.scaleToFit(cmToPixel(11f), cmToPixel(11f));
            image.setAbsolutePosition((cmToPixel(14.85f) - image.getScaledWidth()) / 2,
                    (cmToPixel(11f) - image.getScaledHeight()) / 2 + cmToPixel(5.5f));
            document.add(image);
        }
    }

    private void createAthleteInfo(int rank, Athlete athlete, Category category) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{2f, 10f, 10f, 3f, 2f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(1.5f));

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
                new Phrase(ranking.getCompetition().getName(),
                        FontFactory.getFont(FontFactory.HELVETICA, 25f)));
        cell.setBorder(0);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(
                new Phrase(SDF.format(ranking.getCompetition().getCompetitionDate()),
                        FontFactory.getFont(FontFactory.HELVETICA, 25f)));
        cell.setBorder(0);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        document.add(table);
    }
}
