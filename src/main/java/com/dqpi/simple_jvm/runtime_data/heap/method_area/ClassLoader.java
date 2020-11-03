package com.dqpi.simple_jvm.runtime_data.heap.method_area;

import com.dqpi.simple_jvm.classfile.ClassFile;
import com.dqpi.simple_jvm.classpath.ClassPath;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.ClassFactory;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Field;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.utils.ClassUtil;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.RtConstantPool;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.string_pool.StringPool;
import com.dqpi.simple_jvm.runtime_data.opt.LocalVars;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ClassLoader {
    @Resource
    private ApplicationContext ctx;
    @Resource
    private ClassPath classPath;
    @Resource
    private ClassFactory factory;
    private final Map<String, Clazz> classMap = new HashMap<>();
    private boolean verboseFlag;
    
    public ClassLoader setInfo(boolean verboseFlag) {
        this.verboseFlag = verboseFlag;
        loadBasicClasses();
        loadPrimitiveClasses();
        return this;
    }
    
    private void loadBasicClasses() {
        Clazz jClassClass = loadClass("java/lang/Class");
        classMap.values().forEach(clazz -> {
            if (clazz.getJClass() == null) {
                clazz.setJClass(ctx.getBean(Obj.class).setInfo(jClassClass));
                clazz.getJClass().setExtra(clazz);
            }
        });
    }

    private void loadPrimitiveClasses() {
        ClassUtil.PRIMITIVE_TYPES.keySet().forEach(className -> {
            Clazz clazz = factory.newClass(
                    className, this, ctx.getBean(Obj.class).setInfo(classMap.get("java/lang/Class")));
            classMap.put(className, clazz);
        });
    }
    
    public Clazz loadClass(String name) {
        Clazz clazz = classMap.get(name);
        if (clazz != null) {
            return clazz;
        }
        
        if (name.charAt(0) == '[') {
            clazz =  loadArrayClass(name);
        }
        else {
            clazz = loadNonArrayClass(name);
        }

        Clazz jClassClass = classMap.get("java/lang/Class");
        if (jClassClass != null) {
            clazz.setJClass(ctx.getBean(Obj.class).setInfo(jClassClass));
            clazz.getJClass().setExtra(clazz);
        }
        return clazz;
    }

    private Clazz loadArrayClass(String name) {
        Clazz clazz = factory.newArray(name, this);
        classMap.put(name, clazz);
        return clazz;
    }

    @SneakyThrows
    private Clazz loadNonArrayClass(String name) {
        byte[] data = classPath.readClass(name);
        if (data == null) {
            throw new ClassNotFoundException(name);
        }
        Clazz clazz = defineClass(data);
        link(clazz);
        if (verboseFlag) {
            System.out.println("Loaded " + name);
        }
        return clazz;
    }

    private Clazz defineClass(byte[] data) {
        Clazz clazz = parseClass(data);
        clazz.setLoader(this);
        resolveSuperClass(clazz);
        resolveInterfaces(clazz);
        classMap.put(clazz.getName(), clazz);
        return clazz;
    }

    private Clazz parseClass(byte[] data) {
        ClassFile classFile = ctx.getBean(ClassFile.class).parse(data);
        return factory.newClass(classFile);
    }

    private void resolveSuperClass(Clazz clazz) {
        if (!"java/lang/Object".equals(clazz.getName())) {
            clazz.setSuperClazz(clazz.getLoader().loadClass(clazz.getSuperClassName()));
        }
    }

    private void resolveInterfaces(Clazz clazz) {
        String[] interfaceNames = clazz.getInterfaceNames();
        int count = interfaceNames.length;
        if (count > 0) {
            clazz.setInterfaces(new Clazz[count]);
            for (int i = 0; i < count; i++) {
                clazz.getInterfaces()[i] = clazz.getLoader().loadClass(interfaceNames[i]);
            }
        }
    }

    private void link(Clazz clazz) {
        verify(clazz);
        prepare(clazz);
    }

    private void verify(Clazz clazz) {}
    
    private void prepare(Clazz clazz) {
        calcInstanceFieldSlotIds(clazz);
        calcStaticFieldSlotIds(clazz);
        allocAndInitStaticVars(clazz);
    }

    private void calcInstanceFieldSlotIds(Clazz clazz) {
        int slotId = 0;
        if (clazz.getSuperClazz() != null) {
            slotId = clazz.getSuperClazz().getInstanceSlotCount();
        }
        for (Field field : clazz.getFields()) {
            if (!field.isStatic()) {
                field.setFIndex(slotId);
                slotId ++;
                if (field.isLongOrDouble()) {
                    slotId ++;
                }
            }
        }
        clazz.setInstanceSlotCount(slotId);
    }

    private void calcStaticFieldSlotIds(Clazz clazz) {
        int slotId = 0;
        for (Field field : clazz.getFields()) {
            if (field.isStatic()) {
                field.setFIndex(slotId);
                slotId ++;
                if (field.isLongOrDouble()) {
                    slotId ++;
                }
            }
        }
        clazz.setStaticSlotCount(slotId);
    }

    private void allocAndInitStaticVars(Clazz clazz) {
        LocalVars staticVars = ctx.getBean(LocalVars.class).setSlots(clazz.getStaticSlotCount(), true, true);
        clazz.setStaticVars(staticVars);
        for (Field field : clazz.getFields()) {
            if (field.isStatic() && field.isFinal()) {
                initStaticFinalVar(clazz, field);
            }
        }
    }

    private void initStaticFinalVar(Clazz clazz, Field field) {
        StringPool stringPool = ctx.getBean(StringPool.class);
        LocalVars vars = clazz.getStaticVars();
        RtConstantPool rtcp = clazz.getRtConstantPool();
        int index = field.getConstantValueIndex();
        int fIndex = field.getFIndex();
        
        if (index > 0) {
            switch (field.getDescriptor()) {
                case "Z":
                case "B":
                case "C":
                case "S":
                case "I":
                    vars.setInt(fIndex, (Integer) rtcp.getConstant(index));
                    break;
                case "J":
                    vars.setLong(fIndex, (Long) rtcp.getConstant(index));
                    break;
                case "F":
                    vars.setFloat(fIndex, (Float) rtcp.getConstant(index));
                    break;
                case "D":
                    vars.setDouble(fIndex, (Double) rtcp.getConstant(index));
                    break;
                case "Ljava/lang/String;":
                    String str = (String) rtcp.getConstant(index);
                    Obj strObj = stringPool.toJString(clazz.getLoader(), str);
                    vars.setRef(fIndex, strObj);
                    break;
                default:
                    break;
            }
        }
    }

}
