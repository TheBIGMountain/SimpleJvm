package com.dqpi.simple_jvm.instructions.references;

import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.native_method.java.lang.Throwable;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.string_pool.StringPool;
import com.dqpi.simple_jvm.runtime_data.opt.OperandStack;
import com.dqpi.simple_jvm.vm.MainVm;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/11/1
 */
@Component
public class ATHROW extends NoOperandsInstruction {
    @Resource
    private StringPool stringPool;
    @Resource
    private ApplicationContext ctx;
    /**
     * 执行指令
     *
     * @param frame 当前栈帧
     */
    @Override
    public void execute(Thread.Frame frame) {
        Obj ex = frame.getOperandStack().popRef();
        if (ex == null) {
            throw new NullPointerException();
        }

        Thread thread = frame.getThread();
        if (!findAndGotoExceptionHandler(thread, ex)) {
            handleUncaughtException(thread, ex);
        }
    }

    private void handleUncaughtException(Thread thread, Obj ex) {
        MainVm mainVm = ctx.getBean(MainVm.class);
        thread.clearStack();

        Obj jMsg = ex.getRefVar("detailMessage", "Ljava/lang/String;");
        String msg = stringPool.getString(jMsg);
        mainVm.setConsole(ex.getClazz().getJavaName() + ":" + msg);

        Throwable.StackTraceElement[] stes = (Throwable.StackTraceElement[]) ex.getExtra();
        for (Throwable.StackTraceElement ste : stes) {
            msg = "\t at " + ste.getClassName() + "." + ste.getMethodName() + "(" + ste.getFileName() + ":" + ste.getLineNumber() + ")";
            mainVm.setConsole(msg);
        }
    }

    private boolean findAndGotoExceptionHandler(Thread thread, Obj ex) {
        while (true) {
            Thread.Frame frame = thread.currentFrame();
            int pc = frame.getNextPc() - 1;

            int handlerPc = frame.getMethod().findExceptionHandler(ex.getClazz(), pc);
            if (handlerPc > 0) {
                OperandStack stack = frame.getOperandStack();
                stack.clear();
                stack.pushRef(ex);
                frame.setNextPc(handlerPc);
                return true;
            }
            
            thread.popFrame();
            if (thread.isStackEmpty()) {
                break;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
