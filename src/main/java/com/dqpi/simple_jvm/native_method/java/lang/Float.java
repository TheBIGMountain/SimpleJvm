package com.dqpi.simple_jvm.native_method.java.lang;

import com.dqpi.simple_jvm.native_method.NativeMethod;
import com.dqpi.simple_jvm.native_method.Natives;
import com.dqpi.simple_jvm.runtime_data.Thread;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
@Component
public class Float {
    
    public Float(Natives natives, ApplicationContext ctx) {
        natives.registry("java/lang/Float", "floatToRawIntBits", 
                "(F)I", ctx.getBean(FloatToRawIntBits.class));
    }
    
    @Component
    public static class FloatToRawIntBits implements NativeMethod {

        @Override
        public void execute(Thread.Frame frame) {
            float val = frame.getLocalVars().getFloat(0);
            int bits = java.lang.Float.floatToIntBits(val);
            frame.getOperandStack().pushInt(bits);
        }
    }
}
