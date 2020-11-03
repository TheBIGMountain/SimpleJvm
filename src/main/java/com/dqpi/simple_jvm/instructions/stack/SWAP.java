package com.dqpi.simple_jvm.instructions.stack;

import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import com.dqpi.simple_jvm.runtime_data.opt.Slot;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/28
 */
@Component
public class SWAP extends NoOperandsInstruction {
    /**
     * 执行指令
     *
     * @param frame 当前栈帧
     */
    @Override
    public void execute(Thread.Frame frame) {
        OperandStack stack = frame.getOperandStack();
        Slot slot1 = stack.popSlot();
        Slot slot2 = stack.popSlot();
        stack.pushSlot(slot1);
        stack.pushSlot(slot2);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
