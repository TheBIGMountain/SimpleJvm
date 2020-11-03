package com.dqpi.simple_jvm.native_method;

import com.dqpi.simple_jvm.runtime_data.Thread;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
@FunctionalInterface
public interface NativeMethod {
    void execute(Thread.Frame frame);
}
