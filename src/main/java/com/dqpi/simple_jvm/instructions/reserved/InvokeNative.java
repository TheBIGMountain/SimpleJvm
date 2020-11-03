package com.dqpi.simple_jvm.instructions.reserved;

import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.native_method.NativeMethod;
import com.dqpi.simple_jvm.native_method.Natives;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
@Component
public class InvokeNative extends NoOperandsInstruction {
    @Resource
    private Natives natives;
    /**
     * 执行指令
     *
     * @param frame 当前栈帧
     */
    @Override
    public void execute(Thread.Frame frame) {
        Method method = frame.getMethod();
        String className = method.getClazz().getName();
        String methodName = method.getName();
        String descriptor = method.getDescriptor();

        NativeMethod nativeMethod = natives.findNativeMethod(className, methodName, descriptor);
        if (nativeMethod == null) {
            String methodInfo = className + "." + methodName + descriptor;
            throw new UnsatisfiedLinkError(methodInfo);
        }
        nativeMethod.execute(frame);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
