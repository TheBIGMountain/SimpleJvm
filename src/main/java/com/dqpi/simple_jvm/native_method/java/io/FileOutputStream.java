package com.dqpi.simple_jvm.native_method.java.io;

import com.dqpi.simple_jvm.native_method.Natives;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.opt.LocalVars;
import org.springframework.stereotype.Component;


/**
 * @author TheBIGMountain
 * @date 2020/11/1
 */
@Component
public class FileOutputStream {
    public FileOutputStream(Natives natives) {
        natives.registry("java/io/FileOutputStream", "writeBytes", "([BIIZ)V", 
        frame -> {
            LocalVars vars = frame.getLocalVars();
            Obj b = vars.getRef(1);
            int off = vars.getInt(2);
            int len = vars.getInt(3);

            Byte[] jBytes = (Byte[]) b.getData();
            byte[] res = new byte[len];
            for (int i = 0; i < jBytes.length; i++) {
                res[i] = jBytes[i + off];
            }
            System.out.println(new String(res));
        });
    }
}
