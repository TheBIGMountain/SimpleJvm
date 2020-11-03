package com.dqpi.simple_jvm.native_method.sun.misc;

import com.dqpi.simple_jvm.instructions.base.InstructionUtil;
import com.dqpi.simple_jvm.native_method.NativeMethod;
import com.dqpi.simple_jvm.native_method.Natives;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.ClassLoader;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
@Component
public class VM {
    public VM(Natives natives, ApplicationContext ctx) {
        natives.registry("sun/misc/VM", "initialize", 
                "()V", ctx.getBean(Initialize.class));
    }
    
    @Component
    public static class Initialize implements NativeMethod {
        @Override
        public void execute(Thread.Frame frame) {
            ClassLoader loader = frame.getMethod().getClazz().getLoader();
            Clazz jSysClass = loader.loadClass("java/lang/System");
            Method initSysMethod = jSysClass.getStaticMethod("initializeSystemClass", "()V");
            InstructionUtil.invokeMethod(frame, initSysMethod);
        }
    }
}
