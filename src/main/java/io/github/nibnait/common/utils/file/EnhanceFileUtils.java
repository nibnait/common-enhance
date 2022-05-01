package io.github.nibnait.common.utils.file;

import io.github.nibnait.common.exception.ClientViewException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by nibnait on 2022/03/02
 */
@Slf4j
public class EnhanceFileUtils {

    /**
     * 创建文件
     */
    public static File createIfNecessary(String fileFullPathName) throws IOException {
        File file = new File(fileFullPathName);
        if (!file.exists()) {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                throw new ClientViewException("{} 文件夹创建失败", file.getParentFile().getAbsolutePath());
            }
            if (!file.createNewFile()) {
                throw new ClientViewException("{} 文件创建失败", file.getAbsolutePath());
            }
        }
        return file;
    }

    /**
     * 删除文件 或 跟文件夹下的所有文件
     */
    public static boolean deleteIfExists(String fileFullPathName) throws IOException {
        File file = new File(fileFullPathName);
        return delete(file);
    }

    private static boolean delete(File file) throws IOException {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            assert files != null;
            for (File f : files) {
                delete(f);
            }
        }
        return Files.deleteIfExists(Paths.get(file.getPath()));
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
