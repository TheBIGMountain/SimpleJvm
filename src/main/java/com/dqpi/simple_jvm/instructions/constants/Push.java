package com.dqpi.simple_jvm.instructions.constants;

import com.dqpi.simple_jvm.instructions.BytecodeReader;
import com.dqpi.simple_jvm.instructions.Instruction;
import com.dqpi.simple_jvm.runtime_data.Thread;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
public class Push {
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class BIPUSH implements Instruction {
        @Resource
        private BytecodeReader reader;
        private int val;
        /**
         * 获取所有操作数
         */
        @Override
        public void fetchOperands() {
            val = reader.readInt8();
        }

        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            frame.getOperandStack().pushInt(val);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " ====> " + val;
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class SIPUSH implements Instruction {
        @Resource
        private BytecodeReader reader;
        private int val;
        /**
         * 获取所有操作数
         */
        @Override
        public void fetchOperands() {
            val = reader.readInt16();
        }

        /**
         * 执行指令
         *
         * @param frame 当前栈帧
         */
        @Override
        public void execute(Thread.Frame frame) {
            frame.getOperandStack().pushInt(val);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " ====> " + val;
        }
    }
}
