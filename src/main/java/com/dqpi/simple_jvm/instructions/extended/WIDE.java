package com.dqpi.simple_jvm.instructions.extended;

import com.dqpi.simple_jvm.instructions.BytecodeReader;
import com.dqpi.simple_jvm.instructions.Instruction;
import com.dqpi.simple_jvm.instructions.base.Index8Instruction;
import com.dqpi.simple_jvm.instructions.loads.*;
import com.dqpi.simple_jvm.instructions.math.IINC;
import com.dqpi.simple_jvm.instructions.stores.*;
import com.dqpi.simple_jvm.runtime_data.Thread;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TheBIGMountain
 * @date 2020/10/29
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WIDE implements Instruction {
    @Resource
    private ApplicationContext ctx;
    private final Map<Integer, GetInstruction> INSTRUCTION_MAP;
    private Instruction modifiedInstruction;
    
    public WIDE() {
        INSTRUCTION_MAP = new HashMap<>(16);
        INSTRUCTION_MAP.put(0x15, () -> ctx.getBean(IntLoad.ILOAD.class));
        INSTRUCTION_MAP.put(0x16, () -> ctx.getBean(LongLoad.LLOAD.class));
        INSTRUCTION_MAP.put(0x17, () -> ctx.getBean(FloatLoad.FLOAD.class));
        INSTRUCTION_MAP.put(0x18, () -> ctx.getBean(DoubleLoad.DLOAD.class));
        INSTRUCTION_MAP.put(0x19, () -> ctx.getBean(RefLoad.ALOAD.class));
        INSTRUCTION_MAP.put(0x36, () -> ctx.getBean(IntStore.ISTORE.class));
        INSTRUCTION_MAP.put(0x37, () -> ctx.getBean(LongStore.LSTORE.class));
        INSTRUCTION_MAP.put(0x38, () -> ctx.getBean(FloatStore.FSTORE.class));
        INSTRUCTION_MAP.put(0x39, () -> ctx.getBean(DoubleStore.DSTORE.class));
        INSTRUCTION_MAP.put(0x3a, () -> ctx.getBean(RefStore.ASTORE.class));
    }
    
    /**
     * 获取所有操作数
     */
    @SneakyThrows
    @Override
    public void fetchOperands() {
        BytecodeReader reader = ctx.getBean(BytecodeReader.class);
        int opcode = reader.readUint8();
        
        Instruction instruction;
        GetInstruction fun = INSTRUCTION_MAP.get(opcode);
        if (fun != null) {
            Index8Instruction index8Instr = fun.execute();
            index8Instr.setIndex(reader.readUint16());
            instruction = index8Instr;
        }
        else if (opcode == 0x84){
            IINC iinc = ctx.getBean(IINC.class);
            iinc.setIndex(reader.readUint16());
            iinc.setCon(reader.readInt16());
            instruction = iinc;
        }
        else {
            throw new Exception("Unsupported opcode!");
        }
        this.modifiedInstruction = instruction;
    }

    /**
     * 执行指令
     *
     * @param frame 当前栈帧
     */
    @Override
    public void execute(Thread.Frame frame) {
        this.modifiedInstruction.execute(frame);
    }
    
    private interface GetInstruction {
        Index8Instruction execute();
    }
}
