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
public class Double {
    public Double(Natives natives, ApplicationContext ctx) {
        natives.registry("java/lang/Double", "doubleToRawLongBits",
                "(D)J", ctx.getBean(DoubleToRawIntBits.class));
        natives.registry("java/lang/Double", "longBitsToDouble",
                "(J)D", ctx.getBean(LongBitsToDouble.class));
    }

    @Component
    public static class DoubleToRawIntBits implements NativeMethod {

        @Override
        public void execute(Thread.Frame frame) {
            double val = frame.getLocalVars().getDouble(0);
            long bits = java.lang.Double.doubleToLongBits(val);
            frame.getOperandStack().pushLong(bits);
        }
    }

    @Component
    public static class LongBitsToDouble implements NativeMethod {

        @Override
        public void execute(Thread.Frame frame) {
            long val = frame.getLocalVars().getLong(0);
            double value = java.lang.Double.longBitsToDouble(val);
            frame.getOperandStack().pushDouble(value);
        }
    }
}
