package com.dqpi.simple_jvm.classfile.constant_pool.constant_info;

import com.dqpi.simple_jvm.classfile.ClassReader;
import com.dqpi.simple_jvm.classfile.constant_pool.ConstantPool;
import lombok.Getter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
public class CharInfo {
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class ConstantClassInfo implements ConstantInfo {
        @Resource
        private ConstantPool constantPool;
        @Resource
        private ClassReader reader;
        @Getter
        private int nameIndex;

        /**
         * 读取常量信息
         */
        @Override
        public void readInfo() {
            this.nameIndex = reader.readUint16();
        }

        public String getName() {
            return constantPool.getUtf8(nameIndex);
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class ConstantNameAndDescriptorInfo implements ConstantInfo {
        @Resource
        private ClassReader reader;
        @Resource
        private ConstantPool constantPool;
        @Getter
        private int nameIndex;
        @Getter
        private int descriptorIndex;

        /**
         * 读取常量信息
         */
        @Override
        public void readInfo() {
            this.nameIndex = reader.readUint16();
            this.descriptorIndex = reader.readUint16();
        }
        
        public String getName() {
            return constantPool.getUtf8(nameIndex);
        }
        
        public String getDescriptor() {
            return constantPool.getUtf8(descriptorIndex);
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class ConstantStringInfo implements ConstantInfo {
        @Resource
        private ConstantPool constantPool;
        @Resource
        private ClassReader reader;
        @Getter
        private int stringIndex;

        /**
         * 读取常量信息
         */
        @Override
        public void readInfo() {
            this.stringIndex = reader.readUint16();
        }

        public String string() {
            return constantPool.getUtf8(stringIndex);
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class ConstantUtf8Info implements ConstantInfo {
        @Resource
        private ClassReader reader;
        @Getter
        private String str;
        /**
         * 读取常量信息
         */
        @Override
        public void readInfo() {
            int length = reader.readUint16();
            int[] bytes = reader.readBytes(length);
            byte[] str = new byte[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                str[i] = (byte) bytes[i];
            }
            this.str = new String(str);
        }
    }
}
