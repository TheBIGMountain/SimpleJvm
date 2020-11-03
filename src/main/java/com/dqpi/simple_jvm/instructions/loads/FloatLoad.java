package com.dqpi.simple_jvm.instructions.loads;

import com.dqpi.simple_jvm.instructions.base.Index8Instruction;
import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */
public class FloatLoad {
    private static void fLoad(Thread.Frame frame, int index) {
        float val = frame.getLocalVars().getFloat(index);
        frame.getOperandStack().pushFloat(val);
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class FLOAD extends Index8Instruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            fLoad(frame, index);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :   " + index;
        }
    }

    @Component
    public static class FLOAD_0 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            fLoad(frame, 0);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class FLOAD_1 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            fLoad(frame, 1);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class FLOAD_2 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            fLoad(frame, 2);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class FLOAD_3 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            fLoad(frame, 3);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
