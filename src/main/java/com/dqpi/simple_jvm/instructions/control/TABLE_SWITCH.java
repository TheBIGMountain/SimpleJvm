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
public class TABLE_SWITCH extends BranchInstruction {
    private int defaultOffset;
    private int low;
    private int high;
    private int[] jumpOffsets;

    /**
     * 获取所有操作数
     */
    @Override
    public void fetchOperands() {
        reader.skipPadding();
        this.defaultOffset = reader.readInt32();
        this.low = reader.readInt32();
        this.high = reader.readInt32();
        this.jumpOffsets = reader.readInt32s(high - low + 1);
    }

    /**
     * 执行指令
     *
     * @param frame 当前栈帧
     */
    @Override
    public void execute(Thread.Frame frame) {
        int index = frame.getOperandStack().popInt();
        int offset;
        if (index >= low && index <= high) {
            offset = jumpOffsets[index - low];
        }
        else {
            offset = defaultOffset;
        }
        InstructionUtil.branch(frame, offset);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
