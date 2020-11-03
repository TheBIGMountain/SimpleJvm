package com.dqpi.simple_jvm.runtime_data.heap;

import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
@Component
public class Arr {
    @Resource
    private ApplicationContext ctx;
    
    private final Map<String, Fun> funMap = new HashMap<>(16);
    
    public Arr() {
        funMap.put("[Z", (clazz, count) -> ctx.getBean(Obj.class).setArrInfo(clazz, new Byte[count]));
        funMap.put("[B", (clazz, count) -> ctx.getBean(Obj.class).setArrInfo(clazz, new Byte[count]));
        funMap.put("[C", (clazz, count) -> ctx.getBean(Obj.class).setArrInfo(clazz, new Character[count]));
        funMap.put("[S", (clazz, count) -> ctx.getBean(Obj.class).setArrInfo(clazz, new Short[count]));
        funMap.put("[I", (clazz, count) -> ctx.getBean(Obj.class).setArrInfo(clazz, new Integer[count]));
        funMap.put("[J", (clazz, count) -> ctx.getBean(Obj.class).setArrInfo(clazz, new Long[count]));
        funMap.put("[F", (clazz, count) -> ctx.getBean(Obj.class).setArrInfo(clazz, new Float[count]));
        funMap.put("[D", (clazz, count) -> ctx.getBean(Obj.class).setArrInfo(clazz, new Double[count]));
    }
    
    @SneakyThrows
    public Obj newArray(Clazz clazz, int count) {
        if (!clazz.isArray()) {
            throw new Exception("Not array class: " + clazz.getName());
        }
        Fun fun = funMap.get(clazz.getName());
        if (fun != null) {
            return fun.execute(clazz, count);
        }
        return ctx.getBean(Obj.class).setArrInfo(clazz, new Obj[count]);
    }

    public Obj newMultiDimensionalArray(int[] counts, Clazz arrClazz) {
        Obj array = newArray(arrClazz, counts[0]);
        
        if (counts.length > 1) {
            Obj[] refs = array.getArr(Obj.class);
            for (int i = 0; i < refs.length; i++) {
                int[] a = Arrays.copyOfRange(counts, 1, counts.length);
                Clazz componentClass = arrClazz.getComponentClass();
                refs[i] = newMultiDimensionalArray(a, componentClass);
            }
        }
        return array;
    }


    @FunctionalInterface
    private interface Fun {
        Obj execute(Clazz clazz, int count);
    }
}
