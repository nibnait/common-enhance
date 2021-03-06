package io.github.nibnait.common.bo.excel;

import com.google.common.collect.Lists;
import io.github.nibnait.common.constants.CommonConstants;
import io.github.nibnait.common.utils.date.DateUtils;
import io.github.nibnait.common.utils.file.EnhanceFileUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nibnait on 2021/01/21
 */
@Data
@Slf4j
public class WorkBookBO {

    private HSSFWorkbook workbook;

    private List<SheetBO> sheetList = new ArrayList<>();

    public WorkBookBO() {
        workbook = new HSSFWorkbook();
        sheetList.add(new SheetBO(workbook.createSheet("Sheet1")));
    }

    public WorkBookBO(List<List<String>> sheetTitle, String... sheetName) {
        workbook = new HSSFWorkbook();
        initSheetList(sheetTitle, Lists.newArrayList(sheetName));
    }

    public WorkBookBO(List<List<String>> sheetTitle, List<String> sheetNameList) {
        workbook = new HSSFWorkbook();
        initSheetList(sheetTitle, sheetNameList);
    }

    private void initSheetList(List<List<String>> sheetTitle, List<String> sheetNameList) {
        if (CollectionUtils.isEmpty(sheetTitle) || CollectionUtils.isEmpty(sheetNameList)
                || sheetTitle.size() != sheetNameList.size()) {
            // 直接创建一个默认的sheet
            sheetList.add(new SheetBO(workbook.createSheet("Sheet1")));
            return;
        }

        for (int i = 0; i < sheetTitle.size(); i++) {
            HSSFSheet sheet = workbook.createSheet(sheetNameList.get(i));
            SheetBO sheetBO = new SheetBO(sheet);
            sheetBO.appendRow(sheetTitle.get(i));
            sheetList.add(sheetBO);
        }
    }

    /**
     * 将 workbook 写成文件
     */
    public File writeToDefaultLocalPath(String fileName) {
        return writeToFile(CommonConstants.DEFAULT_LOCAL_FILE_PATH, fileName);
    }

    public File writeToFile(String filePath, String fileName) {
        if (StringUtils.isBlank(fileName)) {
            fileName = String.valueOf(DateUtils.currentTimeMillis());
        }

        fileName = filePath + CommonConstants.SLASH_SEPARATOR + fileName + CommonConstants.SUFFIX_XLS;
        File file = new File(fileName);

        try {
            file = EnhanceFileUtils.createIfNecessary(fileName);
            workbook.write(file);
            workbook.close();
        } catch (Exception e) {
            log.error("WorkBookBO writeToFile error ", e);
        }

        return file;
    }

}
