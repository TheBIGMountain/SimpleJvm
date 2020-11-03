package com.dqpi.simple_jvm.classpath.entry;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipFile;

/**
 * @author TheBIGMountain
 * @date 2020/10/26
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZipEntry extends Entry {
    /**
     * 加载class文件
     *
     * @param className 类名
     * @return class文件内容
     */
    @Override
    public byte[] readClass(String className) {
        try (ZipFile zipFile = new ZipFile(classpath)){
            Enumeration<? extends java.util.zip.ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                java.util.zip.ZipEntry file = entries.nextElement();
                if (file.getName().equals(className)) {
                    return readFile(zipFile.getInputStream(file));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    
    byte[] readFile(InputStream inputStream) {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)){
            byte[] bytes = new byte[bis.available()];
            bis.read(bytes);
            return bytes;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
