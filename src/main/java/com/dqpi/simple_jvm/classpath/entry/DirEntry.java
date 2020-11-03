package com.dqpi.simple_jvm.classpath.entry;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author TheBIGMountain
 * @date 2020/10/26
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DirEntry extends Entry {
    /**
     * 加载class文件
     *
     * @param className 类名
     * @return class文件内容
     */
    @Override
    public byte[] readClass(String className) {
        try {
            return Files.readAllBytes(Paths.get(classpath + "/" + className));
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
}
