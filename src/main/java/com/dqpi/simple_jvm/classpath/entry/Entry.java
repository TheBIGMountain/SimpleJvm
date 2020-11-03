package com.dqpi.simple_jvm.classpath.entry;

import lombok.Setter;

/**
 * @author TheBIGMountain
 * @date 2020/10/26
 */
@Setter
public abstract class Entry {
    protected String classpath;
    /**
     * 加载class文件
     * 
     * @param className   类名
     * @return            class文件内容  
     */
    public abstract byte[] readClass(String className);

    /**
     * 获取class文件名
     * 
     * @return  class文件名
     */
    public abstract String string();
    
}
