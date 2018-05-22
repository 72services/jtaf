package ch.jtaf.control.reporting.report

import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfWriter
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

abstract class AbstractReport protected constructor(protected val locale: Locale) {

    protected val sdf = SimpleDateFormat("dd.MM.yyyy")
    protected var numberOfRows: Int = 0

    protected fun cmToPixel(cm: Float?): Float {
        return cm!! / CM_PER_INCH * DPI
    }

    protected fun addCell(table: PdfPTable, text: String) {
        val cell = PdfPCell(Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, DEFAULT_FONT_SIZE)))
        cell.border = 0
        table.addCell(cell)
    }

    protected fun addCell(table: PdfPTable, text: String, fontSize: Float) {
        val cell = PdfPCell(Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSize)))
        cell.border = 0
        table.addCell(cell)
    }

    protected fun addCellAlignRight(table: PdfPTable, text: String) {
        val cell = PdfPCell(Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, DEFAULT_FONT_SIZE)))
        cell.border = 0
        cell.horizontalAlignment = PdfPCell.ALIGN_RIGHT
        table.addCell(cell)
    }

    internal inner class HeaderFooter(left: String, middle: String, right: String) : PdfPageEventHelper() {
        private val header: PdfPTable = PdfPTable(floatArrayOf(2.5f, 4f, 1f))

        init {
            header.widthPercentage = 100f
            header.spacingBefore = cmToPixel(1f)

            addHeaderCellAlignLeft(header, left)
            addHeaderCellAlignCenter(header, middle)
            addHeaderCellAlignRight(header, right)
        }

        override fun onStartPage(writer: PdfWriter?, document: Document?) {
            try {
                document!!.add(header)
                numberOfRows = 0
            } catch (ex: DocumentException) {
                Logger.getLogger(CompetitionRanking::class.java.name).log(Level.SEVERE, null, ex)
            }

        }

        override fun onEndPage(writer: PdfWriter?, document: Document?) {
            val table = PdfPTable(3)
            table.widthPercentage = 100f

            val cellLeft = PdfPCell(Phrase(sdf.format(Date()), FontFactory.getFont(FontFactory.HELVETICA, DEFAULT_FONT_SIZE)))
            cellLeft.border = 0
            cellLeft.borderWidthTop = 1f
            table.addCell(cellLeft)

            val cellCenter = PdfPCell(Phrase("JTAF - Track and Field | www.jtaf.io sponsored by 72 Services LLC", FontFactory.getFont(FontFactory.HELVETICA, DEFAULT_FONT_SIZE)))
            cellCenter.border = 0
            cellCenter.borderWidthTop = 1f
            cellCenter.horizontalAlignment = PdfPCell.ALIGN_CENTER
            table.addCell(cellCenter)

            val cellRight = PdfPCell(Phrase("Page" + " " + document!!.pageNumber, FontFactory.getFont(FontFactory.HELVETICA, DEFAULT_FONT_SIZE)))
            cellRight.border = 0
            cellRight.borderWidthTop = 1f
            cellRight.horizontalAlignment = PdfPCell.ALIGN_RIGHT
            table.addCell(cellRight)

            val page = document.pageSize
            table.totalWidth = page.width - document.leftMargin() - document.rightMargin()
            table.writeSelectedRows(0, 1, document.leftMargin(), document.bottomMargin(), writer!!.directContent)
        }

        private fun addHeaderCellAlignLeft(table: PdfPTable, text: String) {
            val cell = PdfPCell(Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, HEADER_FONT)))
            cell.border = 0
            cell.verticalAlignment = PdfPCell.ALIGN_BOTTOM
            table.addCell(cell)
        }

        private fun addHeaderCellAlignCenter(table: PdfPTable, text: String) {
            val cell = PdfPCell(Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, HEADER_FONT)))
            cell.border = 0
            cell.horizontalAlignment = PdfPCell.ALIGN_CENTER
            cell.verticalAlignment = PdfPCell.ALIGN_BOTTOM
            table.addCell(cell)
        }

        private fun addHeaderCellAlignRight(table: PdfPTable, text: String) {
            val cell = PdfPCell(Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, HEADER_FONT_SMALL)))
            cell.border = 0
            cell.horizontalAlignment = PdfPCell.ALIGN_RIGHT
            cell.verticalAlignment = PdfPCell.ALIGN_BOTTOM
            table.addCell(cell)
        }

    }

    companion object {
        protected const val HEADER_FONT = 16f
        protected const val HEADER_FONT_SMALL = 12f

        protected const val DEFAULT_FONT_SIZE = 9f
        protected const val CM_PER_INCH = 2.54f
        protected const val DPI = 72f
    }
}
