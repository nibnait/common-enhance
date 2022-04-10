package cc.tianbin.common.utils;

import cc.tianbin.common.exception.ClientViewException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * Created by nibnait on 2022/03/02
 */
@Slf4j
public class EnhanceFileUtils {

    public static File createIfNecessary(String fileFullPathName) throws IOException {
        File file = new File(fileFullPathName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            if (!file.createNewFile()) {
                throw new ClientViewException("文件上传失败");
            }
        }
        return file;
    }


    /**
     * 将InputStream写入本地文件
     * @param fileFullPathName 文件 全路径名
     * @param input	输入流
     */
    public static void writeToFile(String fileFullPathName, InputStream input) throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(fileFullPathName);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();
        input.close();
    }

    /**
     * inputStream 转为 byte[]
     */
    public static byte[] convert2ByteArray(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc;
        while ((rc = inStream.read(buff, 0, 1024)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();// in_b为转换之后的结果
    }
}
