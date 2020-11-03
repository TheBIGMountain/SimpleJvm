package com.dqpi.simple_jvm.instructions.references;

import com.dqpi.simple_jvm.instructions.base.Index16Instruction;
import com.dqpi.simple_jvm.instructions.base.InstructionUtil;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Field;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.RtConstantPool;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.FieldRef;
import com.dqpi.simple_jvm.runtime_data.opt.LocalVars;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */
public class FieldInst {
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class PUT_FIELD extends Index16Instruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            Method currentMethod = frame.getMethod();
            Clazz currentClazz = currentMethod.getClazz();
            RtConstantPool rtcp = currentClazz.getRtConstantPool();
            FieldRef fieldRef = (FieldRef) rtcp.getConstant(index);
            Field field = fieldRef.resolvedField();
            
            if (field.isStatic()) {
                throw new IncompatibleClassChangeError();
            }
            
            if (field.isFinal()) {
                if (currentClazz != field.getClazz() || !"<init>".equals(currentMethod.getName())) {
                    throw new IllegalAccessError();
                }
            }

            String descriptor = field.getDescriptor();
            int fIndex = field.getFIndex();
            OperandStack stack = frame.getOperandStack();
            
            InstructionUtil.execute(descriptor.charAt(0),
                    () -> {
                        int val = stack.popInt();
                        Obj ref = stack.popRef();
                        if (ref == null) {
                            throw new NullPointerException();
                        }
                        ref.getFields().setInt(fIndex, val);
                    },
                    () -> {
                        float val = stack.popFloat();
                        Obj ref = stack.popRef();
                        if (ref == null) {
                            throw new NullPointerException();
                        }
                        ref.getFields().setFloat(fIndex, val);
                    },
                    () -> {
                        long val = stack.popLong();
                        Obj ref = stack.popRef();
                        if (ref == null) {
                            throw new NullPointerException();
                        }
                        ref.getFields().setLong(fIndex, val);
                    },
                    () -> {
                        double val = stack.popDouble();
                        Obj ref = stack.popRef();
                        if (ref == null) {
                            throw new NullPointerException();
                        }
                        ref.getFields().setDouble(fIndex, val);
                    },
                    () -> {
                        Obj val = stack.popRef();
                        Obj ref = stack.popRef();
                        if (ref == null) {
                            throw new NullPointerException();
                        }
                        ref.getFields().setRef(fIndex, val);
                    });
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :  #" + index;
        }
    }
    
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class GET_FIELD extends Index16Instruction {

        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            RtConstantPool rtcp = frame.getMethod().getClazz().getRtConstantPool();
            FieldRef fieldRef = (FieldRef) rtcp.getConstant(index);
            Field field = fieldRef.resolvedField();
            if (field.isStatic()) {
                throw new IncompatibleClassChangeError();
            }

            OperandStack stack = frame.getOperandStack();
            Obj ref = stack.popRef();
            if (ref == null) {
                throw new NullPointerException();
            }

            String descriptor = field.getDescriptor();
            int fIndex = field.getFIndex();
            LocalVars fields = ref.getFields();
            
            InstructionUtil.execute(descriptor.charAt(0),
                    () -> stack.pushInt(fields.getInt(fIndex)),
                    () -> stack.pushFloat(fields.getFloat(fIndex)),
                    () -> stack.pushLong(fields.getLong(fIndex)),
                    () -> stack.pushDouble(fields.getDouble(fIndex)),
                    () -> stack.pushRef(fields.getRef(fIndex)));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :  #" + index;
        }
    }
}
