package com.dqpi.simple_jvm.instructions.comparisons;

import com.dqpi.simple_jvm.instructions.base.BranchInstruction;
import com.dqpi.simple_jvm.instructions.base.InstructionUtil;
import com.dqpi.simple_jvm.runtime_data.Thread;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
public class IfCond {
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class IFEQ extends BranchInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            int val = frame.getOperandStack().popInt();
            if (val == 0) {
                InstructionUtil.branch(frame, offset);
            }
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :   " + (offset > 0 ? "+" + offset : offset);
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class IFNE extends BranchInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            int val = frame.getOperandStack().popInt();
            if (val != 0) {
                InstructionUtil.branch(frame, offset);
            }
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :   " + (offset > 0 ? "+" + offset : offset);
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class IFLT extends BranchInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            int val = frame.getOperandStack().popInt();
            if (val < 0) {
                InstructionUtil.branch(frame, offset);
            }
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :   " + (offset > 0 ? "+" + offset : offset);
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class IFLE extends BranchInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            int val = frame.getOperandStack().popInt();
            if (val <= 0) {
                InstructionUtil.branch(frame, offset);
            }
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :   " + (offset > 0 ? "+" + offset : offset);
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class IFGT extends BranchInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            int val = frame.getOperandStack().popInt();
            if (val > 0) {
                InstructionUtil.branch(frame, offset);
            }
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :   " + (offset > 0 ? "+" + offset : offset);
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class IFGE extends BranchInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            int val = frame.getOperandStack().popInt();
            if (val >= 0) {
                InstructionUtil.branch(frame, offset);
            }
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :   " + (offset > 0 ? "+" + offset : offset);
        }
    }
}
