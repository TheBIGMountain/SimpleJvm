package com.dqpi.simple_jvm.classfile.constant_pool.constant_info;

import com.dqpi.simple_jvm.classfile.ClassReader;
import com.dqpi.simple_jvm.classfile.constant_pool.constant_info.nothing.ConstantInvokeDynamicInfo;
import com.dqpi.simple_jvm.classfile.constant_pool.constant_info.nothing.ConstantMethodHandlerInfo;
import com.dqpi.simple_jvm.classfile.constant_pool.constant_info.nothing.ConstantMethodTypeInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TheBIGMountain
 * @date 2020/10/27
 */
@Component
public class ConstantInfoFactory {
    @Resource
    private ApplicationContext ctx;
    @Resource
    private ClassReader reader;
    private final Map<Integer, GetConstant> constantMap = new HashMap<>(24);
    
    public ConstantInfoFactory() {
        int constantUtf8 = 1;
        int constantInteger = 3;
        int constantFloat = 4;
        int constantLong = 5;
        int constantDouble = 6;
        int constantClass = 7;
        int constantString = 8;
        int constantFieldref = 9;
        int constantMethodref = 10;
        int constantInterfaceMethodref = 11;
        int constantNameAndType = 12;
        int constantMethodHandle = 15;
        int constantMethodType = 16;
        int invokeDynamic = 18;
        
        constantMap.put(constantUtf8, () -> ctx.getBean(CharInfo.ConstantUtf8Info.class));
        constantMap.put(constantInteger, () -> ctx.getBean(Numeric.ConstantIntegerInfo.class));
        constantMap.put(constantFloat, () -> ctx.getBean(Numeric.ConstantFloatInfo.class));
        constantMap.put(constantLong, () -> ctx.getBean(Numeric.ConstantLongInfo.class));
        constantMap.put(constantDouble, () -> ctx.getBean(Numeric.ConstantDoubleInfo.class));
        constantMap.put(constantClass, () -> ctx.getBean(CharInfo.ConstantClassInfo.class));
        constantMap.put(constantString, () -> ctx.getBean(CharInfo.ConstantStringInfo.class));
        constantMap.put(constantFieldref, () -> ctx.getBean(MemberrefInfo.ConstantFieldrefInfo.class));
        constantMap.put(constantMethodref, () -> ctx.getBean(MemberrefInfo.ConstantMethodrefInfo.class));
        constantMap.put(constantInterfaceMethodref,  () -> ctx.getBean(MemberrefInfo.ConstantInterFaceMethodrefInfo.class));
        constantMap.put(constantNameAndType, () -> ctx.getBean(CharInfo.ConstantNameAndDescriptorInfo.class));
        constantMap.put(constantMethodHandle, () -> ctx.getBean(ConstantMethodHandlerInfo.class));
        constantMap.put(constantMethodType, () -> ctx.getBean(ConstantMethodTypeInfo.class));
        constantMap.put(invokeDynamic, () -> ctx.getBean(ConstantInvokeDynamicInfo.class));
    }
    
    public ConstantInfo readConstantInfo() {
        int tag = reader.readUint8();
        ConstantInfo constantInfo = newConstantInfo(tag);
        constantInfo.readInfo();
        return constantInfo;
    }
    
    private ConstantInfo newConstantInfo(int tag) {
        GetConstant constant = constantMap.get(tag);
        if (constant != null) {
            return constant.execute();
        }
        throw new ClassFormatError("constant pool tag!");
    }
    
    @FunctionalInterface
    private interface GetConstant {
        /**
         * 获取常量信息对象
         *
         * @return  常量信息对象
         */
        ConstantInfo execute();
    }
}
