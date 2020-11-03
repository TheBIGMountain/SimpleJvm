package com.dqpi.simple_jvm.native_method.sun.misc;

import com.dqpi.simple_jvm.native_method.Natives;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/11/1
 */
@Component
public class Unsafe {
    public Unsafe(Natives natives) {
        natives.registry("sun/misc/Unsafe", "arrayBaseOffset", "(Ljava/lang/Class;)I", 
                frame -> {
                    frame.getOperandStack().pushInt(0);
                });
        natives.registry("sun/misc/Unsafe", "arrayIndexScale", "(Ljava/lang/Class;)I",
                frame -> {
                    frame.getOperandStack().pushInt(1);
                });
        natives.registry("sun/misc/Unsafe", "addressSize", "()I",
                frame -> {
                    frame.getOperandStack().pushInt(8);
                });
    }
}
