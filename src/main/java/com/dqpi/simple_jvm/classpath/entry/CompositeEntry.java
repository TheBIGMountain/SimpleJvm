package com.dqpi.simple_jvm.classpath.entry;

import com.dqpi.simple_jvm.classpath.EntryFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TheBIGMountain
 * @date 2020/10/26
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CompositeEntry extends Entry {
    
    @Resource
    private EntryFactory factory;
    
    private List<Entry> entries;
    
    /**
     * 加载class文件
     *
     * @param className 类名
     * @return class文件内容
     */
    @Override
    public byte[] readClass(String className) {
        if (entries == null) {
            setEntries();
        }
        for (Entry entry : entries) {
            byte[] bytes = entry.readClass(className);
            if (bytes != null) {
                return bytes;
            }
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
        if (entries == null) {
            setEntries();
        }
        StringBuilder res = new StringBuilder();
        for (Entry entry : entries) {
            res.append(entry.string()).append(File.pathSeparator);
        }
        res.replace(res.length() - 1, res.length() - 1, "");
        return res.toString();
    }
    
    private void setEntries() {
        entries = new ArrayList<>();
        String[] entryPath = classpath.split(File.pathSeparator);
        for (String path : entryPath) {
            entries.add(factory.newEntry(path));
        }
    }
}
