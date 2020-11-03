package com.dqpi.simple_jvm.instructions.constants;

import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
public class DoubleConst {
    @Component
    public static class DCONST_0 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            frame.getOperandStack().pushDouble(0.0);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class DCONST_1 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            frame.getOperandStack().pushDouble(1.0);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
