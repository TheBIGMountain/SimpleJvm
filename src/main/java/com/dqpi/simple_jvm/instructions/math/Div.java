package com.dqpi.simple_jvm.instructions.math;

import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
public class Div {
    @Component
    public static class DDIV extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            double v2 = stack.popDouble();
            double v1 = stack.popDouble();
            stack.pushDouble(v1 / v2);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class FDIV extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            float v2 = stack.popFloat();
            float v1 = stack.popFloat();
            stack.pushFloat(v1 / v2);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class IDIV extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            int v2 = stack.popInt();
            int v1 = stack.popInt();
            if (v2 == 0) {
                throw new ArithmeticException("/ by zero");
            }
            stack.pushInt(v1 / v2);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class LDIV extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            long v2 = stack.popLong();
            long v1 = stack.popLong();
            if (v2 == 0) {
                throw new ArithmeticException("/ by zero");
            }
            stack.pushLong(v1 / v2);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
