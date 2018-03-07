package ch.jtaf.control.reporting.report

import ch.jtaf.control.reporting.data.ClubRankingData
import ch.jtaf.control.reporting.data.ClubResultData
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class ClubRanking(private val ranking: ClubRankingData, locale: Locale) : Ranking(locale) {

    private var document: Document? = null

    fun create(): ByteArray {
        try {
            ByteArrayOutputStream().use { baos ->
                val border = cmToPixel(1.5f)
                document = Document(PageSize.A4, border, border, border, border)
                val pdfWriter = PdfWriter.getInstance(document!!, baos)
                pdfWriter.pageEvent = HeaderFooter("Club Ranking", ranking.series.name, "")
                document!!.open()
                createRanking()
                document!!.close()
                pdfWriter.flush()
                return baos.toByteArray()
            }
        } catch (e: DocumentException) {
            LOGGER.error(e.message, e)
            return ByteArray(0)
        } catch (e: IOException) {
            LOGGER.error(e.message, e)
            return ByteArray(0)
        }

    }

    @Throws(DocumentException::class)
    private fun createRanking() {
        val table = PdfPTable(floatArrayOf(2f, 10f, 10f))
        table.widthPercentage = 100f
        table.spacingBefore = cmToPixel(1f)

        var position = 1
        for (cr in ranking.clubs) {
            createClubRow(table, position, cr)
            position++
        }
        document!!.add(table)
    }

    private fun createClubRow(table: PdfPTable, position: Int, cr: ClubResultData) {
        addCell(table, position.toString() + ".")
        addCell(table, cr.club.name)
        addCellAlignRight(table, "" + cr.points)
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(ClubRanking::class.java)
    }

}
