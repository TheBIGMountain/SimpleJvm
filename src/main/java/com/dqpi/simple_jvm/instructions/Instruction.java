package com.dqpi.simple_jvm.instructions;

import com.dqpi.simple_jvm.runtime_data.Thread;

/**
 * @author TheBIGMountain
 * @date 2020/10/28
 */
public interface Instruction {
    /**
     * 获取所有操作数
     */
    void fetchOperands();

    /**
     * 执行指令
     * 
     * @param frame  当前栈帧
     */
    void execute(Thread.Frame frame);
    
    
}
