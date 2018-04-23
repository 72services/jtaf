package ch.jtaf.control.reporting.report

import ch.jtaf.entity.AthleteDTO
import com.itextpdf.text.Document
import com.itextpdf.text.FontFactory
import com.itextpdf.text.PageSize
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.util.*

class Numbers(private val athletes: List<AthleteDTO>, locale: Locale) : AbstractReport(locale) {

    private val fontSizeInfo = 12f
    private val fontSizeText = 90f

    fun create(): ByteArray {
        try {
            ByteArrayOutputStream().use { baos ->
                val document = Document(PageSize.A4, cmToPixel(1.5f), cmToPixel(1.7f), cmToPixel(0.8f), cmToPixel(0f))
                val pdfWriter = PdfWriter.getInstance(document, baos)
                document.open()
                var i = 0
                var number = 1
                var table = createMainTable()
                athletes.forEach {
                    if (i > 9) {
                        document.add(table)
                        document.newPage()
                        i = 0
                        table = createMainTable()
                    }
                    addAthleteInfo(table, it, number)
                    if (i % 2 == 0) {
                        addEmptyCell(table)
                    }
                    if (i == 1 || i == 3 || i == 5 || i == 7) {
                        addEmptyRow(table)
                    }
                    i++
                    number++
                }
                document.add(table)
                document.close()
                pdfWriter.flush()
                return baos.toByteArray()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ByteArray(0)
        }

    }

    private fun createMainTable(): PdfPTable {
        val table = PdfPTable(floatArrayOf(10f, 1.8f, 10f))
        table.widthPercentage = 100f
        return table
    }

    private fun addEmptyCell(table: PdfPTable) {
        val cellEmpty = PdfPCell(Phrase(""))
        cellEmpty.border = 0
        table.addCell(cellEmpty)
    }

    private fun addAthleteInfo(table: PdfPTable, athlete: AthleteDTO, number: Int) {
        val atable = PdfPTable(1)
        atable.widthPercentage = 100f

        val cellId = PdfPCell(Phrase(number.toString() + "", FontFactory.getFont(FontFactory.HELVETICA, fontSizeText)))
        cellId.border = 0
        cellId.minimumHeight = cmToPixel(2.5f)
        cellId.horizontalAlignment = PdfPCell.ALIGN_CENTER
        atable.addCell(cellId)

        var text = athlete.lastName + " " + athlete.firstName + "\n"
        text += athlete.category
        if (athlete.club != null) {
            text += " / " + athlete.club
        }

        val cellName = PdfPCell(Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSizeInfo)))
        cellName.border = 0
        cellName.minimumHeight = cmToPixel(1.8f)
        cellName.horizontalAlignment = PdfPCell.ALIGN_CENTER
        cellName.verticalAlignment = PdfPCell.ALIGN_MIDDLE
        atable.addCell(cellName)

        val cellTable = PdfPCell(atable)
        cellTable.border = 0

        table.addCell(cellTable)
    }

    private fun addEmptyRow(table: PdfPTable) {
        val cellId = PdfPCell(Phrase(" "))
        cellId.border = 0
        cellId.minimumHeight = cmToPixel(0.5f)
        cellId.horizontalAlignment = PdfPCell.ALIGN_CENTER
        table.addCell(cellId)

        addEmptyCell(table)

        val cellName = PdfPCell(Phrase(" "))
        cellName.border = 0
        cellName.minimumHeight = cmToPixel(0.5f)
        cellName.horizontalAlignment = PdfPCell.ALIGN_CENTER
        table.addCell(cellName)
    }

}
