package com.dqpi.simple_jvm.painter;

import com.dqpi.simple_jvm.vm.utils.ViewUtil;
import com.dqpi.simple_jvm.vm.view.MainView;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.springframework.stereotype.Component;

/**
 * @author TheBIGMountain
 * @date 2020/11/1
 */
@Component
public class Painter {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private AnimationTimer animationTimer;
    
    public static int threadStackLength;
    public static String[] methodNames = new String[14];
    public static int methodIndex;
    
    public static int varsLength;
    public static String[] varValue;
    public static boolean isStaticVars;
    public static boolean isFieldVars;
    
    public static int opStackLength;
    public static String[] opValue;
    
    public static int rtcpLength;
    public static String[] rtcpValue;
    
    public static String[] instructions = new String[16];
    public static int instSize;
    
    public void clear() {
        threadStackLength = 0;
        methodNames = new String[14];
        methodIndex = 0;
        varsLength = 0;
        varValue = new String[13];
        opStackLength = 0;
        opValue = new String[9];
        instructions = new String[16];
        instSize = 0;
    }
    
    public Painter(MainView mainView) {
        this.canvas = ViewUtil.getElement(mainView.getView(), "canvas", Canvas.class);
        this.gc = this.canvas.getGraphicsContext2D();
    }
    
    public void strokeRectangle(int x, int y, int w, int h){
        gc.strokeRect(x, y, w, h);
    }

    public void fillRectangle(int x, int y, int w, int h){
        gc.fillRect(x, y, w, h);
    }

    public void setStrokeColor(Color color){
        gc.setStroke(color);
    }

    public void setFillColor(Color color) {
        gc.setFill(color);
    }

    public void setStrokeWidth(int w){
        gc.setLineWidth(w);
    }

    public void drawText(String text, int x, int y, int fontSize) {
        gc.setFont(new Font(fontSize));
        gc.strokeText(text, x, y);
    }
    
    public int getCanvasWidth() {
        return (int) canvas.getWidth();
    }

    public int getCanvasHeight() {
        return (int) canvas.getHeight();
    }
    
    public static void rest() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void draw()  {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, getCanvasWidth(), getCanvasHeight());
                go();
            }
        };
        animationTimer.start();
    }
    
    
    private void go() {
        strokeRectangle(40, 20, 200, 500);
        drawText("虚拟机栈", 100, 550, 20);
        strokeRectangle(800, 20, 370, 500);
        drawText("操作指令", 955, 550, 20);
        
        if (threadStackLength > 14) {
            threadStackLength = 14;
        }
        for (int i = 0; i < threadStackLength; i ++) {
            setFillColor(ColorUtil.LIGHT_BLUE);
            fillRectangle(40, 485 - i * 35, 200, 33);
            drawText(methodNames[i], 70, 510 - i * 35, 20);
        }
        
        if (varsLength > 8) {
            varsLength = 8;
        }
        for (int i = 0; i < varsLength; i ++) {
            if (isStaticVars || isFieldVars) {
                break;
            }
            else {
                drawText("局部变量表", 270, 20, 15);
            }
            strokeRectangle(260 + 60 * i, 25, 60, 50);
            drawText(i + "", 280 + 60 * i, 50, 15);
            strokeRectangle(260 + 60 * i, 75, 60, 50);
            if (varValue != null) {
                if (varValue[i] == null) {
                    varValue[i] = "";
                }
                drawText(varValue[i], 265 + 60 * i, 105, 18);
            }
        }
        
        if (opStackLength > 9) {
            opStackLength = 9;
        }
        for (int i = 0; i < opStackLength; i ++) {
            strokeRectangle(260, 480 - 40 * i, 120, 40);
            if (i == opStackLength - 1) {
                drawText("操作数栈", 280, 470 - 40 * i, 15);
            }
            if (opValue != null) {
                if (opValue[i] == null) {
                    opValue[i] = "";
                }
                drawText(opValue[i], 305, 505 - 40 * i, 20);
            }
        }

        if (rtcpLength > 44) {
            rtcpLength = 44;
        }
        for (int i = 0; i < rtcpLength; i++) {
            drawText("运行时常量池", 530, 150, 15);
            strokeRectangle(420, 160, 350, 360);
            
            if (rtcpValue != null && i < 22) {
                drawText(rtcpValue[i], 425, 175 + 16 * i, 15);
            }  
            else if (rtcpValue != null){
                drawText(rtcpValue[i], 580, 175 + 16 * (i - 22), 15);
            }
        }
        
        for (int i = 0; i < instructions.length; i++) {
            if (instructions[i] == null) {
                if (i - 1 >=0 && instructions[i - 1] != null) {
                    strokeRectangle(805, 512 - 30 * i,  360, 20);
                    drawText(instructions[i], 820, 500 - 30 * i, 20);
                }
            }
            else {
                if (instructions[i] != null) {
                    drawText(instructions[i], 820, 500 - 30 * i, 20);
                }
            }
        }
    }
}
