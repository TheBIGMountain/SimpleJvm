package com.dqpi.simple_jvm.classfile.constant_pool;

import com.dqpi.simple_jvm.classfile.ClassReader;
import com.dqpi.simple_jvm.classfile.constant_pool.constant_info.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/27
 */
@Component
public class ConstantPool {
    @Resource
    private ClassReader reader;
    @Resource
    private ConstantInfoFactory factory;
    private ConstantInfo[] constants;
    
    public ConstantPool readConstantPool() {
        int cpCount = reader.readUint16();
        constants = new ConstantInfo[cpCount];
        for (int i = 1; i < cpCount; i++) {
            constants[i] = factory.readConstantInfo();
            if (constants[i] instanceof Numeric.ConstantLongInfo 
                    || constants[i] instanceof Numeric.ConstantDoubleInfo) {
                i ++;
            }
        }
        return this;
    }
    
    @SneakyThrows
    public ConstantInfo getConstantInfo(int index) {
        if (index >= constants.length || index < 1) {
            throw new Exception("Invalid constant pool index!");
        }
        return constants[index];
    }
    
    @SneakyThrows
    public MemberrefInfo.NameAndDescriptor getNameAndDescriptor(int index) {
        if (index >= constants.length || index < 1) {
            throw new Exception("Invalid constant pool index!");
        }
        CharInfo.ConstantNameAndDescriptorInfo nameAndTypeInfo = (CharInfo.ConstantNameAndDescriptorInfo) constants[index];
        String name = getUtf8(nameAndTypeInfo.getNameIndex());
        String descriptor = getUtf8(nameAndTypeInfo.getDescriptorIndex());
        return new MemberrefInfo.NameAndDescriptor(name, descriptor);
    }
    
    @SneakyThrows
    public String getClassName(int index) {
        if (index >= constants.length || index < 1) {
            throw new Exception("Invalid constant pool index!");
        }
        CharInfo.ConstantClassInfo classInfo = (CharInfo.ConstantClassInfo) constants[index];
        return getUtf8(classInfo.getNameIndex());
    }
    
    @SneakyThrows
    public String getUtf8(int index) {
        if (index >= constants.length || index < 1) {
            throw new Exception("Invalid constant pool index!");
        }
        CharInfo.ConstantUtf8Info utf8Info = (CharInfo.ConstantUtf8Info) constants[index];
        return utf8Info.getStr();
    }
    
    public int size() {
        return constants.length;
    }
}
