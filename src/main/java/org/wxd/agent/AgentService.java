package org.wxd.agent;

import lombok.extern.slf4j.Slf4j;
import org.wxd.agent.loader.ClassFileLoader;
import org.wxd.agent.loader.ClassInfo;
import org.wxd.agent.loader.JarFileLoader;
import org.wxd.agent.tool.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态替换class类，只能修改方法体
 *
 * @author: 特别鸣谢 上海-念念（qq:596889735）
 * @author: Troy.Chen(無心道, 15388152619)
 * @version: 2020-12-29 20:24
 **/
@Slf4j
public class AgentService implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String Full_Class_Name = "com.sun.tools.attach.VirtualMachine";

    public static String jdkHome() {
        return System.getProperty("java.ext.dirs");
    }

    public static String jreHome() {
        return System.getProperty("java.home");
    }

    /**
     * @param agentJarPath agent Jar 路径
     * @param agentPath    执行热加载的class文件目录
     * @throws Exception
     */
    public static void agentClass(String agentJarPath, String agentPath) throws Throwable {
        check(agentJarPath);
        com.sun.tools.attach.VirtualMachine vm = com.sun.tools.attach.VirtualMachine.attach(getPid());
        vm.loadAgent(agentJarPath, agentPath);
        vm.detach();
    }

    public static void check(String agentJarPath) throws IOException {
             /*
                注意，是jre的bin目录，不是jdk的bin目录，
                VirtualMachine need the attach.dll in the jre of the JDK.
            */
        String jreHome = jreHome();
        log.warn("java_home：" + jreHome);
        String jar_bin;
        if (jreHome.toLowerCase().endsWith("jre")) {
            jar_bin = FileUtil.getCanonicalPath(new File(jreHome + File.separator + ".." + File.separator + "lib" + File.separator + "tools.jar"));
        } else {
            jar_bin = FileUtil.getCanonicalPath(new File(jreHome + File.separator + "lib" + File.separator + "tools.jar"));
        }
        log.warn("java_home：" + jar_bin);
        try {
            JarFileLoader.addJar2UrlClassLoader(Full_Class_Name, jar_bin);
        } catch (Exception e) {
            throw new RuntimeException("启动异常，找不到：" + Full_Class_Name, e);
        }
        final File file = new File(agentJarPath);
        if (!file.exists()) {
            throw new RuntimeException("agent jar 无法找到：" + agentJarPath);
        }
    }

    /**
     * agentArgs就是VirtualMachine.loadAgent()的第二个参数
     *
     * @param sourceDir 代理调用的热更java文件目录
     * @param inst      热更代理
     */
    public static void agentmain(String sourceDir, Instrumentation inst) throws Throwable {

        final Class[] allLoadedClasses = inst.getAllLoadedClasses();
        /*解析原始加载类*/
        Map<String, Class<?>> loadClassMap = new HashMap<>();
        for (Class loadedClass : allLoadedClasses) {
            loadClassMap.put(loadedClass.getName(), loadedClass);
        }
        ClassFileLoader classFileloader = new ClassFileLoader(sourceDir);

        classFileloader.getClassBytesLoader().setClassesMap(loadClassMap);

        Collection<ClassInfo> classes = classFileloader.toAllClass();

        for (ClassInfo classInfo : classes) {
            /*这一步很关键，必须是从原始的classloader 获取原始的类*/
            Class<?> oldLoadClass = loadClassMap.get(classInfo.getLoadClassClassName());
            if (oldLoadClass == null) {
                log.warn("需要被替换的原始类：" + classInfo.getLoadClassClassName() + ", 并未找到");
                continue;
            }
            try {
                /*把类的定义与新的类文件关联起来*/
                ClassDefinition reporterDef = new ClassDefinition(oldLoadClass, classInfo.getLoadClassBytes());
                inst.redefineClasses(reporterDef);
                log.warn("成功热更新：" + oldLoadClass.getName());
            } catch (Throwable e) {
                throw e;
            }
        }
    }

    /**
     * 程序的进程id
     *
     * @return
     */
    public static String getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.split("@")[0];
    }


}
