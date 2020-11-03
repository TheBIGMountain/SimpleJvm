package com.dqpi.simple_jvm.instructions.loads;

import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
public class ArrEleLoad {
    private static <T> void load(OperandStack stack, Class<T> cls, Fun fun) {
        int index = stack.popInt();
        Obj arrRef = stack.popRef();

        if (arrRef == null) {
            throw new NullPointerException();
        }
        
        T[] arr = arrRef.getArr(cls);
        if (index < 0 || index >= arr.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        fun.execute(arr[index]);
    }
    
    @FunctionalInterface
    private interface Fun {
        void execute(Object element);
    }
    
    @Component
    public static class AALOAD extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            load(stack, Obj.class, element -> stack.pushRef((Obj) element));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class BALOAD extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            load(stack, Byte.class, element -> stack.pushInt((Integer) element));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class CALOAD extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            load(stack, Character.class, element -> stack.pushInt((Character) element));
        }
        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class DALOAD extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            load(stack, Double.class, element -> stack.pushDouble((Double) element));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class FALOAD extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            load(stack, Float.class, element -> stack.pushFloat((Float) element));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class IALOAD extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            load(stack, Integer.class, element -> stack.pushInt((Integer) element));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class LALOAD extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            load(stack, Long.class, element -> stack.pushLong((Long) element));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class SALOAD extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            load(stack, Short.class, element -> stack.pushInt((Short) element));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
