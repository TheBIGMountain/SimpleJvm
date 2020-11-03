package com.dqpi.simple_jvm.instructions.references;

import com.dqpi.simple_jvm.instructions.base.Index16Instruction;
import com.dqpi.simple_jvm.instructions.base.InstructionUtil;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.RtConstantPool;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.ClassRef;
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
public class INVOKE_SPECIAL extends Index16Instruction {
    /**
     * 执行指令
     *
     * @param frame 当前栈帧
     */
    @Override
    public void execute(Thread.Frame frame) {
        Clazz currentClass = frame.getMethod().getClazz();
        RtConstantPool rtcp = currentClass.getRtConstantPool();
        MethodRef methodRef = (MethodRef) rtcp.getConstant(index);
        Clazz methodRefClass = methodRef.resolvedClass();
        Method method = methodRef.resolveMethod();
        Clazz methodClazz = method.getClazz();

        if ("<init>".equals(method.getName()) && methodClazz != methodRefClass) {
            throw new NoSuchMethodError();
        }
        if (method.isStatic()) {
            throw new IncompatibleClassChangeError();
        }

        Obj ref = frame.getOperandStack().getRefFromTop(method.getArgCount());
        if (ref == null) {
            throw new NullPointerException();
        }

        if (method.isProtected() && methodClazz.isSuperClassOf(currentClass)
                && !methodClazz.getPackageName().equals(currentClass.getPackageName())
                && ref.getClazz() != currentClass && !ref.getClazz().isSubClassOf(currentClass)) {
            throw new IllegalAccessError();
        }


        Method methodToBeInvoke = method;
        if (currentClass.isSuper() && methodRefClass.isSuperClassOf(currentClass)
                && !"<init>".equals(method.getName())) {
            methodToBeInvoke = ClassRef.lookupMethodInClass(
                    currentClass.getSuperClazz(), methodRef.getName(), methodRef.getDescriptor());
        }

        if (methodToBeInvoke == null || methodToBeInvoke.isAbstract()) {
            throw new AbstractMethodError();
        }
        InstructionUtil.invokeMethod(frame, methodToBeInvoke);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " :  #" + index;
    }
}
