package com.dqpi.simple_jvm.instructions.base;

import com.dqpi.simple_jvm.instructions.BytecodeReader;
import com.dqpi.simple_jvm.instructions.Instruction;
import lombok.Setter;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/28
 */
public abstract class Index8Instruction implements Instruction {
    @Resource
    private BytecodeReader reader;
    @Setter
    protected int index;
    /**
     * 获取所有操作数
     */
    @Override
    public void fetchOperands() {
        this.index = reader.readUint8();
    }
}
