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
public class LocalVars {
    @Getter
    private Slot[] slots;
    public String[] tempVarValue;
    public static boolean isStatic;
    private boolean isStaticVars;
    private boolean isFieldVars;
    
    public LocalVars setSlots(int maxLocals, boolean isStaticVars, boolean isFieldVars) {
        if (maxLocals <= 0) {
            return this;
        }
        this.isStaticVars = isStaticVars;
        this.isFieldVars = isFieldVars;
        slots = new Slot[maxLocals];
        tempVarValue = new String[maxLocals];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new Slot();
        }
        return this;
    } 
    
    public void setInt(int index, int val) {
        if (slots == null || index < 0 || index >= slots.length) {
            return;
        }
        slots[index].setNum(val);
        if (slots[index].getRef() != null) {
            slots[index].setRef(null);
        }
        draw();
    }
    
    public int getInt(int index) {
        if (slots == null || index < 0 || index >= slots.length) {
            return -1;
        }
        Integer res = slots[index].getNum();
        if (slots[index].getRef() != null) {
            slots[index].setRef(null);
        }
        draw();
        return res;
    }
    
    public void setFloat(int index, float val) {
        if (slots == null || index < 0 || index >= slots.length) {
            return;
        }
        slots[index].setNum(Float.floatToIntBits(val));
        if (slots[index].getRef() != null) {
            slots[index].setRef(null);
        }
        draw();
    }
    
    public float getFloat(int index) {
        if (slots == null || index < 0 || index >= slots.length) {
            return -1;
        }
        float res = Float.intBitsToFloat(slots[index].getNum());
        if (slots[index].getRef() != null) {
            slots[index].setRef(null);
        }
        draw();
        return res;
    }
    
    public void setLong(int index, long val) {
        if (slots == null || index < 0 || index >= slots.length) {
            return;
        }
        int high = (int) (val >> 32);
        int low = (int) val;
        slots[index].setNum(high);
        slots[++index].setNum(low);
        if (slots[index - 1].getRef() != null) {
            slots[index - 1].setRef(null);
        }
        if (slots[index].getRef() != null) {
            slots[index].setRef(null);
        }
        draw();
    }
    
    public long getLong(int index) {
        if (slots == null || index < 0 || index >= slots.length) {
            return -1;
        }
        long high = (long) (slots[index].getNum()) << 32;
        long low = Integer.toUnsignedLong(slots[index + 1].getNum());
        if (slots[index].getRef() != null) {
            slots[index].setRef(null);
        }
        if (slots[index + 1].getRef() != null) {
            slots[index + 1].setRef(null);
        }
        draw();
        return high | low;
    }
    
    public void setDouble(int index, double val) {
        if (slots == null || index < 0 || index >= slots.length) {
            return;
        }
        setLong(index, Double.doubleToLongBits(val));
    }
    
    public double getDouble(int index) {
        if (slots == null || index < 0 || index >= slots.length) {
            return -1;
        }
        return Double.longBitsToDouble(getLong(index));
    }
    
    public void setRef(int index, Obj ref) {
            if (slots == null || index < 0 || index >= slots.length) {
            return;
        }
        slots[index].setRef(ref);
        if (slots[index].getNum() != null) {
            slots[index].setNum(null);
        }
        draw();
    }
    
    public Obj getRef(int index) {
        if (slots == null || index < 0 || index >= slots.length) {
            return null;
        }
        Obj res = slots[index].getRef();
        if (slots[index].getNum() != null) {
            slots[index].setNum(null);
        }
        draw();
        return res;
    }

    public void setSlot(int index, Slot slot) {
        slots[index] = slot;
        draw();
    }
    
    private void draw() {
        Painter.rest();
        Painter.varValue = tempVarValue;
        Painter.varsLength = slots.length;
        Painter.isStaticVars = isStaticVars;
        Painter.isFieldVars = isFieldVars;
        for (int i = 0; i < slots.length; i++) {
            if (slots[i].getRef() != null) {
                tempVarValue[i] = "obj";
                if (!isStatic && i == 0) {
                    tempVarValue[i] = "this";
                }
            }
            else if (slots[i].getNum() != null) {
                tempVarValue[i] = slots[i].getNum() + "";
            }
        }
    }
}
