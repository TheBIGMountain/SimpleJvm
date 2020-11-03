package com.dqpi.simple_jvm.painter;

import javafx.scene.paint.Color;

/**
 * @author Mountain
 * @date 2020/10/13
 */
public class ColorUtil {
    private ColorUtil() {}
    
    public static final Color RED = Color.valueOf("0xF44336");
    public static final Color PINK = Color.valueOf("0xE91E63");
    public static final Color PURPLE = Color.valueOf("0x9C27B0");
    public static final Color DEEP_PURPLE = Color.valueOf("0x673AB7");
    public static final Color INDIGO = Color.valueOf("0x3F51B5");
    public static  final Color BLUE = Color.valueOf("0x2196F3");
    public static final Color LIGHT_BLUE = Color.valueOf("0x03A9F4");
    public static final Color CYAN = Color.valueOf("0x00BCD4");
    public static final Color TEAL = Color.valueOf("0x009688");
    public static final Color GREEN = Color.valueOf("0x4CAF50");
    public static final Color LIGHT_GREEN = Color.valueOf("0x8BC34A");
    public static final Color LIME = Color.valueOf("0xCDDC39");
    public static final Color YELLOW = Color.valueOf("0xFFEB3B");
    public static final Color AMBER = Color.valueOf("0xFFC107");
    public static final Color ORANGE = Color.valueOf("0xFF9800");
    public static final Color DEEP_ORANGE = Color.valueOf("0xFF5722");
    public static final Color BROWN = Color.valueOf("0x795548");
    public static final Color GREY = Color.valueOf("0x9E9E9E");
    public static final Color BLUE_GREY = Color.valueOf("0x607D8B");
    public static final Color BLACK = Color.valueOf("0x000000");
    public static final Color WHITE = Color.valueOf("0xFFFFFF");

    public static Color getRandomColor() {
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        return Color.rgb(r, g, b, 0.5);
    }
}
