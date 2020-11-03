package com.dqpi.simple_jvm.instructions.stack;

import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */
public class Pop {
    @Component
    public static class POP extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            frame.getOperandStack().popSlot();
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
    
    @Component
    public static class POP2 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            OperandStack stack = frame.getOperandStack();
            stack.popSlot();
            stack.popSlot();
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
