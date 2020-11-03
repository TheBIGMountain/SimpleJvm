package com.dqpi.simple_jvm.jvm;

import com.dqpi.simple_jvm.classpath.ClassPath;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Arr;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.ClassLoader;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.Clazz;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.string_pool.StringPool;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/28
 */
@Component
public class SimpleJvm {
    @Resource
    private ApplicationContext ctx;
    @Resource
    private Arr arr;
    @Resource
    private StringPool stringPool;
    @Resource
    private Cmd cmd;
    @Resource
    private ClassPath classPath;
    @Resource
    private ClassLoader loader;
    @Resource
    private Thread mainThread;
    @Resource
    private Interpreter interpreter;
    
    @Async
    public void start(String... args) {
        cmd.parse(args);;
        if (cmd.isHelp()) {
            System.out.println("not help!");
        }
        else if (cmd.isVersion()) {
            System.out.println("version 0.0.1");
        }
        else {
            classPath.parse();
            loader.setInfo(cmd.isVerboseClassFlag());
            startJvm();
        }
    }

    private void startJvm() {
        execMain();
    }
    
    private void execMain() {
        Clazz mainClass = loader.loadClass(cmd.getClassName());
        Method mainMethod = mainClass.getMainMethod();
        if (mainMethod == null) {
            System.out.println("Main method not found in class " + cmd.getClassName());
            return;
        }

        Obj argsArr = createArgsArray();
        
        Thread.Frame frame = mainThread.newFrame(mainMethod, false, false);
        frame.getLocalVars().setRef(0, argsArr);
        mainThread.pushFrame(frame, mainMethod);

        Method clinitMethod = mainClass.getClinitMethod();
        if (clinitMethod != null) {
            mainThread.pushFrame(mainThread.newFrame(clinitMethod, true, false), clinitMethod);
        }
        interpreter.interpreter(mainThread, cmd.isVerboseInstFlag());
    }

    private Obj createArgsArray() {
        Clazz stringClass = loader.loadClass("[java/lang/String");
        Obj argsArr = arr.newArray(stringClass, cmd.getArgList().size());
        Obj[] jArgs = argsArr.getArr(Obj.class);
        for (int i = 0; i < cmd.getArgList().size(); i++) {
            jArgs[i] = stringPool.toJString(loader, cmd.getArgList().get(i));
        }
        return argsArr;
    }
}
