package io.github.nibnait.common.utils;

import io.github.nibnait.common.bo.email.EmailAccount;
import io.github.nibnait.common.bo.email.EmailBO;
import io.github.biezhi.ome.OhMyEmail;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * Created by nibnait on 2022/01/26
 */
public class EmailUtils {

    private static final String SENDER_NAME = "common-enhance";

    private static void initEmailCfg(EmailAccount emailAccount) {
        Properties props = new Properties();
        props.setProperty("username", emailAccount.getUsername());
        props.setProperty("password", emailAccount.getPassword());
        // 开启debug调试
        props.setProperty("mail.debug", "false");  //false
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.partner.outlook.cn");
        props.setProperty("mail.smtp.host", "smtp.partner.outlook.cn");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        OhMyEmail.config(props);
    }


    public static void sendEmailThrowException(EmailAccount emailAccount, EmailBO emailBO) throws Exception {
        OhMyEmail.config(OhMyEmail.SMTP_ENT_QQ(false), emailAccount.getUsername(), emailAccount.getPassword());

        if (StringUtils.isBlank(emailBO.getToAddress()) || StringUtils.isBlank(emailBO.getSubject())) {
            return;
        }

        initEmailCfg(emailAccount);

        String fromNickName = emailBO.getFromNickName();
        if (StringUtils.isBlank(fromNickName)) {
            fromNickName = SENDER_NAME;
        }

        OhMyEmail ohMyEmail = OhMyEmail.subject(emailBO.getSubject())
                .from(fromNickName)
                .to(emailBO.getToAddress());

        if (StringUtils.isNotBlank(emailBO.getCcAddress())) {
            ohMyEmail.cc(emailBO.getCcAddress());
        }
        List<File> files = emailBO.getAttachFiles();
        if (files != null && !files.isEmpty()) {
            for (File file : files) {
                ohMyEmail.attach(file);
            }
        }

        ohMyEmail.html(emailBO.getContent());
        ohMyEmail.send();

    }

}
