package com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref;

import com.dqpi.simple_jvm.classfile.constant_pool.constant_info.CharInfo;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.RtConstantPool;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ClassRef extends SymRef {
    public ClassRef setInfo(RtConstantPool rtConstantPool, CharInfo.ConstantClassInfo classInfo) {
        this.rtConstantPool = rtConstantPool;
        this.className = classInfo.getName();
        return this;
    }

    public static Method lookupMethodInClass(Clazz clazz, String name, String descriptor) {
        if (clazz == null) {
            return null;
        }

        if (clazz.getMethods() != null) {
            for (Method clazzMethod : clazz.getMethods()) {
                if (clazzMethod.getName().equals(name) && clazzMethod.getDescriptor().equals(descriptor)) {
                    return clazzMethod;
                }
            }
        }
        return lookupMethodInClass(clazz.getSuperClazz(), name, descriptor);
    }

    public static Method lookupMethodInInterfaces(Clazz[] interfaces, String name, String descriptor) {
        for (Clazz iface : interfaces) {
            for (Method ifaceMethod : iface.getMethods()) {
                if (ifaceMethod.getName().equals(name) && ifaceMethod.getDescriptor().equals(descriptor)) {
                    return ifaceMethod;
                }
            }

            Method method = null;
            Clazz[] ifaces = iface.getInterfaces();
            if (ifaces != null) {
                method = lookupMethodInInterfaces(ifaces, name, descriptor);
            }
            if (method != null) {
                return method;
            }
        }
        return null;
    }
}
