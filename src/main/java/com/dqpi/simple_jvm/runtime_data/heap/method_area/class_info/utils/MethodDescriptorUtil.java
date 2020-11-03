package com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.utils;

import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */ 
public class MethodDescriptorUtil {
    private MethodDescriptorUtil() {}
    
    private static String descriptor;
    private static int offset;
    private static Method.MethodDescriptor parsed;
    private static final Set<Character> TYPE_SET = new HashSet<>(16);
    
    static {
        TYPE_SET.addAll(Arrays.asList('B', 'C', 'D', 'F', 'I', 'J', 'S', 'Z'));
    }

    public static Method.MethodDescriptor parse(String descriptor) {
        MethodDescriptorUtil.descriptor = descriptor;
        MethodDescriptorUtil.offset = 0;
        MethodDescriptorUtil.parsed = new Method.MethodDescriptor();
        startParams();
        parseParamTypes();
        endParams();
        parseReturnType();
        finish();
        return parsed;
    }
    
    private static void startParams() {
        if (readUint8() != '(') {
            thr();
        }
    }

    private static void parseParamTypes() {
        while (true) {
            String type = parseFieldType();
            if (!"".equals(type)) {
                parsed.getParameterTypes().add(type);
                continue;
            }
            break;
        }
    }
    
    private static void endParams() {
        if (readUint8() != ')') {
            thr();
        }
    }

    private static void parseReturnType() {
        if (readUint8() == 'V') {
            parsed.setReturnType("V");
            return;
        }
        
        offset --;
        String type = parseFieldType();
        if (!"".equals(type)) {
            parsed.setReturnType(type);
            return;
        }
        thr();
    }
    
    private static void finish() {
        if (offset != descriptor.length()) {
            thr();
        }
    }
    
    private static char readUint8() {
        char b = descriptor.charAt(offset);
        offset ++;
        return b;
    }
    
    private static String parseFieldType() {
        char c = readUint8();
        if (TYPE_SET.contains(c)) {
            return String.valueOf(c);
        }
        if (c == 'L') {
            return parseObjectType();
        }
        if (c == '[') {
            return parseArrayType();
        }
        offset --;
        return "";
    }
    
    private static String parseObjectType() {
        int s = descriptor.substring(offset).indexOf(';');
        if (s == -1) {
            thr();
        }
        int objStart = offset - 1;
        int objEnd = offset + s + 1;
        offset = objEnd;
        return descriptor.substring(objStart, objEnd);
    }
    
    private static String parseArrayType() {
        int arrStart = offset - 1;
        parseFieldType();
        int arrEnd = offset;
        return descriptor.substring(arrStart, arrEnd);
    }
    
    @SneakyThrows
    private static void thr() {
        throw new Exception("BAD descriptor: " + descriptor);
    }
}
