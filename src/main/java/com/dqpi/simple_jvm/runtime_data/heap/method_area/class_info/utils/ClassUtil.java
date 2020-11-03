package com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.utils;

import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
public class ClassUtil {
    public static final Map<String, String> PRIMITIVE_TYPES = new HashMap<>();

    static {
        PRIMITIVE_TYPES.put("void", "V");
        PRIMITIVE_TYPES.put("boolean", "Z");
        PRIMITIVE_TYPES.put("byte", "B");
        PRIMITIVE_TYPES.put("short", "S");
        PRIMITIVE_TYPES.put("int", "I");
        PRIMITIVE_TYPES.put("long", "J");
        PRIMITIVE_TYPES.put("char", "C");
        PRIMITIVE_TYPES.put("float", "F");
        PRIMITIVE_TYPES.put("double", "D");
    }
    
    private ClassUtil() {}

    public static boolean isJlObject(Clazz clazz) {
        return "java/lang/Object".equals(clazz.getName());
    }
    
    public static boolean isJlCloneable(Clazz clazz) {
        return "java/lang/Cloneable".equals(clazz.getName());
    }
    
    public static boolean isJioSerializable(Clazz clazz) {
        return "java/lang/Serializable".equals(clazz.getName());
    }
    
    public static boolean isImplements(Clazz iface, Clazz clazz) {
        if (clazz == null) {
            return false;
        }

        if (clazz.getInterfaces() != null) {
            for (Clazz i : clazz.getInterfaces()) {
                if (i == iface) {
                    return true;
                }
            }
        }
        return isImplements(iface, clazz.getSuperClazz());
    }

    public static String getArrayClassName(String className) {
        return "[" + toDescriptor(className);
    }

    private static String toDescriptor(String className) {
        if (className.charAt(0) == '[') {
            return className;
        }
        String type = PRIMITIVE_TYPES.get(className);
        if (type != null) {
            return type;
        }
        return "L" + className + ";";
    }
    
    @SneakyThrows
    public static String getComponentClassName(String className) {
        if (className.charAt(0) == '[') {
            return toClassName(className.substring(1));
        }
        throw new Exception("Not array: " + className);
    }

    @SneakyThrows
    private static String toClassName(String descriptor) {
        if (descriptor.charAt(0) == '[') {
            return descriptor;
        }
        if (descriptor.charAt(0) == 'L') {
            return descriptor.substring(1, descriptor.length() - 1);
        }
        for (Map.Entry<String, String> entry : PRIMITIVE_TYPES.entrySet()) {
            if (entry.getValue().equals(descriptor)) {
                return entry.getKey();
            }
        }
        throw new Exception("Invalid descriptor: " + descriptor);
    }
}
