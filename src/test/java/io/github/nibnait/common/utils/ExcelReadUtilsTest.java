package io.github.nibnait.common.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.List;

/**
 * Created by nibnait on 2022/03/07
 */
@Slf4j
public class ExcelReadUtilsTest {

    @Data
    public static class NFT {
        private String sourceAddress;
        private String linkId;
        private Integer stock;
        private String specKey1;
        private String specKey2;
        private String specKey3;
    }

    @Test
    public void testReadByModule() {
        try {
            // 获取文件流
            String path = "/Users/nibnait/Desktop/";
            FileInputStream inputStream = new FileInputStream(path + "nft.xlsx");

            List<NFT> read = ExcelReadUtils.read(inputStream, NFT.class);
            System.out.println(DataUtils.toJsonStringArray(read));
        } catch (Exception e) {
//            log.error("testReadByModule ", e);
        }
    }

    @Test
    public void testReadByWorkbook() {
        try {
            // 获取文件流
            String path = "/Users/nibnait/Desktop/";
            FileInputStream inputStream = new FileInputStream(path + "nft_干杯.xlsx");

            Workbook workbook = ExcelReadUtils.convert2Workbook(inputStream);

            // 获取标题内容
            Sheet sheet = workbook.getSheetAt(0);
            List<String> title = ExcelReadUtils.getTitle(sheet);
            System.out.println(title);

            // 获取表中的内容
            int rowCount = sheet.getPhysicalNumberOfRows();
            for (int rowNum = 1; rowNum < rowCount; rowNum++) {
                List<String> rowData = ExcelReadUtils.getRowData(sheet, rowNum);
                System.out.println(rowData);
            }
        } catch (Exception e) {
//            log.error("testReadByWorkbook ", e);
        }
    }


}
