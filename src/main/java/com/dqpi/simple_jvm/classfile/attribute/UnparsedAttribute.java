package com.dqpi.simple_jvm.classfile.attribute;

import com.dqpi.simple_jvm.classfile.ClassReader;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/27
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UnparsedAttribute implements AttributeInfo {
    @Resource
    private ClassReader reader;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private int length;
    @Getter
    @Setter
    private int[] info;
    
    @Override
    public void readInfo() {
        this.info = reader.readBytes(this.length);
    }
}
