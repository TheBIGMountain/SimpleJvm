package com.dqpi.simple_jvm.instructions.references;

import com.dqpi.simple_jvm.instructions.BytecodeReader;
import com.dqpi.simple_jvm.instructions.Instruction;
import com.dqpi.simple_jvm.instructions.base.InstructionUtil;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.RtConstantPool;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.ClassRef;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.InterfaceMethodRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class INVOKE_INTERFACE implements Instruction {
    @Resource
    private BytecodeReader reader;
    private int index;
    
    @Override
    public void fetchOperands() {
        this.index = reader.readUint16();
        reader.readUint8(); // count
        reader.readUint8(); // must be 0
    }

    /**
     * 执行指令
     *
     * @param frame 当前栈帧
     */
    @Override
    public void execute(Thread.Frame frame) {
        RtConstantPool rtcp = frame.getMethod().getClazz().getRtConstantPool();
        InterfaceMethodRef methodRef = (InterfaceMethodRef) rtcp.getConstant(index);
        Method method = methodRef.resolveInterfaceMethod();
        if (method.isStatic() || method.isPrivate()) {
            throw new IncompatibleClassChangeError();
        }

        Obj ref = frame.getOperandStack().getRefFromTop(method.getArgCount());
        if (ref == null) {
            throw new NullPointerException();
        }
        if (!ref.getClazz().isImplements(methodRef.resolvedClass())) {
            throw new IncompatibleClassChangeError();
        }
        
        Method methodToBeInvoke = ClassRef.lookupMethodInClass(
                ref.getClazz(), methodRef.getName(), methodRef.getDescriptor());
        if (methodToBeInvoke == null || methodToBeInvoke.isAbstract()) {
            throw new AbstractMethodError();
        }
        if (!methodToBeInvoke.isPublic()) {
            throw new IllegalAccessError();
        }
        InstructionUtil.invokeMethod(frame, methodToBeInvoke);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " :  #" + index;
    }
}
