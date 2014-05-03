package ch.jtaf.report;

import ch.jtaf.i18n.I18n;
import ch.jtaf.vo.ClubRankingVO;
import ch.jtaf.vo.ClubResultVO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import org.jboss.logging.Logger;

public class ClubRanking extends Ranking {

    private Document document;
    private PdfWriter pdfWriter;
    private final ClubRankingVO ranking;

    public ClubRanking(ClubRankingVO ranking, Locale locale) {
        super(locale);
        this.ranking = ranking;
    }

    public byte[] create() {
        try {
            byte[] ba;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                float border = cmToPixel(1.5f);
                document = new Document(PageSize.A4, border, border, border, border);
                pdfWriter = PdfWriter.getInstance(document, baos);
                pdfWriter.setPageEvent(new HeaderFooter(
                        I18n.getInstance().getString(locale, "Club Ranking"),
                        ranking.getSeries().getName(),
                        ""));
                document.open();
                createRanking();
                document.close();
                pdfWriter.flush();
                ba = baos.toByteArray();
            }

            return ba;
        } catch (DocumentException | IOException e) {
            Logger.getLogger(ClubRanking.class).error(e.getMessage(), e);
            return new byte[0];
        }
    }

    private void createRanking() throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{2f, 10f, 10f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(1f));

        int position = 1;
        for (ClubResultVO cr : ranking.getClubs()) {
            createClubRow(table, position, cr);
            position++;
        }
        document.add(table);
    }

    private void createClubRow(PdfPTable table, int position, ClubResultVO cr) throws DocumentException {
        addCell(table, position + ".");
        addCell(table, cr.getClub().getName());
        addCellAlignRight(table, "" + cr.getPoints());
    }

}
