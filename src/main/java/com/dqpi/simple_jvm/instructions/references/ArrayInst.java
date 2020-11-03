package com.dqpi.simple_jvm.instructions.references;

import com.dqpi.simple_jvm.instructions.BytecodeReader;
import com.dqpi.simple_jvm.instructions.Instruction;
import com.dqpi.simple_jvm.instructions.base.Index16Instruction;
import com.dqpi.simple_jvm.instructions.base.InstructionUtil;
import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Arr;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.ClassLoader;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.RtConstantPool;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.ClassRef;
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
public class ArrayInst  {
    @Resource
    protected BytecodeReader reader;
    @Resource
    protected Arr arr;
    
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class NEW_ARRAY extends ArrayInst implements Instruction {
        private int aType;
        /**
         * 获取所有操作数
         */
        @Override
        public void fetchOperands() {
            this.aType = reader.readUint8();
        }

        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            int count = stack.popInt();
            if (count < 0) {
                throw new NegativeArraySizeException();
            }

            ClassLoader loader = frame.getMethod().getClazz().getLoader();
            Clazz arrClass = getPrimitiveArrayClass(loader, aType);
            Obj array = arr.newArray(arrClass, count);
            stack.pushRef(array);
        }

        @SneakyThrows
        private Clazz getPrimitiveArrayClass(ClassLoader loader, int aType) {
            InstructionUtil.Fun2 fun = InstructionUtil.FUN_MAP.get(aType);
            if (fun != null) {
                return fun.execute(loader);
            }
            throw new Exception("Invalid atype");
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :   " + aType;
        }
    }
    
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class ANEW_ARRAY extends Index16Instruction {
        @Resource
        private Arr arr;
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            RtConstantPool rtcp = frame.getMethod().getClazz().getRtConstantPool();
            ClassRef classRef = (ClassRef) rtcp.getConstant(index);
            Clazz clazz = classRef.resolvedClass();

            OperandStack stack = frame.getOperandStack();
            int count = stack.popInt();
            if (count < 0) {
                throw new NegativeArraySizeException();
            }

            Clazz arrClass = clazz.arrayClass();
            Obj array = arr.newArray(arrClass, count);
            stack.pushRef(array);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :   " + index;
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class MULTI_ANEW_ARRAY extends ArrayInst implements Instruction {
        private int index;
        private int dimensions;
        /**
         * 获取所有操作数
         */
        @Override
        public void fetchOperands() {
            this.index = reader.readUint16();
            this.dimensions = reader.readUint8();
        }

        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            RtConstantPool rtcp = frame.getMethod().getClazz().getRtConstantPool();
            ClassRef classRef = (ClassRef) rtcp.getConstant(index);
            Clazz arrClass = classRef.resolvedClass();

            OperandStack stack = frame.getOperandStack();
            int[]  counts = popAndCheckCounts(stack);
            Obj array = arr.newMultiDimensionalArray(counts, arrClass);
            stack.pushRef(array);
        }

        private int[] popAndCheckCounts(OperandStack stack) {
            int[] counts = new int[dimensions];
            for (int i = dimensions - 1; i >= 0; i --) {
                counts[i] = stack.popInt();
                if (counts[i] < 0) {
                    throw new NegativeArraySizeException();
                }
            }
            return counts;
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class ARRAY_LENGTH extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            Obj arrRef = stack.popRef();
            if (arrRef == null) {
                throw new NullPointerException();
            }

            int arrLen = arrRef.getArrLength();
            stack.pushInt(arrLen);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
