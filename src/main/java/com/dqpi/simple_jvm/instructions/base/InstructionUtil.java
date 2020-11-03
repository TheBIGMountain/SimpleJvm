package com.dqpi.simple_jvm.instructions.base;

import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.ClassLoader;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import com.dqpi.simple_jvm.runtime_data.opt.Slot;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
public class InstructionUtil {
    private InstructionUtil() {};
    
    public static final int AT_BOOLEAN = 4;
    public static final int AT_CHAR = 5;
    public static final int AT_FLOAT = 6;
    public static final int AT_DOUBLE = 7;
    public static final int AT_BYTE = 8;
    public static final int AT_SHORT = 9;
    public static final int AT_INT = 10;
    public static final int AT_LONG = 11;
    
    public static final Map<Integer, Fun2> FUN_MAP = new HashMap<>();
    
    static {
        FUN_MAP.put(AT_BOOLEAN, loader -> loader.loadClass("[Z"));
        FUN_MAP.put(AT_BYTE, loader -> loader.loadClass("[B"));
        FUN_MAP.put(AT_CHAR, loader -> loader.loadClass("[C"));
        FUN_MAP.put(AT_SHORT, loader -> loader.loadClass("[S"));
        FUN_MAP.put(AT_INT, loader -> loader.loadClass("[I"));
        FUN_MAP.put(AT_LONG, loader -> loader.loadClass("[J"));
        FUN_MAP.put(AT_FLOAT, loader -> loader.loadClass("[F"));
        FUN_MAP.put(AT_DOUBLE, loader -> loader.loadClass("[D"));
    }
    
    public static void execute(char c, Fun f1, Fun f2, Fun f3, Fun f4, Fun f5) {
        switch (c) {
            case 'Z':
            case 'B':
            case 'C':
            case 'S':
            case 'I':
                f1.execute();
                break;
            case 'F':
                f2.execute();
                break;
            case 'J':
                f3.execute();
                break;
            case 'D':
                f4.execute();
                break;
            case 'L':
            case '[':
                f5.execute();
                break;
            default:
                break;
        }
    }

    public static void initClass(Thread thread, Clazz clazz) {
        clazz.setInitStarted(true);
        scheduleClinit(thread, clazz);
        initSuperClass(thread, clazz);
    }

    public static void branch(Thread.Frame frame, int offset) {
        int pc = frame.getThread().getPc();
        frame.setNextPc(pc + offset);
    }
    
    @SneakyThrows
    public static void invokeMethod(Thread.Frame invokeFrame, Method method) {
        Thread thread = invokeFrame.getThread();
        Thread.Frame newFrame = thread.newFrame(method, false, false);
        thread.pushFrame(newFrame, method);

        int argCount = method.getArgCount();
        if (argCount > 0) {
            for (int i = argCount - 1; i >= 0; i --) {
                Slot slot = invokeFrame.getOperandStack().popSlot();
                newFrame.getLocalVars().setSlot(i, slot);
            }
        }
    }
    
    private static void initSuperClass(Thread thread, Clazz clazz) {
        if (!clazz.isInterface()) {
            Clazz superClass = clazz.getSuperClazz();
            if (superClass != null && !superClass.isInitStarted()) {
                initClass(thread, superClass);
            }
        }
    }

    private static void scheduleClinit(Thread thread, Clazz clazz) {
        Method clinit = clazz.getClinitMethod();
        if (clinit != null) {
            Thread.Frame newFrame = thread.newFrame(clinit, false, false);
            thread.pushFrame(newFrame, clinit);
        }
    }

    @FunctionalInterface
    public interface Fun {
        void execute();
    }
    
    @FunctionalInterface
    public interface Fun2 {
        Clazz execute(ClassLoader loader);
    }
}
