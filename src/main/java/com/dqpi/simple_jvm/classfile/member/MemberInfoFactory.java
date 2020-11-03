package com.dqpi.simple_jvm.classfile.member;

import com.dqpi.simple_jvm.classfile.ClassReader;
import com.dqpi.simple_jvm.classfile.attribute.AttributeInfoFactory;
import com.dqpi.simple_jvm.classfile.constant_pool.ConstantPool;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/27
 */
@Component
public class MemberInfoFactory {
    @Resource
    private ApplicationContext ctx;
    @Resource
    private ClassReader reader;
    @Resource
    private AttributeInfoFactory factory;
    @Resource
    private ConstantPool constantPool;
    
    public MemberInfo[] readMembers() {
        int memberCount = reader.readUint16();
        MemberInfo[] members = new MemberInfo[memberCount];
        for (int i = 0; i < members.length; i++) {
            members[i] = readMember();
        }
        return members;
    }

    private MemberInfo readMember() {
        MemberInfo memberInfo = ctx.getBean(MemberInfo.class);
        memberInfo.setConstantPool(constantPool);
        memberInfo.setAccessFlags(reader.readUint16());
        memberInfo.setNameIndex(reader.readUint16());
        memberInfo.setDescriptorIndex(reader.readUint16());
        memberInfo.setAttributes(factory.readAttributes());
        return memberInfo;
    }
}
