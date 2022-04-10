package io.github.nibnait.common.bo.email;

import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * Created by nibnait on 2021/01/22
 */
@Data
public class EmailBO {

    /**
     * 发件人 昵称
     */
    private String fromNickName;

    private String toAddress;

    private String ccAddress;

    private String subject;

    private String content;

    private List<File> attachFiles;
}
