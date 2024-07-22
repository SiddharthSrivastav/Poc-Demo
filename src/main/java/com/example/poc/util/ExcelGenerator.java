package com.example.poc.util;

import com.example.poc.entity.Report;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class ExcelGenerator {

        private List<Report> reportList;
        private XSSFWorkbook workbook;
        private XSSFSheet sheet;



    public ExcelGenerator(List <Report> reportList) {
            this.reportList = reportList;
            workbook = new XSSFWorkbook();
        }
        private void writeHeader() {
            sheet = workbook.createSheet("Report");
            Row row = sheet.createRow(0);
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            font.setFontHeight(16);
            style.setFont(font);
            createCell(row, 0, "Report ID", style);
            createCell(row, 1, "Department", style);
            createCell(row, 2, "Report Description", style);
            createCell(row, 3, "Report Created On", style);
        }
        private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
            sheet.autoSizeColumn(columnCount);
            Cell cell = row.createCell(columnCount);
            if (valueOfCell instanceof Integer) {
                cell.setCellValue((Integer) valueOfCell);
            } else if (valueOfCell instanceof Long) {
                cell.setCellValue((Long) valueOfCell);
            } else if (valueOfCell instanceof String) {
                cell.setCellValue((String) valueOfCell);
            } else {
                cell.setCellValue((Boolean) valueOfCell);
            }
            cell.setCellStyle(style);
        }
        private void write() {
            int rowCount = 1;
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontHeight(14);
            style.setFont(font);
            for (Report record: reportList) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;
                createCell(row, columnCount++, record.getReportId(), style);
                createCell(row, columnCount++, record.getDepartment(), style);
                createCell(row, columnCount++, record.getDescription(), style);
                createCell(row, columnCount++, record.getDateOfCreation().toString(), style);
            }
        }
        public void generateExcelFile(HttpServletResponse response) throws IOException {
            writeHeader();
            write();
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        }
}
