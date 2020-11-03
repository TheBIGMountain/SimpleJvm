package com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info;

import com.dqpi.simple_jvm.classfile.ClassFile;
import com.dqpi.simple_jvm.classfile.attribute.debug.SourceFileAttribute;
import com.dqpi.simple_jvm.classfile.member.MemberInfo;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.ClassLoader;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Field;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.utils.AccessFlags;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.utils.MethodDescriptorUtil;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.RtConstantPool;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
@Component
public class ClassFactory {
    @Resource
    private ApplicationContext ctx;
    
    public Clazz newArray(String name, ClassLoader loader) {
        Clazz clazz = ctx.getBean(Clazz.class);
        clazz.setAccessFlags(AccessFlags.ACC_PUBLIC);
        clazz.setName(name);
        clazz.setLoader(loader);
        clazz.setInitStarted(true);
        clazz.setSuperClazz(loader.loadClass("java/lang/Object"));
        clazz.setInterfaces(new Clazz[] {
                loader.loadClass("java/lang/Cloneable"),
                loader.loadClass("java/io/Serializable")
        });
        clazz.setInitStarted(true);
        return clazz;
    }
    
    
    public Clazz newClass(String className, ClassLoader loader, Obj jClassClass) {
        Clazz clazz = ctx.getBean(Clazz.class);
        clazz.setAccessFlags(AccessFlags.ACC_PUBLIC);
        clazz.setName(className);
        clazz.setLoader(loader);
        clazz.setInitStarted(true);
        clazz.setJClass(jClassClass);
        clazz.getJClass().setExtra(clazz);
        return clazz;
    }
    
    public Clazz newClass(ClassFile classFile) {
        Clazz clazz = ctx.getBean(Clazz.class);
        clazz.setAccessFlags(classFile.getAccessFlags());
        clazz.setName(classFile.getThisClassName());
        clazz.setSuperClassName(classFile.getSuperClassName());
        clazz.setInterfaceNames(classFile.getInterfaceNames());
        clazz.setRtConstantPool(ctx.getBean(RtConstantPool.class).setInfo(clazz));
        clazz.setFields(newFields(clazz, classFile.getFields()));
        clazz.setMethods(newMethods(clazz, classFile.getMethods()));
        clazz.setSourceFile(getSourceFile(classFile));
        clazz.setInitStarted(true);
        return clazz;
    }

    private String getSourceFile(ClassFile classFile) {
        SourceFileAttribute sourceFileAttribute = classFile.getSourceFileAttribute();
        if (sourceFileAttribute != null) {
            return sourceFileAttribute.getFileName();
        }
        return "Unknown";
    }

    private Field[] newFields(Clazz clazz, MemberInfo[] fieldInfos) {
        Field[] fields = new Field[fieldInfos.length];
        for (int i = 0; i < fieldInfos.length; i++) {
            fields[i] = ctx.getBean(Field.class);
            fields[i].setClazz(clazz);
            fields[i].copyMemberInfo(fieldInfos[i]);
            fields[i].copyAttributes(fieldInfos[i]);
        }
        return fields;
    }
    
    private Method[] newMethods(Clazz clazz, MemberInfo[] methodInfos) {
        Method[] methods = new Method[methodInfos.length];
        for (int i = 0; i < methodInfos.length; i++) {
            methods[i] = newMethod(clazz, methodInfos[i]);
        }
        return methods;
    }

    private Method newMethod(Clazz clazz, MemberInfo methodInfo) {
        Method method = ctx.getBean(Method.class);
        method.setClazz(clazz);
        method.copyMemberInfo(methodInfo);
        method.copyAttributes(methodInfo);
        Method.MethodDescriptor  md = MethodDescriptorUtil.parse(method.getDescriptor());
        method.calcArgCount(md.getParameterTypes());
        if (method.isNative()) {
            method.injectCodeAttribute(md.getReturnType());
        }
        return method;
    }
}
