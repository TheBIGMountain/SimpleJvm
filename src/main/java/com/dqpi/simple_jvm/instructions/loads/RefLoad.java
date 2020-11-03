package com.dqpi.simple_jvm.instructions.loads;

import com.dqpi.simple_jvm.instructions.base.Index8Instruction;
import com.dqpi.simple_jvm.instructions.base.NoOperandsInstruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */
public class RefLoad {
    private static void aLoad(Thread.Frame frame, int index) {
        Obj ref = frame.getLocalVars().getRef(index);
        frame.getOperandStack().pushRef(ref);
    }
    
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class ALOAD extends Index8Instruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            aLoad(frame, index);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " :   " + index;
        }
    }

    @Component
    public static class ALOAD_0 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            aLoad(frame, 0);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class ALOAD_1 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            aLoad(frame, 1);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class ALOAD_2 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            aLoad(frame, 2);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Component
    public static class ALOAD_3 extends NoOperandsInstruction {
        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            aLoad(frame, 3);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
