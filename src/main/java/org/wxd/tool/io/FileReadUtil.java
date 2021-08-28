package org.wxd.tool.io;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author: Troy.Chen(失足程序员, 15388152619)
 * @version: 2021-08-18 14:41
 **/
public class FileReadUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件夹当前下面文件内容
     *
     * @param file
     * @return
     */
    public static Map<String, byte[]> readListBytes(String file, String... suffixs) throws Exception {
        return readListBytes(FileUtil.file(file), suffixs);
    }

    public static Map<String, byte[]> readListBytes(File file, String... suffixs) throws Exception {
        Map<String, byte[]> bytesMap = new TreeMap<>();
        FileUtil.loopFile(file, false, (tmpFile) -> bytesMap.put(tmpFile.getName(), readBytes(tmpFile)));
        return bytesMap;
    }

    public static Map<String, byte[]> loopReadBytes(String file, String... suffixs) throws Exception {
        return loopReadBytes(FileUtil.file(file), suffixs);
    }

    public static Map<String, byte[]> loopReadBytes(File file, String... suffixs) throws Exception {
        Map<String, byte[]> bytesMap = new TreeMap<>();
        FileUtil.loopFile(file, true, (tmpFile) -> bytesMap.put(tmpFile.getName(), readBytes(tmpFile)));
        return bytesMap;
    }

    public static byte[] readBytes(String file) throws IOException {
        return readBytes(FileUtil.file(file));
    }

    public static byte[] readBytes(File file) throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(file);
        final long length = file.length();
        if (length >= Integer.MAX_VALUE) {
            throw new RuntimeException(file.getName() + "，文件太大；");
        }
        byte[] bytes = new byte[(int) length];
        fileInputStream.read(bytes);
        return bytes;
    }

}
