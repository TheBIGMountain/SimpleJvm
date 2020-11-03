package com.dqpi.simple_jvm.native_method.java.lang;

import com.dqpi.simple_jvm.native_method.NativeMethod;
import com.dqpi.simple_jvm.native_method.Natives;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
@Component
public class Object {
    public Object(Natives natives, ApplicationContext ctx) {
        natives.registry("java/lang/Object", "getClass", 
                "()Ljava/lang/Class;", ctx.getBean(GetClass.class));
        natives.registry("java/lang/Object", "hashCode",
                "()I", ctx.getBean(HashCode.class));
        natives.registry("java/lang/Object", "clone",
                "()Ljava/lang/Object;", ctx.getBean(Clone.class));
    }
    
    @Component
    public static class GetClass implements NativeMethod {
        @Override
        public void execute(Thread.Frame frame) {
            Obj thisRef = frame.getLocalVars().getRef(0);
            Obj clazz = thisRef.getClazz().getJClass();
            frame.getOperandStack().pushRef(clazz);
        }
    }

    @Component
    public static class HashCode implements NativeMethod {
        @Override
        public void execute(Thread.Frame frame) {
            Obj thisRef = frame.getLocalVars().getRef(0);
            frame.getOperandStack().pushInt(thisRef.hashCode());
        }
    }

    @Component
    public static class Clone implements NativeMethod {
        @SneakyThrows
        @Override
        public void execute(Thread.Frame frame) {
            Obj thisRef = frame.getLocalVars().getRef(0);

            Clazz cloneable = thisRef.getClazz().getLoader().loadClass("java/lang/Cloneable");
            if (!thisRef.getClazz().isImplements(cloneable)) {
                throw new CloneNotSupportedException();
            }
            
            frame.getOperandStack().pushRef(thisRef.clo());
        }
    }
}
