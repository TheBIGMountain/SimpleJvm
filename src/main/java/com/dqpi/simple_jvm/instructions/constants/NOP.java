package com.dqpi.simple_jvm.instructions.constants;

import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/28
 */
@Component
public class NOP extends NoOperandsInstruction {

    /**
     * 执行指令
     *
     * @param frame 当前栈帧
     */
    @Override
    public void execute(Thread.Frame frame) {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
