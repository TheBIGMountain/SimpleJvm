package com.dqpi.simple_jvm.classfile.constant_pool.constant_info;

import com.dqpi.simple_jvm.classfile.ClassReader;
import com.dqpi.simple_jvm.classfile.constant_pool.ConstantPool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/27
 */
public class MemberrefInfo implements ConstantInfo {
    @Resource
    private ConstantPool constantPool;
    @Resource
    private ClassReader reader;
    @Getter
    private int classIndex;
    @Getter
    private int nameAndTypeIndex;
    
    /**
     * 读取常量信息
     */
    @Override
    public void readInfo() {
        this.classIndex = reader.readUint16();
        this.nameAndTypeIndex = reader.readUint16();
    }
    
    public String getClassName() {
        return constantPool.getClassName(classIndex);
    }
    
    public NameAndDescriptor getNameAndDescriptor() {
        return constantPool.getNameAndDescriptor(nameAndTypeIndex);
    }
    
    @AllArgsConstructor
    @Getter
    public static class NameAndDescriptor {
        private final String name;
        private final String descriptor;
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class ConstantFieldrefInfo extends MemberrefInfo implements ConstantInfo {
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class ConstantMethodrefInfo extends MemberrefInfo implements ConstantInfo {
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class ConstantInterFaceMethodrefInfo extends MemberrefInfo implements ConstantInfo {
    }
}
