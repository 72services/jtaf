package ch.jtaf.control.reporting.report

import com.itextpdf.text.FontFactory
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import java.util.*

abstract class Ranking protected constructor(locale: Locale) : AbstractReport(locale) {

    protected fun addCategoryTitleCellWithColspan(table: PdfPTable, text: String, colspan: Int) {
        val cell = PdfPCell(Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12f)))
        cell.border = 0
        cell.colspan = colspan
        table.addCell(cell)
    }

    protected fun addResultsCell(table: PdfPTable, text: String) {
        val cell = PdfPCell(Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 7f)))
        cell.colspan = 5
        cell.border = 0
        cell.paddingBottom = 8f
        table.addCell(cell)
    }
}
