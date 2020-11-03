package com.dqpi.simple_jvm.instructions.comparisons;

import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
@Component
public class LCMP extends NoOperandsInstruction {
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
        if (v1 > v2) {
            stack.pushInt(1);
        }
        else if (v1 == v2) {
            stack.pushInt(0);
        }
        else {
            stack.pushInt(-1);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
