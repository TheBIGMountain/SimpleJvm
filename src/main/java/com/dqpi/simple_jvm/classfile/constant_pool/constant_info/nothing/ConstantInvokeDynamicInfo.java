package com.dqpi.simple_jvm.classfile.constant_pool.constant_info.nothing;

import com.dqpi.simple_jvm.classfile.ClassReader;
import com.dqpi.simple_jvm.classfile.constant_pool.constant_info.ConstantInfo;
import lombok.Getter;
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
public class ConstantInvokeDynamicInfo implements ConstantInfo {
    @Resource
    private ClassReader reader;
    @Getter
    private int bootstrapMethodAttrIndex;
    @Getter
    private int nameAndTypeIndex;
    /**
     * 读取常量信息
     */
    @Override
    public void readInfo() {
        this.bootstrapMethodAttrIndex = reader.readUint16();
        this.nameAndTypeIndex = reader.readUint16();
    }
}
