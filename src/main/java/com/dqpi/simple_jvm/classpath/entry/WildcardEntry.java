package com.dqpi.simple_jvm.classpath.entry;

import lombok.SneakyThrows;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Enumeration;
import java.util.zip.ZipFile;

/**
 * @author TheBIGMountain
 * @date 2020/10/26
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WildcardEntry extends Entry {
    
    @Resource
    private ZipEntry zipEntry;
    
    /**
     * 加载class文件
     *
     * @param className 类名
     * @return class文件内容
     */
    @Override
    public byte[] readClass(String className) {
        classpath = classpath.replace("*", "");
        return findClassFile(className);
    }

    /**
     * 获取class文件名
     *
     * @return class文件名
     */
    @Override
    public String string() {
        return classpath;
    }
    
    @SneakyThrows
    private byte[] findClassFile(String className) {
        File[] files = new File(classpath).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }
                if (file.getName().endsWith(".jar") || file.getName().endsWith(".JAR")) {
                    try (ZipFile zipFile = new ZipFile(file.getAbsoluteFile())){
                        Enumeration<? extends java.util.zip.ZipEntry> entries = zipFile.entries();
                        while (entries.hasMoreElements()) {
                            java.util.zip.ZipEntry e = entries.nextElement();
                            if (e.getName().startsWith(className)) {
                                return zipEntry.readFile(zipFile.getInputStream(e));
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    } 
}
