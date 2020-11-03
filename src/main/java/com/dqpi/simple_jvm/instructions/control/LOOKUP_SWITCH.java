package com.dqpi.simple_jvm.instructions.control;

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
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LOOKUP_SWITCH extends BranchInstruction {
    private int defaultOffset;
    private int pairs;
    private int[] matchOffsets;

    @Override
    public void fetchOperands() {
        reader.skipPadding();
        this.defaultOffset = reader.readInt32();
        this.pairs = reader.readInt32();
        this.matchOffsets = reader.readInt32s(pairs * 2);
    }

    /**
     * 执行指令
     *
     * @param frame 当前栈帧
     */
    @Override
    public void execute(Thread.Frame frame) {
        int key = frame.getOperandStack().popInt();
        for (int i = 0; i < pairs * 2; i += 2) {
            if (matchOffsets[i] == key) {
                InstructionUtil.branch(frame, matchOffsets[i + 1]);
                return;
            }
        }
        InstructionUtil.branch(frame, defaultOffset);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
}
