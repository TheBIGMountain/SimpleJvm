package com.dqpi.simple_jvm.classfile;

import com.dqpi.simple_jvm.classfile.attribute.AttributeInfo;
import com.dqpi.simple_jvm.classfile.attribute.AttributeInfoFactory;
import com.dqpi.simple_jvm.classfile.attribute.debug.SourceFileAttribute;
import com.dqpi.simple_jvm.classfile.constant_pool.ConstantPool;
import com.dqpi.simple_jvm.classfile.member.MemberInfo;
import com.dqpi.simple_jvm.classfile.member.MemberInfoFactory;
import lombok.Getter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author TheBIGMountain
 * @date 2020/10/27
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ClassFile {
    @Resource
    private MemberInfoFactory memberInfoFactory;
    @Resource
    private AttributeInfoFactory attributeInfoFactory;
    @Resource
    private ClassReader reader;
    
    @Getter
    private int minorVersion;
    @Getter
    private int majorVersion;
    @Getter
    @Resource
    private ConstantPool constantPool;
    @Getter
    private int accessFlags;
    @Getter
    private MemberInfo[] fields;
    @Getter
    private MemberInfo[] methods;
    @Getter
    private AttributeInfo[] attributes;

    private int thisClass;
    private int superClass;
    private short[] interfaces;
    
    public ClassFile parse(byte[] classData) {
        reader.data = classData;
        read();
        return this;
    }
    
    private void read() {
        readAndCheckMagic();
        readAndCheckVersion();
        this.constantPool = constantPool.readConstantPool();
        this.accessFlags = reader.readUint16();
        this.thisClass = reader.readUint16();
        this.superClass = reader.readUint16();
        this.interfaces = reader.readUint16s();
        this.fields = memberInfoFactory.readMembers();
        this.methods = memberInfoFactory.readMembers();
        this.attributes = attributeInfoFactory.readAttributes();
    }
    
    private void readAndCheckMagic() {
        long magic = reader.readUint32();
        if (magic != 0xCAFEBABE) {
            throw new ClassFormatError("magic!");
        }
    }
    
    private void readAndCheckVersion() {
        this.minorVersion = reader.readUint16();
        this.majorVersion = reader.readUint16();
        Set<Integer> versionSet = new HashSet<>(16);
        versionSet.addAll(Arrays.asList(46, 47, 48, 49, 50, 51, 52, 53, 54, 55));
        if (majorVersion == 45) {
            return;
        }
        else if (versionSet.contains(majorVersion) && minorVersion == 0){
            return;
        }
        throw new UnsupportedClassVersionError();
    }
    
    public String getThisClassName() {
        return constantPool.getClassName(thisClass);
    }
    
    public String getSuperClassName() {
        if (superClass > 0) {
            return constantPool.getClassName(superClass);
        }
        return "";
    }
    
    public String[] getInterfaceNames() {
        String[] interfaceNames = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            interfaceNames[i] = constantPool.getClassName(interfaces[i]);
        }
        return interfaceNames;
    }

    public SourceFileAttribute getSourceFileAttribute() {
        for (AttributeInfo attribute : attributes) {
            if (attribute instanceof SourceFileAttribute) {
                return (SourceFileAttribute) attribute;
            }
        }
        return null;
    }
}
