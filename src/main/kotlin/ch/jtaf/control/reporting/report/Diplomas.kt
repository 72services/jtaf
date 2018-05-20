package ch.jtaf.control.reporting.report

import ch.jtaf.control.reporting.data.CompetitionRankingData
import ch.jtaf.entity.AthleteWithResultsDTO
import ch.jtaf.entity.Category
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class Diplomas(private val ranking: CompetitionRankingData, private val logo: ByteArray?, locale: Locale) : AbstractReport(locale) {

    private val logger = LoggerFactory.getLogger(Diplomas::class.java)

    private val simpleDateFormat = SimpleDateFormat("d. MMMMM yyyy")
    private val athleteFontSize = 12f

    private var document: Document? = null

    @Throws(DocumentException::class)
    fun create(): ByteArray {
        try {
            ByteArrayOutputStream().use { baos ->
                document = Document(PageSize.A5, cmToPixel(1.5f), cmToPixel(1.5f), cmToPixel(1f), cmToPixel(1.5f))
                val pdfWriter = PdfWriter.getInstance(document!!, baos)
                document!!.open()
                var first = true
                for (cat in ranking.categories) {
                    var rank = 1
                    cat.getAthletesSortedByPointsDesc().forEach {
                        if (!first) {
                            document!!.newPage()
                        }
                        createTitle()
                        createLogo()
                        createCompetitionInfo()
                        createAthleteInfo(rank, it, cat.category)
                        first = false
                        rank++
                    }
                }
                document!!.close()
                pdfWriter.flush()
                return baos.toByteArray()
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
            return ByteArray(0)
        }

    }

    private fun createLogo() {
        if (logo != null) {
            val image = Image.getInstance(logo)
            image.scaleToFit(cmToPixel(11f), cmToPixel(11f))
            image.setAbsolutePosition((cmToPixel(14.85f) - image.scaledWidth) / 2,
                    (cmToPixel(11f) - image.scaledHeight) / 2 + cmToPixel(5.5f))
            document!!.add(image)
        }
    }

    private fun createAthleteInfo(rank: Int, athlete: AthleteWithResultsDTO, category: Category) {
        val table = PdfPTable(floatArrayOf(2f, 10f, 10f, 3f, 2f))
        table.widthPercentage = 100f
        table.spacingBefore = cmToPixel(1.5f)

        addCell(table, rank.toString() + ".", athleteFontSize)
        addCell(table, athlete.athlete.lastName, athleteFontSize)
        addCell(table, athlete.athlete.firstName, athleteFontSize)
        addCell(table, athlete.athlete.yearOfBirth.toString(), athleteFontSize)
        addCell(table, category.abbreviation, athleteFontSize)

        document!!.add(table)
    }

    private fun createTitle() {
        val table = PdfPTable(1)
        table.widthPercentage = 100f

        val cell = PdfPCell(Phrase("Diploma", FontFactory.getFont(FontFactory.HELVETICA, 60f)))
        cell.border = 0
        cell.horizontalAlignment = PdfPCell.ALIGN_CENTER

        table.addCell(cell)
        document!!.add(table)
    }

    private fun createCompetitionInfo() {
        val table = PdfPTable(1)
        table.widthPercentage = 100f
        table.spacingBefore = cmToPixel(12f)

        var cell = PdfPCell(Phrase(ranking.competition.name, FontFactory.getFont(FontFactory.HELVETICA, 25f)))
        cell.border = 0
        cell.horizontalAlignment = PdfPCell.ALIGN_CENTER
        table.addCell(cell)

        cell = PdfPCell(Phrase(simpleDateFormat.format(ranking.competition.competitionDate), FontFactory.getFont(FontFactory.HELVETICA, 25f)))
        cell.border = 0
        cell.horizontalAlignment = PdfPCell.ALIGN_CENTER
        table.addCell(cell)

        document!!.add(table)
    }

}
