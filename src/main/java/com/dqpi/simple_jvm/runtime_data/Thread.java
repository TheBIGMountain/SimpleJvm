package com.dqpi.simple_jvm.runtime_data;

import com.dqpi.simple_jvm.painter.Painter;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.ClassRef;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.FieldRef;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.InterfaceMethodRef;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.MethodRef;
import com.dqpi.simple_jvm.runtime_data.opt.LocalVars;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;


/**
 * @author TheBIGMountain
 * @date 2020/10/28
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Thread {
    @Resource
    private ApplicationContext ctx;
    @Getter
    @Setter
    private int pc;
    private final Stack stack;
    
    public Thread(ApplicationContext ctx) {
        this.stack = ctx.getBean(Stack.class);
        stack.setMaxSize(1024);
    }
    
    public void pushFrame(Frame frame, Method method) {
        stack.push(frame, method);
    }
    
    public Frame popFrame() {
        if(currentFrame().lower == null) {
            return stack.pop(null);
        }
        return stack.pop(currentFrame().lower.method);
    }
    
    public Frame currentFrame() {
        return stack.peek();
    }
    
    public Frame newFrame(Method method, boolean isStaticVars, boolean isFieldVars) {
        Frame frame = ctx.getBean(Frame.class);
        frame.thread = this;
        frame.method = method;
        frame.setLocalVars(method.getMaxLocals(), isStaticVars, isFieldVars);
        frame.setOperandStack(method.getMaxStack());
        return frame;
    }

    public boolean isStackEmpty() {
        return stack.isEmpty();
    }

    public void clearStack() {
        stack.clear();
    }
    
    public int getFrameSize() {
        return stack.size;
    }

    public Frame[] getFrames(int start, int end) {
        Frame[] frames = new Frame[end - start];
        int i = 0;
        for (Frame f = stack.top; f != null; f = f.lower) {
            i ++;
            if (i < start) {
                continue;
            }
            if (i == end) {
                break;
            }
            frames[i - start] = f;
        }
        return frames;
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class Stack {
        @Setter
        private int maxSize;
        private int size;
        private Frame top;

        public void push(Frame frame, Method method) {
            if (size >= maxSize) {
                throw new StackOverflowError();
            }
            if (top != null) {
                frame.lower = top;
            }
            top = frame;
            size ++;
            Painter.methodNames[Painter.methodIndex ++] = method.getName();
            LocalVars.isStatic = method.isStatic();
            setData(method);
        }
        
        @SneakyThrows
        public Frame pop(Method currentMethod) {
            if (top == null) {
                throw new Exception("jvm stack is empty!");
            }
            Frame tempTop = top;
            top = top.lower;
            tempTop.lower = null;
            size --;
            if (currentMethod != null) {
                Painter.methodNames[Painter.methodIndex --] = null;
                LocalVars.isStatic = currentMethod.isStatic();
                setData(currentMethod);
            }
            return tempTop;
        }
        
        private void setData(Method method) {
            Painter.threadStackLength = size;
            Painter.instSize = 0;
            Arrays.fill(Painter.instructions, null);

            Painter.varsLength = 0;
            Painter.varValue = null;

            Painter.opStackLength = 0;
            Painter.opValue = null;

            Object[] constants = method.getClazz().getRtConstantPool().getConstants();
            Painter.rtcpLength = constants.length - 1;
            Painter.rtcpValue = new String[constants.length - 1];
            for (int i = 1; i < constants.length; i ++) {
                if (constants[i] instanceof Integer) {
                    Painter.rtcpValue[i - 1] = "#" + i + "  " + constants[i];
                }
                else if (constants[i] instanceof Float) {
                    Painter.rtcpValue[i - 1] = "#" + i + "  " + constants[i];
                }
                else if (constants[i] instanceof Long) {
                    Painter.rtcpValue[i - 1] = "#" + i + "  " + constants[i];
                    i ++;
                }
                else if (constants[i] instanceof Double) {
                    Painter.rtcpValue[i - 1] = "#" + i + "  " + constants[i];
                    i ++;
                }
                else if (constants[i] instanceof String) {
                    Painter.rtcpValue[i - 1] = "#" + i + "  " + constants[i];
                }
                else if (constants[i] instanceof ClassRef) {
                    ClassRef classRef = (ClassRef) constants[i];
                    int index = classRef.className.lastIndexOf('/');
                    Painter.rtcpValue[i - 1] = "#" + i + "  " + classRef.className.substring(index == -1 ? 0 : index + 1);
                }
                else if (constants[i] instanceof FieldRef) {
                    FieldRef fieldRef = (FieldRef) constants[i];
                    Painter.rtcpValue[i - 1] = "#" + i + "  " + fieldRef.getName();
                }
                else if (constants[i] instanceof MethodRef) {
                    MethodRef methodRef = (MethodRef) constants[i];
                    Painter.rtcpValue[i - 1] = "#" + i + "  " + methodRef.getName();
                }
                else if (constants[i]  instanceof InterfaceMethodRef) {
                    InterfaceMethodRef interfaceMethodRef = (InterfaceMethodRef) constants[i];
                    Painter.rtcpValue[i - 1] = "#" + i + "  " + interfaceMethodRef.getName();
                }
            }
        }

        @SneakyThrows
        public Frame peek() {
            if (top == null) {
                throw new Exception("jvm stack is empty!");
            }
            return top;
        }

        public boolean isEmpty() {
            return top == null;
        }

        public void clear() {
            while (!isEmpty()) {
                pop(top.lower.method);
            }
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class Frame {
        @Resource
        private ApplicationContext ctx;
        @Getter
        private Frame lower;
        @Getter
        private LocalVars localVars;
        @Getter
        private OperandStack operandStack;
        @Getter
        private Thread thread;
        @Getter
        private Method method;
        @Getter
        @Setter
        private int nextPc;

        private void setLocalVars(int maxLocals, boolean isStaticVars, boolean isFieldVars) {
            localVars = ctx.getBean(LocalVars.class);
            localVars.setSlots(maxLocals, isStaticVars, isFieldVars);
        }

        private void setOperandStack(int maxStack) {
            operandStack = ctx.getBean(OperandStack.class);
            operandStack.setSlots(maxStack);
        }

        public void revertNextPc() {
            this.nextPc = this.thread.pc;
        }
    }
}
