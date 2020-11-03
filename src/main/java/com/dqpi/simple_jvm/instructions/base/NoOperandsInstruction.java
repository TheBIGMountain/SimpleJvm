package com.dqpi.simple_jvm.instructions.base;

import com.dqpi.simple_jvm.instructions.Instruction;

/**
 * @author TheBIGMountain
 * @date 2020/10/28
 */
public abstract class NoOperandsInstruction implements Instruction {
    /**
     * 获取所有操作数
     */
    @Override
    public void fetchOperands() {}
}
