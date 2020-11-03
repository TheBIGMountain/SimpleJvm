package com.dqpi.simple_jvm.runtime_data.opt;

import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TheBIGMountain
 * @date 2020/10/28
 */
@Getter
@Setter
public class Slot {
    private Integer num = null;
    private Obj ref = null;
}
