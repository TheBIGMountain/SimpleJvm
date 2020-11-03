package com.dqpi.simple_jvm.native_method.java.lang;

import com.dqpi.simple_jvm.native_method.NativeMethod;
import com.dqpi.simple_jvm.native_method.Natives;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.string_pool.StringPool;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
@Component
public class String {
    public String(Natives natives, ApplicationContext ctx) {
        natives.registry("java/lang/String", "intern", 
                "()Ljava/lang/String;", ctx.getBean(Intern.class));
    }
    
    @Component
    public static class Intern implements NativeMethod {
        @Resource
        private StringPool stringPool;
        
        @Override
        public void execute(Thread.Frame frame) {
            Obj thisRef = frame.getLocalVars().getRef(0);
            Obj interned = stringPool.internString(thisRef);
            frame.getOperandStack().pushRef(interned);
        }
    }
}
