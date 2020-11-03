package com.dqpi.simple_jvm.runtime_data.opt;

import com.dqpi.simple_jvm.painter.Painter;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import lombok.Getter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/10/28
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OperandStack {
    private int current;
    @Getter
    private Slot[] slots;
    private String[] tempOpValue;
    
    public void setSlots(int maxStack) {
        if (maxStack <= 0) {
            return;
        }
        slots = new Slot[maxStack];
        tempOpValue = new String[maxStack];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new Slot();
        }
        draw();
    }
    
    public void pushInt(int val) {
        if (slots == null) {
            return;
        }
        slots[current].setNum(val);
        current ++;
        draw();
    }
    
    public int popInt() {
        if (slots == null) {
            return -1;
        }
        current --;
        Integer temp = slots[current].getNum();
        draw();
        return temp;
    }
    
    public char popChar() {
        if (slots == null) {
            return 0;
        }
        current --;
        char temp = (char) (int) (slots[current].getNum());
        draw();
        return  temp;
    }
    
    public void pushFloat(float val) {
        if (slots == null) {
            return;
        }
        slots[current].setNum(Float.floatToIntBits(val));
        current ++;
        draw();
    }
    
    public float popFloat() {
        if (slots == null) {
            return -1;
        }
        current --;
        float temp = Float.intBitsToFloat(slots[current].getNum());
        draw();
        return temp;
    }
    
    public void pushLong(long val) {
        if (slots == null) {
            return;
        }
        slots[current].setNum((int) (val >>> 32));
        slots[current + 1].setNum((int) val);
        current += 2;
        draw();
    }
    
    public long popLong() {
        if (slots == null) {
            return -1;
        }
        current -= 2;
        long high = (long) slots[current].getNum() << 32;
        long low = Integer.toUnsignedLong(slots[current + 1].getNum());
        draw();
        return high | low;
    }
    
    public void pushDouble(double val) {
        if (slots == null) {
            return;
        }
        pushLong(Double.doubleToLongBits(val));
    }
    
    public double popDouble() {
        if (slots == null) {
            return -1;
        }
        draw();
        return Double.longBitsToDouble(popLong());
    }
    
    public void pushRef(Obj ref) {
        slots[current].setRef(ref);
        current ++;
        draw();
    }
    
    public Obj popRef() {
        current --;
        draw();
        return slots[current].getRef();
    }
    
    public void pushSlot(Slot slot) {
        slots[current] = slot;
        current ++;
        draw();
    }
    
    public Slot popSlot() {
        current --;
        Slot temp = slots[current];
        draw();
        return temp;
    }

    public Obj getRefFromTop(int argCount) {
        return slots[current - argCount].getRef();
    }
    
    private void draw() {
        Painter.rest();
        Painter.opValue = tempOpValue;
        Painter.opStackLength = slots.length;
        for (int i = 0; i < slots.length; i++) {
            if (i >= current) {
                tempOpValue[i] = "";
            }
            else if (slots[i].getRef() != null) {
                tempOpValue[i] = "obj";
            }
            else if (slots[i].getNum() != null) {
                tempOpValue[i] = slots[i].getNum() + "";
            }
        }
    }

    public void clear() {
        current = 0;
        for (Slot slot : slots) {
            slot.setRef(null);
        }
    }
}
