package ch.jtaf.control.reporting.report

import ch.jtaf.entity.AthleteDTO
import ch.jtaf.entity.Category
import ch.jtaf.entity.Competition
import ch.jtaf.entity.EventType
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.util.*

class Sheets : AbstractReport {

    private val logger = LoggerFactory.getLogger(Sheets::class.java)

    private val fontSizeInfo = 8f
    private val fontSizeText = 16f
    private val infoLineHeight = 40f

    private var document: Document? = null
    private var pdfWriter: PdfWriter? = null
    private val competition: Competition?
    private val athletes: List<AthleteDTO>
    private val categories: List<Category>
    private val logo: ByteArray?

    constructor(athlete: AthleteDTO, categories: List<Category>, logo: ByteArray?, locale: Locale) : super(locale) {
        this.competition = null
        this.athletes = ArrayList()
        this.athletes.add(athlete)
        this.categories = categories
        this.logo = logo
    }

    constructor(competition: Competition, athlete: AthleteDTO, categories: List<Category>, logo: ByteArray?, locale: Locale) : super(locale) {
        this.competition = competition
        this.athletes = ArrayList()
        this.athletes.add(athlete)
        this.categories = categories
        this.logo = logo
    }

    constructor(competition: Competition, athletes: List<AthleteDTO>, categories: List<Category>, logo: ByteArray?, locale: Locale) : super(locale) {
        this.competition = competition
        this.athletes = athletes
        this.categories = categories
        this.logo = logo
    }

    fun create(): ByteArray {
        try {
            ByteArrayOutputStream().use { baos ->
                val oneCm = cmToPixel(1f)
                document = Document(PageSize.A5, oneCm, oneCm, cmToPixel(4.5f), oneCm)
                pdfWriter = PdfWriter.getInstance(document!!, baos)
                document!!.open()
                var first = true
                var number = 1
                athletes.forEach { athlete ->
                    if (!first) {
                        document!!.newPage()
                    }
                    createLogo()
                    createCategory(athlete)
                    createAthleteInfo(athlete, number)
                    createCompetitionRow()
                    createEventTable(categories.find { it.abbreviation == athlete.category }!!)
                    first = false
                    number++
                }
                document!!.close()
                pdfWriter!!.flush()
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
            image.setAbsolutePosition(cmToPixel(1f), cmToPixel(17.5f))
            image.scaleToFit(120f, 60f)
            document!!.add(image)
        }
    }

    private fun createCategory(athlete: AthleteDTO) {
        val table = PdfPTable(1)
        table.widthPercentage = 100f
        addCategoryCell(table, athlete.category)

        val page = document!!.pageSize
        table.totalWidth = page.width - document!!.leftMargin() - document!!.rightMargin()
        table.writeSelectedRows(0, 1, document!!.leftMargin(), cmToPixel(20.5f), pdfWriter!!.directContent)
    }

    private fun createAthleteInfo(athlete: AthleteDTO, number: Int) {
        val table = PdfPTable(2)
        table.widthPercentage = 100f
        table.spacingBefore = cmToPixel(1f)

        if (athlete.id != 0L) {
            addInfoCell(table, "" + number)
            addCell(table, athlete.id.toString())
        } else {
            addCell(table, " ")
            addCell(table, " ")
        }
        if (athlete.lastName == "") {
            addInfoCellWithBorder(table, "Last name")
        } else {
            addInfoCell(table, athlete.lastName)
        }
        if (athlete.firstName == "") {
            addInfoCellWithBorder(table, "First name")
        } else {
            addInfoCell(table, athlete.firstName)
        }
        if (athlete.yearOfBirth == 0) {
            addInfoCellWithBorder(table, "Year")
        } else {
            addInfoCell(table, athlete.yearOfBirth.toString())
        }
        if (athlete.club == null) {
            if (athlete.id == 0L) {
                addInfoCellWithBorder(table, "Club")
            } else {
                addInfoCell(table, "")
            }
        } else {
            addInfoCell(table, athlete.club)
        }

        document!!.add(table)
    }

    private fun createCompetitionRow() {
        val table = PdfPTable(1)
        table.widthPercentage = 100f
        table.spacingBefore = cmToPixel(0.5f)
        table.spacingAfter = cmToPixel(0.5f)

        addCompetitionCell(table, if (competition == null) "" else competition.name + " " + sdf.format(competition.competitionDate))

        document!!.add(table)
    }

    private fun createEventTable(category: Category) {
        val table = PdfPTable(4)
        table.widthPercentage = 100f
        table.spacingBefore = cmToPixel(1f)

        category.events.forEach {
            if (it.eventType.equals(EventType.JUMP_THROW)) {
                addInfoCell(table, it.name)
                addInfoCellWithBorder(table, "")
                addInfoCellWithBorder(table, "")
                addInfoCellWithBorder(table, "")
            } else {
                addInfoCellWithColspan(table, it.name, 3)
                addInfoCellWithBorder(table, "")
            }
        }

        document!!.add(table)
    }

    private fun addCategoryCell(table: PdfPTable, text: String) {
        val cell = PdfPCell(Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 80f)))
        cell.border = 0
        cell.horizontalAlignment = PdfPCell.ALIGN_RIGHT
        table.addCell(cell)
    }

    private fun addCompetitionCell(table: PdfPTable, text: String) {
        val cell = PdfPCell(Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, fontSizeText)))
        cell.border = 0
        table.addCell(cell)
    }

    private fun addInfoCell(table: PdfPTable, text: String) {
        val cell = PdfPCell(Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSizeText)))
        cell.border = 0
        cell.minimumHeight = infoLineHeight
        table.addCell(cell)
    }

    private fun addInfoCellWithColspan(table: PdfPTable, text: String, colspan: Int) {
        val cell = PdfPCell(Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSizeText)))
        cell.border = 0
        cell.colspan = colspan
        cell.minimumHeight = infoLineHeight
        table.addCell(cell)
    }

    private fun addInfoCellWithBorder(table: PdfPTable, text: String) {
        val cell = PdfPCell(Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSizeInfo)))
        cell.minimumHeight = infoLineHeight
        cell.borderWidth = 1f
        table.addCell(cell)
    }
}
