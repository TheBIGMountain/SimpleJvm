package com.dqpi.simple_jvm.vm;

import com.dqpi.simple_jvm.jvm.Interpreter;
import com.dqpi.simple_jvm.jvm.SimpleJvm;
import com.dqpi.simple_jvm.painter.Painter;
import com.dqpi.simple_jvm.vm.utils.ViewUtil;
import com.dqpi.simple_jvm.vm.view.MainView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author TheBIGMountain
 * @date 2020/11/1
 */
@FXMLController
public class MainVm {
    @Resource
    private SimpleJvm jvm;
    @Resource
    private MainView mainView;
    @Resource
    private ApplicationContext ctx;
    private String userClassName;
    private String userClasspath;
    private static final FileChooser FILE_CHOOSER = new FileChooser();
    
    static {
        FILE_CHOOSER.setInitialDirectory(new File(System.getProperty("user.home")));
    }
    
    public void startJvm(MouseEvent event) {
        if (StringUtils.isEmpty(userClasspath)) {
            setConsole("请选择class文件!");
            return;
        }
        setVmBtnShow(false);
        ctx.getBean(Painter.class).clear();
        jvm.start("-cp", userClasspath, userClassName);
    }
    
    public void setConsole(String msg) {
        TextArea textArea = ViewUtil.getElement(mainView.getView(), "console", TextArea.class);
        textArea.appendText(msg + '\n');
    }
    
    public void setVmBtnShow(boolean isShow) {
        Button button = ViewUtil.getElement(mainView.getView(), "starJvm", Button.class);
        button.setDisable(!isShow);
    }
    
    public void setNextBtnShow(boolean isShow) {
        Button button = ViewUtil.getElement(mainView.getView(), "next", Button.class);
        button.setDisable(!isShow);
    }

    public void select(MouseEvent event) {
        File file = FILE_CHOOSER.showOpenDialog(mainView.getView().getScene().getWindow());
        if (file == null) {
            return;
        }
        if (!file.getName().endsWith(".class")) {
            setConsole("请选择class文件!");
            return;
        }
        
        TextField textField = ViewUtil.getElement(mainView.getView(), "classFileName", TextField.class);
        textField.setText(file.getName());

        userClassName = file.getName().substring(0, file.getName().lastIndexOf("."));
        userClasspath = file.getParent();
    }

    public void stop(MouseEvent event) {
        if (userClasspath == null || userClassName == null) {
            setConsole("请选择class文件!");
            return;
        }
        setNextBtnShow(false);
        Interpreter.isNext.set(true);
    }
}
