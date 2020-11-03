package com.dqpi.simple_jvm.instructions.references;

import com.dqpi.simple_jvm.instructions.base.Index16Instruction;
import com.dqpi.simple_jvm.instructions.base.InstructionUtil;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.RtConstantPool;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.MethodRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class INVOKE_STATIC extends Index16Instruction {
    /**
     * 执行指令
     *
     * @param frame 当前栈帧
     */
    @Override
    public void execute(Thread.Frame frame) {
        RtConstantPool rtcp = frame.getMethod().getClazz().getRtConstantPool();
        MethodRef methodRef = (MethodRef) rtcp.getConstant(index);
        Method method = methodRef.resolveMethod();
        if (!method.isStatic()) {
            throw new IncompatibleClassChangeError();
        }
        
        if (!method.getClazz().isInitStarted()) {
            frame.revertNextPc();
            InstructionUtil.initClass(frame.getThread(), method.getClazz());
            return;
        }
        InstructionUtil.invokeMethod(frame, method);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " :  #" + index;
    }
}
