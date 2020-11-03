package com.dqpi.simple_jvm.classfile.attribute.useful;

import com.dqpi.simple_jvm.classfile.ClassReader;
import com.dqpi.simple_jvm.classfile.attribute.AttributeInfo;
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
public class ExceptionsAttribute implements AttributeInfo {
    @Resource
    private ClassReader reader;
    @Getter
    private short[] exceptionIndexTable;
    
    @Override
    public void readInfo() {
        this.exceptionIndexTable = reader.readUint16s();
    }
    
}
