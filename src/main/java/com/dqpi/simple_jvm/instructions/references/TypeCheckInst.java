package com.dqpi.simple_jvm.instructions.references;

import com.dqpi.simple_jvm.instructions.base.Index16Instruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.RtConstantPool;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.ClassRef;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */
public class TypeCheckInst {
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class INSTANCE_OF extends Index16Instruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            Obj ref = stack.popRef();
            if (ref == null) {
                stack.pushInt(0);
                return;
            }

            RtConstantPool rtConstantPool = frame.getMethod().getClazz().getRtConstantPool();
            ClassRef classRef = (ClassRef) rtConstantPool.getConstant(index);
            Clazz clazz = classRef.resolvedClass();
            
            if (ref.isInstanceOf(clazz)) {
                stack.pushInt(1);
                return;
            }
            stack.pushInt(0);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :  #" + index;
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class CHECK_CAST extends Index16Instruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            Obj ref = stack.popRef();
            stack.pushRef(ref);
            if (ref == null) {
                return;
            }

            RtConstantPool rtcp = frame.getMethod().getClazz().getRtConstantPool();
            ClassRef classRef = (ClassRef) rtcp.getConstant(index);
            Clazz clazz = classRef.resolvedClass();
            
            if (!ref.isInstanceOf(clazz)) {
                throw new ClassCastException();
            }
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :  #" + index;
        }
    }
}
