package com.dqpi.simple_jvm.native_method.java.lang;

import com.dqpi.simple_jvm.native_method.NativeMethod;
import com.dqpi.simple_jvm.native_method.Natives;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/11/1
 */
@Component
public class Throwable {
    public Throwable(Natives natives, ApplicationContext ctx) {
        natives.registry("java/lang/Throwable", "fillInStackTrace",
                "(I)Ljava/lang/Throwable;", ctx.getBean(FillInStackTrace.class));
    }
    
    @Component
    public static class FillInStackTrace implements NativeMethod {

        @Override
        public void execute(Thread.Frame frame) {
            Obj thisRef = frame.getLocalVars().getRef(0);
            frame.getOperandStack().pushRef(thisRef);

            StackTraceElement[] stes = createStackTraceElements(thisRef, frame.getThread());
            thisRef.setExtra(stes);
        }

        private StackTraceElement[] createStackTraceElements(Obj tObj, Thread thread) {
            int skip = distanceToObject(tObj.getClazz());
            Thread.Frame[] frames = thread.getFrames(skip, thread.getFrameSize());
            StackTraceElement[] stes = new StackTraceElement[frames.length];
            for (int i = 0; i < frames.length; i ++) {
                stes[i] = createStackTraceElement(frames[i]);
            }
            return stes;
        }

        private StackTraceElement createStackTraceElement(Thread.Frame frame) {
            Method method = frame.getMethod();
            Clazz clazz = method.getClazz();
            return new StackTraceElement(
                    clazz.getSourceFile(),
                    clazz.getJavaName(),
                    method.getName(),
                    method.getLineNumber(frame.getNextPc() - 1)
            );
        }

        private int distanceToObject(Clazz clazz) {
            int distance = 0;
            for (Clazz c = clazz; c != null; c = c.getSuperClazz()) {
                distance ++;
            }
            return distance;
        }
    }
    
    @AllArgsConstructor
    @Getter
    public static class StackTraceElement {
        private final java.lang.String fileName;
        private final java.lang.String className;
        private final java.lang.String methodName;
        private final int lineNumber;
    }
}
