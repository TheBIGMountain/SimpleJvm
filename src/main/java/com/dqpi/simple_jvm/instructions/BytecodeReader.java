package com.dqpi.simple_jvm.instructions;

import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/28
 */
@Component
public class BytecodeReader {
    private int[] code;
    @Getter
    private int pc;
    
    public void reset(int[] code, int pc) {
        this.code = code;
        this.pc = pc;
    }
    
    public int readUint8() {
        return code[pc++];
    }
    
    public byte readInt8() {
        return (byte) readUint8();
    }
    
    public int readUint16() {
        int a = readUint8();
        int b = readUint8();
        return (a << 8) | b;
    }
    
    public int readInt16() {
        return (short) readUint16();
    }
    
    public int readInt32() {
        long a = Integer.toUnsignedLong(readUint16());
        long b = Integer.toUnsignedLong(readUint16());
        return (int) (a << 16 | b);
    }
    
    public int[] readInt32s(int n) {
        int[] res = new int[n];
        for (int i = 0; i < res.length; i++) {
            res[i] = readInt32();
        }
        return res;
    }
    
    public void skipPadding() {
        while (pc % 4 != 0) {
            readUint8();
        }
    }
}
