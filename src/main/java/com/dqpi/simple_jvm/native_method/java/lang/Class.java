package com.dqpi.simple_jvm.native_method.java.lang;

import com.dqpi.simple_jvm.native_method.NativeMethod;
import com.dqpi.simple_jvm.native_method.Natives;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.ClassLoader;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.string_pool.StringPool;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
@Component
public class Class {
    
    public Class(Natives natives, ApplicationContext ctx) {
        natives.registry("java/lang/Class", "getPrimitiveClass", 
                "(Ljava/lang/String;)Ljava/lang/Class;", ctx.getBean(GetPrimitiveClass.class));
        natives.registry("java/lang/Class", "getName0",
                "()Ljava/lang/String;", ctx.getBean(GetName0.class));
        natives.registry("java/lang/Class", "desiredAssertionStatus0",
                "(Ljava/lang/Class;)Z", ctx.getBean(DesiredAssertionStatus0.class));
    }
    
    @Component
    public static class GetPrimitiveClass implements NativeMethod {
        @Resource
        private StringPool stringPool;
        @Override
        public void execute(Thread.Frame frame) {
            Obj nameObj = frame.getLocalVars().getRef(0);
            java.lang.String name = stringPool.getString(nameObj);

            ClassLoader loader = frame.getMethod().getClazz().getLoader();
            Obj jClass = loader.loadClass(name).getJClass();
            
            frame.getOperandStack().pushRef(jClass);
        }
    }
    
    @Component
    public static class GetName0 implements NativeMethod {
        @Resource
        private StringPool stringPool;
        @Override
        public void execute(Thread.Frame frame) {
            Obj thisRef = frame.getLocalVars().getRef(0);
            Clazz clazz = (Clazz) thisRef.getExtra();

            java.lang.String name = clazz.getJavaName();
            Obj nameObj = stringPool.toJString(clazz.getLoader(), name);
            
            frame.getOperandStack().pushRef(nameObj);
        }
    }

    @Component
    public static class DesiredAssertionStatus0 implements NativeMethod {
        @Resource
        private StringPool stringPool;
        @Override
        public void execute(Thread.Frame frame) {
            frame.getOperandStack().pushInt(0);
        }
    }
}
