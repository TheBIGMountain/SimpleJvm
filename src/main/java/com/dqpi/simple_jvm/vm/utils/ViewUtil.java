package com.dqpi.simple_jvm.vm.utils;

import javafx.scene.Parent;

/**
 * @author TheBIGMountain
 * @date 2020/11/1
 */
public class ViewUtil {
    private ViewUtil() {}
    
    public static <T> T getElement(Parent view, String id, Class<T> clazz) {
        return (T) view.lookup("#" + id);
    } 
}
