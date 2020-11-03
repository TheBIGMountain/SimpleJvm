package com.dqpi.simple_jvm.native_method.java.io;

import com.dqpi.simple_jvm.native_method.Natives;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/11/1
 */
@Component
public class FileInputStream {
    public FileInputStream(Natives natives) {
        natives.registry("java/io/FileInputStream", "initIDs",
                "()V", frame -> {});
    }
}
