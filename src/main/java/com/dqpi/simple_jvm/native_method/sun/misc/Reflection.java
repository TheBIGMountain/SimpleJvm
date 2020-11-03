package com.dqpi.simple_jvm.native_method.sun.misc;

import com.dqpi.simple_jvm.native_method.Natives;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/11/1
 */
@Component
public class Reflection {
    public Reflection(Natives natives) {
        natives.registry("sun/reflect/Reflection", "getCallerClass", "()Ljava/lang/Class;",
                frame -> {
                    
                });
    }
}
