package com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref;

import com.dqpi.simple_jvm.classfile.constant_pool.constant_info.MemberrefInfo;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.RtConstantPool;
import lombok.Getter;
import lombok.SneakyThrows;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
public class SymRef {
    public RtConstantPool rtConstantPool;
    public String className;
    public Clazz clazz;
    
    public Clazz resolvedClass() {
        if (this.clazz == null) {
            resolvedClassRef();
        }
        return this.clazz;
    }

    @SneakyThrows
    private void resolvedClassRef() {
        Clazz clazz = rtConstantPool.getClazz().getLoader().loadClass(className);
        if (!clazz.isAccessibleTo(rtConstantPool.getClazz())) {
            throw new IllegalAccessException();
        }
        this.clazz = clazz;
    }
    
    @Getter
    public static class MemberRef extends SymRef {
        String name;
        String descriptor;

        void copyMemberRefInfo(MemberrefInfo memberrefInfo) {
            this.className = memberrefInfo.getClassName();
            this.name = memberrefInfo.getNameAndDescriptor().getName();
            this.descriptor = memberrefInfo.getNameAndDescriptor().getDescriptor();
        }
    }
}
