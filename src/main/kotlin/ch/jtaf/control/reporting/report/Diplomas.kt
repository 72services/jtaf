package ch.jtaf.control.reporting.report

import ch.jtaf.control.reporting.data.CompetitionRankingData
import ch.jtaf.entity.Athlete
import ch.jtaf.entity.AthleteWithResultsDTO
import ch.jtaf.entity.Category
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Diplomas(private val ranking: CompetitionRankingData, private val logo: ByteArray?, locale: Locale) : AbstractReport(locale) {

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
                    for (athlete in cat.athletes) {
                        if (!first) {
                            document!!.newPage()
                        }
                        createTitle()
                        createLogo()
                        createCompetitionInfo()
                        createAthleteInfo(rank, athlete, cat.category)
                        first = false
                        rank++
                    }
                }
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

    @Throws(DocumentException::class, IOException::class)
    private fun createLogo() {
        if (logo != null) {
            val image = Image.getInstance(logo)
            image.scaleToFit(cmToPixel(11f), cmToPixel(11f))
            image.setAbsolutePosition((cmToPixel(14.85f) - image.scaledWidth) / 2,
                    (cmToPixel(11f) - image.scaledHeight) / 2 + cmToPixel(5.5f))
            document!!.add(image)
        }
    }

    @Throws(DocumentException::class)
    private fun createAthleteInfo(rank: Int, athlete: AthleteWithResultsDTO, category: Category) {
        val table = PdfPTable(floatArrayOf(2f, 10f, 10f, 3f, 2f))
        table.widthPercentage = 100f
        table.spacingBefore = cmToPixel(1.5f)

        addCell(table, rank.toString() + ".", ATHLETE_FONT_SIZE)
        addCell(table, athlete.athlete.lastName, ATHLETE_FONT_SIZE)
        addCell(table, athlete.athlete.firstName, ATHLETE_FONT_SIZE)
        addCell(table, athlete.athlete.yearOfBirth.toString(), ATHLETE_FONT_SIZE)
        addCell(table, category.abbreviation, ATHLETE_FONT_SIZE)

        document!!.add(table)
    }

    @Throws(DocumentException::class)
    private fun createTitle() {
        val table = PdfPTable(1)
        table.widthPercentage = 100f

        val cell = PdfPCell(Phrase("Diploma", FontFactory.getFont(FontFactory.HELVETICA, 60f)))
        cell.border = 0
        cell.horizontalAlignment = PdfPCell.ALIGN_CENTER

        table.addCell(cell)
        document!!.add(table)
    }

    @Throws(DocumentException::class)
    private fun createCompetitionInfo() {
        val table = PdfPTable(1)
        table.widthPercentage = 100f
        table.spacingBefore = cmToPixel(12f)

        var cell = PdfPCell(Phrase(ranking.competition.name, FontFactory.getFont(FontFactory.HELVETICA, 25f)))
        cell.border = 0
        cell.horizontalAlignment = PdfPCell.ALIGN_CENTER
        table.addCell(cell)

        cell = PdfPCell(Phrase(SDF.format(ranking.competition.competitionDate), FontFactory.getFont(FontFactory.HELVETICA, 25f)))
        cell.border = 0
        cell.horizontalAlignment = PdfPCell.ALIGN_CENTER
        table.addCell(cell)

        document!!.add(table)
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(Diplomas::class.java)

        private val SDF = SimpleDateFormat("d. MMMMM yyyy")
        private val ATHLETE_FONT_SIZE = 12f
    }
}
