package com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info;

import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.ClassLoader;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Field;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.utils.AccessFlags;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.utils.ClassUtil;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.constant_pool.RtConstantPool;
import com.dqpi.simple_jvm.runtime_data.opt.LocalVars;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Getter
@Setter
public class Clazz {
    private int accessFlags;
    private java.lang.String name;
    private java.lang.String superClassName;
    private java.lang.String[] interfaceNames;
    private RtConstantPool rtConstantPool;
    private Field[] fields;
    private Method[] methods;
    private ClassLoader loader;
    private Clazz superClazz;
    private Clazz[] interfaces;
    private int instanceSlotCount;
    private int staticSlotCount;
    private LocalVars staticVars;
    private boolean initStarted;
    private Obj jClass;
    private java.lang.String sourceFile;
    
    public boolean isPublic() {
        return (this.accessFlags & AccessFlags.ACC_PUBLIC) != 0;
    }
    
    public boolean inFinal() {
        return (this.accessFlags & AccessFlags.ACC_FINAL) != 0;
    }
    
    public boolean isSuper() {
        return (this.accessFlags & AccessFlags.ACC_SUPER) != 0;
    }
    
    public boolean isInterface() {
        return (this.accessFlags & AccessFlags.ACC_INTERFACE) != 0;
    }
    
    public boolean isAbstract() {
        return (this.accessFlags & AccessFlags.ACC_ABSTRACT) != 0;
    }
    
    public boolean isSynthetic() {
        return (this.accessFlags & AccessFlags.ACC_SYNTHETIC) != 0;
    }
    
    public boolean isAnnotation() {
        return (this.accessFlags & AccessFlags.ACC_ANNOTATION) != 0;
    }
    
    public boolean isEnum() {
        return (this.accessFlags & AccessFlags.ACC_ENUM) != 0;
    }
    
    public boolean isAccessibleTo(Clazz other) {
        return isPublic() || getPackageName().equals(other.getPackageName());
    }
    
    public java.lang.String getPackageName() {
        int last = name.lastIndexOf('/');
        if (last != -1) {
            return name.substring(0, last);
        }
        return "";
    }

    public Field getField(java.lang.String name, java.lang.String descriptor, boolean isStatic) {
        for (Clazz c = this; c != null; c = c.superClazz) {
            for (Field field : c.getFields()) {
                if (field.isStatic() == isStatic 
                        && field.getName().equals(name) && field.getDescriptor().equals(descriptor)) {
                    return field;
                }
            }
        }
        return null;
    }

    public Method getMethod(java.lang.String name, java.lang.String descriptor, boolean isStatic) {
        for (Clazz c = this; c != null; c = c.superClazz) {
            for (Method method : c.getMethods()) {
                if (method.isStatic() == isStatic
                        && method.getName().equals(name) && method.getDescriptor().equals(descriptor)) {
                    return method;
                }
            }
        }
        return null;
    }
    
    public Method getMainMethod() {
        return getStaticMethod("main", "([Ljava/lang/String;)V");
    }
    
    public Method getStaticMethod(java.lang.String name, java.lang.String descriptor) {
        return getMethod(name, descriptor, true);
    }
    
    public boolean isAssignableFrom(Clazz other) {
        if (this == other) {
            return true;
        }
        
        if (!other.isArray()) {
            if (!other.isInterface()) {
                if (!this.isInterface()) {
                    return other.isSubClassOf(this);
                }
                else {
                    return other.isImplements(this);
                }
            }
            else {
                if (!this.isInterface()) {
                    return ClassUtil.isJlObject(this);
                }
                else {
                    return this.isSuperInterfaceOf(other);
                }
            }
        }
        else {
            if (!this.isArray()) {
                if (!this.isInterface()) {
                    return ClassUtil.isJlObject(this);
                }
                else {
                    return ClassUtil.isJlCloneable(this) || ClassUtil.isJioSerializable(this);
                }
            }
            else {
                Clazz sc = other.getComponentClass();
                Clazz tc = this.getComponentClass();
                return sc == tc || tc.isAssignableFrom(sc);
            }
        }
    }
    
    public boolean isSubClassOf(Clazz other) {
        return this.superClazz != null && this.superClazz == other;
    }

    public boolean isSuperInterfaceOf(Clazz other) {
        return other.isSubClassOf(this);
    }
    
    public boolean isImplements(Clazz iface) {
        return ClassUtil.isImplements(iface, this);
    }
    
    public boolean isSuperClassOf(Clazz other) {
        return other.isSubClassOf(this);
    }

    public Method getClinitMethod() {
        return getStaticMethod("<clinit>", "()V");
    }
    
    public java.lang.String getJavaName() {
        return name.replaceAll("/", ".");
    }
    
    public boolean isArray() {
        return this.name.charAt(0) == '[';
    }
    
    public Clazz arrayClass() {
        String arrayClassName = ClassUtil.getArrayClassName(name);
        return loader.loadClass(arrayClassName);
    }
    
    public Clazz getComponentClass() {
        String componentClassName = ClassUtil.getComponentClassName(name);
        return loader.loadClass(componentClassName);
    }

    public boolean isPrimitive() {
        return ClassUtil.PRIMITIVE_TYPES.containsKey(name);
    }
    
    public Obj getRefVar(java.lang.String fieldName, java.lang.String fieldDescriptor) {
        Field field = getField(fieldName, fieldDescriptor, true);
        return staticVars.getRef(field.getFIndex());
    }

    public void setRefVar(java.lang.String fieldName, java.lang.String fieldDescriptor, Obj ref) {
        Field field = getField(fieldName, fieldDescriptor, true);
        staticVars.setRef(field.getFIndex(), ref);
    }

    public Method getInterfaceMethod(java.lang.String name, java.lang.String descriptor) {
        return getMethod(name, descriptor, false);
    }
    
}
