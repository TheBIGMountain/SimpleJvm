package com.dqpi.simple_jvm.instructions.base;

import com.dqpi.simple_jvm.instructions.BytecodeReader;
import com.dqpi.simple_jvm.instructions.Instruction;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/28
 */
public abstract class Index16Instruction implements Instruction {
    @Resource
    protected BytecodeReader reader;
    protected int index;
    /**
     * 获取所有操作数
     */
    @Override
    public void fetchOperands() {
        this.index = reader.readUint16();
    }
}
