package com.dqpi.simple_jvm.classfile.member;

import com.dqpi.simple_jvm.classfile.attribute.AttributeInfo;
import com.dqpi.simple_jvm.classfile.attribute.useful.CodeAttribute;
import com.dqpi.simple_jvm.classfile.attribute.useful.ConstantValueAttribute;
import com.dqpi.simple_jvm.classfile.constant_pool.ConstantPool;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/27
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Getter
@Setter
public class MemberInfo {
    private ConstantPool constantPool;
    private int accessFlags;
    private int nameIndex;
    private int descriptorIndex;
    private AttributeInfo[] attributes;
    
    public String getName() {
        return constantPool.getUtf8(nameIndex);
    }
    
    public String getDescriptor() {
        return constantPool.getUtf8(descriptorIndex);
    }
    
    public CodeAttribute getCodeAttribute() {
        for (AttributeInfo attribute : attributes) {
            if (attribute instanceof CodeAttribute) {
                return (CodeAttribute) attribute;
            }
        }
        return null;
    }
    
    public ConstantValueAttribute getConstantValueAttribute() {
        for (AttributeInfo attribute : attributes) {
            if (attribute instanceof ConstantValueAttribute) {
                return (ConstantValueAttribute) attribute;
            }
        }
        return null;
    }
}
