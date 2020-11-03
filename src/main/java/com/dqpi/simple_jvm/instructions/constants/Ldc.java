package com.dqpi.simple_jvm.instructions.constants;

import com.dqpi.simple_jvm.instructions.base.Index16Instruction;
import com.dqpi.simple_jvm.instructions.base.Index8Instruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.RtConstantPool;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.ClassRef;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.string_pool.StringPool;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
public class Ldc {
    @SneakyThrows
    private static void ldc(Thread.Frame frame, int index, StringPool stringPool) {
        OperandStack stack = frame.getOperandStack();
        Clazz clazz = frame.getMethod().getClazz();
        Object c = clazz.getRtConstantPool().getConstant(index);

        if (c instanceof Integer) {
            stack.pushInt((Integer) c);
        }
        else if (c instanceof Float) {
            stack.pushFloat((Float) c);
        }
        else if (c instanceof String) {
            stack.pushRef(stringPool.toJString(clazz.getLoader(), (String) c));
        }
        else if (c instanceof ClassRef) {
            ClassRef classRef = (ClassRef) c;
            Obj classObj = classRef.resolvedClass().getJClass();
            stack.pushRef(classObj);
        }
    }
    
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class LDC extends Index8Instruction {
        @Resource
        private StringPool stringPool;
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            ldc(frame, index, stringPool);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :   #" + index;
        }
    }
    
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class LDC_W extends Index16Instruction {
        @Resource
        private StringPool stringPool;
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            ldc(frame, index, stringPool);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :   #" + index;
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class LDC2_W extends Index16Instruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            RtConstantPool rtcp = frame.getMethod().getClazz().getRtConstantPool();
            Object c = rtcp.getConstant(index);

            if (c instanceof Long) {
                stack.pushLong((Long) c);
            }
            else if (c instanceof Double) {
                stack.pushDouble((Double) c);
            }
            else {
                throw new ClassFormatError();
            }
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :  #" + index;
        }
    }
}
