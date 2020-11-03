package com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref;

import com.dqpi.simple_jvm.classfile.constant_pool.constant_info.MemberrefInfo;
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
public  class InterfaceMethodRef extends SymRef.MemberRef {
    Method method;

    public InterfaceMethodRef setInfo(RtConstantPool rtConstantPool, MemberrefInfo.ConstantInterFaceMethodrefInfo methodrefInfo) {
        this.rtConstantPool = rtConstantPool;
        copyMemberRefInfo(methodrefInfo);
        return this;
    }

    public Method resolveInterfaceMethod() {
        if (this.method == null) {
            resolveInterfaceMethodRef();
        }
        return this.method;
    }

    private void resolveInterfaceMethodRef() {
        resolvedClass();
        if (!clazz.isInterface()) {
            throw new IncompatibleClassChangeError();
        }

        Method method = lookupInterfaceMethod(clazz, name, descriptor);
        if (method == null) {
            throw new NoSuchMethodError();
        }
        if (!method.isAccessibleTo(rtConstantPool.getClazz())) {
            throw new IllegalAccessError();
        }
        this.method = method;
    }

    private Method lookupInterfaceMethod(Clazz iface, String name, String descriptor) {
        for (Method ifaceMethod : iface.getMethods()) {
            if (ifaceMethod.getName().equals(name) && ifaceMethod.getDescriptor().equals(descriptor)) {
                return ifaceMethod;
            }
        }

        return ClassRef.lookupMethodInInterfaces(iface.getInterfaces(), name, descriptor);
    }
}
