package ch.jtaf.control.reporting.report

import ch.jtaf.control.reporting.data.EventsRankingData
import ch.jtaf.control.reporting.data.EventsRankingEventData
import ch.jtaf.entity.Result
import com.itextpdf.text.Document
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.util.*

class EventsRanking(private val ranking: EventsRankingData, locale: Locale) : Ranking(locale) {

    private val logger = LoggerFactory.getLogger(EventsRanking::class.java)

    private var document: Document? = null

    fun create(): ByteArray {
        try {
            ByteArrayOutputStream().use { baos ->
                val border = cmToPixel(1.5f)
                document = Document(PageSize.A4, border, border, border, border)
                val pdfWriter = PdfWriter.getInstance(document!!, baos)
                pdfWriter.pageEvent = HeaderFooter("Event Ranking", ranking.competition.name, sdf.format(ranking.competition.competitionDate))
                document!!.open()
                createRanking()
                document!!.close()
                pdfWriter.flush()
                return baos.toByteArray()
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
            return ByteArray(0)
        }
    }

    private fun createRanking() {
        ranking.events.forEach {
            val table = PdfPTable(floatArrayOf(2f, 10f, 10f, 2f, 2f, 5f, 5f))
            table.widthPercentage = 100f
            table.spacingBefore = cmToPixel(1f)
            table.keepTogether = true

            createEventTitle(table, it)

            var position = 1
            it.athletes.forEach {
                createAthleteRow(table, position, it)
                position++
            }
            document!!.add(table)
        }
    }

    private fun createEventTitle(table: PdfPTable, event: EventsRankingEventData) {
        addCategoryTitleCellWithColspan(table, event.event.name + " / " + event.event.gender, 7)

        addCategoryTitleCellWithColspan(table, " ", 7)
    }

    private fun createAthleteRow(table: PdfPTable, position: Int, result: Result) {
        addCell(table, position.toString() + ".")
        addCell(table, result.athlete!!.lastName)
        addCell(table, result.athlete!!.firstName)
        addCell(table, result.athlete!!.yearOfBirth.toString())
        addCell(table, result.category!!.abbreviation)
        addCell(table, if (result.athlete!!.club == null) "" else result.athlete!!.club?.abbreviation!!)
        addCellAlignRight(table, result.result)
    }

}
