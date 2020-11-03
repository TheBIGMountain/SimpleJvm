package com.dqpi.simple_jvm.jvm;

import com.dqpi.simple_jvm.instructions.BytecodeReader;
import com.dqpi.simple_jvm.instructions.Instruction;
import com.dqpi.simple_jvm.instructions.InstructionFactory;
import com.dqpi.simple_jvm.painter.Painter;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.class_info.member.Method;
import com.dqpi.simple_jvm.vm.MainVm;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author TheBIGMountain
 * @date 2020/11/1
 */
@Component
public class Interpreter {
    @Resource
    private ApplicationContext ctx;
    @Resource
    private BytecodeReader reader;
    @Resource
    private InstructionFactory factory;
    @Resource
    private MainVm mainVm;
    public static final AtomicBoolean isNext = new AtomicBoolean(false);

    public void interpreter(Thread thread, boolean logInst) {
        try {
            loop(thread, logInst);
        }
        catch (Exception e)  {
            logFrames(thread);
            e.printStackTrace();
        }
    }
    
    private void logFrames(Thread thread) {
        while (!thread.isStackEmpty()) {
            Thread.Frame frame = thread.popFrame();
            Method method = frame.getMethod();
            String className = method.getClazz().getName();
            System.out.println(String.format(">> pc: %d %s.%s%s",
                    frame.getNextPc(), className, method.getName(), method.getDescriptor()));
        }
    }

    private void loop(Thread thread, boolean logInst) {
        ctx.getBean(Painter.class).draw();
        while (true) {
            Thread.Frame frame = thread.currentFrame();
            int pc = frame.getNextPc();
            thread.setPc(pc);

            reader.reset(frame.getMethod().getCode(), pc);
            int opcode = reader.readUint8();
            Instruction inst = factory.newInstruction(opcode);
            inst.fetchOperands();
            frame.setNextPc(reader.getPc());

            if (logInst) {
                logInstruction(frame, inst);
            }
            
            setData(thread.getPc() + " <- " + inst);
            inst.execute(frame);
            
            while (!isNext.get()) {
                java.lang.Thread.yield();
            }
            isNext.set(false);
            mainVm.setNextBtnShow(true);
            if (thread.isStackEmpty()) {
                break;
            }
        }
        this.mainVm.setVmBtnShow(true);
    }

    private void logInstruction(Thread.Frame frame, Instruction inst) {
        int pc = frame.getThread().getPc();
        System.out.println(
                String.format("#%d %s", pc, inst.getClass().getSimpleName()));
    }
    
    private void setData(String instName) {
        if (Painter.instSize == Painter.instructions.length) {
            Arrays.fill(Painter.instructions, null);
            Painter.instSize = 0;
        }
        Painter.instructions[Painter.instSize ++] = instName;
        Painter.rest();
    }
}
