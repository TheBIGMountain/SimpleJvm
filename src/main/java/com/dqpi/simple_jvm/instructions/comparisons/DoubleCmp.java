package com.dqpi.simple_jvm.instructions.comparisons;

import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
public class DoubleCmp {
    @Component
    public static class DCMPG extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            cmp(frame, true, true);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class DCMPL extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            cmp(frame, false, true);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    static void cmp(Thread.Frame frame, boolean gFlag, boolean isDouble) {
        OperandStack stack = frame.getOperandStack();
        double v2;
        double v1;
        if (isDouble) {
            v2 = stack.popDouble();
            v1 = stack.popDouble();
        }
        else {
            v2 = stack.popFloat();
            v1 = stack.popFloat();
        }
        
        if (v1 > v2) {
            stack.pushInt(1);
        }
        else if (v1 == v2) {
            stack.pushInt(0);
        }
        else if (v1 < v2) {
            stack.pushInt(-1);
        }
        else if (gFlag) {
            stack.pushInt(1);
        }
        else {
            stack.pushInt(-1);
        }
    }
}
