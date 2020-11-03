package com.dqpi.simple_jvm.classfile.attribute.useful;

import com.dqpi.simple_jvm.classfile.ClassReader;
import com.dqpi.simple_jvm.classfile.attribute.AttributeInfo;
import com.dqpi.simple_jvm.classfile.attribute.AttributeInfoFactory;
import com.dqpi.simple_jvm.classfile.attribute.debug.LineNumberTableAttribute;
import lombok.AllArgsConstructor;
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
public class CodeAttribute implements AttributeInfo {
    @Resource
    private ClassReader reader;
    @Resource
    private AttributeInfoFactory factory;
    @Getter
    private int maxStack;
    @Getter
    private int maxLocals;
    @Getter
    private int[] code;
    @Getter
    private ExceptionEntry[] exceptionTable;
    @Getter
    private AttributeInfo[] attributes;
    
    @Override
    public void readInfo() {
        this.maxStack = reader.readUint16();
        this.maxLocals = reader.readUint16();
        this.code = reader.readBytes(reader.readUint32());
        this.exceptionTable = readExceptionTable();
        this.attributes = factory.readAttributes();
    }

    private ExceptionEntry[] readExceptionTable() {
        int length = reader.readUint16();
        ExceptionEntry[] exceptionTable = new ExceptionEntry[length];
        for (int i = 0; i < exceptionTable.length; i ++) {
            exceptionTable[i] = new ExceptionEntry(
                    reader.readUint16(),
                    reader.readUint16(),
                    reader.readUint16(),
                    reader.readUint16()
            );
        }
        return exceptionTable;
    }

    public LineNumberTableAttribute getLineNumberTableAttribute() {
        for (AttributeInfo attribute : attributes) {
            if (attribute instanceof LineNumberTableAttribute) {
                return (LineNumberTableAttribute) attribute;
            }
        }
        return null;
    }

    @AllArgsConstructor
    @Getter
    public static class ExceptionEntry {
        private final int startPc;
        private final int endPc;
        private final int handlerPc;
        private final int catchType;
    }
}
