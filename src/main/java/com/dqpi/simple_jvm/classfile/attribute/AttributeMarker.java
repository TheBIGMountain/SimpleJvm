package com.dqpi.simple_jvm.classfile.attribute;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/27
 */
public class AttributeMarker implements AttributeInfo {
    @Override
    public void readInfo() {}

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class DeprecatedAttribute extends AttributeMarker {
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class SyntheticAttribute extends AttributeMarker {
    }
}
