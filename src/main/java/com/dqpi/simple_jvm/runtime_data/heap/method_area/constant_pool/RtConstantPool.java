package com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool;

import com.dqpi.simple_jvm.classfile.constant_pool.ConstantPool;
import com.dqpi.simple_jvm.classfile.constant_pool.constant_info.CharInfo;
import com.dqpi.simple_jvm.classfile.constant_pool.constant_info.ConstantInfo;
import com.dqpi.simple_jvm.classfile.constant_pool.constant_info.MemberrefInfo;
import com.dqpi.simple_jvm.classfile.constant_pool.constant_info.Numeric;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.ClassRef;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.FieldRef;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.InterfaceMethodRef;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.ref.MethodRef;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RtConstantPool {
    @Resource
    private ApplicationContext ctx;
    @Resource
    private ConstantPool constantPool;
    @Getter
    private Clazz clazz;
    @Getter
    private Object[] constants;
    
    @SneakyThrows
    public Object getConstant(int index) {
        if (index < 1 || index >= constants.length) {
            throw new Exception("No constants at index " + index);
        }
        return constants[index];
    }
    
    public RtConstantPool setInfo(Clazz clazz) {
        this.clazz = clazz;
        this.constants = new Object[constantPool.size()];

        for (int i = 1; i < constantPool.size(); i ++) {
            ConstantInfo constantInfo = constantPool.getConstantInfo(i);
            if (constantInfo instanceof Numeric.ConstantIntegerInfo) {
                constants[i] = ((Numeric.ConstantIntegerInfo) constantInfo).getVal();
            }
            else if (constantInfo instanceof Numeric.ConstantFloatInfo) {
                constants[i] = ((Numeric.ConstantFloatInfo) constantInfo).getVal();
            }
            else if (constantInfo instanceof Numeric.ConstantLongInfo) {
                constants[i] = ((Numeric.ConstantLongInfo) constantInfo).getVal();
                i ++;
            }
            else if (constantInfo instanceof Numeric.ConstantDoubleInfo) {
                constants[i] = ((Numeric.ConstantDoubleInfo) constantInfo).getVal();
                i ++;
            }
            else if (constantInfo instanceof CharInfo.ConstantNameAndDescriptorInfo) {
                CharInfo.ConstantNameAndDescriptorInfo nameAndDescriptorInfo = (CharInfo.ConstantNameAndDescriptorInfo) constantInfo;
                constants[i] = nameAndDescriptorInfo.getName();
            }
            else if (constantInfo instanceof CharInfo.ConstantUtf8Info) {
                constants[i] = ((CharInfo.ConstantUtf8Info) constantInfo).getStr(); 
            }
            else if (constantInfo instanceof CharInfo.ConstantStringInfo) {
                constants[i] = ((CharInfo.ConstantStringInfo) constantInfo).string();
            }
            else if (constantInfo instanceof CharInfo.ConstantClassInfo) {
                CharInfo.ConstantClassInfo classInfo = (CharInfo.ConstantClassInfo) constantInfo;
                constants[i] = ctx.getBean(ClassRef.class).setInfo(this, classInfo);
            }
            else if (constantInfo instanceof MemberrefInfo.ConstantFieldrefInfo) {
                MemberrefInfo.ConstantFieldrefInfo fieldrefInfo = (MemberrefInfo.ConstantFieldrefInfo) constantInfo;
                constants[i] = ctx.getBean(FieldRef.class).setInfo(this, fieldrefInfo);
            }
            else if (constantInfo instanceof MemberrefInfo.ConstantMethodrefInfo) {
                MemberrefInfo.ConstantMethodrefInfo methodrefInfo = (MemberrefInfo.ConstantMethodrefInfo) constantInfo;
                constants[i] = ctx.getBean(MethodRef.class).setInfo(this, methodrefInfo);
            }
            else if (constantInfo instanceof MemberrefInfo.ConstantInterFaceMethodrefInfo) {
                MemberrefInfo.ConstantInterFaceMethodrefInfo interFaceMethodrefInfo = (MemberrefInfo.ConstantInterFaceMethodrefInfo) constantInfo;
                constants[i] = ctx.getBean(InterfaceMethodRef.class).setInfo(this, interFaceMethodrefInfo);
            }
        }
        
        return this;
    }
}
