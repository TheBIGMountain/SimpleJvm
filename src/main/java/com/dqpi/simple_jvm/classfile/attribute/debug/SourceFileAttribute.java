package com.dqpi.simple_jvm.classfile.attribute.debug;

import com.dqpi.simple_jvm.classfile.ClassReader;
import com.dqpi.simple_jvm.classfile.attribute.AttributeInfo;
import com.dqpi.simple_jvm.classfile.constant_pool.ConstantPool;
import lombok.Getter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/27
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SourceFileAttribute implements AttributeInfo {
    @Resource
    private ClassReader reader;
    @Resource
    private ConstantPool constantPool;
    @Getter
    private int sourceFileIndex;
    
    @Override
    public void readInfo() {
        this.sourceFileIndex = reader.readUint16();
    }
    
    public String getFileName() {
        return constantPool.getUtf8(sourceFileIndex);
    }
}
