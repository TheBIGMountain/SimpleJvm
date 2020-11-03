package com.dqpi.simple_jvm.instructions.control;

import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */
public class Return {
    private static void ret(Thread.Frame frame, Fun fun) {
        Thread thread = frame.getThread();
        Thread.Frame currentFrame = thread.popFrame();
        Thread.Frame invokerFrame = thread.currentFrame();
        fun.execute(currentFrame, invokerFrame);
    } 
    
    @Component
    public static class RETURN extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            frame.getThread().popFrame();
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class ARETURN extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            ret(frame, (c, i) -> i.getOperandStack().pushRef(c.getOperandStack().popRef()));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class DRETURN extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            ret(frame, (c, i) -> i.getOperandStack().pushDouble(c.getOperandStack().popDouble()));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class FRETURN extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            ret(frame, (c, i) -> i.getOperandStack().pushFloat(c.getOperandStack().popFloat()));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class IRETURN extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            ret(frame, (c, i) -> i.getOperandStack().pushInt(c.getOperandStack().popInt()));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class LRETURN extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            ret(frame, (c, i) -> i.getOperandStack().pushLong(c.getOperandStack().popLong()));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
    
    
    @FunctionalInterface
    private interface Fun {
        void execute(Thread.Frame currentFrame, Thread.Frame invokerFrame);
    }
}
