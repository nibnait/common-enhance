package io.github.nibnait.common.utils;

import io.github.nibnait.common.bo.ExcelBOTest;
import io.github.nibnait.common.bo.email.EmailAccount;
import io.github.nibnait.common.bo.email.EmailBO;
import com.google.common.collect.Lists;
import org.junit.Test;

/**
 * Created by nibnait on 2022/01/26
 */
public class EmailUtilsTest {

    @Test
    public void sendEmail() throws Exception {
        EmailAccount emailAccount = new EmailAccount();
        emailAccount.setUsername("w@tianbin.org");
        emailAccount.setPassword("xxx");

        EmailBO emailBO = new EmailBO();
        emailBO.setFromNickName("电商 common-enhance");
        emailBO.setToAddress("x@bilibili.com");
        emailBO.setCcAddress("xx@bilibili.com");
        emailBO.setSubject("测试 excel");
        emailBO.setContent("测试 内容");
        emailBO.setAttachFiles(Lists.newArrayList(
                ExcelBOTest.createExcel().writeToFile("test", "target")
        ));

        EmailUtils.sendEmailThrowException(emailAccount, emailBO);
    }

}
