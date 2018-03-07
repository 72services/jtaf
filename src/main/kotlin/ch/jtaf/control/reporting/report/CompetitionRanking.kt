package ch.jtaf.control.reporting.report

import ch.jtaf.control.reporting.data.CompetitionRankingCategoryData
import ch.jtaf.control.reporting.data.CompetitionRankingData
import ch.jtaf.entity.AthleteWithResultsDTO
import ch.jtaf.entity.Category
import ch.jtaf.entity.Competition
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class CompetitionRanking(private val ranking: CompetitionRankingData, locale: Locale) : Ranking(locale) {

    private var document: Document? = null

    fun create(): ByteArray {
        try {
            ByteArrayOutputStream().use { baos ->
                val border = cmToPixel(1.5f)
                document = Document(PageSize.A4, border, border, border, border)
                val pdfWriter = PdfWriter.getInstance(document!!, baos)
                pdfWriter.pageEvent = HeaderFooter("Ranking", ranking.competition.name, sdf.format(ranking.competition.competitionDate))
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
            if (numberOfRows > 24) {
                document!!.newPage()
            }
            var table = createAthletesTable()
            createCategoryTitle(table, category.category)
            numberOfRows = numberOfRows + 2

            var rank = 1
            for (athlete in category.athletes) {
                if (numberOfRows > 23) {
                    document!!.add(table)
                    table = createAthletesTable()
                    document!!.newPage()
                }
                createAthleteRow(table, rank, athlete, calculateNumberOfMedals(category))
                rank++
                numberOfRows = numberOfRows + 1
            }
            document!!.add(table)
        }
    }

    private fun calculateNumberOfMedals(category: CompetitionRankingCategoryData): Int {
        var numberOfMedals = 0.0
        if (ranking.competition.medalPercentage > 0) {
            val percentage = ranking.competition.medalPercentage
            numberOfMedals = (category.athletes.size * (percentage / 100)).toDouble()
            if (numberOfMedals < 3 && ranking.competition.alwaysFirstThreeMedals) {
                numberOfMedals = 3.0
            }
        }
        return numberOfMedals.toInt()
    }

    private fun createAthletesTable(): PdfPTable {
        val table = PdfPTable(floatArrayOf(2f, 10f, 10f, 2f, 5f, 5f))
        table.widthPercentage = 100f
        table.spacingBefore = cmToPixel(0.6f)
        return table
    }

    private fun createCategoryTitle(table: PdfPTable, category: Category) {
        addCategoryTitleCellWithColspan(table, category.abbreviation, 1)
        addCategoryTitleCellWithColspan(table, category.name + " " + category.yearFrom + " - " + category.yearTo, 5)
        addCategoryTitleCellWithColspan(table, " ", 6)
    }

    private fun createAthleteRow(table: PdfPTable, rank: Int, athlete: AthleteWithResultsDTO, numberOfMedals: Int) {
        if (rank <= numberOfMedals) {
            addCell(table, "* $rank.")
        } else {
            addCell(table, rank.toString() + ".")
        }
        addCell(table, athlete.athlete.lastName)
        addCell(table, athlete.athlete.firstName)
        addCell(table, athlete.athlete.yearOfBirth.toString())
        addCell(table, if (athlete.athlete.club == null) "" else athlete.athlete.club!!.abbreviation)
        addCellAlignRight(table, calculateTotalPoints(ranking.competition).toString())

        val sb = StringBuilder()
        for (result in athlete.results) {
            sb.append(result.event!!.name)
            sb.append(": ")
            sb.append(result.result)
            sb.append(" (")
            sb.append(result.points)
            sb.append(") ")
        }
        addCell(table, "")
        addResultsCell(table, sb.toString())
    }

    private fun calculateTotalPoints(competition: Competition?): Int {
        // TODO
        return 0
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(CompetitionRanking::class.java)
    }

}
