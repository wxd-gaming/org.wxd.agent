package org.wxd.tool.io;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: Troy.Chen(失足程序员, 15388152619)
 * @version: 2021-08-18 14:40
 **/
public class FileUtil implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String[] empty = new String[0];

    public static String getCanonicalPath(String fileName) throws IOException {
        return getCanonicalPath(file(fileName));
    }

    public static String getCanonicalPath(File fileName) throws IOException {
        return fileName.getCanonicalPath();
    }

    public static File file(String fileName) {
        return new File(fileName);
    }

    public static File file(URI uri) {
        return new File(uri);
    }

    public static File createFile(String fileName) throws IOException {
        return createFile(file(fileName));
    }

    public static File createFile(File file) throws IOException {
        return createFile(file, false);
    }

    public static File createFile(File file, boolean fugai) throws IOException {
        if (!fugai) {
            if (file.exists()) {
                /*如果文件已经存在，无需创建*/
                return file;
            }
        }
        file.createNewFile();
        return file;
    }

    public static Collection<File> lists(String dirPath, String... suffixs) throws Exception {
        return lists(file(dirPath), suffixs);
    }

    public static Collection<File> lists(File dir, String... suffixs) throws Exception {
        List<File> files = new ArrayList<>();
        loopFile(dir, false, (file) -> files.add(file), suffixs);
        return files;
    }

    public static Collection<File> loopLists(String dirPath, String... suffixs) throws Exception {
        return loopLists(file(dirPath), suffixs);
    }

    public static Collection<File> loopLists(File dir, String... suffixs) throws Exception {
        List<File> files = new ArrayList<>();
        loopFile(dir, true, (file) -> files.add(file), suffixs);
        return files;
    }

    public static void loopFile(File file, boolean loop, EConsumer<File> fileConsumer, String... suffixs) throws Exception {
        if (file.isFile()) {

            if (suffixs != null && suffixs.length > 0) {
                boolean check = false;
                for (String suffix : suffixs) {
                    if (file.getName().endsWith(suffix)) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    return;
                }
            }

            fileConsumer.accept(file);

        } else if (loop && file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            for (File listFile : listFiles) {
                loopFile(listFile, loop, fileConsumer, suffixs);
            }
        }
    }

}
