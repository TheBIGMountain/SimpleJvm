package com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member;

import com.dqpi.simple_jvm.classfile.attribute.debug.LineNumberTableAttribute;
import com.dqpi.simple_jvm.classfile.attribute.useful.CodeAttribute;
import com.dqpi.simple_jvm.classfile.member.MemberInfo;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.utils.AccessFlags;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.RtConstantPool;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.ClassRef;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Getter
@Setter
public class Method extends ClassMember {
    private int maxStack;
    private int maxLocals;
    private int[] code;
    private int argCount;
    private ExceptionTable exceptionTable;
    private LineNumberTableAttribute lineNumberTable;

    public void copyAttributes(MemberInfo methodInfo) {
        CodeAttribute codeAttr = methodInfo.getCodeAttribute();
        if (codeAttr != null) {
            this.maxStack = codeAttr.getMaxStack();
            this.maxLocals = codeAttr.getMaxLocals();
            this.code = codeAttr.getCode();
            this.lineNumberTable = codeAttr.getLineNumberTableAttribute();
            this.exceptionTable = ExceptionTable.newExceptionTable(codeAttr.getExceptionTable(), clazz.getRtConstantPool());
        }
    }
    
    public int findExceptionHandler(Clazz exClass, int pc) {
        ExceptionHandler handler = exceptionTable.findExceptionHandler(exClass, pc);
        if (handler != null) {
            return handler.handlerPc;
        }
        return -1;
    }

    public boolean isSynchronized() {
        return (this.accessFlags & AccessFlags.ACC_SYNTHETIC) != 0;
    }

    public boolean isBridge() {
        return (this.accessFlags & AccessFlags.ACC_BRIDGE) != 0;
    }

    public boolean isVarargs() {
        return (this.accessFlags & AccessFlags.ACC_VARARGS) != 0;
    }

    public boolean isNative() {
        return (this.accessFlags & AccessFlags.ACC_NATIVE) != 0;
    }

    public boolean isAbstract() {
        return (this.accessFlags & AccessFlags.ACC_ABSTRACT) != 0;
    }

    public boolean isStrict() {
        return (this.accessFlags & AccessFlags.ACC_STRICT) != 0;
    }

    public void calcArgCount(List<String> parameterTypes) {
        for (String paramType : parameterTypes) {
            this.argCount ++;
            if ("J".equals(paramType) || "D".equals(paramType)) {
                this.argCount ++;
            }
        }
        if (!isStatic()) {
            this.argCount ++;
        }
    }
    
    public void injectCodeAttribute(String returnType) {
        this.maxStack = 4;
        this.maxLocals = this.argCount;
        switch (returnType.charAt(0)) {
            case 'V':
                this.code = new int[]{0xfe, 0xb1};
                break;
            case 'D':
                this.code = new int[]{0xfe, 0xaf};
                break;
            case 'F':
                this.code = new int[]{0xfe, 0xae};
                break;
            case 'J':
                this.code = new int[]{0xfe, 0xad};
                break;
            case 'L':
            case '[':
                this.code = new int[]{0xfe, 0xb0};
                break;
            default:
                this.code = new int[]{0xfe, 0xac};
        }
    }

    public int getLineNumber(int pc) {
        if (isNative()) {
            return -2;
        }
        if (lineNumberTable == null) {
            return -1;
        }
        return lineNumberTable.getLineNumber(pc);
    }

    @Getter
    @AllArgsConstructor
    public static class ExceptionTable {
        private ExceptionHandler[] exceptionTable;
        
        private ExceptionTable() {}
        
        public static ExceptionTable newExceptionTable(CodeAttribute.ExceptionEntry[] entries, RtConstantPool rtcp) {
            ExceptionTable table = new ExceptionTable();
            table.exceptionTable = new ExceptionHandler[entries.length];
            for (int i = 0; i < entries.length; i++) {
                table.exceptionTable[i] = new ExceptionHandler(
                       entries[i].getStartPc(),
                       entries[i].getEndPc(),
                       entries[i].getHandlerPc(),
                       entries[i].getCatchType() == 0 ? null : (ClassRef) rtcp.getConstant(entries[i].getCatchType())
                );
            }
            return table;
        }
        
        public ExceptionHandler findExceptionHandler(Clazz exClass, int pc) {
            for (ExceptionHandler handler : exceptionTable) {
                if (pc >= handler.startPc && pc < handler.endPc) {
                    if (handler.catchType == null) {
                        return handler;
                    }
                    Clazz catchClass = handler.catchType.resolvedClass();
                    if (catchClass == exClass || catchClass.isSuperClassOf(exClass)) {
                        return handler;
                    }
                }
            }
            return null;
        }
    }
    
    @Getter
    @AllArgsConstructor
    public static class ExceptionHandler {
        private final int startPc;
        private final int endPc;
        private final int handlerPc;
        private final ClassRef catchType;
    }
    
    public static class MethodDescriptor {
        @Getter
        private final List<String> parameterTypes = new ArrayList<>();
        @Getter
        @Setter
        private String returnType;
    }
}
