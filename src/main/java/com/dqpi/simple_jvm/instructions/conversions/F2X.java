package com.dqpi.simple_jvm.instructions.conversions;

import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
public class F2X {
    @Component
    public static class F2D extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            float f = stack.popFloat();
            stack.pushDouble(f);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class F2I extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            float f = stack.popFloat();
            stack.pushInt((int) f);
            
        }
        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class F2L extends NoOperandsInstruction {

        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            float f = stack.popFloat();
            stack.pushLong((long) f);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
