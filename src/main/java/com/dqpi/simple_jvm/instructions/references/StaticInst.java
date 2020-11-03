package com.dqpi.simple_jvm.instructions.references;

import com.dqpi.simple_jvm.instructions.base.Index16Instruction;
import com.dqpi.simple_jvm.instructions.base.InstructionUtil;
import com.dqpi.simple_jvm.runtime_data.Thread;
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
public class StaticInst {
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class PUT_STATIC extends Index16Instruction {
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
            Clazz clazz = field.getClazz();
            
            if (!clazz.isInitStarted()) {
                frame.revertNextPc();
                InstructionUtil.initClass(frame.getThread(), clazz);
                return;
            }
            
            if (!field.isStatic()) {
                throw new IncompatibleClassChangeError();
            }
            if (field.isFinal()) {
                if (currentClazz != clazz || !"<clinit>".equals(currentMethod.getName())) {
                    throw new IllegalAccessError();
                }
            }

            String descriptor = field.getDescriptor();
            int fIndex = field.getFIndex();
            LocalVars staticVars = clazz.getStaticVars();
            OperandStack stack = frame.getOperandStack();
            
            InstructionUtil.execute(descriptor.charAt(0), 
                    () -> staticVars.setInt(fIndex, stack.popInt()),
                    () -> staticVars.setFloat(fIndex, stack.popFloat()),
                    () -> staticVars.setLong(fIndex, stack.popLong()),
                    () -> staticVars.setDouble(fIndex, stack.popDouble()),
                    () -> staticVars.setRef(fIndex, stack.popRef()));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :  #" + index;
        }
    }
    
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class GET_STATIC extends Index16Instruction {
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
            Clazz clazz = field.getClazz();
            
            
            if (!clazz.isInitStarted()) {
                frame.revertNextPc();
                InstructionUtil.initClass(frame.getThread(), clazz);
                return;
            }
            
            if (!field.isStatic()) {
                throw new IncompatibleClassChangeError();
            }

            String descriptor = field.getDescriptor();
            int fIndex = field.getFIndex();
            LocalVars staticVars = clazz.getStaticVars();
            OperandStack stack = frame.getOperandStack();
            
            InstructionUtil.execute(descriptor.charAt(0),
                    () -> stack.pushInt(staticVars.getInt(fIndex)),
                    () -> stack.pushFloat(staticVars.getFloat(fIndex)),
                    () -> stack.pushLong(staticVars.getLong(fIndex)),
                    () -> stack.pushDouble(staticVars.getDouble(fIndex)),
                    () -> stack.pushRef(staticVars.getRef(fIndex)));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :  #" + index;
        }
    }
    
    
}
