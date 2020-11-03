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
public class ConstantMethodHandlerInfo implements ConstantInfo {
    @Resource
    private ClassReader reader;
    @Getter
    private int referenceKind;
    @Getter
    private int referenceIndex;
    /**
     * 读取常量信息
     */
    @Override
    public void readInfo() {
        this.referenceKind = reader.readUint8();
        this.referenceIndex = reader.readUint16();
    }
}
