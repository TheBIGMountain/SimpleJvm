package com.dqpi.simple_jvm.instructions.stack;

import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import com.dqpi.simple_jvm.runtime_data.opt.Slot;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */
public class Dup {
    private static void stackHelper(Thread.Frame frame, int popNum, int... sort) {
        OperandStack stack = frame.getOperandStack();
        Slot[] slots = new Slot[popNum];
        for (int i = 0; i < popNum; i++) {
            slots[i] = stack.popSlot();
        }
        for (int index : sort) {
            Slot slot = new Slot();
            slot.setNum(slots[index].getNum());
            slot.setRef(slots[index].getRef());
            stack.pushSlot(slot);
        }
    }

    @Component
    public static class DUP extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            stackHelper(frame, 1, 0, 0);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class DUP2 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            stackHelper(frame, 2, 1, 0, 1, 0);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class DUP2_X1 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            stackHelper(frame, 3, 1, 0, 2, 1, 0);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class DUP2_X2 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            stackHelper(frame, 4, 1, 0, 3, 2, 1, 0);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class DUP_X1 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            stackHelper(frame, 2, 0, 1, 0);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class DUP_X2 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            stackHelper(frame, 3, 0, 2, 1, 0);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
