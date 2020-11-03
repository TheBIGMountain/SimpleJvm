package com.dqpi.simple_jvm.instructions.comparisons;

import com.dqpi.simple_jvm.instructions.base.BranchInstruction;
import com.dqpi.simple_jvm.instructions.base.InstructionUtil;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
public class IfRefCmp {
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class IF_ACMPEQ extends BranchInstruction {

        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            if (cmp(frame)) {
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
    public static class IF_ACMPNE extends BranchInstruction {

        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            if (!cmp(frame)) {
                InstructionUtil.branch(frame, offset);
            }
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :   " + (offset > 0 ? "+" + offset : offset);
        }
    }
    
    private static boolean cmp(Thread.Frame frame) {
        OperandStack stack = frame.getOperandStack();
        Obj ref2 = stack.popRef();
        Obj ref1 = stack.popRef();
        return ref1 == ref2;
    }
}
