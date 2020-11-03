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
public class MethodRef extends SymRef.MemberRef {
    Method method;

    public MethodRef setInfo(RtConstantPool rtConstantPool, MemberrefInfo.ConstantMethodrefInfo methodrefInfo) {
        this.rtConstantPool = rtConstantPool;
        copyMemberRefInfo(methodrefInfo);
        return this;
    }

    public Method resolveMethod() {
        if (this.method == null) {
            resolveMethodRef();
        }
        return this.method;
    }

    private void resolveMethodRef() {
        resolvedClass();
        if (clazz.isInterface()) {
            throw new IncompatibleClassChangeError();
        }

        Method method = lookupMethod(clazz, name, descriptor);
        if (method == null) {
            throw new NoSuchMethodError();
        }
        if (!method.isAccessibleTo(rtConstantPool.getClazz())) {
            throw new IllegalAccessError();
        }

        this.method = method;
    }

    private Method lookupMethod(Clazz clazz, String name, String descriptor) {
        Method method = ClassRef.lookupMethodInClass(clazz, name, descriptor);
        if (method == null) {
            method = ClassRef.lookupMethodInInterfaces(clazz.getInterfaces(), name, descriptor);
        }
        return method;
    }
}
