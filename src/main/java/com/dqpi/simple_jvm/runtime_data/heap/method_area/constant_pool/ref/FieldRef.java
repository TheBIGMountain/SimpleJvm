package com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref;

import com.dqpi.simple_jvm.classfile.constant_pool.constant_info.MemberrefInfo;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Field;
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
public class FieldRef extends SymRef.MemberRef {
    Field field;

    public FieldRef setInfo(RtConstantPool rtConstantPool, MemberrefInfo.ConstantFieldrefInfo fieldrefInfo) {
        this.rtConstantPool = rtConstantPool;
        copyMemberRefInfo(fieldrefInfo);
        return this;
    }

    public Field resolvedField() {
        if (this.field == null) {
            resolvedFieldRef();
        }
        return this.field;
    }

    private void resolvedFieldRef() {
        resolvedClass();
        Field field = lookupField(clazz, name, descriptor);

        if (field == null) {
            throw new NoSuchFieldError();
        }
        if (!field.isAccessibleTo(rtConstantPool.getClazz())) {
            throw new IllegalAccessError();
        }
        this.field = field;
    }

    private Field lookupField(Clazz clazz, String name, String descriptor) {
        for (Field field : clazz.getFields()) {
            if (field.getName().equals(name) && field.getDescriptor().equals(descriptor)) {
                return field;
            }
        }
        if (clazz.getInterfaces() != null) {
            for (Clazz iface : clazz.getInterfaces()) {
                Field field = lookupField(iface, name, descriptor);
                if (field != null) {
                    return field;
                }
            }
        }
        if (clazz.getSuperClazz() != null) {
            return lookupField(clazz.getSuperClazz(), name, descriptor);
        }
        return null;
    }
}
