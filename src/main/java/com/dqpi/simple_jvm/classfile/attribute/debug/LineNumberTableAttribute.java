package com.dqpi.simple_jvm.classfile.attribute.debug;

import com.dqpi.simple_jvm.classfile.ClassReader;
import com.dqpi.simple_jvm.classfile.attribute.AttributeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
public class LineNumberTableAttribute implements AttributeInfo {
    @Resource
    private ClassReader reader;
    @Getter
    private LineNumberEntry[] lineNumberTable;
    @Override
    public void readInfo() {
        int length = reader.readUint16();
        lineNumberTable = new LineNumberEntry[length];
        for (int i = 0; i < lineNumberTable.length; i++) {
            lineNumberTable[i] = new LineNumberEntry(
                    reader.readUint16(),
                    reader.readUint16()
            );
        }
    }

    public int getLineNumber(int pc) {
        for (int i = lineNumberTable.length - 1; i >= 0; i --) {
            LineNumberEntry entry = lineNumberTable[i];
            if (pc >= entry.startPc) {
                return entry.lineNumber;
            }
        }
        return -1;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class LineNumberEntry {
        private int startPc;
        private int lineNumber;
    }
}
