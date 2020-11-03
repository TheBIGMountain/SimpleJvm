package com.dqpi.simple_jvm.instructions.base;

import com.dqpi.simple_jvm.instructions.BytecodeReader;
import com.dqpi.simple_jvm.instructions.Instruction;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/28
 */
public abstract class BranchInstruction implements Instruction {
    @Resource
    protected BytecodeReader reader;
    protected int offset;
    
    /**
     * 获取所有操作数
     */
    @Override
    public void fetchOperands() {
        this.offset = reader.readInt16();
    }
}
