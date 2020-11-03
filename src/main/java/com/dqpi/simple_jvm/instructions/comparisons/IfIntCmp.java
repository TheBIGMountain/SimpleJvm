package com.dqpi.simple_jvm.instructions.comparisons;

import com.dqpi.simple_jvm.instructions.base.BranchInstruction;
import com.dqpi.simple_jvm.instructions.base.InstructionUtil;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
public class IfIntCmp {
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class IF_ICMPEQ extends BranchInstruction {

        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            V1AndV2 v1Andv2 = popCmp(frame);
            if (v1Andv2.v1 == v1Andv2.v2) {
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
    public static class IF_ICMPNE extends BranchInstruction {

        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            V1AndV2 v1Andv2 = popCmp(frame);
            if (v1Andv2.v1 != v1Andv2.v2) {
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
    public static class IF_ICMPLT extends BranchInstruction {

        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            V1AndV2 v1Andv2 = popCmp(frame);
            if (v1Andv2.v1 < v1Andv2.v2) {
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
    public static class IF_ICMPLE extends BranchInstruction {

        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            V1AndV2 v1Andv2 = popCmp(frame);
            if (v1Andv2.v1 <= v1Andv2.v2) {
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
    public static class IF_ICMPGT extends BranchInstruction {

        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            V1AndV2 v1Andv2 = popCmp(frame);
            if (v1Andv2.v1 > v1Andv2.v2) {
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
    public static class IF_ICMPGE extends BranchInstruction {

        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            V1AndV2 v1Andv2 = popCmp(frame);
            if (v1Andv2.v1 >= v1Andv2.v2) {
                InstructionUtil.branch(frame, offset);
            }
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :   " + (offset > 0 ? "+" + offset : offset);
        }
    }
    
    private static V1AndV2 popCmp(Thread.Frame frame) {
        OperandStack stack = frame.getOperandStack();
        int v2 = stack.popInt();
        int v1 = stack.popInt();
        return new V1AndV2(v1, v2);
    }
    
    @AllArgsConstructor
    @Getter
    private static class V1AndV2 {
        private final int v1;
        private final int v2;
    }
}
