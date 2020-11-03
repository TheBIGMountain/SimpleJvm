package com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member;

import com.dqpi.simple_jvm.classfile.attribute.useful.ConstantValueAttribute;
import com.dqpi.simple_jvm.classfile.member.MemberInfo;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.utils.AccessFlags;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Getter
@Setter
public class Field extends ClassMember {
    private int constantValueIndex;
    private int fIndex;

    public void copyAttributes(MemberInfo fieldInfo) {
        ConstantValueAttribute valueAttribute = fieldInfo.getConstantValueAttribute();
        if (valueAttribute != null) {
            this.constantValueIndex = valueAttribute.getConstantValueIndex();
        }
    }

    public boolean isVolatile() {
        return (this.accessFlags & AccessFlags.ACC_VOLATILE) != 0;
    }

    public boolean isTransient() {
        return (this.accessFlags & AccessFlags.ACC_TRANSIENT) != 0;
    }

    public boolean isEnum() {
        return (this.accessFlags & AccessFlags.ACC_ENUM) != 0;
    }

    public boolean isLongOrDouble() {
        return "J".equals(descriptor) || "D".equals(descriptor);
    }
}
