package com.dqpi.simple_jvm.classpath;

import com.dqpi.simple_jvm.classpath.entry.Entry;
import com.dqpi.simple_jvm.jvm.Cmd;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author TheBIGMountain
 * @date 2020/10/26
 */
@Component
public class ClassPath {
    @Resource
    private Cmd cmd;
    @Resource
    private EntryFactory factory;
    
    private Entry bootClasspath;
    private Entry extClasspath;
    private Entry userClasspath;
    
    public void parse() {
        parseBootAndExtClasspath();
        parseUserClasspath();
    }

    public String string() {
        return userClasspath.string();
    }

    public byte[] readClass(String className) {
        String clsName = className.replace(".", "/") + ".class";
        byte[] res = bootClasspath.readClass(clsName);
        if (res == null) {
            res = extClasspath.readClass(clsName);
            if (res == null) {
                res = userClasspath.readClass(clsName);
            }
        }
        return res;
    }
    
    private void parseBootAndExtClasspath() {
        String jreDir = getJreDir(cmd.getJreOption());
        
        bootClasspath = factory.newEntry(jreDir + "/lib/*");
        extClasspath = factory.newEntry(jreDir + "/lib/ext/*");
    }
    
    private void parseUserClasspath() {
        userClasspath = factory.newEntry(cmd.getCp());
    }
    
    @SneakyThrows
    private String getJreDir(String jreOption) {
        if (!StringUtils.isEmpty(jreOption) && exist(jreOption)) {
            return jreOption;
        }
        if (exist("./jre")) {
            return "./jre";
        }
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome != null) {
            return javaHome + "/jre";
        }
        throw new Exception("不存在jre目录");
    }
    
    private boolean exist(String path) {
        return new File(path).isAbsolute();
    }
}
