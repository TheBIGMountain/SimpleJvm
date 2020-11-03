package com.dqpi.simple_jvm.instructions.math;

import com.dqpi.simple_jvm.instructions.BytecodeReader;
import com.dqpi.simple_jvm.instructions.Instruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.opt.LocalVars;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IINC implements Instruction {
    @Resource
    private BytecodeReader reader;
    @Setter
    private int index;
    @Setter
    private int con;
    /**
     * 获取所有操作数
     */
    @Override
    public void fetchOperands() {
        this.index = reader.readUint8();
        this.con = reader.readInt8();
    }

    /**
     * 执行指令
     *
     * @param frame 当前栈帧
     */
    @Override
    public void execute(Thread.Frame frame) {
        LocalVars localVars = frame.getLocalVars();
        int val = localVars.getInt(index);
        val += con;
        localVars.setInt(index, val);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " -> " + index + ", val = " + con;
    }
}
