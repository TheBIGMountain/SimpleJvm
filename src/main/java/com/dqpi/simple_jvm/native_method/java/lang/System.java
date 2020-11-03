package com.dqpi.simple_jvm.native_method.java.lang;

import com.dqpi.simple_jvm.native_method.NativeMethod;
import com.dqpi.simple_jvm.native_method.Natives;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.opt.LocalVars;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
@Component
public class System {
    public System(Natives natives, ApplicationContext ctx) {
        natives.registry("java/lang/System", "arraycopy", 
                "(Ljava/lang/Object;ILjava/lang/Object;II)V", ctx.getBean(ArrayCopy.class));
        natives.registry("java/lang/System", "initProperties", 
                "(Ljava/util/Properties;)Ljava/util/Properties;", ctx.getBean(InitProperties.class));
    }
    
    @Component
    public static class ArrayCopy implements NativeMethod {
        private static final Set<java.lang.Class<?>> TYPE_SET = new HashSet<>(16);
        
        static {
            TYPE_SET.addAll(Arrays.asList(Byte[].class, Short[].class, Integer[].class, Long[].class,
                    Float[].class, Double[].class, Boolean[].class, Character[].class, Obj[].class));
        }
        
        @Override
        public void execute(Thread.Frame frame) {
            LocalVars vars = frame.getLocalVars();
            Obj src = vars.getRef(0);
            int srcPos = vars.getInt(1);
            Obj dest = vars.getRef(2);
            int destPos = vars.getInt(3);
            int length = vars.getInt(4);
            
            if (src == null || dest == null) {
                throw new NullPointerException();
            }
            
            if (!checkArrayCopy(src, dest)) {
                throw new ArrayStoreException();
            }
            
            if (srcPos < 0 || destPos < 0 || length < 0
                || srcPos + length > src.getArrLength() 
                || destPos + length > dest.getArrLength()) {
                throw new IndexOutOfBoundsException();
            }   
            
            arrayCopy(src, dest, srcPos, destPos, length);
        }

        @SneakyThrows
        private void arrayCopy(Obj src, Obj dest, int srcPos, int destPos, int length) {
            if (!TYPE_SET.contains(src.getData().getClass())) {
                throw new Exception("Not Array!");
            }
            java.lang.System.arraycopy(src.getData(), srcPos, dest.getData(), destPos, length);
        }

        private boolean checkArrayCopy(Obj src, Obj dest) {
            Clazz srcClazz = src.getClazz();
            Clazz destClazz = dest.getClazz();
            
            if (!srcClazz.isArray() || !destClazz.isArray()) {
                return false;
            }
            if (srcClazz.getComponentClass().isPrimitive() ||
                destClazz.getComponentClass().isPrimitive()) {
                return srcClazz == destClazz;
            }
            return true;
        }
    }
    
    @Component
    public static class InitProperties implements NativeMethod {
        @Override
        public void execute(Thread.Frame frame) {
            LocalVars vars = frame.getLocalVars();
            Obj props = vars.getRef(0);
            OperandStack stack = frame.getOperandStack();
            stack.pushRef(props);

            /*var setPropMethod = props.getClazz().getInterfaceMethod("setProperty", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;");
            var thread = frame.getThread();*/
            

            
        }
    }
}
