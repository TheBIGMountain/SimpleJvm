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
import com.dqpi.simple_jvm.runtime_data.heap.method_area.string_pool.StringPool;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import com.dqpi.simple_jvm.vm.MainVm;
import lombok.SneakyThrows;
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
public class INVOKE_VIRTUAL extends Index16Instruction {
    @Resource
    private StringPool stringPool;
    @Resource
    private MainVm mainVm;
    
    /**
     * 执行指令
     *
     * @param frame 当前栈帧
     */
    @SneakyThrows
    @Override
    public void execute(Thread.Frame frame) {
        Clazz currentClass = frame.getMethod().getClazz();
        RtConstantPool rtcp = currentClass.getRtConstantPool();
        MethodRef methodRef = (MethodRef) rtcp.getConstant(index);
        Method method = methodRef.resolveMethod();
        Clazz methodClazz = method.getClazz();

        if (method.isStatic()) {
            throw new IncompatibleClassChangeError();
        }

        Obj ref = frame.getOperandStack().getRefFromTop(method.getArgCount());
        if (ref == null) {
            if ("println".equals(method.getName())) {
                String descriptor = method.getDescriptor();
                OperandStack stack = frame.getOperandStack();
                switch (descriptor) {
                    case "(Z)V":
                        mainVm.setConsole((stack.popInt() != 0) + "");
                        break;
                    case "(C)V":
                    case "(I)V":
                    case "(B)V":
                    case "(S)V":
                        mainVm.setConsole(stack.popInt() + "");
                        break;
                    case "(F)V":
                        mainVm.setConsole(stack.popFloat() + "");
                        break;
                    case "(J)V":
                        mainVm.setConsole(stack.popLong() + "");
                        break;
                    case "(D)V":
                        mainVm.setConsole(stack.popDouble() + "");
                    case "(Ljava/lang/String;)V":
                        mainVm.setConsole(stringPool.getString(stack.popRef()));
                        break;
                    default:
                        break;
                }
                stack.popRef();
                return;
            }
            throw new NullPointerException();
        }

        if (method.isProtected() && methodClazz.isSuperClassOf(currentClass)
                && !methodClazz.getPackageName().equals(currentClass.getPackageName())
                && ref.getClazz() != currentClass && !ref.getClazz().isSubClassOf(currentClass)) {
            throw new IllegalAccessError();
        }

        Method methodToBeInvoke = ClassRef.lookupMethodInClass(
                ref.getClazz(), methodRef.getName(), methodRef.getDescriptor());
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
