package com.dqpi.simple_jvm.native_method;

import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
@Component
public class Natives {
    private final Map<String, NativeMethod> registry = new HashMap<>();
    
    public void registry(String className, String methodName, String descriptor, NativeMethod method) {
        String key = className + "~" + methodName + "~" + descriptor;
        this.registry.put(key, method);
    }
    
    public NativeMethod findNativeMethod(String className, String methodName, String descriptor) {
        String key = className + "~" + methodName + "~" + descriptor;
        NativeMethod method = registry.get(key);
        if (method != null) {
            return method;
        }
        if ("()V".equals(descriptor) && "registerNatives".equals(methodName)) {
            return frame -> {};
        }
        return null;
    }
    
    
}
