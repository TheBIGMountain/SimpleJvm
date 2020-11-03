package com.dqpi.simple_jvm.classfile.attribute.debug;

import com.dqpi.simple_jvm.classfile.ClassReader;
import com.dqpi.simple_jvm.classfile.attribute.AttributeInfo;
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
public class LocalVariableTableAttribute implements AttributeInfo {
    @Resource
    private ClassReader reader;
    @Getter
    private LocalVariableEntry[] localVariableTable;
    
    @Override
    public void readInfo() {
        int length = reader.readUint16();
        this.localVariableTable = new LocalVariableEntry[length];
        for (int i = 0; i < localVariableTable.length; i++) {
            localVariableTable[i] = new LocalVariableEntry(
                    reader.readUint16(),
                    reader.readUint16(),
                    reader.readUint16(),
                    reader.readUint16(),
                    reader.readUint16()
            );
        }
    }
    
    @Getter
    @AllArgsConstructor
    public static class LocalVariableEntry {
        private final int startPc;
        private final int length;
        private final int nameIndex;
        private final int descriptorIndex;
        private final int index;
    }
}
