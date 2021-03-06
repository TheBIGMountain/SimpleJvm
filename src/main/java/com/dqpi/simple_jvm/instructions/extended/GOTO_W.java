package com.dqpi.simple_jvm.instructions.extended;

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
public class GOTO_W extends BranchInstruction {
    /**
     * 获取所有操作数
     */
    @Override
    public void fetchOperands() {
        this.offset = reader.readInt32();
    }

    /**
     * 执行指令
     *
     * @param frame 当前栈帧
     */
    @Override
    public void execute(Thread.Frame frame) {
        InstructionUtil.branch(frame, offset);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " :   " +  (offset > 0 ? "+" + offset : offset);
    }
}
