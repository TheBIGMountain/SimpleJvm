package com.dqpi.simple_jvm.runtime_data.heap;

import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Field;
import com.dqpi.simple_jvm.runtime_data.opt.LocalVars;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@EqualsAndHashCode
public class Obj {
    @Resource
    private ApplicationContext ctx;
    @Getter
    private Clazz clazz;
    @Getter
    private Object data;
    @Getter
    @Setter
    private Object extra;
    
    private static final Set<java.lang.Class<?>> CLASSES = new HashSet<>(); 
    
    static {
        CLASSES.addAll(Arrays.asList(Byte.class, Short.class, Integer.class, Long.class, 
                Character.class, Float.class, Double.class, Obj.class));    
    }
    
    public Obj setInfo(Clazz clazz) {
        this.clazz = clazz;
        this.data = ctx.getBean(LocalVars.class).setSlots(clazz.getInstanceSlotCount(), false, true);
        return this;
    }
    
    public Obj setArrInfo(Clazz clazz, Object data) {
        this.clazz = clazz;
        this.data = data;
        return this;
    }
    
    public LocalVars getFields() {
        return (LocalVars) data;
    }
    
    
    public boolean isInstanceOf(Clazz other) {
        return other.isAssignableFrom(clazz);
    }
    
    @SneakyThrows
    public <T> T[] getArr(java.lang.Class<T> cls) {
        if (!CLASSES.contains(cls)) {
            throw new Exception("Not Array");
        }
        return (T[]) data;
    }
 
    @SneakyThrows
    public int getArrLength() {
        try {
            return ((Object[]) data).length;
        }
        catch (Exception e) {
            throw new Exception("Not array!");
        }
    }

    public void setRefVar(String name, String descriptor, Obj ref) {
        Field field = clazz.getField(name, descriptor, false);
        LocalVars vars = (LocalVars) data;
        vars.setRef(field.getFIndex(), ref);
    }
    
    public Obj getRefVar(String name, String descriptor) {
        Field field = clazz.getField(name, descriptor, false);
        LocalVars vars = (LocalVars) data;
        return vars.getRef(field.getFIndex());
    }

    public Obj clo() {
        Obj obj = ctx.getBean(Obj.class).setInfo(clazz);
        obj.data = cloneData();
        return obj;
    }

    private Object cloneData() {
        return this.data;
    }
}
