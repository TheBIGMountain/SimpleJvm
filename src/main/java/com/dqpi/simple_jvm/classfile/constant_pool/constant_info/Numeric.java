package com.dqpi.simple_jvm.classfile.constant_pool.constant_info;

import com.dqpi.simple_jvm.classfile.ClassReader;
import lombok.Getter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
public class Numeric {
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class ConstantDoubleInfo implements ConstantInfo {
        @Resource
        private ClassReader reader;
        @Getter
        private double val;
        /**
         * 读取常量信息
         */
        @Override
        public void readInfo() {
            long highByte = Integer.toUnsignedLong(reader.readUint32());
            long lowByte = Integer.toUnsignedLong(reader.readUint32());
            highByte = highByte << 32;
            this.val = Double.longBitsToDouble(highByte | lowByte);
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class ConstantFloatInfo implements ConstantInfo {
        @Resource
        private ClassReader reader;
        @Getter
        private float val;
        /**
         * 读取常量信息
         */
        @Override
        public void readInfo() {
            this.val = Float.intBitsToFloat(reader.readUint32());
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class ConstantIntegerInfo implements ConstantInfo {
        @Resource
        private ClassReader reader;
        @Getter
        private int val;

        /**
         * 读取常量信息
         */
        @Override
        public void readInfo() {
            this.val = reader.readUint32();
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class ConstantLongInfo implements ConstantInfo {
        @Resource
        private ClassReader reader;
        @Getter
        private long val;

        /**
         * 读取常量信息
         */
        @Override
        public void readInfo() {
            long highByte = Integer.toUnsignedLong(reader.readUint32());
            long lowByte = Integer.toUnsignedLong(reader.readUint32());
            highByte = highByte << 32;
            this.val = highByte | lowByte;
        }
    }
}
