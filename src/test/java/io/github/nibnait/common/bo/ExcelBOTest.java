package io.github.nibnait.common.bo;

import io.github.nibnait.common.bo.excel.SheetBO;
import io.github.nibnait.common.bo.excel.WorkBookBO;
import com.google.common.collect.Lists;

/**
 * Created by nibnait on 2022/01/26
 */
public class ExcelBOTest {

    public static void main(String[] args) {
        createExcel();
    }

    public static WorkBookBO createExcel() {
        WorkBookBO workBookBO = new WorkBookBO();
        SheetBO sheetBO = workBookBO.getSheetList().get(0);
        sheetBO.appendRow(Lists.newArrayList("商品id", "商品名"));
        sheetBO.appendRow(Lists.newArrayList("123", "手办"));
        return workBookBO;
    }

}
