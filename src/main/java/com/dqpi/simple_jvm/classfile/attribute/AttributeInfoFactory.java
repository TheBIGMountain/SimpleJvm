package com.dqpi.simple_jvm.classfile.attribute;

import com.dqpi.simple_jvm.classfile.ClassReader;
import com.dqpi.simple_jvm.classfile.attribute.debug.LineNumberTableAttribute;
import com.dqpi.simple_jvm.classfile.attribute.debug.LocalVariableTableAttribute;
import com.dqpi.simple_jvm.classfile.attribute.debug.SourceFileAttribute;
import com.dqpi.simple_jvm.classfile.attribute.useful.CodeAttribute;
import com.dqpi.simple_jvm.classfile.attribute.useful.ConstantValueAttribute;
import com.dqpi.simple_jvm.classfile.attribute.useful.ExceptionsAttribute;
import com.dqpi.simple_jvm.classfile.constant_pool.ConstantPool;
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
public class AttributeInfoFactory {
    @Resource
    private ApplicationContext ctx;
    @Resource
    private ClassReader reader;
    @Resource
    private ConstantPool constantPool;
    private final Map<String, GetAttributeInfo> attributeInfoMap = new HashMap<>(14);
    
    public AttributeInfoFactory() {
        attributeInfoMap.put("Code", () -> ctx.getBean(CodeAttribute.class));
        attributeInfoMap.put("ConstantValue", () -> ctx.getBean(ConstantValueAttribute.class));
        attributeInfoMap.put("Deprecated", () -> ctx.getBean(AttributeMarker.DeprecatedAttribute.class));
        attributeInfoMap.put("Exceptions", () -> ctx.getBean(ExceptionsAttribute.class));
        attributeInfoMap.put("LineNumberTable", () -> ctx.getBean(LineNumberTableAttribute.class));
        attributeInfoMap.put("LocalVariableTable", () -> ctx.getBean(LocalVariableTableAttribute.class));
        attributeInfoMap.put("SourceFile", () -> ctx.getBean(SourceFileAttribute.class));
        attributeInfoMap.put("Synthetic", () -> ctx.getBean(AttributeMarker.SyntheticAttribute.class));
    }
    
    public AttributeInfo[] readAttributes() {
        int attrCount = reader.readUint16();
        AttributeInfo[] attributes = new AttributeInfo[attrCount];
        for (int i = 0; i < attributes.length; i ++) {
            attributes[i] = readAttribute();
        }
        return attributes;
    }
    
    private AttributeInfo readAttribute() {
        int attrNameIndex = reader.readUint16();
        String attrName = constantPool.getUtf8(attrNameIndex);
        int attrLen = reader.readUint32();
        AttributeInfo attributeInfo = newAttributeInfo(attrName, attrLen);
        attributeInfo.readInfo();
        return attributeInfo;
    }
    
    private AttributeInfo newAttributeInfo(String attrName, long attrLen) {
        GetAttributeInfo attributeInfo = attributeInfoMap.get(attrName);
        if (attributeInfo != null) {
            return attributeInfo.execute();
        }
        UnparsedAttribute unparsedAttribute = ctx.getBean(UnparsedAttribute.class);
        unparsedAttribute.setName(attrName);
        unparsedAttribute.setLength((int) attrLen);
        return unparsedAttribute;
    }
    
    @FunctionalInterface
    private interface GetAttributeInfo {
        /**
         * 获取属性信息对象
         * 
         * @return  属性信息对象
         */
        AttributeInfo execute();
    }
}
