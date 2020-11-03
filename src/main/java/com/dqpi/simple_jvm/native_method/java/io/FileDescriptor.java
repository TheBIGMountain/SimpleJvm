package com.dqpi.simple_jvm.native_method.java.io;

import com.dqpi.simple_jvm.native_method.Natives;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/11/1
 */
@Component
public class FileDescriptor {
    public FileDescriptor(Natives natives) {
        natives.registry("java/io/FileDescriptor", "set",
                "(I)J", frame -> frame.getOperandStack().pushLong(0));
        natives.registry("java/io/FileDescriptor", "initIDs",
                "()V", frame -> {});
    }
}
