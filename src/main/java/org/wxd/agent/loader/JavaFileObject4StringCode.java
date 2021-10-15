package org.wxd.agent.loader;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;

/**
 * @author: Troy.Chen(失足程序员, 15388152619)
 * @version: 2021-08-10 10:56
 **/
public class JavaFileObject4StringCode extends SimpleJavaFileObject {

    private static final long serialVersionUID = 1L;

    /**
     * 获取类源码文件里面的类名
     *
     * @param sourceCode 源码
     * @return 类的全名称
     */
    public static String readFullClassName(String sourceCode) {
        try (StringReader stringReader = new StringReader(sourceCode)) {
            try (BufferedReader br = new BufferedReader(stringReader)) {
                String className = "";
                while (br.ready()) {
                    String readLine = br.readLine();
                    readLine = readLine.trim();
                    if (readLine.startsWith("package") && readLine.endsWith(";")) {
                        className += readLine.substring(readLine.indexOf(" ") + 1, readLine.length() - 1);
                        className += ".";
                    }
                    if (readLine.contains("class") && readLine.endsWith("{")) {
                        String[] split = readLine.split(" ");
                        for (int i = 0; i < split.length; i++) {
                            String str = split[i];
                            if (str.contains("class")) {
                                className += split[i + 1];
                                return className;
                            }
                        }
                    }
                }
                throw new UnsupportedOperationException("并未找到的 类名");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 等待编译的源码字段
     */
    private String sourceCoder;

    /**
     * java源代码  StringJavaFileObject对象 的时候使用
     */
    public JavaFileObject4StringCode(String className, String contents) {
        super(URI.create("string:///" + className.replaceAll("\\.", "/") + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
        this.sourceCoder = contents;
    }

    /**
     * 字符串源码会调用该方法
     *
     * @param ignoreEncodingErrors
     * @return
     * @throws IOException
     */
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return sourceCoder;
    }

}
