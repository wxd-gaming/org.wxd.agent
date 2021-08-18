package org.example.tool.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author: Troy.Chen(失足程序员, 15388152619)
 * @version: 2021-08-18 15:47
 **/
public class FileWriteUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void writeString(String fileName, String content) throws IOException {
        final File file = FileUtil.file(fileName);
        writeString(file, content);
    }

    public static void writeString(File file, String content) throws IOException {
        writeBytes(file, content.getBytes(StandardCharsets.UTF_8), false);
    }

    public static void writeBytes(String fileName, byte[] bytes) throws IOException {
        final File file = FileUtil.file(fileName);
        writeBytes(file, bytes);
    }

    public static void writeBytes(File file, byte[] bytes) throws IOException {
        writeBytes(file, bytes, false);
    }

    public static void writeBytes(File file, byte[] bytes, boolean append) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, append)) {
            fileOutputStream.write(bytes);
        }
    }
}
