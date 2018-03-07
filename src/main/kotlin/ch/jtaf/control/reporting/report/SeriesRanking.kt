package ch.jtaf.control.reporting.report

import ch.jtaf.control.reporting.data.SeriesRankingCategoryData
import ch.jtaf.control.reporting.data.SeriesRankingData
import ch.jtaf.entity.AthleteWithResultsDTO
import ch.jtaf.entity.Competition
import ch.jtaf.entity.Result
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class SeriesRanking(private val ranking: SeriesRankingData, locale: Locale) : Ranking(locale) {

    private var document: Document? = null

    fun create(): ByteArray {
        try {
            ByteArrayOutputStream().use { baos ->
                document = Document(PageSize.A4)
                val pdfWriter = PdfWriter.getInstance(document!!, baos)
                pdfWriter.pageEvent = HeaderFooter("Series Ranking", ranking.series.name, "")
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
        for (category in ranking.categories) {
            if (numberOfRows > 22) {
                document!!.newPage()
            }
            var table = createAthletesTable()
            createCategoryTitle(table, category)
            numberOfRows = numberOfRows + 2

            var position = 1
            for (athlete in category.athletes) {
                if (numberOfRows > 22) {
                    document!!.add(table)
                    document!!.newPage()
                    table = createAthletesTable()
                }
                createAthleteRow(table, position, athlete)
                position++
                numberOfRows = numberOfRows + 1
            }
            document!!.add(table)
        }
    }

    private fun createAthletesTable(): PdfPTable {
        val table = PdfPTable(floatArrayOf(2f, 10f, 10f, 2f, 5f, 5f))
        table.widthPercentage = 100f
        table.spacingBefore = cmToPixel(1f)
        return table
    }

    private fun createCategoryTitle(table: PdfPTable, category: SeriesRankingCategoryData) {
        addCategoryTitleCellWithColspan(table, category.category.abbreviation, 1)
        addCategoryTitleCellWithColspan(table, category.category.name + " "
                + category.category.yearFrom + " - " + category.category.yearTo, 5)

        addCategoryTitleCellWithColspan(table, " ", 6)
    }

    private fun createAthleteRow(table: PdfPTable, position: Int, athlete: AthleteWithResultsDTO) {
        addCell(table, position.toString() + ".")
        addCell(table, athlete.athlete.lastName)
        addCell(table, athlete.athlete.firstName)
        addCell(table, athlete.athlete.yearOfBirth.toString())
        addCell(table, if (athlete.athlete.club == null) "" else athlete.athlete.club!!.abbreviation)
        addCellAlignRight(table, getSeriesPoints(athlete.results).toString())

        val sb = StringBuilder()
        for (competition in ranking.series.competitions) {
            sb.append(competition.name)
            sb.append(": ")
            sb.append(getCompetitionPoints(athlete.results, competition))
            sb.append(" ")
        }
        addCell(table, "")
        addResultsCell(table, sb.toString())
    }

    private fun getCompetitionPoints(results: List<Result>, competition: Competition): Int {
        // TODO
        return 0
    }

    private fun getSeriesPoints(results: List<Result>): Int {
        // TODO
        return 0
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(SeriesRanking::class.java)
    }
}
