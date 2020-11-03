package com.dqpi.simple_jvm.instructions.references;

import com.dqpi.simple_jvm.instructions.base.Index16Instruction;
import com.dqpi.simple_jvm.instructions.base.InstructionUtil;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.RtConstantPool;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.ClassRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NEW extends Index16Instruction {
    @Resource
    private ApplicationContext ctx;
    /**
     * 执行指令
     *
     * @param frame 当前栈帧
     */
    @Override
    public void execute(Thread.Frame frame) {
        RtConstantPool rtcp = frame.getMethod().getClazz().getRtConstantPool();
        ClassRef classRef = (ClassRef) rtcp.getConstant(index);
        Clazz clazz = classRef.resolvedClass();
        
        if (!clazz.isInitStarted()) {
            frame.revertNextPc();
            InstructionUtil.initClass(frame.getThread(), clazz);
            return;
        }
        
        if (clazz.isInterface() || clazz.isAbstract()) {
            throw new InstantiationError();
        }

        Obj ref = ctx.getBean(Obj.class).setInfo(clazz);
        frame.getOperandStack().pushRef(ref);
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " :  #" + index;
    }
}
