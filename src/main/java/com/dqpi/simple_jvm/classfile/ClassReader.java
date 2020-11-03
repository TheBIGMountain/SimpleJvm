package com.dqpi.simple_jvm.classfile;

import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/27
 */
@Component
public class ClassReader {
    byte[] data;
    private int index = 0;
    
    public int readUint8() {
        if (data != null) {
            byte res = data[index ++];
            if (index == data.length) {
                data = null;
                index = 0;
            }
            return Byte.toUnsignedInt(res);
        }
        return -1;
    }
    
    public short readUint16() {
        int a = readUint8();
        int b = readUint8();
        a = a << 8;
        return (short) (a | b);
    }
    
    public int readUint32() {
        long a = Short.toUnsignedLong(readUint16());
        long b = Short.toUnsignedLong(readUint16());
        a = a << 16;
        return (int) (a | b);
    }
    
    public short[] readUint16s() {
        int length = readUint16();
        short[] res = new short[length];
        for (int i = 0; i < res.length; i++) {
            res[i] = readUint16();
        }
        return res;
    }
    
    public int[] readBytes(int length) {
        int[] res = new int[length];
        for (int i = 0; i < res.length; i ++) {
            res[i] = readUint8();
        }
        return res;
    }
}
