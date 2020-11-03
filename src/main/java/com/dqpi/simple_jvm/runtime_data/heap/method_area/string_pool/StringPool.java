package com.dqpi.simple_jvm.runtime_data.heap.method_area.string_pool;

import com.dqpi.simple_jvm.runtime_data.heap.Arr;
import com.dqpi.simple_jvm.runtime_data.heap.Obj;
import com.dqpi.simple_jvm.runtime_data.heap.method_area.ClassLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TheBIGMountain
 * @date 2020/10/31
 */
@Component
public class StringPool {
    @Resource
    private ApplicationContext ctx;
    @Resource
    private Arr arr;
    
    private final Map<String, Obj> strings = new HashMap<>();
    
    public String getString(Obj ref) {
        Character[] characters = ref.getRefVar("value", "[C").getArr(Character.class);
        char[] chars = new char[characters.length];
        for (int i = 0; i < characters.length; i++) {
            chars[i] = characters[i];
        }
        return new String(chars);
    }
    
    
    public Obj toJString(ClassLoader loader, String str) {
        Obj strObj = strings.get(str);
        if (strObj != null) {
            return strObj;
        }

        Obj ref = arr.newArray(loader.loadClass("[C"), str.toCharArray().length);
        Character[] chars = ref.getArr(Character.class);;
        for (int i = 0; i < chars.length; i++) {
            chars[i] = str.charAt(i);
        }
        
        strObj = ctx.getBean(Obj.class).setInfo(loader.loadClass("java/lang/String"));
        strObj.setRefVar("value", "[C", ref);
        
        strings.put(str, strObj);
        return strObj;
    }
    
    
    public Obj internString(Obj jString) {
        String str = getString(jString);
        Obj strObj = strings.get(str);
        if (strObj != null) {
            return strObj;
        }
        strings.put(str, jString);
        return jString;
    }
}
