package com.dqpi.simple_jvm.classpath;

import com.dqpi.simple_jvm.classpath.entry.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author TheBIGMountain
 * @date 2020/10/26
 */
@Component
public class EntryFactory {
    
    @Resource
    private ApplicationContext ctx;
    
    /**
     * entry工厂方法
     *
     * @param path 文件路径
     * @return  entry
     */
    public Entry newEntry(String path) {
        Entry entry;
        if (path.contains(File.pathSeparator)) {
            entry = ctx.getBean(CompositeEntry.class);
        }
        else if (path.endsWith("*")) {
            entry = ctx.getBean(WildcardEntry.class);
        }
        else if (path.endsWith(".jar") || path.endsWith(".JAR") 
           || path.endsWith(".zip") || path.endsWith(".ZIP")) {
            entry = ctx.getBean(ZipEntry.class);
        }
        else {
            entry = ctx.getBean(DirEntry.class);
        }
        entry.setClasspath(new File(path).getAbsolutePath());
        return entry;
    }
}
