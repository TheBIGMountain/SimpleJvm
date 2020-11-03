package com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member;

import com.dqpi.simple_jvm.classfile.member.MemberInfo;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.utils.AccessFlags;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
@Getter
@Setter
public class ClassMember {
    protected int accessFlags;
    protected String name;
    protected String descriptor;
    protected Clazz clazz;
    
    public void copyMemberInfo(MemberInfo memberInfo) {
        this.accessFlags = memberInfo.getAccessFlags();
        this.name = memberInfo.getName();
        this.descriptor = memberInfo.getDescriptor();
    }

    public boolean isPublic() {
        return (this.accessFlags & AccessFlags.ACC_PUBLIC) != 0;
    }

    public boolean isPrivate() {
        return (this.accessFlags & AccessFlags.ACC_PRIVATE) != 0;
    }

    public boolean isProtected() {
        return (this.accessFlags & AccessFlags.ACC_PROTECTED) != 0;
    }

    public boolean isStatic() {
        return (this.accessFlags & AccessFlags.ACC_STATIC) != 0;
    }

    public boolean isFinal() {
        return (this.accessFlags & AccessFlags.ACC_FINAL) != 0;
    }

    public boolean isSynthetic() {
        return (this.accessFlags & AccessFlags.ACC_SYNTHETIC) != 0;
    }

    public boolean isAccessibleTo(Clazz other) {
        if (this.isPublic()) {
            return true;
        }
        if (this.isProtected()) {
            return this.clazz == other || other.isSubClassOf(this.clazz)
                    || this.clazz.getPackageName().equals(other.getPackageName());
        }
        if (!this.isPrivate()) {
            return this.clazz.getPackageName().equals(other.getPackageName());
        }
        return this.clazz == other;
    }
    
}
