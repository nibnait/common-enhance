package com.java.common.bo.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * Created by nibnait on 2021/01/21
 */
public class SheetBO {

    private final HSSFSheet sheet;
    private Integer rowIndex;

    public SheetBO(HSSFSheet sheet) {
        this.sheet = sheet;
        rowIndex = 0;
    }

    private void formatCellStyle(HSSFCellStyle cellStyle) {
//        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    public void appendRow(List<String> row) {
        HSSFRow hssfRow = sheet.createRow(rowIndex);
        HSSFCell hssfCell;
        rowIndex += 1;
        for (int i = 0; i < row.size(); i++) {
            hssfCell = hssfRow.createCell(i);
            // set value
            String value = row.get(i);
            hssfCell.setCellValue(value);

            HSSFCellStyle cellStyle = hssfCell.getCellStyle();
            // wrap text, 居中
            formatCellStyle(cellStyle);
            // 自适应列宽
            sheet.autoSizeColumn(i);
            //手动调整列宽，解决中文不能自适应问题
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 12 / 10);
        }

    }

    public void addMergedRegion(int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

}
